package com.devulap.apiserver.client;

import com.devulap.apiserver.model.HardwareState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

import reactor.core.publisher.Sinks;

@Slf4j
public class HardwareClient {
	private static final String COMMAND_START = "START %s";
	private static final String COMMAND_STOP = "STOP";

	private static HardwareClient INSTANCE = null;

	private Socket socket;
	private PrintStream printStream;
	private BufferedReader in;
	private ClientThread clientThread;

	private HardwareClient() {
	}

	public static HardwareClient getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new HardwareClient();
		}
		return INSTANCE;
	}

	public Runnable start(String ip, int port, Sinks.Many<HardwareState> sink, int times) {
		if (!isConnected()) {
			connect(ip, port);
			clientThread = new ClientThread(socket, printStream, in, sink, times);
		} else {
			throw new UnsupportedOperationException("Another process is currently running, please try again later.");
		}
		return clientThread;
	}

	public void stop() {
		if (clientThread != null) {
			clientThread.stop();
		}
		clientThread = null;
	}

	private void connect(String ip, int port) {
		try {
			this.socket = new Socket(ip, port);
			this.printStream = new PrintStream(socket.getOutputStream());
			this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean isConnected() {
		return this.socket != null && !this.socket.isClosed();
	}

	@RequiredArgsConstructor
	public static class ClientThread implements Runnable {
		private volatile boolean stop;

		private final Socket socket;
		private final PrintStream printStream;
		private final BufferedReader in;
		private final Sinks.Many<HardwareState> sink;
		private final int times;

		@Override
		public void run() {
			try {
				log.info("Starting for {} seconds.", times);
				printStream.printf((COMMAND_START) + "%n", times);
				String line;
				while (!stop && (line = in.readLine()) != null) {
					log.info("Received: {}", line);

					HardwareState state = new HardwareState();
					state.setConnectionStatus(HardwareState.ConnectionStatus.CONNECTED);

					if (line.contains("STARTED")) {
						state.setStatus(HardwareState.Status.STARTED);
					} else if (line.contains("STOPPED")) {
						state.setStatus(HardwareState.Status.STOPPED);
					} else if (line.contains(",")) {
						state.setStatus(HardwareState.Status.STARTED);
						state.setRandom(Integer.valueOf(line.split(",")[0]));
						state.setRemaining(Integer.valueOf(line.split(",")[1]));
					}

					Sinks.EmitResult result = sink.tryEmitNext(state);
					if (result.isFailure()) {
						log.error("Emit failed: {}.", result);
					}
				}
				if (stop) {
					log.info("Sending {} to hardware.", COMMAND_STOP);
					printStream.print(COMMAND_STOP);
					printStream.flush();
				}
				terminate();
				log.info("Done");
			} catch (IOException e) {
				log.error("Error communicating with hardware.", e);
			}
		}

		public void stop() {
			this.stop = true;
		}

		public void terminate() throws IOException {
			this.printStream.close();
			this.in.close();
			if (socket != null && !this.socket.isClosed()) {
				this.socket.close();
			}

			//set state to disconnected
			HardwareState state = new HardwareState();
			state.setConnectionStatus(HardwareState.ConnectionStatus.NOT_CONNECTED);
			state.setStatus(HardwareState.Status.STOPPED);

			Sinks.EmitResult result = sink.tryEmitNext(state);
			if (result.isFailure()) {
				log.error("Emit failed: {}.", result);
			}

		}
	}
}

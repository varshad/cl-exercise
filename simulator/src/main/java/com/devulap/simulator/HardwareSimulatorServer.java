package com.devulap.simulator;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

public class HardwareSimulatorServer {

	private Socket socket = null;

	public void startServer(int port) {

		try (var listener = new ServerSocket(port)) {

			System.out.println("The server is running on port " + port);
			var pool = Executors.newFixedThreadPool(1);

			while (true) {
				//make sure only one connection at a time
				if (socket == null || socket.isClosed()) {
					socket = listener.accept();
					pool.execute(new ConnectionHandler(socket));
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	public static void main(String[] args) {

		int port = 4444;
		if (args.length > 0) {
			port = Integer.valueOf(args[0]);
		}

		HardwareSimulatorServer simulator = new HardwareSimulatorServer();
		simulator.startServer(port);
	}

}

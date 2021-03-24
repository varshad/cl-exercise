package com.devulap.simulator;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.Timer;


public class ConnectionHandler implements Runnable {

	private Socket socket;

	ConnectionHandler(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {

		System.out.println("Connected: " + socket);
		try {

			Scanner in = new Scanner(socket.getInputStream());
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			Timer timer = new Timer();

			while (in.hasNextLine()) {

				String inputLine = in.nextLine();
				inputLine = inputLine.trim();
				System.out.println("input: " + inputLine);

				if (inputLine.contains("START")) {
					//get time from string - e.g. START 9
					String[] args = inputLine.split(" ");

					if (args.length == 2) {
						int duration = Integer.valueOf(args[1]);
						//start a new thread to generate numbers
						out.println("STARTED");
						RandomIntegerTask integerTask = new RandomIntegerTask(out, duration);
						timer.schedule(integerTask, 0, 1000); //every second

					} else {
						out.println("INVALID_INPUT");
					}

				} else if (inputLine.equals("STOP")) {
					//stop the thread
					timer.cancel();
					out.println("STOPPED");
					break;
				} else {
					out.println("INVALID_INPUT");
				}
			}

		} catch (Exception e) {
			System.out.println("Error:" + socket);
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				System.out.println(e);
			}
			System.out.println("Closed: " + socket);
		}
	}
}
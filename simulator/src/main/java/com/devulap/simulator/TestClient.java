package com.devulap.simulator;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

//TestClient for testing the Simulator
public class TestClient {

	public static void main(String[] args) throws Exception {

		try (var socket = new Socket("127.0.0.1", 4444)) {
			System.out.println("Enter START <time> or STOP");

			Scanner scanner = new Scanner(System.in);
			Scanner in = new Scanner(socket.getInputStream());
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

			while (scanner.hasNextLine()) {
				String input = scanner.nextLine();
				String[] tokens = input.split(" ");
				if (tokens.length == 2) {
					int count = Integer.valueOf(tokens[1]);

					out.println(input);
					System.out.println(in.nextLine());

					//read next 'n' lines
					while (count >= 0) {
						System.out.println(in.nextLine());
						count--;
					}
				} else {
					out.println(input);
					System.out.println(in.nextLine());
				}
			}
		}
	}
}
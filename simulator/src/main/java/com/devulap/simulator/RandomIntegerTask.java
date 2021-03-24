package com.devulap.simulator;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomUtils;

import java.io.PrintWriter;
import java.util.TimerTask;

@RequiredArgsConstructor
public class RandomIntegerTask extends TimerTask {

	private final PrintWriter out;
	private final int duration;
	private int remaining = -1;

	public void run() {

		if(remaining == -1) {
			remaining = duration;
		}

		if(remaining <= 0){
			//exit if remaining seconds reaches zero
			this.cancel();
			out.println("STOPPED");
			out.flush();
			out.close();

		} else {
			out.println(generateRandomInteger() + "," + remaining);
			remaining--;
		}

	}

	private int generateRandomInteger() {
		return RandomUtils.nextInt(0, 101);
	}
}

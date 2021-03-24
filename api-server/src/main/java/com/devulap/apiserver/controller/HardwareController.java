package com.devulap.apiserver.controller;

import com.devulap.apiserver.model.HardwareState;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

import com.devulap.apiserver.service.HardwareService;

@RestController
@RequiredArgsConstructor
public class HardwareController {
	private final HardwareService hardwareService;

	@GetMapping(value = "/start", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<HardwareState> start(@RequestParam("times") int times) {
		return hardwareService.start(times);
	}

	@PostMapping("/stop")
	public void stop() throws IOException {
		hardwareService.stop();
	}
}

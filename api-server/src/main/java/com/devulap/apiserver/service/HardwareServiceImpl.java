package com.devulap.apiserver.service;

import com.devulap.apiserver.model.HardwareState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Scheduler;

import com.devulap.apiserver.client.HardwareClient;
import com.devulap.apiserver.config.HardwareProperties;

@Slf4j
@RequiredArgsConstructor
@Service
public class HardwareServiceImpl implements HardwareService {
	private final HardwareProperties hardwareProperties;
	private final Scheduler scheduler;

	@Override
	public Flux<HardwareState> start(int times) {
		Sinks.Many<HardwareState> sink = Sinks.many().multicast().onBackpressureBuffer();
		HardwareClient hardwareClient = HardwareClient.getInstance();
		return sink.asFlux()
				.doOnSubscribe(subscription -> scheduler.schedule(hardwareClient.start(
						hardwareProperties.getIp(),
						hardwareProperties.getPort(),
						sink, times)));
	}

	@Override
	public void stop() {
		HardwareClient.getInstance().stop();
	}
}

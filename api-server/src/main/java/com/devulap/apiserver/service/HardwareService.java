package com.devulap.apiserver.service;

import java.io.IOException;

import com.devulap.apiserver.model.HardwareState;
import reactor.core.publisher.Flux;

public interface HardwareService {

	Flux<HardwareState> start(int times);

	void stop() throws IOException;
}

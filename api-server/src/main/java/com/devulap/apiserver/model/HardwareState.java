package com.devulap.apiserver.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

@Data
@NoArgsConstructor
public class HardwareState {

	public enum ConnectionStatus {
		CONNECTED,
		NOT_CONNECTED
	}

	public enum Status {
		STARTED,
		STOPPED
	}

	private Integer random;
	private Integer remaining;
	private ConnectionStatus connectionStatus;
	private Status status;
}

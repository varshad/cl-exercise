package com.devulap.apiserver.config;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "hardware")
public class HardwareProperties {
	private String type;
	private String ip;
	private int port;
}

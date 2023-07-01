package com.livetraffic.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.MediaType;

import reactor.core.publisher.Flux;

import com.livetraffic.api.service.SensorService;

@RestController
@RequestMapping("/sensors")
public class SensorController {

	@Autowired
	private SensorService service;

	@GetMapping
	public Flux<String> getAll() {
		return service.getAll();
	}

	@GetMapping(path = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<String> subscribeAll() {
		return service.subscribeAll();
	}

}

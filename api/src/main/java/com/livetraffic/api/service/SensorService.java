package com.livetraffic.api.service;

import java.util.function.Consumer;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

@Service
public class SensorService {

	@Autowired
	private WebClient orionClient;

	private SensorSink sensorSink = new SensorSink();
	private Flux<String> flux = Flux.create(sensorSink).share();

	public Flux<String> getAll() {
		return orionClient.get()
			.uri("/entities?type=TrafficSensor")
			.retrieve()
			.bodyToFlux(String.class);
	}

	//TODO
	public Flux<String> create() {
		return null;
	}

	public Flux<String> subscribeAll() {
		return flux;
	}

	public Mono<Void> publish(String s) {
		sensorSink.produce(s);
		return Mono.empty();
	}

	private class SensorSink implements Consumer<FluxSink<String>> {
		private FluxSink<String> sink;
		@Override
		public void accept(FluxSink<String> sink) {
			this.sink = sink;
		}
		public void produce(String sensor) {
			this.sink.next(sensor);
		}
	}

}

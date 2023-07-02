package com.livetraffic.api.service;

import java.util.function.Consumer;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

@Service
public class SensorService {

	@Autowired
	private WebClient orionClient;

	private AtomicReference<String> globalSubscriptionId = new AtomicReference<>("");
	private SensorSink sensorSink = new SensorSink();
	private Flux<String> sensorStream = Flux.create(sensorSink).share();

	public Flux<String> getAll() {
		return orionClient.get()
			.uri("/entities?type=TrafficSensor")
			.retrieve()
			.bodyToFlux(String.class);
	}

	public Mono<Void> create(String sensor) {
		return orionClient.post()
			.uri("/entities")
			.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.body(Mono.just(sensor), String.class)
			.retrieve()
			.toBodilessEntity()
			.then(Mono.empty());
	}

	public Mono<Void> update(String id, String attrs) {
		return orionClient.post()
			.uri("/entities/" + id + "/attrs")
			.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.body(Mono.just(attrs), String.class)
			.retrieve()
			.toBodilessEntity()
			.then(Mono.empty());
	}

	public Flux<String> subscribeAll() {
		String id = globalSubscriptionId.get();
		if (id == "" || id == null) {
			orionClient.post()
				.uri("/subscriptions")
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.body(Mono.just(this.subscriptionStr()), String.class)
				.retrieve()
				.toBodilessEntity()
				.subscribe();
			globalSubscriptionId.set("asdf");
		}
		return sensorStream;
	}

	public Mono<Void> publishOne(String sensor) {
		sensorSink.produce(sensor);
		System.out.println(sensor);
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

	private String subscriptionStr() {
		return "{\"description\":\"Global TrafficSensor subscription\",\"subject\":{\"entities\":[{\"idPattern\":\".*\",\"type\":\"TrafficSensor\"}],\"condition\":{\"attrs\":[\"flow\",\"location\"]}},\"notification\":{\"http\":{\"url\":\"http://api:8080/sensors/events\"},\"attrs\":[]},\"throttling\":2}";
	}

}

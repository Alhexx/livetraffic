package com.livetraffic.api.service;

import java.util.function.Consumer;
import java.util.Map;

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

	private SensorSink sensorSink = new SensorSink();
	private String globalSubscription = null;
	private Flux<String> sensorStream = Flux.create(sensorSink).share();

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
		if (globalSubscription == null) {
			orionClient.post()
				.uri("/subscriptions")
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.body(Mono.just(this.subscriptionStr()), String.class)
				//.body(BodyInserters.fromValue(this.subscriptionStr()))
				//.bodyValue(this.subscriptionStr())
				.retrieve()
				.toBodilessEntity()
				//.bodyToMono(String.class)
				//.doOnNext(s -> globalSubscription = s)
				.subscribe();
			globalSubscription = "";
			System.out.println(subscriptionStr());
			System.out.println(globalSubscription);
		}
		return sensorStream;
	}

	public Mono<Void> publish(String sensor) {
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
		return "{\"description\":\"Global TrafficSensor subscription\",\"subject\":{\"entities\":[{\"idPattern\":\".*\",\"type\":\"TrafficSensor\"}],\"condition\":{\"attrs\":[\"flow\",\"location\"]}},\"notification\":{\"http\":{\"url\":\"http://api:8080/sensors\"},\"attrs\":[]},\"throttling\":2}";
	}

}

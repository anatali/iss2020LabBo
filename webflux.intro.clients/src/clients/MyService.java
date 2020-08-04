package clients;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class MyService {

	private final WebClient webClient;

	public MyService(WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder.baseUrl("http://example.org").build();
	}

	public Mono<String> someRestCall(String name) {
		return this.webClient.get().uri("/{name}/details", name)
						.retrieve().bodyToMono(String.class);
	}

}

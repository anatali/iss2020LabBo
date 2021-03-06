package clients;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class WebClientExample2 {
    public static void main(String[] args) throws InterruptedException {
        WebClient webClient = WebClient.create("http://localhost:8082");
        Mono<String> result = webClient.get()
                                       .retrieve()
                                       .bodyToMono(String.class);
        result.subscribe(WebClientExample2::handleResponse);
        System.out.println("After subscribe");
        //wait for a while for the response
        Thread.sleep(1000);
    }

    private static void handleResponse(String s) {
        System.out.println("WebClientExample2 handle response");
        System.out.println(s);
    }
}
package it.unibo.websocket.intro;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

@Controller
public class WebSockHIController {

    @GetMapping(value = "/")
    public Mono<String> home()
    {
        return Mono.just("indexWebsocket");
    }

}

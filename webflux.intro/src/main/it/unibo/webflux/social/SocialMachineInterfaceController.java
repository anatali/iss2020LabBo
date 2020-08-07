package it.unibo.webflux.social;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

 
@RestController
public class SocialMachineInterfaceController {

	private static final Logger log = LoggerFactory.getLogger(SocialMachineInterfaceController.class);

	private static final String API_BASE_PATH = "/api";
	
	private final ImageService imageService;
	
	public SocialMachineInterfaceController(ImageService imageService) {
		this.imageService = imageService;
	}
	
	@GetMapping(API_BASE_PATH  )
	Mono<String> entry(){
		return Mono.just("indexSocial");
	}
 
	//curl localhost:8082/api/images 
	// tag::get[]		//See https://spring.io/guides/gs/gradle/
	@GetMapping(API_BASE_PATH + "/images")
	Flux<Image> images() {
		Hooks.onOperatorDebug();
		return imageService.findAllImages();
	}
	// end::get[]

	// tag::post[]
	@PostMapping(API_BASE_PATH + "/images")
	Mono<Void> create(@RequestPart Flux<FilePart> images) {
		//Hooks.onOperatorDebug();

		return images
			.map(image -> {
				log.info("We will save " + image + " to a Reactive database soon!");
				return image;
			})
			.then();
	}
	// end::post[]

}

/*
 * https://spring.io/blog/2016/06/07/notes-on-reactive-programming-part-i-the-reactive-landscape				part 1
 * https://spring.io/blog/2016/06/13/notes-on-reactive-programming-part-ii-writing-some-code					part 2
 * https://spring.io/blog/2016/07/20/notes-on-reactive-programming-part-iii-a-simple-http-server-application	part 3
 * https://spring.io/blog/2016/04/19/understanding-reactive-types
*/
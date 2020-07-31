/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package  learningstep0;

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
public class MachineInterfaceController {

	private static final Logger log = LoggerFactory.getLogger(MachineInterfaceController.class);

	private static final String API_BASE_PATH = "/api";
	
	private final ImageService imageService;
	
	public MachineInterfaceController(ImageService imageService) {
		this.imageService = imageService;
	}
 
	//curl localhost:8082/api/images 
	// tag::get[]		//See https://spring.io/guides/gs/gradle/
	@GetMapping(API_BASE_PATH + "/images")
	Flux<Image> images() {
		Hooks.onOperatorDebug();
		return imageService.findAllImages();
//		return Flux.just(				
//				new Image(1, "basicrobotlogical.png"),
//				new Image(2, "basicRobotOnRasp.png"),
//				new Image(3, "basicrobotproject.png")
//		);
	}
	// end::get[]

	// tag::post[]
	@PostMapping(API_BASE_PATH + "/images")
	Mono<Void> create(@RequestPart Flux<FilePart> images) {
		Hooks.onOperatorDebug();

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
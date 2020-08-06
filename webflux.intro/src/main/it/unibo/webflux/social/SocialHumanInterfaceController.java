package it.unibo.webflux.social;

import java.io.IOException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;

 
// tag::1[]
@Controller
public class SocialHumanInterfaceController {		//Book pg. 55

	private static final String BASE_PATH = "/images";
	private static final String FILENAME = "{filename:.+}";

	private final ImageService imageService;
	
	private Flux<Image> fluxOfImages = null;

	public SocialHumanInterfaceController(ImageService imageService) {
		this.imageService = imageService;
	}
	// end::1[]

	// tag::5[]
	@GetMapping("/")
	public Mono<String> index(Model model) {	//Book pg. 58
		try {
			if( fluxOfImages == null ){ 
				fluxOfImages = imageService.findAllImagesOk();
				model.addAttribute("images", fluxOfImages );
			}
			//model.addAttribute("images", fluxOfImages );			
		}catch(Exception e) {
			System.out.println("HomeController | ERROR" + e);
		}
		return Mono.just("indexSocial"); 
 	}
	// end::5[]

	//curl localhost:8082/images/basicrobotlogical.png/raw
	// tag::2[]
	@GetMapping(value = BASE_PATH + "/" + FILENAME + "/raw",
		produces = MediaType.IMAGE_JPEG_VALUE)
	@ResponseBody
	public Mono<ResponseEntity<?>> oneRawImage(		//Book pg. 
								@PathVariable String filename) {
		return imageService.findOneImage(filename)
			.map(resource -> {
				try {
					return ResponseEntity.ok()
						.contentLength(resource.contentLength())
						.body(new InputStreamResource(
							resource.getInputStream()));
				} catch (IOException e) {
					return ResponseEntity.badRequest()
						.body("Couldn't find " + filename +
							" => " + e.getMessage());
				}
			});
	}
	// end::2[]

	//curl localhost:8082/images 
	// tag::3[]
	@PostMapping(value = BASE_PATH)
	public Mono<String> createFile(@RequestPart(name = "file") Flux<FilePart> files) {		//Book pg. 57
		return imageService.createImage(files)
			.then(Mono.just("redirect:/"));
	}
	// end::3[]

	// tag::4[]
	@DeleteMapping(BASE_PATH + "/" + FILENAME)
	public Mono<String> deleteFile(@PathVariable String filename) {		//Book pg. 58
		return imageService.deleteImage(filename)
			.then(Mono.just("redirect:/"));
	}
	// end::4[]

}

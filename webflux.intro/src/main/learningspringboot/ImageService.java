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
package  learningspringboot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;

/**
 * @author Greg Turnquist
 */
// tag::1[]
@Service
public class ImageService {

	private static String UPLOAD_ROOT = "upload-dir";

	private final ResourceLoader resourceLoader;

	public ImageService(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
// end::1[]

	// tag::2[]
	public Flux<Image> findAllImagesOk(){			//By AN to avoid ERROR: Iterator already obtained
		return Flux.just(
			new Image(1, "basicrobotlogical.png"),
			new Image(2, "basicRobotOnRasp.png"),
			new Image(3, "basicrobotproject.png")
		);
	}
	public Flux<Image> findAllImages() {
		System.out.println("ImageService | findAllImages");	 
		try {
			/*
			 * The iterator of the Iterable returned by Files.newDirectoryStream 
			 * (DirectoryStream implements Iterable) can only be used once. 
			 * *******  ERROR: Iterator already obtained  *******
			 * Fixed from https://www.isaacnote.com/2020/05/sprint-boot-list-files-to-flux.html?m=1
			 */
// 			Flux<Image> fluxImage = fluxPath.map( path -> 
//   				new Image( path.hashCode(), path.getFileName().toString() )  
//  				);			
 			return Flux.fromStream(Files.list(Paths.get(UPLOAD_ROOT)))
 		            .map(path -> new Image(path.hashCode(), path.getFileName().toString() )); 			
		} catch (Exception e) {
			System.out.println("ImageService | ERROR: "+e.getMessage());
			return Flux.empty();
		}
	}
	// end::2[]

	// tag::3[]
	public Mono<Resource> findOneImage(String filename) {
		return Mono.fromSupplier(() ->
			resourceLoader.getResource(
				"file:" + UPLOAD_ROOT + "/" + filename));
	}
	// end::3[]

	// tag::4[]
	public Mono<Void> createImage(Flux<FilePart> files) {
		return files
			.flatMap(file -> file.transferTo(
				Paths.get(UPLOAD_ROOT, file.filename()).toFile()))
			.then();
	}
	// end::4[]

	// tag::5[]
	public Mono<Void> deleteImage(String filename) {
		return Mono.fromRunnable(() -> {
			try {
				Files.deleteIfExists(Paths.get(UPLOAD_ROOT, filename));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
	}
	// end::5[]

	// tag::6[]
	/**
	 * Pre-load some test images
	 *
	 * @return Spring Boot {@link CommandLineRunner} automatically
	 *         run after app context is loaded.
	 */
	@Bean
	CommandLineRunner setUp() throws IOException {  //pg. 51
		System.out.println("ImageService | setUp populates");	 
		return (args) -> {
			/*
			FileSystemUtils.deleteRecursively(new File(UPLOAD_ROOT));

			Files.createDirectory(Paths.get(UPLOAD_ROOT));

			FileCopyUtils.copy("Test file",
				new FileWriter(UPLOAD_ROOT +
					"/learning-spring-boot-cover.jpg"));

			FileCopyUtils.copy("Test file2",
				new FileWriter(UPLOAD_ROOT +
					"/learning-spring-boot-2nd-edition-cover.jpg"));

			FileCopyUtils.copy("Test file3",
				new FileWriter(UPLOAD_ROOT + "/bazinga.png"));
		*/
		};
	}
	// end::6[]
}

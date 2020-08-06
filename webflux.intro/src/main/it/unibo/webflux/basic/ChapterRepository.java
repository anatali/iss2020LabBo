package it.unibo.webflux.basic;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ChapterRepository				 //Book pg.19
	extends ReactiveCrudRepository<Chapter, String> {

}

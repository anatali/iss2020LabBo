package com.greglturnquist.learningspringboot.part1;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ChapterRepository				 //Book pg.19
	extends ReactiveCrudRepository<Chapter, String> {

}

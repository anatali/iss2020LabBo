package it.unibo.webflux.basic;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Chapter {		 //Book pg.18

//	@Id
	public String id;		//was private but does not compile
	public String name;		//was private but does not compile

	public Chapter(String name) {
		this.name = name;
	}

}

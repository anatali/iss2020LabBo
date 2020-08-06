package com.greglturnquist.learningspringboot.part1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {  //Book pg.17

	@GetMapping
	public String greeting(@RequestParam(required = false,
							defaultValue = "") String name) {
		return name.equals("")
			? "Hey!"
			: "Hey, " + name + "!";
	}

}

package it.unibo.robotWeb2020;

public class ResourceRep {

	private String content;

	public ResourceRep() {
	}

	public ResourceRep(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

}

/*
 * Spring will use the Jackson JSON library to automatically 
 * marshal instances of type Greeting into JSON.
*/
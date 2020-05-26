package it.unibo.robotWeb2020;

public class RequestMessageOnSock {

	private String name;

	public RequestMessageOnSock() {
	}

	public RequestMessageOnSock(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

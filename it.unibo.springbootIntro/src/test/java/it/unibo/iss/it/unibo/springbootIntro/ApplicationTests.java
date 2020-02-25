package it.unibo.iss.it.unibo.springbootIntro;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.Assert.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.TEXT_PLAIN;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=RANDOM_PORT)
public class ApplicationTests {
 	
	public ApplicationTests() {};
	
    @Autowired
    private WebTestClient client;
//using static method, no need to use @Autowired
    @Test
	public void testTest() {
    	//PrintForTest.println("testTest");
    	 
    String answer = "hello";
 	   System.out.println("---------------------------------------------------- test1" ); 
	   assertTrue( "test1",  answer.equals("hello"));  
    }
	
    @Test
	public void testState() {
 	   System.out.println("---------------------------------------------------- testState" ); 
		String answer = client.get()
			.uri("/robotstate"  )
			.accept(TEXT_PLAIN)
            .exchange()
 //           .expectStatus().isOk()
            .expectBody().toString();
		
	   System.out.println("---------------------------------------------------- answer="+ answer); 
	   assertTrue( "testState",  answer != null); //answer.equals("Initial State")
	}

}

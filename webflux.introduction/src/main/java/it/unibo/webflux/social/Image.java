package it.unibo.webflux.social;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Greg Turnquist
 * Property or field 'id' cannot be found on object of type 'com.greglturnquist.learningspringboot.Image' - maybe not public or not valid?
 * com.greglturnquist.learningspringboot
 */
// tag::code[]
@Data
@NoArgsConstructor
public class Image {		//Book pg. 47

//	private int id;
//	private String name;
	public int id;					//By AN
	public String name;				//By AN

	public Image(int id, String name) {
		this.id = id;
		this.name = name;
	}
}
// end::code[]
package it.unibo.iss.it.unibo.springbootIntro;

import org.springframework.stereotype.Controller;

@Controller
public class PrintForTest {
    public static void println(String msg){
        System.out.println("-------------------------------------- "+msg);
    }
}

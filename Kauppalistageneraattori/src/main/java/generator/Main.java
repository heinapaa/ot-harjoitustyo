package generator;

import generator.ui.GeneratorTextUI;
import java.util.Scanner;

/**
 *
 * @author heinapaa
 */

public class Main {
    
    public static void main(String[] args) throws Exception {  
        Scanner lukija = new Scanner(System.in);
        
        GeneratorTextUI generaattori = new GeneratorTextUI(lukija);
        generaattori.kaynnista(); 
    }
}

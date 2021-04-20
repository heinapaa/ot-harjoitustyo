package generator;

import generator.ui.GeneratorTextUI;
import generator.ui.GeneratorUI;
import java.util.Scanner;

public class Main {
    
    public static void main(String[] args) throws Exception {  
        Scanner lukija = new Scanner(System.in);
        GeneratorTextUI generaattori = new GeneratorTextUI(lukija);
        generaattori.kaynnista(); 
        
        //GeneratorUI.main(args);        
    }
}


package generator.ui;

import java.util.Scanner;

/**
 *
 * @author heinapaa
 */
public class GeneratorTextUI {
    
    private Scanner lukija;
    
    public GeneratorTextUI(Scanner lukija) {
        this.lukija = lukija;
    }
    
    public void kaynnista() {
        System.out.println("Anna syote: ");
        String syote = lukija.nextLine();
        System.out.println("Syote oli: " + syote);
        System.out.println("Kiitos ja hyvää päivänjatkoa!");
    }
    
}

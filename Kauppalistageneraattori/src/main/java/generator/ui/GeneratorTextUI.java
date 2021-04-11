
package generator.ui;

import generator.domain.Recipe;
import generator.domain.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

/**
 *
 * @author heinapaa
 */

public class GeneratorTextUI {
    
    private Scanner lukija;
    private Map<String, String> komennot;
    
    public GeneratorTextUI(Scanner lukija) {
        this.lukija = lukija;
        komennot = new TreeMap<>();
        
        komennot.put("x", "x lopeta");
        komennot.put("1", "1 lisää resepti");
        komennot.put("2", "2 poista resepti");        
        komennot.put("3", "3 listaa reseptit");
        komennot.put("4", "4 luo uusi ostoslista");
        
    }
    
    public void kaynnista() {
        System.out.println("Reseptilista");
        System.out.println("");
        System.out.println("Syötä nimi:");
        String nimi = lukija.nextLine();
        
        User kayttaja = new User(nimi);
        
        while (true) {
            tulostaOhjeet();
            System.out.println("Anna syöte:");
            String syote = lukija.nextLine();
            System.out.println("");
            if (syote.equals("x")) break;
            else if (syote.equals("1")) lisaaResepti(kayttaja);
            else if (syote.equals("2")) poistaResepti(kayttaja);
            else if (syote.equals("3")) listaaReseptit(kayttaja);
            else if (syote.equals("4")) System.out.println("Toiminnallisuus tulossa!");
            else System.out.println("Viallinen syöte, yritä uudelleen!");
        }
        System.out.println("Kiitos ja hyvää päivänjatkoa!");
    }
    
    public void tulostaOhjeet() {
        System.out.println("");
        for (String komento : komennot.keySet()) System.out.println(komennot.get(komento));
        System.out.println("");
    }
    
    public void lisaaResepti(User kayttaja) {
        System.out.println("Reseptin nimi:");
        String reseptinNimi = lukija.nextLine();
        System.out.println("Reseptin annoskoko:");
        int reseptinAnnosKoko = Integer.valueOf(lukija.nextLine());
        
        Recipe uusiResepti = new Recipe(reseptinNimi, reseptinAnnosKoko);
        
        while (true) {
            System.out.println("Lisää aines y/n:");
            String valinta = lukija.nextLine();
            
            if (valinta.equals("n")) break;
            
            System.out.println("Ainesosa:");
            String ainesosanNimi = lukija.nextLine();
            System.out.println("Yksikkö:");
            String ainesosanYksikko = lukija.nextLine();
            System.out.println("Määrä:");
            double ainesosanMaara = Integer.valueOf(lukija.nextLine());
            
            uusiResepti.addIngredient(ainesosanNimi, ainesosanMaara, ainesosanYksikko);
        }
        
        kayttaja.addRecipe(uusiResepti);
    }
    
    public void poistaResepti(User kayttaja) {
        System.out.println("Syötä poistettavan reseptin nimi:");
        String reseptinNimi = lukija.nextLine();
        Recipe poistettavaResepti = kayttaja.getRecipeByName(reseptinNimi);
        kayttaja.removeRecipe(poistettavaResepti);
    }
    
    public void listaaReseptit(User kayttaja) {
        Map<String, Recipe> reseptit = kayttaja.getRecipes();
        for (String reseptinNimi : reseptit.keySet()) System.out.println(reseptinNimi);
    }
    
}

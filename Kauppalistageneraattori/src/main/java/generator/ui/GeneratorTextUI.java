
package generator.ui;

import generator.domain.Ingredient;
import generator.domain.Recipe;
import generator.domain.User;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Paths;
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
    
    public void kaynnista() throws Exception {
        System.out.println("Reseptilista");
        System.out.println("");
        System.out.println("Syötä nimi:");
        String nimi = lukija.nextLine();
        
        User kayttaja = new User(nimi);
        
        if (onKayttaja(nimi)) {
            kayttaja = lueKayttajanTiedot(nimi);
        }

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
        
        tallennaReseptit(kayttaja);
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
    
    public Boolean onKayttaja(String nimi) throws Exception {
        
        File userList = new File("users.txt");
        
        if (userList.exists()) {
            try (Scanner tiedostonLukija = new Scanner(Paths.get("users.txt"))) {
                while (tiedostonLukija.hasNextLine()) {
                    if (tiedostonLukija.nextLine().equals(nimi)) return true;
                }
            }            
        }
        
        return false;
               
    }
    
    public User lueKayttajanTiedot(String nimi) throws Exception {
        
        User kayttaja = new User(nimi);
        
        try (Scanner tiedostonLukija = new Scanner(Paths.get(nimi + ".txt"))) {
            
            String rivi = tiedostonLukija.nextLine();
            String[] palat = rivi.split(",");
            Recipe resepti = new Recipe(palat[0], Integer.valueOf(palat[1]));       
            
            while (tiedostonLukija.hasNextLine()) {
                
                rivi = tiedostonLukija.nextLine();
                palat = rivi.split(",");
                        
                if (!palat[0].equals("-")) {
                    kayttaja.addRecipe(resepti);
                    resepti = new Recipe(palat[0], Integer.valueOf(palat[1]));
                } else {
                    Ingredient aines = new Ingredient(palat[1], Double.valueOf(palat[2]), palat[3]);
                }
            }
            
            kayttaja.addRecipe(resepti);
            
        }  
        
        return kayttaja;
       
    }
    
    public void tallennaReseptit(User kayttaja) throws Exception {
        
        FileWriter kirjoittaja = new FileWriter("users.txt", true);
        
        if (!onKayttaja(kayttaja.getUsername())) {
            kirjoittaja.write(kayttaja.getUsername() + "\n");
            kirjoittaja.close();
        }
        
        if (!kayttaja.getRecipes().isEmpty()) {
            kirjoittaja = new FileWriter(kayttaja.getUsername() + ".txt");

            for (Recipe resepti : kayttaja.getRecipes().values()) {
                kirjoittaja.write(resepti.getName() + "," + resepti.getServing() + "\n");
                
                for (Ingredient aines : resepti.getIngredients()) {
                    kirjoittaja.append("-" + "," + aines.getName() + "," + aines.getAmount() + "," + aines.getUnit() + "\n");
                }
            }

            kirjoittaja.close();            
        }
                

    }
    
}

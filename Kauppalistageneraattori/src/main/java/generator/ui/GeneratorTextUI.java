
package generator.ui;

import generator.dao.FileRecipeDao;
import generator.dao.FileUserDao;
import generator.dao.RecipeDao;
import generator.dao.UserDao;
import generator.domain.Ingredient;
import generator.domain.Recipe;
import generator.domain.RecipeService;
import generator.domain.User;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class GeneratorTextUI {
    
    private Scanner lukija;
    private Map<String, String> komennot;
    
    private UserDao userDao;
    private RecipeDao recipeDao;
    private RecipeService recipeService;
    
    public GeneratorTextUI(Scanner lukija) throws Exception {
        
        this.lukija = lukija;
        this.userDao = new FileUserDao();
        this.recipeDao = new FileRecipeDao(userDao);
        this.recipeService = new RecipeService(userDao, recipeDao);
        
        this.komennot = new TreeMap<>();
        
        komennot.put("x", "x lopeta");
        komennot.put("1", "1 lisää resepti");
        komennot.put("2", "2 poista resepti");        
        komennot.put("3", "3 listaa reseptit");
        komennot.put("4", "4 luo uusi ostoslista");
        komennot.put("5", "5 vaihda käyttäjää");
        
    }
    
    public void kaynnista() throws Exception {
        System.out.println("Reseptilista");
        System.out.println("");
        
        kirjaudu();
  
        while (true) {
            tulostaOhjeet();
            System.out.println("Anna syöte:");
            String syote = lukija.nextLine();
            System.out.println("");
            
            if (syote.equals("x")) break;
            else if (syote.equals("1")) lisaaResepti();
            else if (syote.equals("2")) poistaResepti();
            else if (syote.equals("3")) listaaReseptit();
            else if (syote.equals("4")) System.out.println("Toiminnallisuus tulossa!");
            else if (syote.equals("5")) kirjaudu();
            else System.out.println("Viallinen syöte, yritä uudelleen!");
        }
        
        System.out.println("Kiitos ja hyvää päivänjatkoa!");
    }
    
    public void kirjaudu() throws Exception {
        System.out.println("Syötä nimi:");
        String nimi = lukija.nextLine();
        recipeService.login(nimi);        
    }
    
    public void tulostaOhjeet() {
        System.out.println("");
        for (String komento : komennot.keySet()) System.out.println(komennot.get(komento));
        System.out.println("");
    }
    
    public void lisaaResepti() throws Exception {
        System.out.println("Reseptin nimi:");
        String reseptinNimi = lukija.nextLine();
        System.out.println("Reseptin annoskoko:");
        int reseptinAnnosKoko = Integer.valueOf(lukija.nextLine());
        
        recipeService.createRecipe(reseptinNimi, reseptinAnnosKoko);
    }
    
    public void poistaResepti() throws Exception {
        System.out.println("Syötä poistettavan reseptin nimi:");
        String reseptinNimi = lukija.nextLine();
        
        recipeService.removeRecipe(reseptinNimi);
    }
    
    public void listaaReseptit() {
        List<Recipe> reseptit = recipeService.getAllRecipes();
        for (Recipe resepti : reseptit) System.out.println(resepti.getName());            
    }
    
}

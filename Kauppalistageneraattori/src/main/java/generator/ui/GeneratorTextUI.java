
package generator.ui;

import generator.dao.FileIngredientDao;
import generator.dao.FileRecipeDao;
import generator.dao.FileUserDao;
import generator.dao.IngredientDao;
import generator.dao.RecipeDao;
import generator.dao.UserDao;
import generator.domain.Ingredient;
import generator.domain.Recipe;
import generator.domain.RecipeService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class GeneratorTextUI {
    
    private Scanner lukija;
    private Map<String, String> komennot;
    private UserDao userDao;
    private RecipeDao recipeDao;
    private IngredientDao ingredientDao;
    private RecipeService recipeService;

    
    public GeneratorTextUI(Scanner lukija) throws Exception {
        
        this.lukija = lukija;
        this.userDao = new FileUserDao();
        this.recipeDao = new FileRecipeDao(userDao);
        this.ingredientDao = new FileIngredientDao(recipeDao);
        this.recipeService = new RecipeService(userDao, recipeDao, ingredientDao);
        
        this.komennot = new TreeMap<>();
        
        komennot.put("x", "x lopeta");
        komennot.put("1", "1 lisää resepti");
        komennot.put("2", "2 poista resepti");        
        komennot.put("3", "3 listaa reseptit");
        komennot.put("4", "4 luo uusi ostoslista");
        komennot.put("5", "5 vaihda käyttäjää");
        
    }
    
    public void kaynnista() throws Exception {
        System.out.println("Kauppalistageneraattori");
        System.out.println("***********************");
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
            else if (syote.equals("3")) tarkasteleResepteja();
            else if (syote.equals("4")) luoOstoslista();
            else if (syote.equals("5")) kirjaudu();
            else System.out.println("Viallinen syöte, yritä uudelleen!");
        }
        
        System.out.println("Kiitos ja hyvää päivänjatkoa!");
    }
    
    public void kirjaudu() throws Exception {
        System.out.println("Anna käyttäjä:");
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
        System.out.println("");
        System.out.println("Reseptin annoskoko:");
        int reseptinAnnosKoko = Integer.valueOf(lukija.nextLine());
        System.out.println("");
        
        recipeService.createRecipe(reseptinNimi, reseptinAnnosKoko);
        
        while (true) {
            System.out.println("Lisätäänkö ainesosa? (y/n)");
            String kasky = lukija.nextLine();
            
            if (kasky.equals("n")) break;
            if (kasky.equals("y")) {
                System.out.println("Ainesosan nimi:");
                String ainesosanNimi = lukija.nextLine();
                System.out.println("Ainesosan yksikkö:");
                String ainesosanYksikko = lukija.nextLine();
                System.out.println("Ainesosan määrä:");
                double ainesosanMaara = Double.parseDouble(lukija.nextLine());
                recipeService.addIngredient(reseptinNimi, ainesosanNimi, ainesosanYksikko, ainesosanMaara);
            }
            else System.out.println("Virheellinen syöte, yritä uudelleen!");
        }
    }
    
    public void poistaResepti() throws Exception {
        System.out.println("Syötä poistettavan reseptin nimi:");
        String reseptinNimi = lukija.nextLine();
        
        recipeService.removeRecipe(reseptinNimi);
    }
    
    public void tarkasteleResepteja() {
        while (true) {
            listaaReseptit();
            System.out.println("");
            System.out.println("Kirjoita reseptin nimi jos haluat nähdä ainekset / x = palaa");
            String valinta = lukija.nextLine();
            
            if (valinta.equals("x")) break;
            else listaaAinekset(valinta);
        }          
    }
    
    public void listaaReseptit() {
        List<Recipe> reseptit = recipeService.getAllRecipes();
        System.out.println("Tallennetut reseptit: ");
        for (Recipe resepti : reseptit) System.out.println(resepti.getName());
 
    }
    
    public void listaaAinekset(String resepti) {;
        List<Ingredient> ainesLista = recipeService.getIngredients(resepti);
        
        System.out.println("");
        System.out.println("Ainekset reseptissä " + resepti + ":");
        for (Ingredient aines : ainesLista) System.out.println(aines);
        System.out.println("");
    }
    
    public void luoOstoslista() {
        listaaReseptit();
        System.out.println("");
        System.out.println("Kirjoita reseptin nimi jonka haluat lisätä listalle / x = valmis");        

        List<String> valinnat = new ArrayList<>();
        
        while (true) {
            String kasky = lukija.nextLine();
            if (kasky.equals("x")) break;
            else valinnat.add(kasky);
        }
        
        List<String> ostoslista = recipeService.createShoppingList(valinnat);
        
        System.out.println("");
        System.out.println("Ostoslista:");
        for (String rivi : ostoslista) System.out.println(rivi);
        System.out.println("");
    }
    
}


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
import generator.domain.ShoppingListService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import org.apache.commons.validator.GenericValidator;

public class GeneratorTextUI {
    
    private Scanner lukija;
    private GenericValidator validator;
    private Map<String, String> komennot;
    private UserDao userDao;
    private RecipeDao recipeDao;
    private IngredientDao ingredientDao;
    private RecipeService recipeService;
    private ShoppingListService shoppingListService;    

    
    public GeneratorTextUI(Scanner lukija) throws Exception {
        
        this.lukija = lukija;
        this.userDao = new FileUserDao();
        this.recipeDao = new FileRecipeDao(userDao);
        this.ingredientDao = new FileIngredientDao(recipeDao);
        this.recipeService = new RecipeService(userDao, recipeDao, ingredientDao);
        this.shoppingListService = new ShoppingListService(userDao, recipeDao, ingredientDao);        
        
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
        boolean login = false;
        while (true) {
            if (login) break;
            System.out.println("Anna käyttäjätunnus (vähintään 3 kirjainta):");
            String nimi = lukija.nextLine();
         
            while (!validator.minLength(nimi, 3)) {
                System.out.println("Liian lyhyt tunnus, yritä uudelleen!");
                nimi = lukija.nextLine();
            }
            
            try {
                login = recipeService.login(nimi);              
            } catch (Exception e) {
                System.out.println("Kirjautuminen epäonnistui, yritä uudestaan!");
            }     
        }   
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
        
        try {
            recipeService.createRecipe(reseptinNimi, reseptinAnnosKoko);
        } catch (Exception e) {
            System.out.println("Reseptin lisäys epäonnistui :(");
        }

        while (true) {
            System.out.println("Lisätäänkö ainesosa? (y/n)");
            String kasky = lukija.nextLine();
            if (kasky.equals("n")) {
                break;
            } else if (kasky.equals("y")) {
                System.out.println("Ainesosan nimi:");
                String ainesosanNimi = lukija.nextLine();
                System.out.println("Ainesosan yksikkö:");
                String ainesosanYksikko = lukija.nextLine();
                System.out.println("Ainesosan määrä:");
                double ainesosanMaara = Double.parseDouble(lukija.nextLine());
                try {
                    recipeService.addIngredient(reseptinNimi, ainesosanNimi, ainesosanYksikko, ainesosanMaara);
                    System.out.println("Ainesosa lisätty!");
                } catch (Exception e) {
                    System.out.println("Ainesosan lisäys epäonnistui :(");
                }
            }
            else {
                System.out.println("Virheellinen syöte, yritä uudelleen!");
            }
        }            

    }   
    
    public void poistaResepti() throws Exception {
        System.out.println("Syötä poistettavan reseptin nimi:");
        String reseptinNimi = lukija.nextLine();
        try {
            recipeService.removeRecipe(reseptinNimi);
        } catch (Exception e) {
            System.out.println("Reseptin poistaminen epäonnistui :(");
        }
    }
    
    public void tarkasteleResepteja() throws Exception {
        while (true) {
            listaaReseptit();
            System.out.println("");
            System.out.println("Kirjoita reseptin nimi jos haluat nähdä ainekset / x = palaa");
            String valinta = lukija.nextLine();
            
            if (valinta.equals("x")) {
                break;
            } else if (!recipeService.recipeExists(valinta)) {
                System.out.println("Virheellinen syöte, yritä uudelleen");
            } else {
                listaaAinekset(valinta);
            }
        }          
    }
    
    public void listaaReseptit() {
        List<Recipe> reseptit = recipeService.getAllRecipes();
        System.out.println("Tallennetut reseptit: ");
        for (Recipe resepti : reseptit) System.out.println(resepti.getName());
 
    }
    
    public void listaaAinekset(String resepti) throws Exception {
        if(!recipeService.recipeExists(resepti)) {
            System.out.println("Virheellinen syöte, reseptiä ei olemassa!"); 
            return;
        }
        
        List<Ingredient> ainesLista = recipeService.getIngredients(resepti);

        System.out.println("");
        System.out.println("Ainekset reseptissä " + resepti + ":");
        if (ainesLista.size() == 0) {
            System.out.println("Ei aineksia.");
        } else {
            for (Ingredient aines : ainesLista) System.out.println(aines);            
        }
        System.out.println("");
    }
    
    public void luoOstoslista() throws Exception {
        listaaReseptit();
        System.out.println("");
        System.out.println("Kirjoita reseptin nimi jonka haluat lisätä listalle / x = valmis");        

        List<String> valinnat = new ArrayList<>();
        
        while (true) {
            String kasky = lukija.nextLine();
            if (kasky.equals("x")) {
                break;
            } else if (!recipeService.recipeExists(kasky)) {
                System.out.println("Virheellinen syöte, yritä uudelleen");
            } else {
                valinnat.add(kasky);
            }
        }
        
        String ostosLista = shoppingListService.createShoppingList(valinnat);
        
        System.out.println("");
        System.out.println("Ostoslista:");
        System.out.println(ostosLista);
        System.out.println("");
    }
    
}

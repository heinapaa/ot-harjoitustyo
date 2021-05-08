package generator.dao.file;

import generator.dao.RecipeDao;
import generator.dao.UserDao;
import generator.models.Recipe;
import generator.models.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Reseptejä tekstitiedostoon tallentava luokka, joka laajentaa FileDao-luokkaa ja toteuttaa RecipeDao-rajapinnan.
 */

public class FileRecipeDao extends FileDao implements RecipeDao {
    
    private List<Recipe> recipes;
    int latestId;
    
    /**
     * Kostruktori
     * @param fileName  Tiedoston nimi, johon reseptit halutaan tallentaa
     * @param users UserDao-rajapinnan toteuttava olio
     * @throws java.io.IOException
     */
    
    public FileRecipeDao(String fileName, UserDao users) throws IOException {
        super(fileName);
        this.recipes = new ArrayList<>();  
        this.latestId = 1;
        
        for (String line : super.lines) {
            String[] palat = line.split(";;");    
            recipes.add(new Recipe(Integer.valueOf(palat[0]), palat[1], Integer.valueOf(palat[2]), palat[3], users.findByUsername(palat[4])));
            if (Integer.valueOf(palat[0]) > latestId) {
                latestId = Integer.valueOf(palat[0]);
            }            
        }               
    }
    
    /**
     * Metodi tallentaa reseptin.
     * @param recipe    tallennettava resepti
     * @return  true jos tallentaminen onnistuu, muuten false
     */

    @Override
    public boolean create(Recipe recipe) {
        Recipe newRecipe = recipe;
        newRecipe.setId(generateId());
        recipes.add(recipe);
        return save();
    }
    
    /**
     * Metodi hakee reseptin tunnisteen (id) perusteella
     * @param id    tunniste, jota vastaava resepti halutaan löytää
     * @return tunnistetta vastaava resepti, null jos reseptiä ei löydy
     */
    
    @Override
    public Recipe findById(int id) {
        for (Recipe recipe : recipes) {
            if (recipe.getId() == id) {
                return recipe;
            }
        }     
        return null;
    }    
    
    /**
     * Metodi hakee reseptin nimen ja käyttäjän perusteella.
     * @param name  haettavan reseptin nimi
     * @param user  käyttäjä, jolle resepti kuuluu
     * @return haettava resepti, null jos reseptiä ei löydy
     */
    
    @Override
    public Recipe findByNameAndUser(String name, User user) {
        for (Recipe recipe : recipes) {
            if (recipe.getName().equals(name) && recipe.getOwner().equals(user)) {
                return recipe;
            }
        }  
        return null;
    }  
    
    
    /**
     * Metodi palauttaa kaikki tietylle käyttäjälle kuuluvat tietyntyyppiset reseptit.
     * @param type  tyyppi, johon kuuluvia reseptejä haetaan
     * @param user  käyttäjä, johon liittyviä reseptejä haetaan
     * @return List-rakenne, joka sisältää kaikki valitulle käyttäjälle kuuluvat tietyntyyppiset reseptit
     */    

    @Override
    public List<Recipe> findByTypeAndUser(String type, User user) {
        List<Recipe> returnRecipes = new ArrayList<>();
        for (Recipe recipe : recipes) {
            if (recipe.getOwner().getUsername().equals(user.getUsername()) && recipe.getType().equals(type)) {
                returnRecipes.add(recipe);
            }
        }
        return returnRecipes;
    }     
    
    /**
     * Metodi palauttaa kaikki tietylle käyttäjälle kuuluvat reseptit.
     * @param user  käyttäjä, johon liittyviä reseptejä haetaan
     * @return List-rakenne, joka sisältää kaikki valitulle käyttäjälle kuuluvat reseptit
     */

    @Override
    public List<Recipe> findByUser(User user) {
        List<Recipe> userRecipes = new ArrayList<>();
        for (Recipe recipe : recipes) {
            if (recipe.getOwner().getUsername().equals(user.getUsername())) {
                userRecipes.add(recipe);
            }
        }
        return userRecipes;
    }   
    
    /**
     * Metodi palauttaa kaikki reseptit
     * @return List-rakenne, joka sisältää kaikki tallennetut reseptit
     */

    @Override
    public List<Recipe> findAll() {
        return recipes;
    }  
    
    /**
     * Metodi päivittää valitun reseptin tietoja annettujen syötteiden perusteella.
     * @param newName   syötteenä annettu uusi nimi
     * @param newPortion    syötteenä annettu uusi annoskoko
     * @param newType   syötteenä annettu uusi reseptityyppi
     * @param recipe    resepti, jonka tietoja halutaan muuttaa
     * @return true jos reseptin tietojen päivitys onnistuu, muuten false
     */
    
    @Override
    public boolean update(String newName, int newPortion, String newType, Recipe recipe) {
        Recipe recipeToUpdate = recipe;
        recipes.remove(recipe);
        recipeToUpdate.setName(newName);
        recipeToUpdate.setPortion(newPortion);
        recipeToUpdate.setType(newType);
        recipes.add(recipeToUpdate);
        return save();
    }    
    
    /**
     * Metodi poistaa valitun reseptin.
     * @param recipe    resepti, joka halutaan poistaa
     * @return true jos reseptin poistaminen onnistuu, muuten false
     */

    @Override
    public boolean remove(Recipe recipe) {
        recipes.remove(recipe);
        return save();
    }
    
    /**
     * Metodi generoi uniikin tunnisteen (id) reseptille
     * @return kokonaisluku
     */
    
    private int generateId() {
        latestId++;
        return latestId;
    } 
    
    private boolean save() {
        super.lines = new ArrayList<>();
        for (Recipe recipe : recipes) {
            lines.add(recipe.getId() + ";;" + recipe.getName() + ";;" + recipe.getPortion() + ";;" + recipe.getType() + ";;" + recipe.getOwner().getUsername() + "\n");
        }   
        return super.writeToFile();
    }      

}
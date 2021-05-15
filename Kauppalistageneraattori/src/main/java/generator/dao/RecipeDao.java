package generator.dao;

import generator.models.Recipe;
import generator.models.User;

import java.util.List;

/**
 * Reseptien tallennukseen liittyvien luokkien rajapinta.
 */

public interface RecipeDao {
    
    /**
     * Tallentaa reseptin 
     * @param recipe tallennettava resepti
     * @return true jos tallennus onnistuu, muuten false
     */
    
    boolean create(Recipe recipe);
    
    /**
     * Hakee reseptin tunnisteen (id) perusteella
     * @param id    tunniste, jota vastaava resepti halutaan löytää
     * @return tunnistetta vastaava resepti, null jos reseptiä ei löydy
     */
    
    Recipe findById(int id);    
    
    /**
     * Hakee reseptin nimen ja käyttäjän perusteella.
     * @param name  haettavan reseptin nimi
     * @param user  käyttäjä, jolle resepti kuuluu
     * @return haettava resepti, null jos reseptiä ei löydy
     */
    
    Recipe findByNameAndUser(String name, User user);

    /**
     * Palauttaa kaikki tietylle käyttäjälle kuuluvat tietyntyyppiset reseptit.
     * @param type  tyyppi, johon kuuluvia reseptejä haetaan
     * @param user  käyttäjä, johon liittyviä reseptejä haetaan
     * @return List-rakenne, joka sisältää kaikki valitulle käyttäjälle kuuluvat tietyntyyppiset reseptit
     */     
    
    List<Recipe> findByTypeAndUser(String type, User user);
    
    /**
     * Palauttaa kaikki tietylle käyttäjälle kuuluvat reseptit.
     * @param user  käyttäjä, johon liittyviä reseptejä haetaan
     * @return List-rakenne, joka sisältää kaikki valitulle käyttäjälle kuuluvat reseptit
     */
    
    List<Recipe> findByUser(User user);    

    /**
     * Palauttaa kaikki reseptit
     * @return List-rakenne, joka sisältää kaikki tallennetut reseptit
     */    
    
    List<Recipe> findAll();
    
    /**
     * Päivittää valitun reseptin tietoja annettujen syötteiden perusteella.
     * @param newName   syötteenä annettu uusi nimi
     * @param newPortion    syötteenä annettu uusi annoskoko
     * @param newType   syötteenä annettu uusi reseptityyppi
     * @param recipe    resepti, jonka tietoja halutaan muuttaa
     * @return true jos reseptin tietojen päivitys onnistuu, muuten false
     */
    
    boolean update(String newName, int newPortion, String newType, Recipe recipe);

    /**
     * Poistaa valitun reseptin.
     * @param recipe    resepti, joka halutaan poistaa
     * @return true jos reseptin poistaminen onnistuu, muuten false
     */    
    
    boolean remove(Recipe recipe);    
    
}

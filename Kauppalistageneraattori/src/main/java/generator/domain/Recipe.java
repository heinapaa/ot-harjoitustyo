package generator.domain;

/**
 * Yksittäistä reseptiä vastaava luokka.
 */

public class Recipe implements Comparable<Recipe> {
    
    private int id;
    private String name;
    private int portion;
    private String type;
    private User owner;
    
    /**
     * Konstruktori, kun reseptin yksilöivä id on tiedossa.
     * @param id        Reseptin yksilöivä tunnus
     * @param name      Reseptin nimi
     * @param portion   Reseptin annoskoko
     * @param type      Reseptin tyyppi
     * @param user      Käyttäjä, jolle resepti kuuluu
     */
    
    public Recipe(int id, String name, int portion, String type, User user) {
        this.id = id;
        this.name = name;
        this.portion = portion;
        this.type = type;
        this.owner = user;
    }   
    
    /**
     * Konstruktori, kun reseptin yksilöivä id ei ole tiedossa.
     * @param name      Reseptin nimi
     * @param portion   Reseptin annoskoko
     * @param type      Reseptin tyyppi
     * @param owner     Käyttäjä, jolle resepti kuuluu
     */
    
    public Recipe(String name, int portion, String type, User owner) {
        this.name = name;
        this.portion = portion;
        this.type = type;
        this.owner = owner;
    }

    public void setName(String newName) {
        this.name = newName;
    }
    

    public String getName() {
        return this.name;
    }    
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getId() {
        return this.id;
    }

    public void setPortion(int newPortion) {
        this.portion = newPortion;
    }        

    public int getPortion() {
        return this.portion;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getType() {
        return this.type;
    }
    
    public User getOwner() {
        return this.owner;
    }
    
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object == null) {
            return false;
        } else if (getClass() != object.getClass()) {
            return false;
        } else {
            Recipe resepti = (Recipe) object;
            if (resepti.getId() != this.id
                    || !resepti.getName().equals(this.name)
                    || resepti.getPortion() != this.portion
                    || !resepti.getType().equals(this.type)
                    || !resepti.getOwner().equals(this.owner)) {
                return false;
            }          
        }
        return true;
    }  
    
    public int compareTo(Recipe otherRecipe) {
        return this.name.compareTo(otherRecipe.getName());
    }
     
}

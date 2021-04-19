package generator.domain;

public class Recipe {
    
    private int id;
    private String name;
    private int serving;
    private User owner;
    
    public Recipe(int id, String name, int serving, User user) {
        this.id = id;
        this.name = name;
        this.serving = serving;
        this.owner = user;
    }   
    
    public Recipe(String name, int serving, User owner) {
        this.name = name;
        this.serving = serving;
        this.owner = owner;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getServing() {
        return serving;
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
            if (resepti.getId() != this.id) {
                return false;
            } else if (!resepti.getName().equals(this.name)) {
                return false;
            } else if (resepti.getServing() != this.serving) {
                return false;
            } else if (!resepti.getOwner().equals(this.owner)) {
                return false;
            }                 
        }
        return true;
    }     
     
}

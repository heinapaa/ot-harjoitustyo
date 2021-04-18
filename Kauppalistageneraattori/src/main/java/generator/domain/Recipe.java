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
     
}

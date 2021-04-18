package generator.domain;

import java.util.Objects;

public class User {
    
    private String username;
    
    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
    
    @Override
    public boolean equals(Object object) {
        
        if (this == object) {
            return true;
        }
        else if (object == null) {
            return false;
        }
        else if (getClass() != object.getClass()) {
            return false;
        }
        else {
            User kayttaja = (User) object;
            if (kayttaja.getUsername().equals(this.username)) {
                return true;
            }
        }
        return false;
    }
}

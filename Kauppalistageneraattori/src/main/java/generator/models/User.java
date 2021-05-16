package generator.models;

import java.util.Objects;

/**
 * Yksittäistä käyttäjää vastaava luokka. 
 */

public class User {
    
    private final String username;
    
    /**
     * Konstruktori
     * @param username Syötteenä annettu käyttäjänimi
     */
    
    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
    
    /**
     * Metodi tarkistaa, ovatko kaksi oliota sama käyttäjä.
     * @param object    Olio, johon käyttäjää halutaan verrata.
     * @return true jos käyttäjä on sama, muuten false
     */
    
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object == null) {
            return false;
        } else if (getClass() != object.getClass()) {
            return false;
        } else {
            User kayttaja = (User) object;
            if (kayttaja.getUsername().equals(this.username)) {
                return true;
            }
        }
        return false;
    }
}

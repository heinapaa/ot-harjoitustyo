package generator.dao.file;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Tietoja tekstitiedostoon tallentava luokka.
 * 
 */

public class FileDao {

    /**
     * luettavan tiedoston nimi
     */
    public String fileName;
    
    /**
     * lista, johon tallennetaan tiedostosta luetut/tiedostoon kirjoitettavat arvot
     */
    public List<String> lines;
    
    /**
     * Konstruktori, joka tarkistaa onko <code>fileName</code>-nimistä tiedostoa olemassa, ja joko luo tarvittaessa uuden tiedoston tai lukee tiedoston sisältämät arvot  merkkijonoina listaan {@code lines}.
     * @param fileName  luettavan tekstitiedoston nimi
     * @throws java.io.IOException
     */

    public FileDao(String fileName) throws IOException {
        this.fileName = fileName;
        this.lines = new ArrayList<>();
        
        File savefile = new File(fileName);
        
        if ((savefile.exists())) {
            Scanner tiedostonLukija = new Scanner(Paths.get(fileName));
            while (tiedostonLukija.hasNextLine()) {
                lines.add(tiedostonLukija.nextLine());
            }                  
        } else {
            savefile.createNewFile();                 
        }       
    }
    
    /**
     * Metodi tallenta lines-muuttujan arvot riveinä tekstitiedostoon, jonka nimi määräytyy fileName-muuttujan perusteella.
     * @return  true jos tallennus onnistuu, muuten false
     */
    
    public boolean writeToFile() {
        try (FileWriter kirjoittaja = new FileWriter(new File(fileName))) {
            for (String line : lines) {
                kirjoittaja.write(line);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }      

}
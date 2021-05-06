package generator.dao;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Tietoja tekstitiedostoon tallentava luokka.
 * 
 */

public class FileDao {

    public String fileName;
    public List<String> lines;
    
    /**
     * Konstruktori
     * @param fileName  tallennettavan tekstitiedoston nimi
     */

    public FileDao(String fileName) {
        this.fileName = fileName;
        this.lines = new ArrayList<>();
        
        File savefile = new File(fileName);
        if (savefile.exists()) {
            try (Scanner tiedostonLukija = new Scanner(Paths.get(fileName))) {
                while (tiedostonLukija.hasNextLine()) {
                    lines.add(tiedostonLukija.nextLine());
                }
            } catch (Exception e) {
                // JOTAIN
            }             
        } else {
            try {
                savefile.createNewFile();                
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }         
    }
    
    /**
     * Metodi tallentaa olion lines-muuttujan sisällön riveinä tekstitiedostoon, jonka nimi määräytyy fileName-muuttujan perusteella.
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

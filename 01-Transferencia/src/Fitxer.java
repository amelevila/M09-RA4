import java.io.*;
import java.nio.file.*;

public class Fitxer implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nom;
    private byte[] contingut;

    public Fitxer(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public byte[] getCotingut() {
        File f = new File(nom);
        if (!f.exists()) {
            return null;
        }
        try {
            contingut = Files.readAllBytes(Paths.get(nom));
        } catch (IOException e) {
            return null;
        }
        return contingut;
    }
}

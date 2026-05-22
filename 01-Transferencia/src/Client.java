import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.Scanner;

public class Client {
    private static final String DIR_ARRIBADA = System.getProperty("java.io.tmpdir");
    private static final String HOST = "localhost";
    private static final int PORT = 9999;
    private Socket socket;
    private ObjectOutputStream sortida;
    private ObjectInputStream entrada;

    public void connectar() throws IOException {
        System.out.println("Connectant a -> " + HOST + ":" + PORT);
        socket = new Socket(HOST, PORT);
        System.out.println("Connexio acceptada: " + socket.getInetAddress());
        sortida = new ObjectOutputStream(socket.getOutputStream());
        sortida.flush();
        entrada = new ObjectInputStream(socket.getInputStream());
    }

    public void tancarConnexio() throws IOException {
        socket.close();
        System.out.println("Connexio tancada.");
    }

    public void rebreFitxers() throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Nom del fitxer a rebre ('sortir' per sortir): ");
            String nomFitxer = scanner.nextLine().trim();

            if (nomFitxer.equalsIgnoreCase("sortir")) {
                System.out.println("Sortint...");
                break;
            }

            String nomBase = Paths.get(nomFitxer).getFileName().toString();
            String rutaDesti = Paths.get(DIR_ARRIBADA, nomBase).toString();
            System.out.println("Nom del fitxer a guardar: " + rutaDesti);

            try {
                sortida.writeObject(nomFitxer);
                sortida.flush();
                byte[] contingut = (byte[]) entrada.readObject();
                Files.write(Paths.get(rutaDesti), contingut);
                System.out.println("Fitxer rebut i guardat com: " + rutaDesti);
            } catch (ClassNotFoundException e) {
                System.out.println("Error rebent el fitxer: " + e.getMessage());
            }
        }
        scanner.close();
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.connectar();
        client.rebreFitxers();
        client.tancarConnexio();
    }
}

import java.io.*;
import java.net.*;

public class Servidor {
    private static final int PORT = 9999;
    private static final String HOST = "localhost";
    private ServerSocket serverSocket;

    public Socket connectar() throws IOException {
        serverSocket = new ServerSocket(PORT);
        System.out.println("Acceptant connexions en -> " + HOST + ":" + PORT);
        System.out.println("Esperant connexio...");
        Socket socket = serverSocket.accept();
        System.out.println("Connexio acceptada: " + socket.getInetAddress());
        return socket;
    }

    public void tancarConnexio(Socket socket) throws IOException {
        System.out.println("Tancant connexió amb el client: " + socket.getInetAddress());
        socket.close();
        serverSocket.close();
    }

    public void enviarFitxers(Socket socket) throws IOException {
        ObjectOutputStream sortida = new ObjectOutputStream(socket.getOutputStream());
        sortida.flush();
        ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());

        while (true) {
            System.out.println("Esperant el nom del fitxer del client...");
            String nomFitxer = null;
            try {
                nomFitxer = (String) entrada.readObject();
            } catch (Exception e) {
                System.out.println("Error llegint el fitxer del client: " + e.getMessage());
            }

            if (nomFitxer == null || nomFitxer.isEmpty()) {
                System.out.println("Nom del fitxer buit o nul. Sortint...");
                break;
            }

            System.out.println("Nomfitxer rebut: " + nomFitxer);

            Fitxer fitxer = new Fitxer(nomFitxer);
            byte[] contingut = fitxer.getCotingut();

            if (contingut == null) {
                System.out.println("Error: fitxer no trobat o no llegible: " + nomFitxer);
                try { sortida.writeObject(new byte[0]); sortida.flush(); } catch (IOException ignored) {}
                continue;
            }

            System.out.println("Contingut del fitxer a enviar: " + contingut.length + " bytes");

            try {
                sortida.writeObject(contingut);
                sortida.flush();
            } catch (IOException e) {
                System.out.println("Error enviant el fitxer: " + e.getMessage());
                break;
            }

            System.out.println("Fitxer enviat al client: " + nomFitxer);
        }
    }

    public static void main(String[] args) throws IOException {
        Servidor servidor = new Servidor();
        Socket socket = servidor.connectar();
        servidor.enviarFitxers(socket);
        servidor.tancarConnexio(socket);
    }
}

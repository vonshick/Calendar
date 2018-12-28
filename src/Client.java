import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class Client extends Thread{
    private Socket clientSocket;
    private BufferedReader reader;
    private PrintWriter writer;
    private String host;
    private int port;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;

        try {
            clientSocket = new Socket(host, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            writer = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {

        while(true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Podaj odpowiednia liczbe:\n- przesyl: 1\n- odczyt: 2");
            String choice = scanner.nextLine();
            if (choice.equals("1")){
                System.out.println("Podaj wiadomosc do przeslania:\n");
                String string = scanner.nextLine();
                writer.println(string);
            } else if( choice.equals("2")){
                String serverMessage = null;
                try {
                    serverMessage = reader.readLine();
                    if (serverMessage == null){
                        clientSocket.close();
                        break;
                    }
                    else {
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Podałeś złą liczbę - podaj ją jeszcze raz.\n");
            }
        }
    }

    public void sendData(String message){
        writer.println(message);
    }
}


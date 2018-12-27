import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class Client extends Thread{
    Socket clientSocket;
    BufferedReader reader;
    PrintWriter writer;
    String host;
    int port;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() {
//        Socket clientSocket = null;
        try {
//            clientSocket = new Socket("localhost", 1234);
            clientSocket = new Socket(host, port);
//            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//            writer = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        PrintWriter writer = null;
        try {
            writer = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

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
}


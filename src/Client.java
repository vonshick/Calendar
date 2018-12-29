import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Pattern;


public class Client extends Thread{
    private Socket clientSocket;
    private BufferedReader reader;
    private PrintWriter writer;
    private String host;
    private int port;

    Client(String host, int port) {
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

        while(true){
            String serverMessage;
            try {
                serverMessage = reader.readLine();
                if (serverMessage == null){
                    clientSocket.close();
                    break;
                }
                else if (serverMessage.equals("loaded")) {
                    System.out.println("Mamy to!");
                    break;
                }else{
                    System.out.println(serverMessage);
                    String[] parts = serverMessage.split(Pattern.quote("~"));
                    Main.eventsList.add(new Event(parts[0], parts[1], parts[2],
                            parts[3], parts[4], parts[5], parts[6], parts[7], parts[8]));
                    for(String str : parts){
                        System.out.println(str);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        while(true){
            String serverMessage;
            try {
                serverMessage = reader.readLine();
                if (serverMessage == null){
                    clientSocket.close();
//                    System.out.println("");
                    JFrame frame = new JFrame();
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Close when X is clicked
                    JOptionPane.showMessageDialog(frame, "Server connection lost");
                    System.exit(0);
                }
                else {
                    System.out.println(serverMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void sendData(String message){
        writer.println(message);
    }
}


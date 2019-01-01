import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.regex.Pattern;


public class Client extends Thread{
    private Socket clientSocket;
    private BufferedReader reader;
    private PrintWriter writer;
    private String host;
    private int port;
    private JFrame frame;
    Client(){}

    void initialize(String host, int port) {
        this.host = host;
        this.port = port;
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Close when X is clicked

        try {
            clientSocket = new Socket(host, port);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "TCP socket can not be created");
            e.printStackTrace();
            System.exit(0);
        }
        try {
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Buffered reader can not be created");
            e.printStackTrace();
            System.exit(0);
        }
        try {
            writer = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Print writer can not be created");
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void run() {
        while(true){
            String serverMessage;
            System.out.println("Getting startup data from server");
            try {
                serverMessage = reader.readLine();
                if (serverMessage == null){
                    clientSocket.close();
                    JOptionPane.showMessageDialog(frame, "Server connection lost");
                    System.exit(0);
                } else if (serverMessage.equals("loading")) {
                    System.out.println("Startup data from server loaded");
                    break;
                } else {
                    try {
                        System.out.println(serverMessage);
                        String[] parts = serverMessage.split(Pattern.quote("~"));
                        Main.eventsList.add(new Event(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7], parts[8]));
                    }catch(Exception e){
                        JOptionPane.showMessageDialog(frame, "Error while processing data from server");
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
                    JFrame frame = new JFrame();
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Close when X is clicked
                    JOptionPane.showMessageDialog(frame, "Server connection lost");
                    System.exit(0);
                }
                else if (serverMessage.equals("update")){
                    System.out.println("Data updating");
                    Main.eventsList.clear();
                }
                else{
                    System.out.println("New event from server");
                    String[] parts = serverMessage.split(Pattern.quote("~"));
                    System.out.println("Number of data string elements: "+parts.length);
                    Main.eventsList.add(new Event(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7], parts[8]));
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


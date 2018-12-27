import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static ArrayList<Event> eventsList;
    public static Client tcpClient;
    public static void main (String args[]) {
        eventsList = new ArrayList<Event>();
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Pass server IP address:\n");
//        String host = scanner.nextLine();
//        System.out.println("Pass server port:\n");
//        int port = Integer.parseInt(scanner.nextLine());
//        tcpClient = new Client(host, port);
//        tcpClient.start();
//        new Client().start();
        new CalendarInterface().start();
    }
}

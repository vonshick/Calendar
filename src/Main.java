import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static ArrayList<Event> eventsList;
    static Client tcpClient;
    public static void main (String[] args) {
        eventsList = new ArrayList<>();
//        eventsList.add(new Event("czary", 1,1,2,2, "bla bla", 1,1,2018));
//        eventsList.add(new Event("mary", 1,1,2,2, "bla bla", 1,1,2018));
//        eventsList.add(new Event("hokus", 1,1,2,2, "bla bla", 1,1,2018));
//        eventsList.add(new Event("pokus", 1,1,2,2, "bla bla", 1,1,2018));
//        eventsList.add(new Event("simsala", 1,1,2,2, "bla bla", 1,1,2018));
//        eventsList.add(new Event("simsala", 1,1,2,2, "bla bla", 1,1,2018));


//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Pass server IP address:\n");
//        String host = scanner.nextLine();
//        System.out.println("Pass server port:\n");
//        int port = Integer.parseInt(scanner.nextLine());
        int port = 1234;
        String host = "localhost";
        tcpClient = new Client(host, port);
        tcpClient.start();
        new CalendarInterface().start();
    }
}

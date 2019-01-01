import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static ArrayList<Event> eventsList;
    static Client tcpClient;
    public static void main (String[] args) {
        eventsList = new ArrayList<>();
        eventsList.add(new Event("czary", "1","1","2","2", "bla bla", "29","11","2018"));
        eventsList.add(new Event("mary", "1","1","2","2", "bla bla", "29","11","2018"));
        tcpClient = new Client();
        new Host();
    }
}

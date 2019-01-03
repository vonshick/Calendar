import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static ArrayList<Event> eventsList;
    static Client tcpClient;
    public static void main (String[] args) {
        eventsList = new ArrayList<>();
        tcpClient = new Client();
        new Host();
    }
}

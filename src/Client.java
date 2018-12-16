import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client extends Thread{
    public void run() {
        Socket clientSocket = null;
        try {
            clientSocket = new Socket("localhost", 1234);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(true) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            String serverMessage = null;
            try {
                serverMessage = reader.readLine();
                if (serverMessage == null){
                    clientSocket.close();
                }
                else {
                    System.out.println(serverMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

    // ###############	II task  ###############
    //            InputStream inputStream = clientSocket.getInputStream();
    //            byte[] buffer = new byte[100];
    //            int bytesCount = inputStream.read(buffer);
    //            if ( bytesCount == -1) {
    //                clientSocket.close();
    //            }
    //            else {
    //                System.out.write(buffer, 0, bytesCount);
    //            }
        }
    }
}


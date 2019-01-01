import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


class Host {
    private JFrame frame;
    private JButton btnOk;
    private JPanel panel;
    //    private Container pane;
    private JTextField txtAddress, txtPort;
    private JLabel labAddress, labPort;

    Host() {
        initializeElements();
        addElementsToPanel();
        setElementsBounds();
        setUpActionListeners();
        frame.setResizable(false);
        frame.setVisible(true);
    }

    private void initializeElements(){
        frame = new JFrame("Enter the server address");
        frame.setSize(300, 190);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Close when X is clicked
        //display window in the middle of the screen
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
        txtAddress = new JTextField();
        txtPort = new JTextField();
        labAddress = new JLabel("Address");
        labPort = new JLabel("Port");
        btnOk= new JButton("OK" );
        panel = new JPanel(null);
        Container pane = frame.getContentPane();
        pane.setLayout(null);
        pane.add(panel);
    }

    private void addElementsToPanel(){
        panel.add(txtAddress);
        panel.add(txtPort);
        panel.add(labAddress);
        panel.add(labPort);
        panel.add(btnOk);
    }

    private void setElementsBounds(){
        panel.setBounds(10, 10, 290, 190);
        txtAddress.setBounds(80, 10, 200, 30);
        labAddress.setBounds(5, 10, 80, 30);
        txtPort.setBounds(80, 60, 200, 30);
        labPort.setBounds(5, 60, 80,30);
        btnOk.setBounds(105, 110, 80,30);
    }

    private void setUpActionListeners(){
        btnOk.addActionListener(e -> {
            String address = txtAddress.getText();
            if(address.equals("")){
                JOptionPane.showMessageDialog(frame, "Server address can not be empty");
            } else if(txtPort.getText().equals("")){
                JOptionPane.showMessageDialog(frame, "Server port can not be empty");
            } else {
                int port = Integer.parseInt(txtPort.getText());
                Main.tcpClient.initialize(address, port);
                Main.tcpClient.start();
                new CalendarInterface();
                frame.dispose();
            }
        });
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

class DayBrowser {
    private JFrame frame;
    private JScrollPane scrollPane; //The scrollpane
    private JButton btnAdd, btnRemove;
    private JPanel panel;
    private Container pane;
    private JList eventsList;

    DayBrowser(int day, int month, int year) {
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        frame = new JFrame(day +" "+months[month]+" "+ year);
        frame.setSize(400, 500);

        //display window in the middle of the screen
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);

        //add elements to the list
        DefaultListModel model = new DefaultListModel();
        eventsList = new JList(model);
        ArrayList<Event> thisDayEvents = new ArrayList<>();
        for(Event event: Main.eventsList){
            if(Integer.parseInt(event.getDay()) == day && Integer.parseInt(event.getMonth()) == month && Integer.parseInt(event.getYear()) == year) {
                thisDayEvents.add(event);
                model.addElement(event.getName());
            }
        }

        scrollPane = new JScrollPane(eventsList);
        btnAdd = new JButton("New event" );
        btnRemove = new JButton("Remove");
        pane = frame.getContentPane();
        pane.setLayout(null);
        panel = new JPanel(null);

        pane.add(panel);
        panel.add(scrollPane);
        panel.add(btnAdd);
        panel.add(btnRemove);

        //set bounds of elements
        panel.setBounds(10, 10, 380, 480);
        scrollPane.setBounds(10, 10, 360, 400);
        btnAdd.setBounds(250, 420, 100, 30);
        btnRemove.setBounds(50, 420, 100, 30);

        //remove button action
        btnRemove.addActionListener(e -> {
            DefaultListModel model1 = (DefaultListModel) eventsList.getModel();
            int selectedIndex = eventsList.getSelectedIndex();
            if (selectedIndex != -1) {
                //tu usuwamy z serwera!
                //dopiero potem z klienta
                Main.tcpClient.sendData("~");
                Main.tcpClient.sendData(thisDayEvents.get(selectedIndex).concatenateData());
                model1.remove(selectedIndex);
                Main.eventsList.remove(thisDayEvents.get(selectedIndex));
                thisDayEvents.remove(selectedIndex);
            }
        });

        btnAdd.addActionListener(e -> {
            NewEvent newEvent = new NewEvent(day, month, year);
        });

        frame.setResizable(false);
        frame.setVisible(true);
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

class DayBrowser {
    private JFrame frame;
    private JScrollPane scrollPane; //The scrollpane
    private JButton btnAdd, btnRemove;
    private JPanel panel;
    private Container pane;
    private JList eventsList;

    DayBrowser(int day, int month, int year) {
        ArrayList<Event> thisDayEvents = initializeElements(day, month, year);
        addElementsToPanel();
        setElementsBounds();
        setUpActionListeners(thisDayEvents, day, month, year);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    private ArrayList<Event> initializeElements(int day, int month, int year){
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        frame = new JFrame(day +" "+months[month]+" "+ year);
        frame.setSize(400, 500);

        //display frame in the middle of the screen
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
        return thisDayEvents;
    }

    private void addElementsToPanel(){
        pane.add(panel);
        panel.add(scrollPane);
        panel.add(btnAdd);
        panel.add(btnRemove);
    }

    private void setElementsBounds(){
        panel.setBounds(10, 10, 380, 480);
        scrollPane.setBounds(10, 10, 360, 400);
        btnAdd.setBounds(250, 420, 100, 30);
        btnRemove.setBounds(50, 420, 100, 30);
    }

    private void setUpActionListeners(ArrayList<Event> thisDayEvents, int day, int month, int year){
        //remove button action
        btnRemove.addActionListener(e -> {
            DefaultListModel model = (DefaultListModel) eventsList.getModel();
            int selectedIndex = eventsList.getSelectedIndex();
            if (selectedIndex != -1) {
                // '~' means for server that we are going to remove event
                // next we send data of event to remove
                Main.tcpClient.sendData("~");
                Main.tcpClient.sendData(thisDayEvents.get(selectedIndex).concatenateData());
                model.remove(selectedIndex);
                Main.eventsList.remove(thisDayEvents.get(selectedIndex));
                thisDayEvents.remove(selectedIndex);
            }
        });

        btnAdd.addActionListener(e -> {
            NewEvent newEvent = new NewEvent(day, month, year);
            frame.dispose();
        });

        eventsList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList)evt.getSource();
                if (evt.getClickCount() == 2) {
                    int selectedIndex = eventsList.getSelectedIndex();
                    EditEvent edit = new EditEvent(thisDayEvents.get(selectedIndex));
                    frame.dispose();
                }
            }
        });
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class DayBrowser {
    private JFrame frame;
    private JScrollPane scrollPane; //The scrollpane
    private JButton btnAdd, btnRemove;
    private JPanel panel;
    private Container pane;
    private JList<String> eventsList;

    public DayBrowser(String day, int month, int year) {
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        frame = new JFrame(day+" "+months[month]+" "+Integer.toString(year));
        frame.setSize(400, 500);

        //display window in the middle of the screen
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);

        //add elements to the list
        DefaultListModel model = new DefaultListModel();
        eventsList = new JList(model);
        for ( int i = 0; i < months.length; i++ ){
            model.addElement(months[i]);
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
        btnRemove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DefaultListModel model = (DefaultListModel) eventsList.getModel();
                int selectedIndex = eventsList.getSelectedIndex();
                if (selectedIndex != -1) {
                    model.remove(selectedIndex);
                }
            }
        } );

        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                NewEvent newEvent = new NewEvent(day, month, year);
            }
        } );

        frame.setResizable(false);
        frame.setVisible(true);
    }
}

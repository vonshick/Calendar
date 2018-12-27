import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class NewEvent {
    private JFrame frame;
    private JButton btnAdd;
    private JPanel panel;
    private Container pane;
    private JComboBox<Integer> cmbStartHour, cmbStartMinutes, cmbEndHour, cmbEndMinutes;
    private JTextArea txtName, txtDescription;
    private JLabel labName, labStartHour, labEndHour, labDescription;

    public NewEvent(int day, int month, int year) {
        frame = new JFrame("Add new event");
        frame.setSize(400, 500);

        //display window in the middle of the screen
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);

        setUpComboBoxes();
        initializeElements();
        addElementsToPanel();
        setElementsBounds();

        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = txtName.getText();
                String description = txtDescription.getText();
                int startHour = (int)cmbStartHour.getSelectedItem();
                int endHour = (int)cmbEndHour.getSelectedItem();
                int startMinutes = (int)cmbStartMinutes.getSelectedItem();
                int endMinutes = (int)cmbEndMinutes.getSelectedItem();
                Event event = new Event(name, startHour, startMinutes, endHour, endMinutes, description, day, year, month);
                Main.eventsList.add(event);
                //tu akcja, która wyśle eventa na serwer
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        } );

        frame.setResizable(false);
        frame.setVisible(true);
    }

    private void initializeElements(){
        txtName = new JTextArea();
        txtDescription = new JTextArea();
        labName = new JLabel("Name");
        labDescription = new JLabel("Description");
        labEndHour = new JLabel("End hour");
        labStartHour = new JLabel("Start hour");
        btnAdd = new JButton("Add" );
        panel = new JPanel(null);
        pane = frame.getContentPane();
        pane.setLayout(null);
        pane.add(panel);
    }

    private void setUpComboBoxes(){
        cmbStartHour = new JComboBox<Integer>();
        cmbStartMinutes = new JComboBox<Integer>();
        cmbEndHour = new JComboBox<Integer>();
        cmbEndMinutes = new JComboBox<Integer>();

        for (int i = 0; i < 24; i++) {
            cmbStartHour.addItem(i);
            cmbEndHour.addItem(i);
        }

        for (int i = 0; i < 60; i++) {
            cmbStartMinutes.addItem(i);
            cmbEndMinutes.addItem(i);
        }

    }

    private void addElementsToPanel(){
        panel.add(btnAdd);
        panel.add(cmbStartHour);
        panel.add(cmbStartMinutes);
        panel.add(cmbEndHour);
        panel.add(cmbEndMinutes);
        panel.add(txtName);
        panel.add(txtDescription);
        panel.add(labName);
        panel.add(labDescription);
        panel.add(labEndHour);
        panel.add(labStartHour);
    }

    private void setElementsBounds(){
        panel.setBounds(10, 10, 380, 480);
        labName.setBounds(15, 5, 50, 40);
        txtName.setBounds(15, 35, 350, 80);
        labStartHour.setBounds(15, 120, 100, 40);
        cmbStartHour.setBounds(15, 150, 80,40);
        cmbStartMinutes.setBounds(100, 150, 80,40);
        labEndHour.setBounds(15, 190, 100, 40);
        cmbEndHour.setBounds(15, 220, 80,40);
        cmbEndMinutes.setBounds(100, 220, 80,40);
        labDescription.setBounds(15,260,100,40);
        txtDescription.setBounds(15, 290, 350, 120);
        btnAdd.setBounds(150, 420, 80, 30);
    }
}

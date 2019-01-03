import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


class NewEvent {
    private final JFrame frame;
    private JButton btnAdd;
    private JPanel panel;
    //    private Container pane;
    private JComboBox<Integer> cmbStartHour, cmbStartMinutes, cmbEndHour, cmbEndMinutes;
    private JTextArea txtName, txtDescription;
    private JLabel labName, labStartHour, labEndHour, labDescription;

    NewEvent(int day, int month, int year) {
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

        btnAdd.addActionListener(e -> {
            String name = txtName.getText();
            String description = txtDescription.getText();
            int startHour = (int)cmbStartHour.getSelectedItem();
            int endHour = (int)cmbEndHour.getSelectedItem();
            int startMinutes = (int)cmbStartMinutes.getSelectedItem();
            int endMinutes = (int)cmbEndMinutes.getSelectedItem();

            if(name.equals("")){
                JOptionPane.showMessageDialog(frame, "Event's name can't be empty!");
            } else if(description.equals("")){
                    JOptionPane.showMessageDialog(frame, "Event's description can not be empty");
                } else if (startHour > endHour || startHour==endHour && startMinutes>endMinutes) {
                        JOptionPane.showMessageDialog(frame, "Start time has to be earlier than the end time");
                    } else if (description.contains("~") || name.contains("~")){
                            JOptionPane.showMessageDialog(frame, "Text can not contain '~' character");
                        } else if(description.length() > 100 ){
                                JOptionPane.showMessageDialog(frame, "Description can not contain more than 100 characters");
                            } else if(name.length() > 100 ){
                                    JOptionPane.showMessageDialog(frame, "Name can not contain more than 100 characters");
                                } else {
                                    //if input is correct create Event object, send it to the server and save in Main.eventsList
                                    Event event = new Event(name, Integer.toString(startHour), Integer.toString(startMinutes),
                                            Integer.toString(endHour), Integer.toString(endMinutes), description,
                                            Integer.toString(day), Integer.toString(month), Integer.toString(year));
                                    try{
                                        Main.tcpClient.sendData(event.concatenateData());
                                        synchronized (Main.eventsList) {
                                            Main.eventsList.add(event);
                                        }
                                    } catch(Exception writeException) {
                                        JOptionPane.showMessageDialog(frame, "Sending a message to the server failed");
                                        writeException.printStackTrace();
                                    }
                                    DayBrowser browser = new DayBrowser(day, month, year);
                                    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                                }
        });

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
        Container pane = frame.getContentPane();
        pane.setLayout(null);
        pane.add(panel);
    }

    private void setUpComboBoxes(){
        cmbStartHour = new JComboBox<>();
        cmbStartMinutes = new JComboBox<>();
        cmbEndHour = new JComboBox<>();
        cmbEndMinutes = new JComboBox<>();

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

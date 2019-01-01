import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


class EditEvent {
    private JFrame frame;
    private JButton btnChange;
    private JPanel panel;
    //    private Container pane;
    private JComboBox<Integer> cmbStartHour, cmbStartMinutes, cmbEndHour, cmbEndMinutes;
    private JTextArea txtName, txtDescription;
    private JLabel labName, labStartHour, labEndHour, labDescription;

    EditEvent(Event event) {
        initializeElements();
        setUpComboBoxes();
        setElementsValues(event);
        addElementsToPanel();
        setElementsBounds();
        setUpActionListeners(event);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    private void initializeElements(){
        frame = new JFrame("Browse event");
        frame.setSize(400, 500);

        //display window in the middle of the screen
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
        cmbStartHour = new JComboBox<>();
        cmbStartMinutes = new JComboBox<>();
        cmbEndHour = new JComboBox<>();
        cmbEndMinutes = new JComboBox<>();
        txtName = new JTextArea();
        txtDescription = new JTextArea();
        labName = new JLabel("Name");
        labDescription = new JLabel("Description");
        labEndHour = new JLabel("End hour");
        labStartHour = new JLabel("Start hour");
        btnChange = new JButton("Change" );
        panel = new JPanel(null);
        Container pane = frame.getContentPane();
        pane.setLayout(null);
        pane.add(panel);
    }

    private void setUpComboBoxes(){
        for (int i = 0; i < 24; i++) {
            cmbStartHour.addItem(i);
            cmbEndHour.addItem(i);
        }
        for (int i = 0; i < 60; i++) {
            cmbStartMinutes.addItem(i);
            cmbEndMinutes.addItem(i);
        }
    }

    private void setElementsValues(Event event){
        txtName.setText(event.getName());
        txtDescription.setText(event.getDescription());
        cmbStartHour.setSelectedItem(Integer.parseInt(event.getStartHour()));
        cmbStartMinutes.setSelectedItem(Integer.parseInt(event.getStartMinutes()));
        cmbEndHour.setSelectedItem(Integer.parseInt(event.getEndHour()));
        cmbEndMinutes.setSelectedItem(Integer.parseInt(event.getEndMinutes()));
    }

    private void addElementsToPanel(){
        panel.add(btnChange);
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
        btnChange.setBounds(150, 420, 80, 30);
    }

    private void setUpActionListeners(Event event){
        btnChange.addActionListener(e -> {
            //first remove old version of the event from main() and from the server
            Main.tcpClient.sendData("~");
            Main.tcpClient.sendData(event.concatenateData());
            Main.eventsList.remove(event);

            //next set its values once again
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
                }
                else if (startHour > endHour || startHour==endHour && startMinutes>endMinutes) {
                        JOptionPane.showMessageDialog(frame, "Start time has to be earlier than the end time");
                    } else if (description.contains("~") || name.contains("~")){
                            JOptionPane.showMessageDialog(frame, "Text can not contain '~' character");
                        } else {
                            Event ev = new Event(name, Integer.toString(startHour), Integer.toString(startMinutes),
                                    Integer.toString(endHour), Integer.toString(endMinutes), description,
                                    event.getDay(), event.getMonth(), event.getYear());
                            try{
                                Main.tcpClient.sendData(ev.concatenateData());
                                Main.eventsList.add(ev);
                            } catch(Exception writeException) {
                                writeException.printStackTrace();
                            }
                            frame.dispose();
                            new DayBrowser(Integer.parseInt(event.getDay()),
                                    Integer.parseInt(event.getMonth()),
                                    Integer.parseInt(event.getYear()));
                        }
        });
    }
}

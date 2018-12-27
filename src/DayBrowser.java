import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class DayBrowser {
    private JFrame frame;
    private JScrollPane scrollPane; //The scrollpane
    private JButton add, remove;

    public DayBrowser(String day, int month, int year) {


        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        this.frame = new JFrame(day+" "+months[month]+" "+Integer.toString(year));
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500);

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);

        this.scrollPane = new JScrollPane();
        this.add = new JButton();
        this.remove = new JButton();
        frame.setResizable(false);
        frame.setVisible(true);
    }

}

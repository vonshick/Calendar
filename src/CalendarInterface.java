import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class CalendarInterface extends Thread {
    static JLabel labelMonth, labelYear;
    static JButton buttonBack, buttonNext;
    static JComboBox comboBoxYear;
    static JTable tableCalendar;
    static JFrame mainFrame;
    static JPanel panelCalendar;
    static Container pane;
    static DefaultTableModel modelTableCalendar; //Table model
    static JScrollPane scrollPaneCalendar; //The scrollpane
    static int realYear, realMonth, realDay, currentYear, currentMonth;

    public void run() {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        } catch (UnsupportedLookAndFeelException e) {
        }


        //Prepare frame
        mainFrame = new JFrame("Calendar");
        mainFrame.setSize(330, 375);
        pane = mainFrame.getContentPane(); //Get content pane
        pane.setLayout(null); //Apply null layout
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Close when X is clicked

        //Create controls
        labelMonth = new JLabel("January");
        labelYear = new JLabel("Year:");
        comboBoxYear = new JComboBox();
        buttonBack = new JButton("<<");
        buttonNext = new JButton(">>");
        modelTableCalendar = new DefaultTableModel() {
            public boolean isCellEditable(int rowIndex, int mColIndex) {
                return false;
            }
        };
        tableCalendar = new JTable(modelTableCalendar);
        scrollPaneCalendar = new JScrollPane(tableCalendar);
        panelCalendar = new JPanel(null);

        //Set border
//        panelCalendar.setBorder(BorderFactory.createTitledBorder("Calendar"));

        //Register action listeners
        buttonBack.addActionListener(new btnPrevAction());
        buttonNext.addActionListener(new btnNextAction());
        comboBoxYear.addActionListener(new cmbYearAction());
        tableCalendar.addMouseListener(new cellAction());

        //Add controls to pane
        pane.add(panelCalendar);
        panelCalendar.add(labelMonth);
        panelCalendar.add(labelYear);
        panelCalendar.add(comboBoxYear);
        panelCalendar.add(buttonBack);
        panelCalendar.add(buttonNext);
        panelCalendar.add(scrollPaneCalendar);
//        panelCalendar.add(tableCalendar);

        //Set bounds

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - mainFrame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - mainFrame.getHeight()) / 2);
        mainFrame.setLocation(x, y);
//        panelCalendar.setBounds(0, 0, 320, 335);
        panelCalendar.setBounds(0, 0, 320, 340);
        labelMonth.setBounds(160 - labelMonth.getPreferredSize().width / 2, 25, 100, 25);
        labelYear.setBounds(10, 310, 80, 20);
        comboBoxYear.setBounds(230, 310, 80, 20);
        buttonBack.setBounds(10, 25, 50, 25);
        buttonNext.setBounds(260, 25, 50, 25);
//        tableCalendar.setBounds(10,50,300,255);
        scrollPaneCalendar.setBounds(10, 50, 300, 255);

        //Make frame visible
        mainFrame.setResizable(false);
        mainFrame.setVisible(true);

        //Get real month/year
        GregorianCalendar cal = new GregorianCalendar(); //Create calendar
        realDay = cal.get(GregorianCalendar.DAY_OF_MONTH); //Get day
        realMonth = cal.get(GregorianCalendar.MONTH); //Get month
        realYear = cal.get(GregorianCalendar.YEAR); //Get year
        currentMonth = realMonth; //Match month and year
        currentYear = realYear;

        //Add headers
        String[] headers = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"}; //All headers
        for (int i = 0; i < 7; i++) {
            modelTableCalendar.addColumn(headers[i]);
        }

//        tableCalendar.getParent().setBackground(tableCalendar.getBackground()); //Set background

        //No resize/reorder
        tableCalendar.getTableHeader().setResizingAllowed(false);
        tableCalendar.getTableHeader().setReorderingAllowed(false);

        //Single cell selection
        tableCalendar.setColumnSelectionAllowed(true);
        tableCalendar.setRowSelectionAllowed(true);
        tableCalendar.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //Set row/column count
        tableCalendar.setRowHeight(38);
        modelTableCalendar.setColumnCount(7);
        modelTableCalendar.setRowCount(6);

        //Populate table
        for (int i = realYear - 10; i <= realYear + 10; i++) {
            comboBoxYear.addItem(String.valueOf(i));
        }

        //Refresh calendar
        refreshCalendar(realMonth, realYear);
    }

    public static void refreshCalendar(int month, int year) {
        //Variables
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        int daysCount, monthStart; //Number Of Days, Start Of Month

        //Allow/disallow buttons
        buttonBack.setEnabled(true);
        buttonNext.setEnabled(true);
        if (month == 0 && year <= realYear - 10) {
            buttonBack.setEnabled(false);
        } //Too early
        if (month == 11 && year >= realYear + 10) {
            buttonNext.setEnabled(false);
        } //Too late
        labelMonth.setText(months[month]); //Refresh the month label (at the top)
        labelMonth.setBounds(160 - labelMonth.getPreferredSize().width / 2, 25, 180, 25); //Re-align label with calendar
        comboBoxYear.setSelectedItem(String.valueOf(year)); //Select the correct year in the combo box

        //Clear table
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                modelTableCalendar.setValueAt(null, i, j);
            }
        }

        //Get first day of month and number of days
        GregorianCalendar cal = new GregorianCalendar(year, month, 1);
        daysCount = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
        monthStart = cal.get(GregorianCalendar.DAY_OF_WEEK);

        //Draw calendar
        for (int i = 1; i <= daysCount; i++) {
            int row = new Integer((i + monthStart - 2) / 7);
            int column = (i + monthStart - 2) % 7;
            modelTableCalendar.setValueAt(i, row, column);
        }

        //Apply renderers
        tableCalendar.setDefaultRenderer(tableCalendar.getColumnClass(0), new tblCalendarRenderer());
    }

    static class tblCalendarRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {
            super.getTableCellRendererComponent(table, value, selected, focused, row, column);
            if (column == 0 || column == 6) { //Week-end
                setBackground(new Color(255, 220, 220));
            } else { //Week
                setBackground(new Color(255, 255, 255));
            }
            if (value != null) {
                if (Integer.parseInt(value.toString()) == realDay && currentMonth == realMonth && currentYear == realYear) { //Today
                    setBackground(new Color(220, 220, 255));
                }
            }
            setBorder(null);
            setForeground(Color.black);
            return this;
        }
    }

    static class btnPrevAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (currentMonth == 0) { //Back one year
                currentMonth = 11;
                currentYear -= 1;
            } else { //Back one month
                currentMonth -= 1;
            }
            refreshCalendar(currentMonth, currentYear);
        }
    }

    static class btnNextAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (currentMonth == 11) { //Foward one year
                currentMonth = 0;
                currentYear += 1;
            } else { //Foward one month
                currentMonth += 1;
            }
            refreshCalendar(currentMonth, currentYear);
        }
    }

    static class cmbYearAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (comboBoxYear.getSelectedItem() != null) {
                String b = comboBoxYear.getSelectedItem().toString();
                currentYear = Integer.parseInt(b);
                refreshCalendar(currentMonth, currentYear);
            }
        }
    }

    static class cellAction implements MouseListener {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tableCalendar.rowAtPoint(evt.getPoint());
                int col = tableCalendar.columnAtPoint(evt.getPoint());
                String day = String.valueOf(modelTableCalendar.getValueAt(row, col));
                if (evt.getClickCount() == 2 && tableCalendar.getSelectedRow() != -1) {
                    DayBrowser browser = new DayBrowser(day, currentMonth, currentYear);
                }
//                DayBrowser browser = new DayBrowser(day, currentMonth, currentYear);
        }
        public void mousePressed(java.awt.event.MouseEvent evt) {}
        public void mouseReleased(java.awt.event.MouseEvent evt) {}
        public void mouseEntered(java.awt.event.MouseEvent evt) {}
        public void mouseExited(java.awt.event.MouseEvent evt) {}
    }

}

import javax.swing.*;
import java.awt.*;

public class ParkingGUI {

    ParkingSystem ps = new ParkingSystem();

    public ParkingGUI() {

        ps.initializeSlots(10);

        JFrame frame = new JFrame("Smart Parking System PRO");
        frame.setSize(450, 350);
        frame.setLayout(new FlowLayout());

        frame.setLocationRelativeTo(null); // center
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton showBtn = new JButton("Show Slots");
        JButton bookBtn = new JButton("Book Slot");
        JButton exitBtn = new JButton("Exit Slot");

        JTextArea output = new JTextArea(12, 35);
        output.setEditable(false);

        JScrollPane scroll = new JScrollPane(output);

        showBtn.addActionListener(e -> {
            output.setText(ps.showSlots());
        });

        bookBtn.addActionListener(e -> {
            try {
                String id = JOptionPane.showInputDialog("Enter Slot ID:");
                String vehicleNo = JOptionPane.showInputDialog("Enter Vehicle Number:");
                String type = JOptionPane.showInputDialog("Vehicle Type:");

                output.setText(ps.bookSlot(Integer.parseInt(id), vehicleNo, type));

            } catch(Exception ex) {
                output.setText("Invalid input!");
            }
        });

        exitBtn.addActionListener(e -> {
            try {
                String id = JOptionPane.showInputDialog("Enter Slot ID:");
                output.setText(ps.exitSlot(Integer.parseInt(id)));

            } catch(Exception ex) {
                output.setText("Invalid input!");
            }
        });

        frame.add(showBtn);
        frame.add(bookBtn);
        frame.add(exitBtn);
        frame.add(scroll);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new ParkingGUI();
    }
}

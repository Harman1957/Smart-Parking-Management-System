import java.sql.*;
import java.util.*;

class ParkingSystem {

    Queue<String> waitingQueue = new LinkedList<>();

    void initializeSlots(int totalSlots) {
        try (Connection con = DBConnection.getConnection()) {

            String check = "SELECT COUNT(*) FROM slots";
            PreparedStatement cps = con.prepareStatement(check);
            ResultSet rs = cps.executeQuery();

            if(rs.next() && rs.getInt(1) == 0) {

                for(int i = 0; i < totalSlots; i++) {
                    String query = "INSERT INTO slots (available) VALUES (true)";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.executeUpdate();
                }

                System.out.println("✅ " + totalSlots + " slots initialized");
            }

        } catch(Exception e) {
            System.out.println(e);
        }
    }

    String showSlots() {
        StringBuilder result = new StringBuilder();

        try (Connection con = DBConnection.getConnection()) {

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM slots");

            while(rs.next()) {
                result.append("Slot ID: ").append(rs.getInt("id"))
                      .append(" | Available: ").append(rs.getBoolean("available"))
                      .append("\n");
            }

            result.append("\nWaiting Queue: ").append(waitingQueue.toString());

        } catch(Exception e) {
            return "Error: " + e.getMessage();
        }

        return result.toString();
    }

   
    String bookSlot(int id, String vehicleNo, String type) {
        try (Connection con = DBConnection.getConnection()) {

            String check = "SELECT available FROM slots WHERE id = ?";
            PreparedStatement cps = con.prepareStatement(check);
            cps.setInt(1, id);
            ResultSet rs = cps.executeQuery();

            if(rs.next()) {

                if(rs.getBoolean("available")) {

                    String query = "UPDATE slots SET available = false, entry_time = NOW(), exit_time = NULL WHERE id = ?";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setInt(1, id);
                    ps.executeUpdate();

                    String vQuery = "INSERT INTO vehicles (slot_id, vehicle_number, vehicle_type) VALUES (?, ?, ?)";
                    PreparedStatement vps = con.prepareStatement(vQuery);
                    vps.setInt(1, id);
                    vps.setString(2, vehicleNo);
                    vps.setString(3, type);
                    vps.executeUpdate();

                    return "✅ Slot booked successfully!";

                } else {
                    waitingQueue.add(vehicleNo);
                    return "⚠ Slot full! Added to waiting queue.";
                }

            } else {
                return "❌ Invalid Slot ID!";
            }

        } catch(Exception e) {
            return "Error: " + e.getMessage();
        }
    }

   
    String exitSlot(int id) {
        try (Connection con = DBConnection.getConnection()) {

            String fetch = "SELECT entry_time FROM slots WHERE id = ?";
            PreparedStatement fps = con.prepareStatement(fetch);
            fps.setInt(1, id);
            ResultSet rs = fps.executeQuery();

            if(!rs.next()) {
                return "❌ Invalid Slot ID!";
            }

            if(rs.getTimestamp("entry_time") != null) {

                Timestamp entry = rs.getTimestamp("entry_time");
                Timestamp exit = new Timestamp(System.currentTimeMillis());

                long diff = exit.getTime() - entry.getTime();
                long hours = (diff / (1000 * 60 * 60)) + 1;

                double bill = 20 + (hours - 1) * 10;

                String update = "UPDATE slots SET available = true, exit_time = NOW() WHERE id = ?";
                PreparedStatement ups = con.prepareStatement(update);
                ups.setInt(1, id);
                ups.executeUpdate();

                String message = "✅ Slot released!\nTime: " + hours + " hrs\nBill: ₹" + bill;

                if(!waitingQueue.isEmpty()) {
                    String nextVehicle = waitingQueue.poll();

                    String assign = "UPDATE slots SET available = false, entry_time = NOW(), exit_time = NULL WHERE id = ?";
                    PreparedStatement aps = con.prepareStatement(assign);
                    aps.setInt(1, id);
                    aps.executeUpdate();

                    message += "\n🚗 Slot given to waiting vehicle: " + nextVehicle;
                }

                return message;

            } else {
                return "❌ Slot was not booked!";
            }

        } catch(Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}

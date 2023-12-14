package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class MenuHistory extends History {

	private static int sendTo2;
	JFrame frame;
	JPanel panel;

	DbConnector dbConnector = new DbConnector();
    String host = dbConnector.getHost();
    String root = dbConnector.getRoot();
    String password = dbConnector.getPass();

	private int amount;
	private int orderNumber;

	public void sendData(){
		super.sendTo = sendTo;
	}

	public static void setSendTo(int sendTo) {
		sendTo2 = sendTo;
	  }

	public int getAmount(){
		return amount;
	}

	public int getOrderNumber(){
		return orderNumber;
	}

	public static void main(String[] args) {
		try {
			new MenuHistory();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public MenuHistory() throws Exception {
	
		frame = new JFrame("Menu History");
        panel = new JPanel();
		frame.setBounds(0, 0, 465, 245);
		Connection conn = DriverManager.getConnection(host, root, password);
		String sql = "SELECT * FROM menuhistory WHERE orderNumber = ?";
		PreparedStatement stmt2 = conn.prepareStatement(sql);
		// Object orderNum2 = table.getValueAt(selectedRow, 0); // Get Value of orderNumber for query
		System.out.println(sendTo);
		stmt2.setInt(1, (int) sendTo2);
		ResultSet mn = stmt2.executeQuery();

		//Create first table for bill history
		DefaultTableModel model = new DefaultTableModel();

		model.addColumn("MENU ID");
		model.addColumn("MENU NAME");
		model.addColumn("TYPE");
		model.addColumn("AMOUNT");
		model.addColumn("PRICE");

		while (mn.next()) {
			int menuID = mn.getInt("menuID");
			String menuName = mn.getString("menuName");
			String type = mn.getString("type");
			int amount = mn.getInt("amount");
			double price = mn.getDouble("price");
			model.addRow(new Object[] { menuID, menuName, type, amount, price + " $"});
		}

		JTable table = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(table);
		frame.add(scrollPane);
		frame.setSize(465, 220);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().add(scrollPane);

	}

	// public void saveMenu(){
	// 	try {
	// 		Class.forName("com.mysql.cj.jdbc.Driver");
	// 		Connection con = DriverManager.getConnection(host, root, password);
    //         Statement statement = con.createStatement();
	// 		String sql = "INSERT INTO menuhistory(,orderNumber, menuID, ,menuName, type, amount, price) VALUES ("+orderNumber+", "+getID()+", '"+getName()+"', '"+getType()+"', "+amount+", "+getPrice()+")";
	// 		statement.execute(sql);
			
	// 	} catch (Exception e) {
	// 		e.printStackTrace();
	// 	}
	// }
}
package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class History  implements ActionListener {

	JFrame frame;
    JPanel panel;
    JButton btnGetRowSelected;  // Declare the button here
	JTable table;

	int selectedRow, sendTo;
	static int testNum;
	int orderNum2;

    DbConnector dbConnector = new DbConnector();
    String host = dbConnector.getHost();
    String root = dbConnector.getRoot();
    String password = dbConnector.getPass();

	private double total;
	private String time;

	public double getTotal(){
		return total;
	}

	public String getTime(){
		return time;
	}

	public static void main(String[] args) {
		try {
			new History();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public History() throws Exception{
		frame = new JFrame("Bill History");
        panel = new JPanel();
        frame.setBounds(0, 0, 665, 360);

        Connection conn = DriverManager.getConnection(host, root, password);
        Statement stmt = conn.createStatement();
        ResultSet bl = stmt.executeQuery("SELECT * FROM billhistory");

        //Create first table for bill history
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ORDER NUMBER");
        model.addColumn("TIME");
        model.addColumn("PRICE");

        while (bl.next()) {
            int orderNumber = bl.getInt("orderNumber");
            String time = bl.getString("time");
            Float price = bl.getFloat("price");
            model.addRow(new Object[] { orderNumber, time, price +" $"});
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane);
        frame.setSize(665, 420);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().add(scrollPane);

        btnGetRowSelected = new JButton("Get Data");
        frame.add(btnGetRowSelected, BorderLayout.SOUTH);

		btnGetRowSelected.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evv) {
				if(evv.getSource().equals(btnGetRowSelected)){
					
					
					int selectedRow = table.getSelectedRow();
					// System.out.println(table.getValueAt(selectedRow, 0));
					
					if (selectedRow != -1) {
						try {
							Object orderNum2 = table.getValueAt(selectedRow, 0);
							sendTo = (int) orderNum2;
							frame.setVisible(false);
							MenuHistory.setSendTo(sendTo);
							new MenuHistory();
							
							
							
						} catch (Exception e) {
							
							e.printStackTrace();
						}
					}
				}
				
			}
		});	
	}
	@Override
	public void actionPerformed(java.awt.event.ActionEvent ev) {

	}

	// public void saveBill(){
	// 	try {
    //         Class.forName("com.mysql.cj.jdbc.Driver");
    //         Connection con = DriverManager.getConnection(host, root, password);
    //         Statement statement = con.createStatement();
	// 		String sql = "INSERT INTO billhistory(orderNumber, time, price) VALUES ("+getOrderNumber()+", '"+time+"', "+total+")";
	// 		statement.execute(sql);
            
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
	// }
}

    
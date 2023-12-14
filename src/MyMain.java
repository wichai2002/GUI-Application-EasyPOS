package src;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;


import java.awt.event.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.EventObject;
import javax.swing.table.TableCellEditor;

public class MyMain implements ActionListener{
    JFrame frame;
    JPanel panel, billPanel;
    // inside menu panel;
    JPanel menuPanel[];
    JLabel labelMenu[], labelIcon[];
    Icon imgMenu[];
    // inside billpanel;
    JPanel billMenu;
    JTable table;
    DefaultTableModel model;
    Object[] row = new Object[5];
    Object[] listCol = {"Image","ID","Name", "Amount", "Price", ""};
    // inside gridRow 2 ;
    JLabel orderNumber,amountMenu, tax, total; // label
    JLabel Order, priceTax, numMenu, priceTotal;  // values
    JButton checkOut, clear, addMenu, refresh, history;
    int countRow = 0;
    int amountOf_menu_in_my_App = 0;
    int my_orderNumber;
    // get host, root, password data
    DbConnector dbConnector = new DbConnector();
    String host = dbConnector.getHost();
    String root = dbConnector.getRoot();
    String password = dbConnector.getPass();


    MyMain(){

        

        setRow();
        setOrdernumber();
        frame = new JFrame("EasyPOS");
        panel = new JPanel();
        panel.setLayout(new GridLayout(0,2));
        panel.setBackground(new Color(192, 192, 192));
        panel.setBorder(new EmptyBorder(20,10,20,0));
        panel.setBackground(null);
        menuPanel = new JPanel[countRow];
        labelMenu = new JLabel[countRow];
        imgMenu = new ImageIcon[countRow];
        labelIcon = new JLabel[countRow];
        // Displaye Menu
        showMenu();

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setLayout(new ScrollPaneLayout());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        billPanel = new JPanel();
        billPanel.setLayout(new GridLayout(3,1));
        model = new DefaultTableModel();
        model.setColumnIdentifiers(listCol);

        table = new JTable(model){
            public Class getColumnClass(int column) {
              return (column == 0) ? Icon.class : Object.class;
            }
            public boolean isCellEditable(int row, int column) {
                if (column == 5){
                    return true;
                }               
                return true;               
            };
        };

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("Tahoma", Font.PLAIN, 28));

        table.setFont(new Font("Tahoma", Font.PLAIN, 28));
        table.setRowHeight(100);
        table.setAutoCreateRowSorter(true);
        table.setShowGrid(false);

        billPanel.add(new JScrollPane(table));
        billMenu = new JPanel();
        billMenu.setBackground(new Color(144,99,130));
        billMenu.setBorder(new EmptyBorder(10,50,40,50));
        billMenu.setBackground(null);
        billMenu.setLayout(new GridLayout(4,1));

        // bill
        JPanel orderP = new JPanel(); orderP.setLayout(new BorderLayout());
        orderNumber = new JLabel("OrderNumber : "); orderNumber.setFont(new Font("Tahoma", Font.PLAIN, 35));
        // key order
        Order = new JLabel(""); Order.setFont(new Font("Tahoma", Font.PLAIN, 35));
        orderP.add(orderNumber); orderP.add(Order, BorderLayout.LINE_END);

        JPanel amountManuP = new JPanel(); amountManuP.setLayout(new BorderLayout());
        amountMenu = new JLabel("Amount : "); amountMenu.setFont(new Font("Tahoma", Font.PLAIN, 35));
        numMenu = new JLabel(""+0); numMenu.setFont(new Font("Tahoma", Font.PLAIN, 35));
        amountManuP.add(amountMenu); amountManuP.add(numMenu, BorderLayout.LINE_END);

        JPanel totalP = new JPanel(); totalP.setLayout(new BorderLayout());
        total = new JLabel("Total : "); total.setFont(new Font("Tahoma", Font.PLAIN, 35));
        priceTotal = new JLabel(""+0); priceTotal.setFont(new Font("Tahoma", Font.PLAIN, 35));
        totalP.add(total); totalP.add(priceTotal, BorderLayout.LINE_END);

        JPanel btnPanel = new JPanel(); btnPanel.setLayout(new FlowLayout());
        btnPanel.setBorder(new EmptyBorder(15,0,0,0));
        btnPanel.setBackground(null);
        checkOut = new JButton("CheckOut");
        checkOut.addActionListener(this);
        checkOut.setBackground(new Color(100,158,33));
        checkOut.setPreferredSize(new Dimension(300, 60));
        checkOut.setFont(new Font("Tahoma", Font.PLAIN, 35));
        checkOut.setForeground(Color.white);
        checkOut.setBorder(new RoundBorder(12));

        clear = new JButton("Clear");
        clear.addActionListener(this);
        clear.setPreferredSize(new Dimension(300, 60));
        clear.setFont(new Font("Tahoma", Font.PLAIN, 35));
        clear.setBackground(new Color(130,51,25));
        clear.setForeground(Color.white);
        clear.setBorder(new RoundBorder(12));
        btnPanel.add(clear); btnPanel.add(checkOut);

        billMenu.add(orderP);
        billMenu.add(amountManuP);
        billMenu.add(totalP);
        billMenu.add(btnPanel);
        billPanel.add(billMenu);

        JPanel FuncP = new JPanel();
        FuncP.setBorder(new EmptyBorder(50,50,40,50));
        FuncP.setBackground(null);

        addMenu = new JButton("Add Menu");
        addMenu.setBackground(new Color(139, 26, 125 ));
        addMenu.setPreferredSize(new Dimension(800, 80));
        addMenu.setFont(new Font("Tahoma", Font.PLAIN, 35));
        addMenu.setForeground(Color.white);
        addMenu.setBorder(new RoundBorder(12));
        addMenu.addActionListener(this);

        refresh = new JButton("Refresh");
        refresh.setBackground(new Color(190, 123, 12 ));
        refresh.setPreferredSize(new Dimension(800, 80));
        refresh.setFont(new Font("Tahoma", Font.PLAIN, 35));
        refresh.setForeground(Color.white);
        refresh.setBorder(new RoundBorder(12));
        refresh.addActionListener(this);
        

        history = new JButton("Order History");
        history.setBackground(new Color(10, 184, 198 ));
        history.setPreferredSize(new Dimension(800, 80));
        history.setFont(new Font("Tahoma", Font.PLAIN, 35));
        history.setForeground(Color.white);
        history.setBorder(new RoundBorder(12));
        history.addActionListener(this);

        FuncP.add(addMenu); 
        FuncP.add(refresh); 
        FuncP.add(history);
        billPanel.add(FuncP);

        frame.setLayout(new GridLayout(0,2));
        frame.add(scrollPane);
        frame.add(billPanel);
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(false);
        frame.setVisible(true);

    }

    public static void main(String[] args) {
        new MyMain();
    }

    public void showMenu(){
        

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(host, root, password);
            Statement statement = con.createStatement();
            String sql = "select * from menu";
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                int n = amountOf_menu_in_my_App;
                String name = resultSet.getString(2);
                String id = resultSet.getString(1);
                String img = "img/"+ resultSet.getString(1) + ".jpg";
                double price = resultSet.getDouble(3);

                imgMenu[amountOf_menu_in_my_App] = new ImageIcon(new ImageIcon("img/"+ resultSet.getString(1) + ".jpg").getImage().getScaledInstance(155+100, 114+100, Image.SCALE_DEFAULT));
                labelIcon[amountOf_menu_in_my_App] = new JLabel(imgMenu[amountOf_menu_in_my_App]);
                labelMenu[amountOf_menu_in_my_App] = new JLabel(resultSet.getString(2) + "  " + resultSet.getString(3)+".$");
                labelMenu[amountOf_menu_in_my_App].setFont(new Font("Tahoma", Font.PLAIN, 30));
                labelMenu[amountOf_menu_in_my_App].setHorizontalAlignment(SwingConstants.CENTER);
                frame.getContentPane().setBackground(new Color(192, 192, 192));
                menuPanel[amountOf_menu_in_my_App] = new JPanel();

                menuPanel[amountOf_menu_in_my_App].setLayout(new BorderLayout());
                Border blackline = BorderFactory.createLineBorder(Color.white);
                menuPanel[amountOf_menu_in_my_App].setBorder(blackline);
                menuPanel[amountOf_menu_in_my_App].add(labelIcon[amountOf_menu_in_my_App], BorderLayout.NORTH);
                menuPanel[amountOf_menu_in_my_App].add(labelMenu[amountOf_menu_in_my_App]);
                menuPanel[amountOf_menu_in_my_App].addMouseListener(new MouseAdapter() {
                    public void mousePressed(MouseEvent e) {
                        Order.setText(my_orderNumber+"");
                        if (currentMenu(name)){
                            ImageIcon myimg = new ImageIcon(new ImageIcon(img).getImage().getScaledInstance(160, 120, Image.SCALE_DEFAULT));
                            row[0] = myimg;
                            row[1] = id;
                            row[2] = name;
                            row[3] = 1;
                            row[4] = price;
                            ButtonRenderer renderer = new ButtonRenderer();


                            table.getColumnModel().getColumn(5).setCellRenderer(renderer);
                            table.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor());
                            model.addRow(row);
                            checkPrice_in(price);
                            
                        }else{
                            checkPrice_in(price);
                        }
                    }
                  });
                panel.add(menuPanel[amountOf_menu_in_my_App]);
                ++amountOf_menu_in_my_App;
            }

        }catch(Exception er){
            er.printStackTrace();
        }
    }

    public boolean currentMenu(String name){
        for (int i = 0; i < table.getRowCount(); i++){
            if (table.getValueAt(i, 2).toString().equals(name)){
                int val = Integer.parseInt(table.getValueAt(i, 3).toString()) +1;
                table.setValueAt(val, i,3);
                return false;
            }
        }
        return true;
    }

    public void setOrdernumber(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(host, root, password);
            Statement statement = con.createStatement();
            String sql = "select count(*) from billhistory";
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();
            my_orderNumber = resultSet.getInt(1) + 1;
            System.out.println(my_orderNumber);
        }catch(Exception er){
            er.printStackTrace();
        }
    }

    public void checkPrice_in(double price){
        int num = Integer.parseInt(numMenu.getText()) +1;
        numMenu.setText(""+num);
        double sumPrice = Double.parseDouble(priceTotal.getText()) + price;
        priceTotal.setText(sumPrice+"");
    }

    public void checkPrice_out(double price){
        int num = Integer.parseInt(numMenu.getText()) -1;
        numMenu.setText(""+num);
        double sumPrice = Double.parseDouble(priceTotal.getText()) - price;
        priceTotal.setText(sumPrice+"");
    }

    public void setRow(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(host, root, password);
            Statement statement = con.createStatement();
            String sql = "select count(*) from menu";
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();
            countRow = resultSet.getInt(1);
            System.out.println(countRow);
        }catch(Exception er){
            er.printStackTrace();
        }
    }

    public void checkOut(){
        int order_Number = Integer.parseInt(Order.getText());
        String time = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
        System.out.println(time);
        double totol = Double.parseDouble(priceTotal.getText());

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(host, root, password);
            Statement statement = con.createStatement();
            String sql = "insert into billhistory values("+ order_Number +", '" + time+"' , +" +totol+");";
            // ResultSet resultSet = statement.executeQuery(sql);
            statement.executeUpdate(sql);
            for (int i = 0;i < table.getRowCount(); i++){
                int munu_id = Integer.parseInt(table.getValueAt(i, 1).toString()); System.out.println(munu_id);
                String munu_name = table.getValueAt(i, 2).toString(); System.out.println(munu_name);
                int all_amount = Integer.parseInt(table.getValueAt(i, 3).toString()); System.out.println(all_amount);
                double price = Double.parseDouble(table.getValueAt(i, 4).toString()); System.out.println(price);

                System.out.println("select type from menu where(menuID ="+munu_id+")");
                ResultSet resultSet = statement.executeQuery("select type from menu where(menuID ="+munu_id+")");
                resultSet.next();
                String sql_insert = "insert into menuhistory values("+order_Number +", "+ munu_id +", '"+ munu_name +"', '"+ resultSet.getString(1) +"', "+ all_amount +", "+ price+");";
                System.out.println(sql_insert);
                statement.executeUpdate(sql_insert);
                setOrdernumber();
            }

            for( int i = model.getRowCount() - 1; i >= 0; i-- ){
                model.removeRow(i);
            }
        }catch(Exception er){
            er.printStackTrace();
        }
        //popup checkout 
        String newline = System.getProperty("line.separator");
        JOptionPane.showMessageDialog(null, "Ordernumber  :   "+ Order.getText() + newline + 
        "Total Amount :   "+ numMenu.getText() + newline +
        "Total Price  :   "+ priceTotal.getText() +" $"+ newline, "Bill", JOptionPane.INFORMATION_MESSAGE);
        Order.setText(""); numMenu.setText(0+""); priceTotal.setText(""+0);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(addMenu)){
            new Setmenu();
        }
        if (e.getSource().equals(refresh)){
            frame.dispose();
            new MyMain();
        }
        if (e.getSource().equals(history)){
            new History();
        }
        if (e.getSource().equals(checkOut)){
            checkOut();
        }
        if (e.getSource().equals(clear)){
            Order.setText(""); numMenu.setText(0+""); priceTotal.setText(""+0);
            for( int i = model.getRowCount() - 1; i >= 0; i--){
                model.removeRow(i);
            }
        }
    }

    public class RoundBorder implements Border{
        private int radius;

        RoundBorder(int radius) {
           this.radius = radius;
        }

       public Insets getBorderInsets(Component c) {
           return new Insets(this.radius+1, this.radius+1, this.radius+2, this.radius);
       }

       public boolean isBorderOpaque() {
           return true;
       }

       public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
           g.drawRoundRect(x, y, width-1, height-1, radius, radius);
       }
        
    }

    public class ButtonPane extends JPanel {

        private JButton increase;
        private JButton reduce;
        private String state;

        public ButtonPane() {
            // setLayout(new GridBagLayout());
            setLayout(new GridLayout(1,2));
            increase = new JButton("+");
            increase.setFont(new Font("Tahoma", Font.PLAIN, 30));
            increase.setForeground(Color.white);
            increase.setBackground(new Color(80, 120, 70));

            reduce = new JButton("-");
            reduce.setFont(new Font("Tahoma", Font.PLAIN, 30));
            reduce.setForeground(Color.white);
            reduce.setBackground(new Color(113, 43, 60));

            ActionListener listener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource().equals(increase)){
                        int n = Integer.parseInt(model.getValueAt(table.getSelectedRow(), 3).toString()) +1;
                        double price = Double.parseDouble(model.getValueAt(table.getSelectedRow(), 4).toString());
                        table.getModel().setValueAt(n, table.getSelectedRow(), 3);
                        checkPrice_in(price);
                    }
                    if (e.getSource().equals(reduce)){
                        int n = Integer.parseInt(model.getValueAt(table.getSelectedRow(), 3).toString()) -1;
                        double price = Double.parseDouble(model.getValueAt(table.getSelectedRow(), 4).toString());

                        if (n == 0){
                            model.removeRow(table.getSelectedRow());
                        }else{
                            table.getModel().setValueAt(n, table.getSelectedRow(), 3);
                        }
                        checkPrice_out(price);
                    }
                }
            };
            
            increase.addActionListener(listener);
            reduce.addActionListener(listener);

            add(increase);
            add(reduce);
        }

        public JButton getIncrease(){
            return this.increase;
        }
        public JButton getReduce(){
            return this.reduce;
        }

        public String getState() {
            return state;
        }
    }
    
    public class ButtonRenderer extends DefaultTableCellRenderer{
        private ButtonPane buttonPane;
        public ButtonRenderer() {
            buttonPane = new ButtonPane();
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                buttonPane.setBackground(table.getSelectionBackground());
            } else {
                buttonPane.setBackground(table.getBackground());
            }
            return buttonPane;
        }
        public ButtonPane getButtonPane(){
            return buttonPane;
        }
    }

    public class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private ButtonPane buttonPane;
        public ButtonEditor() {
            buttonPane = new ButtonPane();
        }

        @Override
        public Object getCellEditorValue() {
            return buttonPane.getState();
        }

        @Override
        public boolean isCellEditable(EventObject e) {
            return true;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (isSelected) {
                buttonPane.setBackground(table.getSelectionBackground());
            } else {
                buttonPane.setBackground(table.getBackground());
            }
            return buttonPane;
        }
    } 
}
package src;

import java.io.*;
import java.io.BufferedInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.*;
import java.awt.event.*;
import java.io.FilenameFilter;
import java.sql.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.IOException;

public class Setmenu implements ActionListener, MouseListener{
    JFrame frame;
    JPanel panel;
    JScrollPane scrollPane;
    JTable table;
    JButton btnAddMenu,btnUpdate,btnDelete,btnOpenFile, btnSearch;
    JComboBox comboBox, comboBox_search;
    JFileChooser imgChooser;
    ImageIcon chooserIcon;
    JLabel lableID, lableName, labelPrice, labelType, labelImg, chooserIMGlabel;
    JTextField fieldID, fieldName, fieldPrice, fieldSearch;
    DefaultTableModel model;
    DefaultTableModel tableModel;
    
    // get host, root, password data
    DbConnector dbConnector = new DbConnector();
    String host = dbConnector.getHost();
    String root = dbConnector.getRoot();
    String password = dbConnector.getPass();

    String pathImg = "";
    Object[] row = new Object[5];
    Object[] col = {"ID", "NAME", "PRICE", "IMG", "TYPE"};

    Setmenu(){
        model = new DefaultTableModel();
        frame = new JFrame();
        frame.getContentPane().setBackground(new Color(192, 192, 192));
        frame.setBounds(100,100,1136,799);
        model.setColumnIdentifiers(col);
        showData();
        table = new JTable(model){
            public Class getColumnClass(int column) {
              return (column == 3) ? Icon.class : Object.class;
            }
            public boolean isCellEditable(int row, int column) {                
                return false;               
            };
        };
        table.addMouseListener(this);
        table.setBackground(Color.white);
        table.setRowHeight(100);
        table.setFont(new Font("Tahoma", Font.PLAIN, 28));
        table.setAutoCreateRowSorter(true);
        scrollPane = new JScrollPane(table);
        
        // scrollPane.setBackground(Color.MAGENTA);
        scrollPane.setBounds(10,20,1100,480);
        frame.getContentPane().add(scrollPane);
    
        frame.getContentPane().setLayout(null);
        
        lableID = new JLabel("ID :");
        lableID.setForeground(Color.BLACK);
        lableID.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lableID.setBackground(Color.WHITE);
        lableID.setBounds(117, 539, 34, 25);
        frame.getContentPane().add(lableID);
        
        fieldID = new JTextField();
        fieldID.setColumns(20);
        fieldID.setFont(new Font("Tahoma", Font.PLAIN, 20));
        fieldID.setBounds(161, 537, 358, 30);
        frame.getContentPane().add(fieldID);
        
        lableName = new JLabel("NAME :");
        lableName.setForeground(Color.BLACK);
        lableName.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lableName.setBounds(542, 539, 64, 25);
        frame.getContentPane().add(lableName);
        
        fieldName = new JTextField();
        fieldName.setFont(new Font("Tahoma", Font.PLAIN, 20));
        fieldName.setColumns(20);
        fieldName.setBounds(622, 540, 358, 30);
        frame.getContentPane().add(fieldName);
        
        labelPrice = new JLabel("PRICE :");
        labelPrice.setFont(new Font("Tahoma", Font.PLAIN, 20));
        labelPrice.setBounds(83, 576, 66, 25);
        frame.getContentPane().add(labelPrice);
        
        fieldPrice = new JTextField();
        fieldPrice.setColumns(20);
        fieldPrice.setFont(new Font("Tahoma", Font.PLAIN, 20));
        fieldPrice.setBounds(161, 583, 358, 30);
        frame.getContentPane().add(fieldPrice);
        
        labelType = new JLabel("Type :");
        labelType.setFont(new Font("Tahoma", Font.PLAIN, 20));
        labelType.setBounds(552, 581, 105, 30);
        frame.getContentPane().add(labelType);
        
        labelImg = new JLabel("IMG :");
        labelImg.setFont(new Font("Tahoma", Font.PLAIN, 20));
        labelImg.setBounds(105, 621, 56, 30);
        frame.getContentPane().add(labelImg);
        
        comboBox = new JComboBox();
        comboBox.setFont(new Font("Tahoma", Font.PLAIN, 20));
        comboBox.setBounds(622, 584, 179, 30);
        comboBox.addItem("main course");
        comboBox.addItem("drinks");
        comboBox.addItem("dessert");
        frame.getContentPane().add(comboBox);
        
        btnOpenFile = new JButton("OpenFile");
        btnOpenFile.setFont(new Font("Tahoma", Font.PLAIN, 20));
        btnOpenFile.setBounds(161, 624, 120, 30);
        frame.getContentPane().add(btnOpenFile);

        chooserIcon = new ImageIcon(new ImageIcon("img/noIMG.jpg").getImage().getScaledInstance(147, 114, Image.SCALE_DEFAULT));
        chooserIMGlabel = new JLabel(chooserIcon);

        chooserIMGlabel.setBounds(299, 624, 147, 114);
        frame.getContentPane().add(chooserIMGlabel);
        
        btnAddMenu = new JButton("ADD MENU");
        btnAddMenu.setForeground(new Color(255, 255, 255));
        btnAddMenu.setBackground(new Color(0, 128, 128));
        btnAddMenu.setBounds(505, 668, 120, 37);
        frame.getContentPane().add(btnAddMenu);
        
        btnUpdate = new JButton("UPDATE");
        btnUpdate.setForeground(new Color(255, 255, 255));
        btnUpdate.setBackground(new Color(128, 0, 255));
        btnUpdate.setBounds(653, 668, 120, 37);
        frame.getContentPane().add(btnUpdate);
        
        btnDelete = new JButton("DELETE");
        btnDelete.setForeground(new Color(255, 255, 255));
        btnDelete.setBackground(new Color(149, 34, 46));
        btnDelete.setBounds(797, 668, 120, 37);


        btnAddMenu.addActionListener(this); btnUpdate.addActionListener(this); btnDelete.addActionListener(this);
        btnOpenFile.addActionListener(this);
        frame.getContentPane().add(btnDelete);
        
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public static void main(String[] args){
        new Setmenu();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnOpenFile)){
            // showOpenDialog(null);
            // File chooser processing for get File_path 
            pathImg = "";
            try{
                imgChooser = new JFileChooser();
                imgChooser.setCurrentDirectory(new File(""));

                // filter File type for *.Imges, jpg, png, gif
                FileNameExtensionFilter filter =  new FileNameExtensionFilter("*.Images", "jpg", "png", "gif"); 
                imgChooser.addChoosableFileFilter(filter);
                imgChooser.showSaveDialog(null);

                File selectedFile = imgChooser.getSelectedFile();
                String path = selectedFile.getAbsolutePath();
                pathImg+=path;
                //chooserIcon.setImage(new ImageIcon(path).getImage().getScaledInstance(147, 114, Image.SCALE_DEFAULT));
                chooserIcon = new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(147, 114, Image.SCALE_DEFAULT));
                chooserIMGlabel.setIcon(chooserIcon);
                System.out.println(path);

            }catch(Exception er){
                er.printStackTrace();
            }
        }

        if (e.getSource().equals(btnAddMenu)){
            addMenu();
        }
        if (e.getSource().equals(btnUpdate)){
            updateMenu();
        }
        if (e.getSource().equals(btnDelete)){
            delete();
            model.removeRow(table.getSelectedRow());
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        // Doble click for select row from table
        if (e.getClickCount() == 2){
            tableModel = (DefaultTableModel)table.getModel();
            String id = tableModel.getValueAt(table.getSelectedRow(), 0).toString();
            String name = tableModel.getValueAt(table.getSelectedRow(), 1).toString();
            String price = tableModel.getValueAt(table.getSelectedRow(), 2).toString();
            String type = tableModel.getValueAt(table.getSelectedRow(), 4).toString();
            // System.out.println(id + " "+ name + " " + price+ " " + type);
            fieldID.setText(id);
            fieldName.setText(name);
            fieldPrice.setText(price);
            comboBox.setSelectedItem(type);
            chooserIcon = new ImageIcon(new ImageIcon("img/"+id+".jpg").getImage().getScaledInstance(147, 114, Image.SCALE_DEFAULT));
            chooserIMGlabel.setIcon(chooserIcon);
            pathImg = "img/"+id+".jpg";
        }
    }

    @Override
    public void mousePressed(MouseEvent e) { 
    }
    @Override
    public void mouseReleased(MouseEvent e) { 
    }
    @Override
    public void mouseEntered(MouseEvent e) {
    }
    @Override
    public void mouseExited(MouseEvent e) { 
    }

    public void addMenu(){
        int id = Integer.parseInt(fieldID.getText());
        String name = fieldName.getText();
        double price = Double.parseDouble(fieldPrice.getText());
        String img = fieldID.getText() + ".jpg";
        String foodType = comboBox.getSelectedItem().toString();
        
        BufferedImage bImage = null;
        try{
            File initialImage = new File(pathImg);
            bImage = ImageIO.read(initialImage);
            ImageIO.write(bImage, "jpg", new File("img/" + img));
        }catch(IOException e){
            e.printStackTrace();
        }
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(host, root, password);
            Statement statement = con.createStatement();
            String sql = "insert into menu(menuID, menuName, price, img, type) values("+ id +", '"+ name + "', " + price +", '" + img + "', '"+foodType+"')";
            // ResultSet resultSet = statement.ex(sql);
            statement.execute(sql);

            row[0] = id;
            row[1] = name;
            row[2] = price;
            ImageIcon myimg = new ImageIcon(new ImageIcon("img/" + img).getImage().getScaledInstance(155, 114, Image.SCALE_DEFAULT));
            row[3] = myimg;
            row[4] = foodType;
            model.addRow(row);

        }catch(Exception e){    
            e.printStackTrace();
        }

        fieldID.setText(null); fieldName.setText(null); fieldPrice.setText(null); comboBox.setSelectedItem("main course");
        chooserIcon = new ImageIcon(new ImageIcon("img/noIMG.jpg").getImage().getScaledInstance(147, 114, Image.SCALE_DEFAULT));
        chooserIMGlabel.setIcon(chooserIcon);
        pathImg = "";
    }

    public void updateMenu(){
        int id = Integer.parseInt(fieldID.getText());
        String name = fieldName.getText();
        double price = Double.parseDouble(fieldPrice.getText());
        String img = fieldID.getText() + ".jpg";
        String foodType = comboBox.getSelectedItem().toString();
        System.out.println(foodType);

        if (pathImg.equals("img/"+id+".jpg")){

        }else if (pathImg.equals("")){
            img = "noIMG.jfif";
        }
        else{
            File getInFile = new File("img/"+id+".jpg");
            getInFile.delete();
            BufferedImage bImage = null;
            try{
                File initialImage = new File(pathImg);
                bImage = ImageIO.read(initialImage);
                ImageIO.write(bImage, "jpg", new File("img/" + img));
            }catch(IOException e){
                e.printStackTrace();
            }
            pathImg = "";
        }
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(host, root, password);
            Statement statement = con.createStatement();
            String sql = "update menu set menuID =" + id + ", menuName="+"'"+name+"', price="+price+",img="+"'"+img+"', type="+"'"+foodType+"' where menuID =" + id ;
            // ResultSet resultSet = statement.ex(sql);
            statement.execute(sql);
        }catch(Exception er){
            er.printStackTrace();
        }

        fieldID.setText(null); fieldName.setText(null); fieldPrice.setText(null); comboBox.setSelectedItem("main course");
        chooserIcon = new ImageIcon(new ImageIcon("img/noIMG.jpg").getImage().getScaledInstance(147, 114, Image.SCALE_DEFAULT));
        chooserIMGlabel.setIcon(chooserIcon);

        tableModel = (DefaultTableModel)table.getModel();
        ImageIcon myimg = new ImageIcon(new ImageIcon("img/" + img).getImage().getScaledInstance(155, 114, Image.SCALE_DEFAULT));
        tableModel.setValueAt(id, table.getSelectedRow(), 0);
        tableModel.setValueAt(name, table.getSelectedRow(), 1);
        tableModel.setValueAt(price, table.getSelectedRow(), 2);
        tableModel.setValueAt(myimg, table.getSelectedRow(), 3);
        tableModel.setValueAt(foodType, table.getSelectedRow(), 4);
        
        pathImg = "";
    }

    public void delete(){
        int id = Integer.parseInt(fieldID.getText()); // use only primary key for delete row ;
        // String name = fieldName.getText();
        // double price = Double.parseDouble(fieldPrice.getText());
        // String img = fieldID.getText() + ".jpg";
        // String foodType = comboBox.getSelectedItem().toString();

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(host, root, password);
            Statement statement = con.createStatement();
            String sql = "delete from menu where(menuID= "+ id +")";
            statement.execute(sql);
            File getInFile = new File("img/"+id+".jpg");
            getInFile.delete();
        }catch(Exception er){
            er.printStackTrace();
        }

        tableModel = (DefaultTableModel)table.getModel();
        tableModel.removeRow(table.getSelectedRow());

        fieldID.setText(null); fieldName.setText(null); fieldPrice.setText(null); comboBox.setSelectedItem("main course");
        chooserIcon = new ImageIcon(new ImageIcon("img/noIMG.jpg").getImage().getScaledInstance(155, 144, Image.SCALE_DEFAULT));
        chooserIMGlabel.setIcon(chooserIcon);
        File getInFile = new File("img/"+id+".jpg");
        getInFile.delete();

    }

    public void showData(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(host, root, password);
            Statement statement = con.createStatement();
            String sql = "select * from menu";
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                row[0] = resultSet.getString(1);
                row[1] = resultSet.getString(2);
                row[2] = resultSet.getString(3);
                ImageIcon myimg = new ImageIcon(new ImageIcon("img/" + resultSet.getString(4)).getImage().getScaledInstance(155, 114, Image.SCALE_DEFAULT));
                row[3] = myimg;
                row[4] = resultSet.getString(5);
                model.addRow(row);
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}

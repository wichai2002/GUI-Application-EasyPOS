package src;

public class DbConnector {
    private String host;
    private String root;
    private String password;

    public DbConnector(){
        this.host = "jdbc:mysql://localhost:3306/oop_project_2565";
        this.root = "root";
        this.password = ""; // add your database password
    }

    public String getHost(){
        return host;
    }

    public void setHost(String host){
        this.host = host;
    }

    public String getRoot(){
        return root;
    }

    public void setRoot(String root){
        this.root = root;
    }

    public String getPass(){
        return password;
    }

    public void setPass(String password){
        this.password = password;
    }
}
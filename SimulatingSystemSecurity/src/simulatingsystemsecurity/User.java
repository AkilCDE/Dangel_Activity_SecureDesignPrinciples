package simulatingsystemsecurity;

public class User {
    private String username;
    private String role;
    
    public User(String username, String role){
        this.username = username;
        this.role = role;
    }
    
    public String getUsername(){
        return this.username;
    }
    
    public String getRole(){
        return this.role;
    }
}

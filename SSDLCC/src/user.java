public class user {
    private String username;
    private String role;
    private String password;

    public user(String username, String role, String password) {
        this.username = username;
        this.role = role;
        this.password = password;
    }

    public String getUsername() { return username; }
    public String getRole() { return role; }
    public String getPassword() { return password; }
}

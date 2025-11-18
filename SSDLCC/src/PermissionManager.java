public class PermissionManager {

    public String getPermissions(String role) {
        switch (role.toLowerCase()) {
            case "student":
                return "VIEW ONLY";
            case "teacher":
                return "VIEW + EDIT";
            case "admin":
                return "FULL ACCESS";
            default:
                return "NO ACCESS";
        }
    }
}

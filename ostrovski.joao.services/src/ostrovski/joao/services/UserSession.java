package ostrovski.joao.services;

import ostrovski.joao.common.model.dto.UserDTO;

public class UserSession {

    // Singleton UserSession instance
    public static UserSession instance;
    // UserDTO class with userInformation
    private UserDTO userInSession;

    private UserSession() {}

    // Singleton instance getter
    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    // returns authenticated user profile name 
    // to check privileges and methods access without exposing user information
    public String getUserInSessionPrivilege() {
        if (this.userInSession != null) {
            return this.userInSession.profile().profileName();
        }
        return null;
    }

    // private getter for user information
    public int getUserInSessionId() {
        return this.userInSession.userId();
    }

    // instantiate user session to single instance of this class
    public void setUserInSession(UserDTO user) {
        this.userInSession = user;
    }

    public boolean isAdmin() {
        // get user privilege STRING with profile name
        String privilege = this.getUserInSessionPrivilege();
        return privilege != null && privilege.equalsIgnoreCase("admin");
    }

    // closes current session
    public void clearUserInSession() {
        this.userInSession = null;
    }
}
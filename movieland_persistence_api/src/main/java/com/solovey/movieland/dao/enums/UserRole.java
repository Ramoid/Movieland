package com.solovey.movieland.dao.enums;


public enum UserRole {
    USER("USER"),
    ADMIN("ADMIN");
    private final String role;

    UserRole(String role) {
        this.role = role;
    }

    public static UserRole getRole(String roleString) {

        for (UserRole role : UserRole.values()) {
            if (role.role.equalsIgnoreCase(roleString)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Wrong role");
        // UAH in case when wrong parameter value
        //return UserRole.UAH;
    }

    public String getCurrency(){
        return  role;
    }
}

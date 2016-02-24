package com.example.PhysiotherapistApp.Model;

/**
 * Created by Amar on 2016-02-06.
 */
public class UserState {
    private static String authToken;

    private static boolean isPhysio;

    private static String userName;

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        UserState.userName = userName;
    }

    public static boolean isPhysio() {
        return isPhysio;
    }

    public static void setIsPhysio(boolean isPhysio) {
        UserState.isPhysio = isPhysio;
    }

    public static String getAuthToken() {
        return authToken;
    }

    public static void setAuthToken(String authToken) {
        UserState.authToken = authToken;
    }
}

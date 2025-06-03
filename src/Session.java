package src;

/**
 * Manages user session state including login, logout, and username tracking.
 * This class provides static methods to handle user authentication state
 * throughout the application lifecycle.
 */
public class Session {
    /**
     * The currently logged in username, null if no user is logged in.
     */
    private static String username = null;

    /**
     * Performs user login by setting the current username.
     *
     * @param user the username to log in
     */
    public static void login(String user) {
        username = user;
    }

    /**
     * Performs user logout by clearing the current username.
     */
    public static void logout() {
        username = null;
    }

    /**
     * Returns the currently logged in username.
     *
     * @return the current username, or null if no user is logged in
     */
    public static String getUsername() {
        return username;
    }

    /**
     * Checks whether a user is currently logged in.
     *
     * @return true if a user is logged in, false otherwise
     */
    public static boolean isLoggedIn() {
        return username != null;
    }
}
package src;

/**
 * Verwaltet die Benutzersitzung (Login, Logout, Benutzername).
 */
public class Session {
    private static String username = null;

    /**
     * Führt einen Login durch.
     *
     * @param user der Benutzername
     */
    public static void login(String user) {
        username = user;
    }

    /**
     * Führt einen Logout durch.
     */
    public static void logout() {
        username = null;
    }

    /**
     * Gibt den aktuell eingeloggten Benutzernamen zurück.
     * Falls kein Benutzer eingeloggt ist, wird der Systemname verwendet.
     *
     * @return Benutzername
     */
    public static String getUsername() {
        return username;
    }

    /**
     * Gibt zurück, ob ein Benutzer eingeloggt ist.
     *
     * @return true wenn eingeloggt, sonst false
     */
    public static boolean isLoggedIn() {
        return username != null;
    }
}

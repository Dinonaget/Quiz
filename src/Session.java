package src;

/**
 * Verwaltet den Benutzersitzungsstatus einschließlich Anmeldung, Abmeldung und Benutzernamenverfolgung.
 * Diese Klasse bietet statische Methoden zur Handhabung des Benutzerauthentifizierungsstatus
 * während des gesamten Anwendungslebenszyklus.
 */
public class Session {
    /**
     * Der aktuell angemeldete Benutzername, null, wenn kein Benutzer angemeldet ist.
     */
    private static String username = null;

    /**
     * Führt die Benutzeranmeldung durch, indem der aktuelle Benutzername gesetzt wird.
     *
     * @param user der Benutzername, der angemeldet werden soll
     */
    public static void login(String user) {
        username = user;
    }

    /**
     * Führt die Benutzerabmeldung durch, indem der aktuelle Benutzername gelöscht wird.
     */
    public static void logout() {
        username = null;
    }

    /**
     * Gibt den aktuell angemeldeten Benutzernamen zurück.
     *
     * @return der aktuelle Benutzername oder null, wenn kein Benutzer angemeldet ist
     */
    public static String getUsername() {
        return username;
    }

    /**
     * Überprüft, ob ein Benutzer aktuell angemeldet ist.
     *
     * @return true, wenn ein Benutzer angemeldet ist, false sonst
     */
    public static boolean isLoggedIn() {
        return username != null;
    }
}

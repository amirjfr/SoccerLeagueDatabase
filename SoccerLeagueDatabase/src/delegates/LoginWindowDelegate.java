package delegates;

/**
 *  Taken from Tutorial 6 JavaDemo
 * This interface uses the delegation design pattern where instead of having
 * the LoginWindow class try to do everything, it will only focus on
 * handling the UI. The actual logic/operation will be delegated to the controller
 * class.
 */
public interface LoginWindowDelegate {
    void login(String username, String password);
}

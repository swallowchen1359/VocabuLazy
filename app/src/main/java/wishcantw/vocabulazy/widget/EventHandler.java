package wishcantw.vocabulazy.widget;

public interface EventHandler {

    int EVENT_PLAYER        = 0x0 << 16;
    int EVENT_PLAYER_OPTION = 0x1 << 16;

    /**
     * The function for EventDispatcher usage.
     * @param eventID specify which event should be trigger
     * @param args passed to the handler
     */
    void execute(int eventID, Object ... args);
}
package wishcantw.vocabulazy.widget;

import wishcantw.vocabulazy.widget.EventHandler;
import wishcantw.vocabulazy.utility.Logger;
import java.util.HashMap;

public class EventDispatcher {

    private static HashMap<Integer, EventHandler> mEventMap = new HashMap<>();

    /**------------------------------------ Public methods -------------------------------------**/

    /**
     *
     * @param eventID
     * @return boolean true means request successfully and the handler will execute immediately,
     *         or false otherwise, may be caused by there's no handler registered with the eventID.
     *         Try registerEventHandler first.
     */
    public static boolean request(int eventID, Object ... args) {
        EventHandler handler;
        if ((handler = getEventHandler(eventID)) != null) {
            handler.execute(eventID, args);
            return true;
        }
        return false;
    }

    /**
     *
     * @param eventID
     * @param hdlr
     * @return true means register successfully, or false otherwise, may be caused by duplicate
     *         registering. Try to unregisterEventHandler first.
     */
    public static boolean registerEventHandler(int eventID, EventHandler hdlr) {
        if (!mEventMap.containsKey(eventID)) {
            mEventMap.put(eventID, hdlr);
            return true;
        }
        return false;
    }

    /**
     *
     * @param eventID
     * @return true means unregister successfully, or false otherwise, may be caused by there's
     *         no handler registered with eventID. Try to registerEventHandler first.
     */
    public static boolean unregisterEventHandler(int eventID) {
        if (mEventMap.containsKey(eventID)) {
            mEventMap.remove(eventID);
            return true;
        }
        return false;
    }

    /**------------------------------------ Private methods ------------------------------------**/

    /**
     *
     * @param eventID
     */
    private static EventHandler getEventHandler(int eventID) {
        return mEventMap.get(eventID);
    }
}
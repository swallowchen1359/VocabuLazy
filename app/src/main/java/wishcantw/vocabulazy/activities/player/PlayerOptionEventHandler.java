package wishcantw.vocabulazy.activities.player;

import android.view.View;

import wishcantw.vocabulazy.widget.EventHandler;

/**
 * @author Swallow Chen
 * @since 2016/12/16
 */
public interface PlayerOptionEventHandler extends EventHandler {
    public static final int EVENT_OPTION_CHANGED = 0x1 | EVENT_PLAYER_OPTION;

    /**
     * The callback function when option is changed by user
     * @param optionID indicate which item is changed
     * @param mode indicate item in which mode is changed
     * @param value indicate the current item value
     */
    void onOptionChanged(int optionID, int mode, View v, int value);
}
package wishcantw.vocabulazy.activities.player.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.database.object.OptionSettings;
import wishcantw.vocabulazy.widget.DialogViewNew;

/**
 * Created by SwallowChen on 11/21/16.
 */

public class PlayerOptionDialogView extends DialogViewNew {

    public interface PlayerOptionEventListener {
        /**
         * The callback function when any one of Option in PlayerOptionContent changed
         * @param optionID indicate which option is changed by user
         * @param mode indicate which mode is currently being changed
         * @param v the changed view
         * @param value left/right arrow of pickers
         * @see PlayerOptionView
         * */
        void onPlayerOptionChanged(int optionID, int mode, View v, int value);
    }

    private static final int VIEW_PLAYER_OPTION_VIEW_RES_ID = R.id.view_player_option;

    private PlayerOptionView mPlayerOptionView;
    private PlayerOptionEventListener mPlayerOptionEventListener;

    public PlayerOptionDialogView(Context context) {
        this(context, null);
    }

    public PlayerOptionDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mPlayerOptionView = (PlayerOptionView) findViewById(VIEW_PLAYER_OPTION_VIEW_RES_ID);

        registerEventListener();
    }

    /**------------------------------------- public method --------------------------------------**/

    public void setPlayerOptionEventListener(PlayerOptionEventListener listener) {
        mPlayerOptionEventListener = listener;
    }
    /**
     * The api for setting PlayerOptionView content by OptionSettings
     * @param option
     */
    public void setPlayerOptionModeContent(OptionSettings option, boolean init) {
        mPlayerOptionView.setOptionInModeContent(option, init);
    }

    /**------------------------------------ private method --------------------------------------**/

    private void registerEventListener() {
        mPlayerOptionView.setOnOptionChangedListener(new PlayerOptionView.OnOptionChangedListener() {
            @Override
            public void onOptionChanged(int optionID, int mode, View v, int value) {
                if (mPlayerOptionEventListener != null) {
                    mPlayerOptionEventListener.onPlayerOptionChanged(optionID, mode, v, value);
                }
            }
        });
    }
}

package wishcantw.vocabulazy.activities.player.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.database.object.OptionSettings;
import wishcantw.vocabulazy.widget.DialogViewNew;

/**
 * Created by SwallowChen on 11/21/16.
 */

public class PlayerOptionDialogView extends DialogViewNew {

    public interface PlayerOptionCallbackFunc {
        /**
         * The callback function for user to indicate the value to be shown on seek bar
         * @param seekBarIdx indicate which seek bar is ready to show the value
         * @param seekBarVal indicate the current value of seek bar
         * @return the value to be shown on seek bar
         */
        int getBalloonVal(int seekBarIdx, int seekBarVal);

        /**
         *
         */
        void playPrank(int count);
    }

    private static final int VIEW_PLAYER_OPTION_VIEW_RES_ID = R.id.view_player_option;

    private PlayerOptionView mPlayerOptionView;
    private PlayerOptionCallbackFunc mPlayerOptionCallbackFunc;

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

    public void setPlayerOptionCallbackFunc(PlayerOptionCallbackFunc callbackFunc) {
        mPlayerOptionCallbackFunc = callbackFunc;
    }
    /**
     * The api for setting PlayerOptionView content by OptionSettings
     * @param option
     */
    public void setPlayerOptionModeContent(@NonNull OptionSettings option,
                                           boolean init,
                                           boolean voiceEnable) {
        mPlayerOptionView.setOptionInModeContent(option, init, voiceEnable);
    }

    /**------------------------------------ private method --------------------------------------**/

    private void registerEventListener() {

        mPlayerOptionView.setOptionCallbackFunc(new PlayerOptionView.OptionCallbackFunc() {
            @Override
            public int getBalloonVal(int seekBarIdx, int i) {
                if (mPlayerOptionCallbackFunc != null) {
                    return mPlayerOptionCallbackFunc.getBalloonVal(seekBarIdx, i);
                }
                return i;
            }

            @Override
            public void playPrank(int count) {
                if (mPlayerOptionCallbackFunc != null) {
                    mPlayerOptionCallbackFunc.playPrank(count);
                }
            }
        });
    }
}

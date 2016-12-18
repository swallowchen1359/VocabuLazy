package wishcantw.vocabulazy.activities.player.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.activities.player.PlayerOptionEventHandler;
import wishcantw.vocabulazy.audio.AudioService;
import wishcantw.vocabulazy.database.AppPreference;
import wishcantw.vocabulazy.database.object.OptionSettings;
import wishcantw.vocabulazy.activities.player.activity.PlayerActivity;
import wishcantw.vocabulazy.activities.player.model.PlayerModel;
import wishcantw.vocabulazy.activities.player.view.PlayerOptionDialogView;
import wishcantw.vocabulazy.activities.player.view.PlayerOptionView;
import wishcantw.vocabulazy.utility.Logger;
import wishcantw.vocabulazy.widget.DialogFragmentNew;
import wishcantw.vocabulazy.widget.EventDispatcher;

/**
 * Created by SwallowChen on 11/21/16.
 */

public class PlayerOptionDialogFragment extends DialogFragmentNew implements PlayerOptionEventHandler,
                                                                             PlayerOptionDialogView.PlayerOptionCallbackFunc {

    public interface OnPlayPrankListener {
        void onPlayPrank(int count);
    }
    // layout resources
    private static final int LAYOUT_RES_ID = R.layout.view_player_option_dialog;

    // views
    private PlayerOptionDialogView mPlayerOptionDialogView;

    private Context mContext;

    // Model
    private PlayerModel mPlayerModel;

    private OnPlayPrankListener mOnPlayPrankListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPlayerOptionDialogView = (PlayerOptionDialogView) inflater.inflate(LAYOUT_RES_ID, container, false);
        EventDispatcher.registerEventHandler(EVENT_OPTION_CHANGED, this);
        mPlayerOptionDialogView.setPlayerOptionCallbackFunc(this);
        return mPlayerOptionDialogView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPlayerModel = ((PlayerActivity) mContext).getPlayerModel();

        OptionSettings optionSettings = mPlayerModel.getPlayerOptionSettings();

        mPlayerOptionDialogView.setPlayerOptionModeContent(
                optionSettings,
                true,
                (AppPreference.getInstance().getPlayerVolume() == 1.0f)
        );
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    protected String getGALabel() {
        return null;
    }

    /**------------------------------------- public method --------------------------------------**/
    public void setOnPlayPrankListener(OnPlayPrankListener listener) {
        mOnPlayPrankListener = listener;
    }

    /**---------------------------- Implement PlayerOptionEventHandler --------------------------**/

    @Override
    public void onOptionChanged(int optionID, int mode, View v, int value) {
        if (optionID == PlayerOptionView.IDX_OPTION_MODE) {
            // The value is the mode option index that is selected
            AppPreference.getInstance().setPlayerOptionMode(value);

            mPlayerOptionDialogView.setPlayerOptionModeContent(
                    mPlayerModel.getPlayerOptionSettings(),
                    false,
                    (AppPreference.getInstance().getPlayerVolume() == 1.0f));
        }

        // Refresh option settings
        mPlayerModel.updateOptionSettings(optionID, mode, v, value);

        Intent intent = new Intent(mContext, AudioService.class);
        intent.setAction(AudioService.OPTION_SETTINGS_CHANGED);
        mContext.startService(intent);
    }

    @Override
    public void execute(int eventID, Object... args) {
        Logger.d("PlayerOptionDialogFragment", "execute " + eventID);
        switch (eventID) {
            case EVENT_OPTION_CHANGED:
                onOptionChanged((int) args[0], (int) args[1], (View) args[2], (int) args[3]);
                break;
            default:
                break;
        }
    }

    /**--------------------- PlayerOptionDialogView.PlayerOptionCallbackFunc --------------------**/

    @Override
    public int getBalloonVal(int seekBarIdx, int seekBarVal) {
        switch (seekBarIdx) {

            case PlayerOptionView.IDX_SEEK_BAR_SPEED:
                // TODO : Beibei please fill in the formula of the speed
                break;

            case PlayerOptionView.IDX_SEEK_BAR_REPEAT:
                seekBarVal += 1;
                break;

            case PlayerOptionView.IDX_SEEK_BAR_PLAY_TIME:
                seekBarVal += 10;
                break;

            default:
                break;
        }

        return seekBarVal;
    }

    @Override
    public void playPrank(int count) {
        if (mOnPlayPrankListener != null) {
            mOnPlayPrankListener.onPlayPrank(count);
        }
    }
}

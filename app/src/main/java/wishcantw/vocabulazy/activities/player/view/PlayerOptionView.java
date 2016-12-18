package wishcantw.vocabulazy.activities.player.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.database.object.OptionSettings;
import wishcantw.vocabulazy.activities.player.PlayerOptionEventHandler;
import wishcantw.vocabulazy.utility.Logger;
import wishcantw.vocabulazy.widget.EventDispatcher;

/**
 * Created by SwallowChen on 11/18/16.
 */

public class PlayerOptionView extends LinearLayout {

    /**
     * OptionCallbackFunc is the set of all callback functions in option view
     */
    public interface OptionCallbackFunc {
        /**
          * The callback function for user to indicate the value to be shown on seek bar
          * @param seekBarIdx indicate which seek bar is ready to show the value
          * @param i indicate the current value of seek bar
          * @return the value to be shown on seek bar
         */
        int getBalloonVal(int seekBarIdx, int i);
        /**
         *
         */
        void playPrank(int count);
    }

    public static final int IDX_OPTION_MODE      = 0x0;
    public static final int IDX_OPTION_RANDOM    = 0x1;
    public static final int IDX_OPTION_REPEAT    = 0x2;
    public static final int IDX_OPTION_SENTENCE  = 0x3;
    public static final int IDX_OPTION_SECOND    = 0x4;
    public static final int IDX_OPTION_FREQUENCY = 0x5;
    public static final int IDX_OPTION_SPEED     = 0x6;
    public static final int IDX_OPTION_PLAY_TIME = 0x7;
    public static final int IDX_OPTION_VOICE     = 0x8;

    public static final int IDX_SEEK_BAR_REPEAT = PlayerOptionSeekBarsView.IDX_SEEK_BAR_REPEAT;
    public static final int IDX_SEEK_BAR_SPEED  = PlayerOptionSeekBarsView.IDX_SEEK_BAR_SPEED;
    public static final int IDX_SEEK_BAR_PLAY_TIME = PlayerOptionSeekBarsView.IDX_SEEK_BAR_PLAY_TIME;

    private static final int VIEW_PLAYER_OPTION_VOICE_SWITCH_ID           = R.id.player_option_voice_switch;
    private static final int VIEW_PLAYER_OPTION_SENTENCE_SWITCH_ID        = R.id.player_option_sentence_switch;
    private static final int VIEW_PLAYER_OPTION_MODE_RADIO_GROUP_ID       = R.id.player_option_mode_radio_group;
    private static final int VIEW_PLAYER_OPTION_LIST_ORDER_RADIO_GROUP_ID = R.id.player_option_list_order_radio_group;
    private static final int VIEW_PLAYER_OPTION_VOC_ORDER_RADIO_GROUP_ID  = R.id.player_option_voc_order_radio_group;
    private static final int VIEW_PLAYER_OPTION_SEEK_BARS_ID              = R.id.player_option_seekbars;

    private Switch mVoiceSwitch, mSentenceSwitch;
    private RadioGroup mModeRadioGroup, mListOrderRadioGroup, mVocOrderRadioGroup;
    private PlayerOptionSeekBarsView mPlayerOptionSeekBarsView;
    // play a prank if user keep pressing the sentence switch
    private EasterEggTask mEasterEggTask;
    private OptionCallbackFunc mOptionCallbackFunc;
    private static boolean mContentIsInitializing;

    public PlayerOptionView(Context context) {
        this(context, null);
    }

    public PlayerOptionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mVoiceSwitch         = (Switch)     findViewById(VIEW_PLAYER_OPTION_VOICE_SWITCH_ID);
        mSentenceSwitch      = (Switch)     findViewById(VIEW_PLAYER_OPTION_SENTENCE_SWITCH_ID);
        mModeRadioGroup      = (RadioGroup) findViewById(VIEW_PLAYER_OPTION_MODE_RADIO_GROUP_ID);
        mListOrderRadioGroup = (RadioGroup) findViewById(VIEW_PLAYER_OPTION_LIST_ORDER_RADIO_GROUP_ID);
        mVocOrderRadioGroup  = (RadioGroup) findViewById(VIEW_PLAYER_OPTION_VOC_ORDER_RADIO_GROUP_ID);
        mPlayerOptionSeekBarsView = (PlayerOptionSeekBarsView) findViewById(VIEW_PLAYER_OPTION_SEEK_BARS_ID);

        for (int i = 0; i < Math.max(mModeRadioGroup.getChildCount(), Math.max(mListOrderRadioGroup.getChildCount(), mVocOrderRadioGroup.getChildCount())); i++) {
            if (mModeRadioGroup.getChildAt(i) != null) {
                mModeRadioGroup.getChildAt(i).setId(i);
            }
            if (mListOrderRadioGroup.getChildAt(i) != null) {
                mListOrderRadioGroup.getChildAt(i).setId(i);
            }
            if (mVocOrderRadioGroup.getChildAt(i) != null) {
                mVocOrderRadioGroup.getChildAt(i).setId(i);
            }
        }

        mPlayerOptionSeekBarsView.setSeekBarMax(IDX_SEEK_BAR_REPEAT, 4);
        mPlayerOptionSeekBarsView.setSeekBarMax(IDX_SEEK_BAR_SPEED, 2);
        mPlayerOptionSeekBarsView.setSeekBarMax(IDX_SEEK_BAR_PLAY_TIME, 30);
        registerOptionListener();
    }

    /**------------------------------------ public method ---------------------------------------**/
    public void setOptionCallbackFunc(OptionCallbackFunc callbackFunc) {
        mOptionCallbackFunc = callbackFunc;
    }

    /**
     * The api for setting all options in the PlayerOptionView
     * @param option The object containing all settings about the {@link}OptionSettings
     */
    public void setOptionInModeContent(@NonNull OptionSettings option,
                                       boolean init,
                                       boolean voiceEnable) {
        // unregister listener first to prevent from dependency
        boolean sentenceEnable;
        int modeIdx, listOrderIdx, vocOrderIdx;

        // unregisterListener();
        mContentIsInitializing = true;

        sentenceEnable = option.isSentence();
        modeIdx = option.getMode();
        listOrderIdx = option.getListLoop();
        vocOrderIdx = option.isRandom() ? 1 : 0;

        // Only when init state need to setup mode option
        if (init) {
            // Use input option to determined the initial (or last time saved) mode
            mModeRadioGroup.check(modeIdx);
        }

        mVoiceSwitch.setChecked(voiceEnable);
        mSentenceSwitch.setChecked(sentenceEnable);
        mListOrderRadioGroup.check(listOrderIdx);
        mVocOrderRadioGroup.check(vocOrderIdx);

        mPlayerOptionSeekBarsView.setSeekBarProgress(IDX_SEEK_BAR_REPEAT, option.getItemLoop()-1);
        mPlayerOptionSeekBarsView.setSeekBarProgress(IDX_SEEK_BAR_SPEED, option.getSpeed());
        mPlayerOptionSeekBarsView.setSeekBarProgress(IDX_SEEK_BAR_PLAY_TIME, option.getPlayTime()-10);

        mContentIsInitializing = false;
        // register back
        // restoreListener();
    }

    /**----------------------------------- private method ---------------------------------------**/

    /**
     * Register the callback function to monitor all event in PlayerOptionView. The listener will
     * sent event back if there's listener registered on top level, here is PlayerView.
     */
    private void registerOptionListener() {
        // The callback for voice enable switch
        mVoiceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!mContentIsInitializing) {
                    EventDispatcher.request(PlayerOptionEventHandler.EVENT_OPTION_CHANGED, IDX_OPTION_VOICE, mModeRadioGroup.getCheckedRadioButtonId(), mVoiceSwitch, b ? 1 : 0);
                }
            }
        });
        // The callback for sentence switch
        mSentenceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!mContentIsInitializing) {
                    EventDispatcher.request(PlayerOptionEventHandler.EVENT_OPTION_CHANGED, IDX_OPTION_SENTENCE, mModeRadioGroup.getCheckedRadioButtonId(), mSentenceSwitch, b ? 1 : 0);
                }
                // create easter egg for mSentenceSwitch for currently not support sentence voice
                if (b == true) {
                    if (mEasterEggTask == null) {
                        mEasterEggTask = new EasterEggTask(mSentenceSwitch, false);
                    }
                    mSentenceSwitch.postDelayed(mEasterEggTask, 200);
                }
            }
        });
        // The callback for changing mode choose
        mModeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (!mContentIsInitializing) {
                    EventDispatcher.request(PlayerOptionEventHandler.EVENT_OPTION_CHANGED, IDX_OPTION_MODE, i, mModeRadioGroup, i);
                }
            }
        });
        // The callback for list order choose
        mListOrderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (!mContentIsInitializing) {
                    EventDispatcher.request(PlayerOptionEventHandler.EVENT_OPTION_CHANGED, IDX_OPTION_REPEAT, mModeRadioGroup.getCheckedRadioButtonId(), mListOrderRadioGroup, i);
                }
            }
        });
        // The callback for vocabulary order choose
        mVocOrderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (!mContentIsInitializing) {
                    EventDispatcher.request(PlayerOptionEventHandler.EVENT_OPTION_CHANGED, IDX_OPTION_RANDOM, mModeRadioGroup.getCheckedRadioButtonId(), mVocOrderRadioGroup, i);
                }
            }
        });
        // The callback for any of seek bars is changed
        mPlayerOptionSeekBarsView.setEventListener(new PlayerOptionSeekBarsView.EventListener() {
            @Override
            public void onSeekBarChanged(int seekBarIdx, SeekBar seekBar, int i, boolean b) {
                switch (seekBarIdx) {
                    case IDX_SEEK_BAR_REPEAT:
                    case IDX_SEEK_BAR_SPEED:
                    case IDX_SEEK_BAR_PLAY_TIME:
                        // Because Seek bar idx is started from IDX_SEEK_BAR_REPEAT (FREQUENCY) (0)
                        int startIdx = IDX_OPTION_FREQUENCY;
                        int modeId = mModeRadioGroup.getCheckedRadioButtonId();
                        if (!mContentIsInitializing) {
                            EventDispatcher.request(PlayerOptionEventHandler.EVENT_OPTION_CHANGED, startIdx + seekBarIdx, modeId, seekBar, i);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        // The callback for user to indicate what the value should be shown on the seek bar
        mPlayerOptionSeekBarsView.setBalloonCallbackFunc(new PlayerOptionSeekBarsView.BalloonCallbackFunc() {
            @Override
            public int getBalloonVal(int seekBarIdx, int i) {
                if (!mContentIsInitializing) {
                    if (mOptionCallbackFunc != null) {
                        return mOptionCallbackFunc.getBalloonVal(seekBarIdx, i);
                    }
                }
                return i;
            }
        });
    }

    private void playPrank(int count) {
        if (mOptionCallbackFunc != null) {
            mOptionCallbackFunc.playPrank(count);
        }
    }

    // special task for easter egg, currently not support sentence voice
    private class EasterEggTask implements Runnable {
        private Switch mSwitchView;
        private boolean mForceVal;
        private int mTriggerCount;

        public EasterEggTask(Switch switchView, boolean forceVal) {
            mSwitchView = switchView;
            mForceVal = forceVal;
        }

        @Override
        public void run() {
            if (mSwitchView != null) {
                mTriggerCount++;
                mSwitchView.setChecked(mForceVal);
                playPrank(mTriggerCount);
            }
        }
    }
}

package wishcantw.vocabulazy.activities.player.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.widget.EventDispatcher;
import wishcantw.vocabulazy.activities.player.PlayerEventHandler;

/**
 * @author Swallow Chen
 * @since 2016/1/17
 */
public class PlayerPanelView extends LinearLayout {

	private static final int VIEW_ICON_FAVORITE = R.id.action_player_favorite;
	private static final int VIEW_ICON_PLAY = R.id.action_player_play;
	private static final int VIEW_ICON_OPTION = R.id.action_player_option;

    private static final int ICON_PLAY_RES_ID[] = {
            R.drawable.player_stop,
            R.drawable.player_play
    };
    
    private static final int ICON_PLAY_STATE_STOP = 0x0;
    private static final int ICON_PLAY_STATE_PLAY = 0x1;
    
    private ImageView mActionFavoriteIcon;
    private ImageView mActionPlayIcon;
    private ImageView mActionOptionIcon;

    private int mCurrentIconState;

    public PlayerPanelView(Context context) {
        this(context, null);
    }

    public PlayerPanelView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mCurrentIconState = -1;
    }
    
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mActionFavoriteIcon = (ImageView) findViewById(VIEW_ICON_FAVORITE);
        mActionPlayIcon = (ImageView) findViewById(VIEW_ICON_PLAY);
        mActionOptionIcon = (ImageView) findViewById(VIEW_ICON_OPTION);
        
        registerEventListener();
    }

    /**------------------------------------ Public methods -------------------------------------**/

    /**
     * The api for controller to customized the icon's state
     * @param favorite the icon "favorite" state
     * @param play the icon "play" state
     * @param option the icon "option" state
     * */
    public void setIconState(boolean favorite, boolean play, boolean option){
        if(play) {
            mCurrentIconState = ICON_PLAY_STATE_STOP;
            mActionPlayIcon.setImageResource(ICON_PLAY_RES_ID[mCurrentIconState]);
        } else {
            mCurrentIconState = ICON_PLAY_STATE_PLAY;
            mActionPlayIcon.setImageResource(ICON_PLAY_RES_ID[mCurrentIconState]);
        }
    }

    /**------------------------------------ Private methods ------------------------------------**/

    /**
     * Register all the child's event
     * */
    private void registerEventListener() {
        mActionFavoriteIcon.setOnClickListener(new OnClickListener(){
        	@Override
            public void onClick(View v) {
                EventDispatcher.request(PlayerEventHandler.EVENT_PANEL_FAVORITE_CLICK);
            }
        });
        
        mActionPlayIcon.setOnClickListener(new OnClickListener(){
        	@Override
            public void onClick(View v) {
                if(mCurrentIconState == ICON_PLAY_STATE_STOP) {
                    mCurrentIconState = ICON_PLAY_STATE_PLAY;
                }
                else {
                    mCurrentIconState = ICON_PLAY_STATE_STOP;
                }
                ((ImageView) v).setImageResource(ICON_PLAY_RES_ID[mCurrentIconState]);
                EventDispatcher.request(PlayerEventHandler.EVENT_PANEL_PLAY_CLICK);
            }
        });
        
        mActionOptionIcon.setOnClickListener(new OnClickListener(){
        	@Override
            public void onClick(View v) {
                EventDispatcher.request(PlayerEventHandler.EVENT_PANEL_OPTION_CLICK);
            }
        });
    }
}

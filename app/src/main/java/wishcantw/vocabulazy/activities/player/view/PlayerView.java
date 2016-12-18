package wishcantw.vocabulazy.activities.player.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import wishcantw.vocabulazy.database.object.OptionSettings;
import wishcantw.vocabulazy.R;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * @author Swallow Chen
 * @since 2016/1/17
 */
public class PlayerView extends RelativeLayout {

	private static final int VIEW_PLAYER_MAIN_RES_ID = R.id.player_main_view;
	private static final int VIEW_PLAYER_PANEL_RES_ID = R.id.player_panel;
	
	private PlayerMainView mPlayerMainView;
	private PlayerPanelView mPlayerPanelView;
	
	public PlayerView(Context context) {
		this(context, null);
	}
	
	public PlayerView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
    protected void onFinishInflate() {
		super.onFinishInflate();
        mPlayerMainView = (PlayerMainView) findViewById(VIEW_PLAYER_MAIN_RES_ID);
		mPlayerPanelView = (PlayerPanelView) findViewById(VIEW_PLAYER_PANEL_RES_ID);
    }
    
	/**------------------------------  PlayerMainView related action  ---------------------------**/
	
	/**
	 * Create a new PlayerScrollView and put into PlayerMainView's center as child view
	 * @param playerDataList The linkedlist contains HashMaps &lt;key, value&gt; key will be the data need
	 *                       to be input, while value will be the content.
	 * @param initPosition The param determines the init position when player start playing
	 * @see PlayerMainView.PlayerScrollView
	 * @see PlayerMainView
	 */
	public void addNewPlayer(LinkedList<HashMap> playerDataList, int initPosition) {
		mPlayerMainView.addNewPlayer(playerDataList, initPosition);
	}
	
	/**
	 * Delete an old PlayerScrollView and remove it from PlayerMainView with specific direction
	 * @param direction Specify which PlayerScrollView should be removed
	 * @see PlayerMainView
	 * @see PlayerMainView.PlayerScrollView
	 */
	public void removeOldPlayer(int direction) {
		mPlayerMainView.removeOldPlayer(direction);
	}
	
	/**
	 * Change the Player page according to input
	 * @param direction Specify which page should be shown after the api called
	 * @see PlayerMainView
	 */
	public void moveToPlayer(int direction) {
		mPlayerMainView.setCurrentItem(direction);
	}
	
	/**
	 * Change the Player currently playing item
	 * @param position Specify which playing item should be player immediately after api called
	 * @see PlayerMainView
	 * @see PlayerMainView.PlayerScrollView
	 */
	public void moveToPosition(int position) {
		mPlayerMainView.moveToPosition(position);
	}
	
	/**
	 * Make the Player show the currently playing item's Player detail information 
	 * @see PlayerMainView
	 * @see PlayerMainView.PlayerScrollView
	 */
	public void showDetail() {
		mPlayerMainView.showDetail();
	}
	
	/**
	 * Make the Player hide the currently playing item's Player detail information
	 * @see PlayerMainView
	 * @see PlayerMainView.PlayerScrollView
	 */
	public void hideDetail() {
		mPlayerMainView.hideDetail();
	}
	
	/**
	 * Update the Player detail information
	 * @param dataMap contains &lt;key, value&gt; which cooresponding to input string and the view id
	 *		    	  , the view id is the view to show one data
	 * @see PlayerMainView
	 * @see PlayerMainView.PlayerScrollView
	 */
	public void refreshPlayerDetail(HashMap<String, Object> dataMap) {
		mPlayerMainView.refreshPlayerDetail(dataMap);
	}
	
	/**
	 * Playing item may have more than one page, the api make Player show different playing detail
	 * @param index the page of playing detail information
	 * @see PlayerMainView
	 * @see PlayerMainView.PlayerScrollView
	 */
	public void moveDetailPage(int index) {
		mPlayerMainView.moveToDetailPage(index);
	}
	
	/**----------------------------- PlayerPanelView related action  ---------------------------**/
	
	/**
	 * The api for setting PlayerPanelView states.
	 * @param favorite the state of icon "favorite"
	 * @param play the state of icon "play"
	 * @param option the state of icon "option"
	 * @see PlayerPanelView
	 */
	public void setIconState(boolean favorite, boolean play, boolean option) {
		mPlayerPanelView.setIconState(favorite, play, option);
	}
}
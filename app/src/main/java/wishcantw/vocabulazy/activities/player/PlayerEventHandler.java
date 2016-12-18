package wishcantw.vocabulazy.activities.player;

import wishcantw.vocabulazy.widget.EventHandler;

/**
 * @author Swallow Chen
 * @since 2016/12/15
 */
public interface PlayerEventHandler extends EventHandler {
    int EVENT_VERTICAL_SCROLL_ING  = 0x1 | EVENT_PLAYER;
    int EVENT_VERTICAL_SCROLL_STOP = 0x2 | EVENT_PLAYER;
    int EVENT_PAGE_SCROLL_ING      = 0x3 | EVENT_PLAYER;
    int EVENT_PAGE_SCROLL_STOP     = 0x4 | EVENT_PLAYER;
    int EVENT_DETAIL_SCROLL_ING    = 0x5 | EVENT_PLAYER;
    int EVENT_DETAIL_SCROLL_STOP   = 0x6 | EVENT_PLAYER;
    int EVENT_ITEM_PREPARED_INIT   = 0x7 | EVENT_PLAYER;
    int EVENT_ITEM_PREPARED_LAST   = 0x8 | EVENT_PLAYER;
    int EVENT_PANEL_FAVORITE_CLICK = 0x9 | EVENT_PLAYER;
    int EVENT_PANEL_PLAY_CLICK     = 0xA | EVENT_PLAYER;
    int EVENT_PANEL_OPTION_CLICK   = 0xB | EVENT_PLAYER;

    /**
     * The callback function when vertical scroll performing
     * mapping to event EVENT_VERTICAL_SCROLL_ING
     * @see wishcantw.vocabulazy.activities.player.view.PlayerMainView
     * */
    void onPlayerVerticalScrolling();

    /**
     * The callback function when vertical scroll stopped,
     * mapping to event EVENT_VERTICAL_SCROLL_STOP
     * @param index indicate which player item is in the center
     * @param isViewTouchedDown the boolean is used to notify whether the move is caused by service or by user
     * @see wishcantw.vocabulazy.activities.player.view.PlayerMainView
     * */
    void onPlayerVerticalScrollStop(int index, boolean isViewTouchedDown);

    /**
     * The callback function when horizontal scroll performing
     * mapping to event EVENT_PAGE_SCROLL_ING
     * @see wishcantw.vocabulazy.activities.player.view.PlayerMainView
     * */
    void onPlayerPageScrolling();

    /**
     * The callback function when horizontal scroll stopped,
     * mapping to event EVENT_PAGE_SCROLL_STOP
     * @param isOrderChanged the boolean indicate whether really change the player page
     * @param direction the direction indicate which page is going to switch to
     * @param isViewTouchedDown the boolean is used to notify whether the move is caused by service or by user
     * @see wishcantw.vocabulazy.activities.player.view.PlayerMainView
     * */
    void onPlayerPageScrollStop(boolean isOrderChanged, int direction, boolean isViewTouchedDown);

    /**
     * The callback function when Player detail page scroll performing
     * mapping to event EVENT_DETAIL_SCROLL_ING
     * @see wishcantw.vocabulazy.activities.player.view.PlayerMainView
     * @see wishcantw.vocabulazy.activities.player.view.PlayerMainView.PlayerScrollView
     * */
    void onPlayerDetailScrolling();

    /**
     * The callback function when Player detail page scroll stopped,
     * mapping to event EVENT_DETAIL_SCROLL_STOP
     * @param index indicate the currently showing player detail page
     * @param isViewTouchedDown indicate the move is caused by service or by user
     * @see wishcantw.vocabulazy.activities.player.view.PlayerMainView
     * */
    void onPlayerDetailScrollStop(int index, boolean isViewTouchedDown);

    /**
     * The callback function when first Player item is prepared
     * mapping to event EVENT_ITEM_PREPARED_INIT
     * @see wishcantw.vocabulazy.activities.player.view.PlayerMainView
     * */
    void onPlayerInitialItemPrepared();

    /**
     * The callback function when last Player item is prepared
     * mapping to event EVENT_ITEM_PREPARED_LAST
     * @see wishcantw.vocabulazy.activities.player.view.PlayerMainView
     * */
    void onPlayerFinalItemPrepared();

    /**
     * The callback function when icon "Favorite" is clicked
     * mapping to event int EVENT_PANEL_FAVORITE_CLICK
     * @see wishcantw.vocabulazy.activities.player.view.PlayerMainView
     * */
    void onPlayerPanelFavoriteClick();

    /**
     * The callback function when icon "Play" is clicked
     * mapping to event int EVENT_PANEL_PLAY_CLICK
     * @see wishcantw.vocabulazy.activities.player.view.PlayerMainView
     * */
    void onPlayerPanelPlayClick();

    /**
     * The callback function when icon "Option" is clicked
     * mapping to event int EVENT_PANEL_OPTION_CLICK
     * @see wishcantw.vocabulazy.activities.player.view.PlayerMainView
     * */
    void onPlayerPanelOptionClick();
}
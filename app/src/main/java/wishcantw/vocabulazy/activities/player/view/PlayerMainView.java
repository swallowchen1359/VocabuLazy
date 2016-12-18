package wishcantw.vocabulazy.activities.player.view;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.widget.CustomPageChangeListener;
import wishcantw.vocabulazy.widget.LinkedListPagerAdapter;
import wishcantw.vocabulazy.widget.Infinite3View;
import wishcantw.vocabulazy.widget.PopScrollView;
import wishcantw.vocabulazy.widget.EventDispatcher;
import wishcantw.vocabulazy.activities.player.PlayerEventHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * @author Swallow Chen
 * @since 2016/1/14.
 */
public class PlayerMainView extends Infinite3View {

    public static final String TAG = PlayerMainView.class.getSimpleName();

    private Context mContext;
    private PlayerScrollView mPlayerScrollView;

    private static boolean isViewTouchedDown = false;
    private static boolean isScrolling = false;

    public PlayerMainView(Context context) {
        this(context, null);
    }

    public PlayerMainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mPlayerScrollView = null;
        registerEventListener();
    }

    private void registerEventListener() {
        setOnPageChangedListener(new OnPageChangedListener() {
            @Override
            public void onPageScrolled() {
                if (!isScrolling) {
                    EventDispatcher.request(PlayerEventHandler.EVENT_PAGE_SCROLL_ING);
                    isScrolling = true;
                }
            }

            @Override
            public void onPageScrollStop(boolean isOrderChanged, int direction) {
                EventDispatcher.request(PlayerEventHandler.EVENT_PAGE_SCROLL_STOP, isOrderChanged, direction, isViewTouchedDown);
                isViewTouchedDown = false;
                isScrolling = false;
            }

            @Override
            public void onPageChanged(int direction) {

            }
        });
    }

    /**
     * Create a PopScrollView into Infinite3View with input data and start at input initPosition
     * @param playerDataList the data ready to be shown on PlayerItem
     * @param initPosition the start position of PopScrollView
     * @see PopScrollView
     * @see Infinite3View
     */
    public void addNewPlayer(LinkedList<HashMap> playerDataList, int initPosition) {
        mPlayerScrollView = new PlayerScrollView(mContext);
        mPlayerScrollView.setPopItemAdapter(
                mPlayerScrollView.getPopItemAdapter(mContext,
                        PlayerScrollView.PLAYER_ITEM_LAYOUT_RES_ID, playerDataList,
                        PlayerScrollView.PLAYER_ITEM_CONTENT_FROM,
                        PlayerScrollView.PLAYER_ITEM_CONTENT_TO), initPosition);

        refreshItem(CENTER_VIEW_INDEX, mPlayerScrollView);
    }

    /**
     * Delete the Player from Infinite3View
     * @param position the page ready to be deleted
     * @see Infinite3View
     */
    public void removeOldPlayer(int position){
        refreshItem(position, null);
    }

    /**
     * The api for dynamically refresth Player detail
     * @param dataMap the data want to show on Player Detail
     * @see PopScrollView
     */
    public void refreshPlayerDetail(HashMap<String, Object> dataMap){
        if (mPlayerScrollView != null) {
            mPlayerScrollView.refreshPlayerDetail(dataMap);
        }
    }

    /**
     * The api show playing item detail
     * @see PopScrollView
     */
    public void showDetail() {
        if (mPlayerScrollView != null) {
            mPlayerScrollView.showItemDetails();
        }
    }

    /**
     * The api hides playing item detail
     * @see PopScrollView
     */
    public void hideDetail() {
        if (mPlayerScrollView != null) {
            mPlayerScrollView.hideItemDetails();
        }
    }

    /**
     * Move currently playing index to input position
     * @param position the desired playing player index
     */
    public void moveToPosition(int position) {
        if (mPlayerScrollView != null) {
            mPlayerScrollView.moveToPosition(position);
        }
    }

    /**
     * Move player detail page to input index
     * @param index the desired index of playing detail
     */
    public void moveToDetailPage(int index) {
        if (mPlayerScrollView != null) {
            mPlayerScrollView.setDetailPage(index);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isViewTouchedDown = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (getCurrentItem() != null) {
                    if (((PlayerScrollView) getCurrentItem()).isShowingDetails() == PopScrollView.STATE_ITEM_DETAIL_SHOW) {
                        return false;
                    }
                }
                break;
            default:
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    public static class PlayerScrollView extends PopScrollView {

        private static final int PLAYER_ITEM_LAYOUT_RES_ID = R.layout.view_player_item;
        private static final int PLAYER_ITEM_DETAIL_LAYOUT_RES_ID = R.layout.view_player_details;
        private static final int PLAYER_ITEM_DETAIL_SENTENCE_LAYOUT_RES_ID = R.layout.view_player_details_sentence;
        private static final int PLAYER_ITEM_DETAIL_PAGER_PARENT_LAYOUT_RES_ID = R.id.pager_parent;
        private static final int PLAYER_ITEM_DETAIL_PAGER_INDEX_PARENT_LAYOUT_RES_ID = R.id.pager_index_parent;
        
        public static final String[] PLAYER_ITEM_CONTENT_FROM = {
            "voc_spell", "voc_category", "voc_translation"
        };
        public static final int[] PLAYER_ITEM_CONTENT_TO = {
            R.id.player_voc_spell, R.id.player_voc_category, R.id.player_voc_translation
        };

        public static final String[] PLAYER_ITEM_DETAIL_CONTENT_FROM = {
            "voc_spell_detail",
            "voc_translation_detail",
            "voc_kk_detail",
            "voc_sentence_detail",
            "voc_sentence_translation_detail"
        };

        public static final int[] PLAYER_ITEM_DETAIL_LAYOUT_CONTENT_TO = {
            R.id.player_voc_spell_detail,
            R.id.player_voc_translation_detail,
            R.id.player_voc_kk_detail,
            R.id.player_voc_sentence_detail,
            R.id.player_voc_sentence_translation_detail
        };

        private static final int IDX_DETAIL_SPELL = 0x0;
        private static final int IDX_DETAIL_TRANSLATION = 0x1;
        private static final int IDX_DETAIL_KK = 0x2;
        private static final int IDX_DETAIL_SENTENCE = 0x3;
        private static final int IDX_DETAIL_SENTENCE_TRANSLATION = 0x4;

        private static PlayerDetailView mPlayerDetailView;

        private static HashMap<String, Object> mPlayerDetailDataMap;

        public PlayerScrollView(Context context) {
            super(context);
            mPlayerDetailDataMap = new HashMap<>();
            mPlayerDetailView = new PlayerDetailView(context);
            mPlayerDetailView.setAdapter(
                mPlayerDetailView.getAdapter(
                    context, mPlayerDetailDataMap, PLAYER_ITEM_DETAIL_CONTENT_FROM, PLAYER_ITEM_DETAIL_LAYOUT_CONTENT_TO));
            registerItemPreparedEventListener();
            registerVerticalScrollEventListener();
        }

        private void registerItemPreparedEventListener() {
            setOnItemPreparedListener(new OnPopItemPreparedListener() {
                @Override
                public void onInitialPopItemPrepared() {
                    EventDispatcher.request(PlayerEventHandler.EVENT_ITEM_PREPARED_INIT);
                }

                @Override
                public void onFinalPopItemPrepared() {
                    EventDispatcher.request(PlayerEventHandler.EVENT_ITEM_PREPARED_LAST);
                }
            });
        }

        private void registerVerticalScrollEventListener() {
            setOnPopScrollStoppedListener(new OnPopScrollStoppedListener() {
                @Override
                public void onPopScrollStopped(int index) {
                    EventDispatcher.request(PlayerEventHandler.EVENT_VERTICAL_SCROLL_STOP, index, isViewTouchedDown);
                    isViewTouchedDown = false;
                    isScrolling = false;

                }

                @Override
                public void onPopScrolling() {
                    if (!isScrolling) {
                        EventDispatcher.request(PlayerEventHandler.EVENT_VERTICAL_SCROLL_ING);
                        isScrolling = true;
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                isViewTouchedDown = true;
            }
            return super.onInterceptTouchEvent(ev);
        }

        @Override
        protected View getItemDetailView() {
            return mPlayerDetailView;
        }

        /**
         * The api for dynamically refresth Player detail
         * @param dataMap the data want to show on Player Detail
         */
        public void refreshPlayerDetail(HashMap<String, Object> dataMap) {
            mPlayerDetailDataMap = dataMap;
            mPlayerDetailView = new PlayerDetailView(getContext());
            mPlayerDetailView.setAdapter(
                mPlayerDetailView.getAdapter(
                    getContext(), mPlayerDetailDataMap, PLAYER_ITEM_DETAIL_CONTENT_FROM, PLAYER_ITEM_DETAIL_LAYOUT_CONTENT_TO));
        }

        /**
         * Move the playing detail page
         * @param index the desired page index
         */
        public void setDetailPage(int index) {
            mPlayerDetailView.setCurrentPage(index);
        }

        private class PlayerDetailView extends LinearLayout {

            private Context context;
            private PlayerDetailView mContainer;
            private PagerIndexView pagerIndexView;
            private ViewPager viewPager;
            private LinkedList<ViewGroup> mItemPagesList;
            private PopItemDetailAdapter mAdapter;
            private int pageCount;

            public PlayerDetailView(Context context) {
                super(context);
                this.context = context;
                mContainer = this;
                pageCount = 0;
                setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                setOrientation(VERTICAL);
            }

            /**
             * After input, the PlayerDetailView will automatically fill in the input data based on adapter
             * @param adapter the format of input data
             */
            public void setAdapter(PopItemDetailAdapter adapter) {
                mAdapter = adapter;
                mAdapter.setChildViewToGroup();
            }

            /**
             * API return the adapter according to input param, the formate of input data will automatically assigned by function
             * @return PopItemDetailAdapter the format of input data, can be used to setAdapter
             * @param context the running activity
             * @param dataMap the data ready to show on player detail
             * @param from the data waiting filled in. The array can be got from the class public member
             * @param to the view id waiting filled in. The array can be got from the class public member
             * @see PlayerDetailView#setAdapter(PopItemDetailAdapter)
             */
            public PopItemDetailAdapter getAdapter(Context context, HashMap<String, Object> dataMap, String[] from, int[] to) {
                return new PopItemDetailAdapter(context, dataMap, from, to);
            }

            /**
             * Move the playing detail page
             * @param index the desired page index
             */
            public void setCurrentPage(int index) {
                if (viewPager != null) {
                    viewPager.setCurrentItem(index);
                }
            }

            private class PopItemDetailAdapter {
                private Context mContext;
                private HashMap<String, Object> mDataMap;
                private String[] mFrom;
                private int[] mTo;
                private LayoutInflater mInflater;

                PopItemDetailAdapter(Context context,
                                     HashMap<String, Object> dataMap,
                                     String[] from,
                                     int[] to) {
                    mContext = context;
                    mDataMap = dataMap;
                    mFrom = from;
                    mTo = to;
                    mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                }

                /**
                 * Fill in the data according to the constructor
                 */
                public void setChildViewToGroup() {
                    ViewGroup itemView = (ViewGroup) mInflater.inflate(PLAYER_ITEM_DETAIL_LAYOUT_RES_ID, null);
                    ArrayList<String> enSentences, ceSentences;
                    viewPager = new ViewPager(mContext);

                    if (mDataMap == null || mDataMap.size() == 0) {
                        return;
                    }

                    ((TextView) itemView
                            .findViewById(mTo[IDX_DETAIL_SPELL]))
                            .setText((String) mDataMap.get(mFrom[IDX_DETAIL_SPELL]));
                    ((TextView) itemView
                            .findViewById(mTo[IDX_DETAIL_TRANSLATION]))
                            .setText((String) mDataMap.get(mFrom[IDX_DETAIL_TRANSLATION]));
                    ((TextView) itemView
                            .findViewById(mTo[IDX_DETAIL_KK]))
                            .setText((String) mDataMap.get(mFrom[IDX_DETAIL_KK]));
                    ((TextView) itemView
                            .findViewById(mTo[IDX_DETAIL_KK]))
                            .setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/kk.TTE"));

                    enSentences = (ArrayList<String>) mDataMap.get(mFrom[IDX_DETAIL_SENTENCE]);
                    ceSentences = (ArrayList<String>) mDataMap.get(mFrom[IDX_DETAIL_SENTENCE_TRANSLATION]);

                    pageCount = enSentences.size();
                    pagerIndexView = new PagerIndexView(context, pageCount);

                    ((ViewGroup)itemView.findViewById(PLAYER_ITEM_DETAIL_PAGER_PARENT_LAYOUT_RES_ID)).addView(viewPager);
                    ((ViewGroup)itemView.findViewById(PLAYER_ITEM_DETAIL_PAGER_INDEX_PARENT_LAYOUT_RES_ID)).addView(pagerIndexView);
                    mContainer.addView(itemView);

                    createItemPages(pageCount, enSentences, ceSentences);
                }

                /**
                 * The player detail page will be done by input list, list[0] to page 0, list[1] to page 1, .etc.
                 * @param pageCount total page of player detail
                 * @param en_sentenceList list length should be the same with pageCount
                 * @param cn_sentenceList list length should be the same with pageCount
                 */
                private void createItemPages(int pageCount, ArrayList<String> en_sentenceList,
                                             ArrayList<String> cn_sentenceList) {
                    mItemPagesList = new LinkedList<>();
                    for(int i = 0; i < pageCount; i++) {
                        ViewGroup currentItemDetailsView =
                                (ViewGroup)((LayoutInflater) getContext()
                                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                                        .inflate(PLAYER_ITEM_DETAIL_SENTENCE_LAYOUT_RES_ID, null);

                        ((TextView) currentItemDetailsView
                                .findViewById(mTo[IDX_DETAIL_SENTENCE]))
                                .setText(en_sentenceList.get(i));
                        ((TextView) currentItemDetailsView
                                .findViewById(mTo[IDX_DETAIL_SENTENCE_TRANSLATION]))
                                .setText(cn_sentenceList.get(i));

                        mItemPagesList.add(currentItemDetailsView);
                    }
                    viewPager.setAdapter(new LinkedListPagerAdapter(mItemPagesList));
                    viewPager.addOnPageChangeListener(new OnPageChangeListener(viewPager));
                }
            }

            private class OnPageChangeListener extends CustomPageChangeListener {
                public OnPageChangeListener(ViewPager viewPager) {
                    super(viewPager);
                }

                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    for(int i = 0; i < pageCount; i++) {
                        if(i == position) {
                            ((GradientDrawable) ((ImageView) pagerIndexView.getChildAt(i)).getDrawable())
                                .setColor(ContextCompat.getColor(context, PagerIndexView.PAGER_INDEX_SELECTED_COLOR));
                        }
                        else {
                            ((GradientDrawable) ((ImageView) pagerIndexView.getChildAt(i)).getDrawable())
                                .setColor(ContextCompat.getColor(context, PagerIndexView.PAGER_INDEX_COLOR));
                        }
                    }

                    EventDispatcher.request(PlayerEventHandler.EVENT_DETAIL_SCROLL_STOP, position, isViewTouchedDown);
                    isViewTouchedDown = false;
                    isScrolling = false;
                }
            }

            @Override
            public boolean onInterceptTouchEvent(MotionEvent ev) {
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isViewTouchedDown = true;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (!isScrolling) {
                            EventDispatcher.request(PlayerEventHandler.EVENT_DETAIL_SCROLL_ING);
                            isScrolling = true;
                        }
                        break;
                    default:
                        break;
                }
                return super.onInterceptTouchEvent(ev);
            }

            @Override
            public boolean onTouchEvent(MotionEvent event) {
                return super.onTouchEvent(event);
            }
        }

        private static class PagerIndexView extends LinearLayout {

            private static final int PAGER_INDEX_SELECTED_COLOR = R.color.player_pager_index_selected;
            private static final int PAGER_INDEX_COLOR = R.color.player_pager_index_color;

            private Context context;
            private int pagerCount;

            public PagerIndexView(Context context, int pagerCount) {
                this(context, null, pagerCount);
            }

            public PagerIndexView(Context context, AttributeSet attrs, int pagerCount) {
                super(context, attrs);
                this.context = context;
                this.pagerCount = pagerCount;
                setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                createPagerIndex(this.pagerCount);
            }

            /**
             * The function create the index to indicate which page is currently showing
             * @param indexCount the page number, so the view can create pager index
             */
            private void createPagerIndex(int indexCount) {
                for(int i = 0; i < indexCount; i++){
                    ImageView imageView = new ImageView(context);
                    imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.player_detail_pager_index));
                    if(i == 0) {
                        ((GradientDrawable) imageView.getDrawable()).setColor(ContextCompat.getColor(context, PAGER_INDEX_SELECTED_COLOR));
                    }
                    else {
                        ((GradientDrawable) imageView.getDrawable()).setColor(ContextCompat.getColor(context, PAGER_INDEX_COLOR));
                    }
                    imageView.setPadding(5, 5, 5, 5);
                    addView(imageView);
                }
            }
        }
    }
}

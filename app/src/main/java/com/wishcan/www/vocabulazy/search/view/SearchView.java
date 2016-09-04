package com.wishcan.www.vocabulazy.search.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.wishcan.www.vocabulazy.R;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * @since 2016/8/31
 */
public class SearchView extends RelativeLayout {
    public interface SearchEventListener {
        void onSearchItemClick();
        void onSearchAddClick();
    }

    private static final int VIEW_SEARCH_LIST_RES_ID = R.id.search_list_view;
    private SearchListView mSearchListView;

    private SearchEventListener mSearchEventListener;

    public SearchView(Context context) {
        this(context, null);
    }

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mSearchListView = (SearchListView) findViewById(VIEW_SEARCH_LIST_RES_ID);

        registerEventListener();
    }

    public void setSearchEventListener(SearchEventListener listener) {
        mSearchEventListener = listener;
    }

    public void refreshSearchList(LinkedList<HashMap> dataList) {
        mSearchListView.refreshView(dataList.size(), dataList);
    }

    private void registerEventListener() {
        mSearchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mSearchEventListener != null) {
                    mSearchEventListener.onSearchItemClick();
                }
            }
        });
    }
}

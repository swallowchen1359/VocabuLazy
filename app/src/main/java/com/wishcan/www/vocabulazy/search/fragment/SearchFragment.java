package com.wishcan.www.vocabulazy.search.fragment;


import android.content.Context;
import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.wishcan.www.vocabulazy.application.VLApplication;
import com.wishcan.www.vocabulazy.ga.GASearchFragment;
import com.wishcan.www.vocabulazy.search.SearchActivity;
import com.wishcan.www.vocabulazy.search.model.SearchModel;
import com.wishcan.www.vocabulazy.search.view.SearchDialogView;
import com.wishcan.www.vocabulazy.search.view.SearchView;
import com.wishcan.www.vocabulazy.storage.databaseObjects.Vocabulary;
import com.wishcan.www.vocabulazy.widget.DialogFragment;

import java.util.ArrayList;


public class SearchFragment extends GASearchFragment implements SearchView.OnItemClickListener,
                                                        DialogFragment.OnDialogFinishListener<Integer>,
                                                        SearchDialogFragment.OnSecDialogCreateListener {

    public static final String TAG = "SEARCH";

    public static String M_TAG;

    private SearchDialogFragment mSearchListDialogFragment;
    private DialogFragment mNewNoteDialogFragment;
    private SearchView mSearchView;
    private SearchModel mSearchModel;
    private ArrayList<Vocabulary> mSuggestedVocabularies;
    private int mSelectVocId;

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        M_TAG = getTag();
        if (mSearchModel == null)
            mSearchModel = new SearchModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mSearchView = new SearchView(getContext());
        mSearchView.setOnItemClickListener(this);
        return mSearchView;
    }

    @Override
    protected String getNameAsGaLabel() {
        return TAG;
    }

    public void refreshSearchResult(ArrayList<Vocabulary> vocabularies) {
        mSuggestedVocabularies = vocabularies;
        mSearchView.refreshSearchResult(
                mSearchModel.createSearchResultMap(vocabularies));
        clearSearchDetail();
        clearDialogFragments();
    }

    public void clearSearchDetail() {
        if (mSearchView != null) {
            mSearchView.closeSearchDetail();
        }
    }

    public void clearDialogFragments() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager == null)
            return;

        if (mNewNoteDialogFragment != null) {
            fragmentManager.beginTransaction().remove(mNewNoteDialogFragment).commit();
        }

        if (mSearchListDialogFragment != null) {
            fragmentManager.beginTransaction().remove(mSearchListDialogFragment).commit();
        }

        mNewNoteDialogFragment = null;
        mSearchListDialogFragment = null;
    }

    @Override
    public void onDialogFinish(Integer obj) {
        mSearchModel.addVocToNote(mSelectVocId, obj);
    }

    @Override
    public void onAddIconClick(int position) {
        mSelectVocId = mSuggestedVocabularies.get(position).getId();
        mSearchListDialogFragment = SearchDialogFragment.newInstance(SearchDialogView.DIALOG_RES_ID_s.LIST);
        mSearchListDialogFragment.setOnDialogFinishListener(this);
        mSearchListDialogFragment.setSecDialogFinishListener(this);
        getFragmentManager()
                .beginTransaction()
                .add(SearchActivity.VIEW_CONTAINER_RES_ID, mSearchListDialogFragment, "SearchDialogFragment_List")
                .addToBackStack("SearchFragment")
                .commit();
        mSearchView.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mSearchView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onListItemClick(int position) {
        Vocabulary voc = mSuggestedVocabularies.get(position);
        mSelectVocId = voc.getId();
        mSearchView.refreshSearchDetail(
                mSearchModel.createSearchResultDetailMap(voc));
        mSearchView.showSearchDetail();
        mSearchView.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mSearchView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void secDialogCreate(DialogFragment fragment) {
        mNewNoteDialogFragment = fragment;
    }
}

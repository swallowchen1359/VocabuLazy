package com.wishcan.www.vocabulazy.mainmenu.exam.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ExpandableListView;

/**
 * Created by allencheng07 on 2016/8/16.
 */
public class ExamIndexNoteView extends ExpandableListView implements ExpandableListView.OnChildClickListener, ExpandableListView.OnGroupClickListener  {

    public interface OnExamIndexNoteClickListener {
        void OnExamIndexNoteClicked(int noteIndex);
    }

    public static final String TAG = "ExamIndexNoteView";

    private int lastExpandedGroupPosition = -1;
    private OnExamIndexNoteClickListener mOnExamIndexNoteClickListener;

    public ExamIndexNoteView(Context context) {
        super(context);
        setOnChildClickListener(this);
        setOnGroupClickListener(this);
    }

    public ExamIndexNoteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnChildClickListener(this);
        setOnGroupClickListener(this);
    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
        if (childPosition == 0) {
            mOnExamIndexNoteClickListener.OnExamIndexNoteClicked(groupPosition);
        }
        return true;
    }

    @Override
    public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long id) {
        if (groupPosition != lastExpandedGroupPosition) {
            expandableListView.collapseGroup(lastExpandedGroupPosition);
            expandableListView.expandGroup(groupPosition, true);
            expandableListView.setSelection(groupPosition);
            lastExpandedGroupPosition = groupPosition;
        } else {
            expandableListView.collapseGroup(groupPosition);
            lastExpandedGroupPosition = -1;
        }
        return true;
    }

    public void addOnExamIndexNoteClickListener(OnExamIndexNoteClickListener listener) {
        mOnExamIndexNoteClickListener = listener;
    }
}

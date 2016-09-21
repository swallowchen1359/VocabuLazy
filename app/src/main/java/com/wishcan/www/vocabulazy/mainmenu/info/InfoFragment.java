package com.wishcan.www.vocabulazy.mainmenu.info;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.mainmenu.activity.MainMenuActivity;

/**
 * Created by allencheng07 on 2016/9/15.
 */
public class InfoFragment extends Fragment {

    public static final String TAG = "InfoFragment";

    private InfoView mInfoView;
    private TextView rateUsTextView;
    private TextView reportTextView;
    private AlertDialog alertDialog;

    public static InfoFragment getInstance() {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInfoView = (InfoView) inflater.inflate(R.layout.view_info, container, false);
        findViews(mInfoView);
        return mInfoView;
    }

    public void findViews(View view) {

        rateUsTextView = (TextView) view.findViewById(R.id.rate_us_text_view);
        rateUsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = getResources().getString(R.string.info_goto_market_message);
                showAlert(message);
            }
        });

        reportTextView = (TextView) view.findViewById(R.id.report_text_view);
        reportTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainMenuActivity) getActivity()).displayReportPage();
            }
        });

    }

    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(message);
        builder.setPositiveButton(R.string.info_alert_dialog_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d(TAG, "ok");
                ((MainMenuActivity) getActivity()).navigateToGooglePlay();
                close();
            }
        });
        builder.setNegativeButton(R.string.info_alert_dialog_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d(TAG, "no");
                close();
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }

    public void close() {
        alertDialog.cancel();
    }
}

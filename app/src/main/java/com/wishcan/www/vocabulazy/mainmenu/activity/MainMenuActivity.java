package com.wishcan.www.vocabulazy.mainmenu.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.mainmenu.fragment.MainMenuFragment;
import com.wishcan.www.vocabulazy.mainmenu.model.MainMenuModel;
import com.wishcan.www.vocabulazy.player.activity.PlayerActivity;
import com.wishcan.www.vocabulazy.service.AudioService;

public class MainMenuActivity extends AppCompatActivity implements MainMenuFragment.OnMainMenuEventListener {

    public static final String TAG = "MainMenuActivity";

    private MainMenuModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (model == null) {
            model = new MainMenuModel(this);
        }

        startAudioService();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        MainMenuFragment mainMenuFragment = (MainMenuFragment) getSupportFragmentManager().findFragmentById(R.id.main_menu_fragment);
        mainMenuFragment.addOnMainMenuEventListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAudioService();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cover, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTextbookSelected(int bookIndex, int lessonIndex) {
        Intent intent = new Intent(MainMenuActivity.this, PlayerActivity.class);
        intent.putExtra(PlayerActivity.ARG_BOOK_INDEX, bookIndex);
        intent.putExtra(PlayerActivity.ARG_LESSON_INDEX, lessonIndex);
        startActivity(intent);
    }

    @Override
    public void onNoteSelected(int noteIndex) {
        Intent intent = new Intent(MainMenuActivity.this, PlayerActivity.class);
        intent.putExtra(PlayerActivity.ARG_BOOK_INDEX, -1);
        intent.putExtra(PlayerActivity.ARG_LESSON_INDEX, noteIndex);
        startActivity(intent);
    }

    @Override
    public void onExamTextbookSelected(int examBookIndex, int examLessonIndex) {

    }

    @Override
    public void onExamNoteSelected(int examNoteIndex) {

    }

    public MainMenuModel getModel() {
        return model;
    }

    private void startAudioService() {
        Intent intent = new Intent(this, AudioService.class);
        intent.setAction(AudioService.START_SERVICE);
        startService(intent);
    }

    private void stopAudioService() {
        Intent intent = new Intent(this, AudioService.class);
        intent.setAction(AudioService.STOP_SERVICE);
        startService(intent);
    }
}
package com.babbel.renuka.languagegame.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;

import com.babbel.renuka.languagegame.R;
import com.babbel.renuka.languagegame.activities.GameEngineActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Renuka on 24/07/16.
 */

public class SetupActivity extends AppCompatActivity {

    @Bind(R.id.no_of_players_seekbar)
    SeekBar mNoOfPlayersSeekbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.start_btn)
    public void startGame() {
        int noOfPlayers = mNoOfPlayersSeekbar.getProgress() + 1; // as seekbar starts from 0, adding 1 to it
        Intent gameIntent = new Intent(this, GameEngineActivity.class);
        gameIntent.putExtra("players", noOfPlayers);
        startActivity(gameIntent);
    }
}

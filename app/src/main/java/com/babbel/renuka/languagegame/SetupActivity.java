package com.babbel.renuka.languagegame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SetupActivity extends AppCompatActivity {

    @Bind(R.id.no_of_players_seekbar)
    SeekBar mNoOfPlayersSeekbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        ButterKnife.bind(this);
    }

}

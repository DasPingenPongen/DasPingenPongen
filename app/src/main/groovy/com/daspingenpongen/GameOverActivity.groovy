package com.daspingenpongen

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import groovy.transform.CompileStatic

@CompileStatic
class GameOverActivity extends AppCompatActivity{

    @Override
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gameover)


        TextView playerName = findViewById(R.id.playerName) as TextView
        playerName.text = getIntent().getStringExtra('PLAYER_NAME')

    }
}

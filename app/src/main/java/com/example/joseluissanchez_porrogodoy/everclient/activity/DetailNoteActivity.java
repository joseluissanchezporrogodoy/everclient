package com.example.joseluissanchez_porrogodoy.everclient.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.joseluissanchez_porrogodoy.everclient.R;

public class DetailNoteActivity extends AppCompatActivity {
    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_CONTENT = "extra_content";
    private String noteTitle;
    private String noteContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_note);
        Intent intent = getIntent();
        noteTitle = intent.getStringExtra(EXTRA_TITLE);
        noteContent = intent.getStringExtra(EXTRA_CONTENT);

    }
}

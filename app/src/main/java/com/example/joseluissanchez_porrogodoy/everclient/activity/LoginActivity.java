package com.example.joseluissanchez_porrogodoy.everclient.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.evernote.client.android.EvernoteSession;
import com.example.joseluissanchez_porrogodoy.everclient.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EvernoteSession.getInstance().authenticate(LoginActivity.this);
    }
}

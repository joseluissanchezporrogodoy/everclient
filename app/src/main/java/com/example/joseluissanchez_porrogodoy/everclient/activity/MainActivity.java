package com.example.joseluissanchez_porrogodoy.everclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.login.EvernoteLoginFragment;
import com.example.joseluissanchez_porrogodoy.everclient.R;

public class MainActivity extends AppCompatActivity implements EvernoteLoginFragment.ResultCallback {

    public static void launch(Activity activity) {
        activity.startActivity(new Intent(activity, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EvernoteSession.getInstance().authenticate(MainActivity.this);
    }

    @Override
    public void onLoginFinished(boolean successful) {
        Toast toast =
                Toast.makeText(getApplicationContext(),
                        "login done", Toast.LENGTH_SHORT);

        toast.show();
    }
    protected void loadData() {

    }
}

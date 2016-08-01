package com.example.joseluissanchez_porrogodoy.everclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.asyncclient.EvernoteCallback;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.client.android.asyncclient.EvernoteSearchHelper;
import com.evernote.client.android.login.EvernoteLoginFragment;
import com.evernote.client.android.type.NoteRef;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.LinkedNotebook;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.NoteSortOrder;
import com.evernote.edam.type.Notebook;
import com.example.joseluissanchez_porrogodoy.everclient.R;
import com.example.joseluissanchez_porrogodoy.everclient.adapter.NoteListAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements EvernoteLoginFragment.ResultCallback {

    private ListView listViewiew;
    private NoteListAdapter adapter;
    public static final int SORT_ALPHABETICAL = 4;
    public static final int SORT_EDIT = 2;
    public static void launch(Activity activity) {
        activity.startActivity(new Intent(activity, MainActivity.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        if (!EvernoteSession.getInstance().isLoggedIn()) {
            // LoginChecker will call finish
            return;
        }
        listViewiew = (ListView)findViewById(R.id.listView);

      findNotes(SORT_ALPHABETICAL);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_alphabetics) {

            return true;
        }
        if (id == R.id.action_edition) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onLoginFinished(boolean successful) {
        Toast toast =
                Toast.makeText(getApplicationContext(),
                        "login done", Toast.LENGTH_SHORT);

        toast.show();
    }

    public void findNotes(int sortMode){
        final NoteList noteListResult = new NoteList();
        if (!EvernoteSession.getInstance().isLoggedIn()) {
            return;
        }

        NoteFilter filter = new NoteFilter();
        // Este parámetro me vale para la creación modificacion
        filter.setOrder(sortMode);
        EvernoteNoteStoreClient noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();
        noteStoreClient.findNotesAsync(filter, 0, 100, new EvernoteCallback<NoteList>() {
            @Override
            public void onSuccess(final NoteList noteList) {
                adapter = new NoteListAdapter(getApplicationContext(),noteList.getNotes());
                listViewiew.setAdapter(adapter);
            }

            @Override
            public void onException(Exception e) {
                ///mostrar mensaje de error
            }
        });

    }







}

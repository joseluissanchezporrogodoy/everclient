package com.example.joseluissanchez_porrogodoy.everclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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


import net.vrallev.android.task.TaskResult;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements EvernoteLoginFragment.ResultCallback {

    public static void launch(Activity activity) {
        activity.startActivity(new Intent(activity, MainActivity.class));
    }
    List<NoteRef> notas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (!EvernoteSession.getInstance().isLoggedIn()) {
            // LoginChecker will call finish
            return;
        }
      findNotes();

    }

    @Override
    public void onLoginFinished(boolean successful) {
        Toast toast =
                Toast.makeText(getApplicationContext(),
                        "login done", Toast.LENGTH_SHORT);

        toast.show();
    }


    protected void loadData() {
        if (!EvernoteSession.getInstance().isLoggedIn()) {
            return;
        }

        EvernoteNoteStoreClient noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();

        noteStoreClient.listNotebooksAsync(new EvernoteCallback<List<Notebook>>() {
            @Override
            public void onSuccess(List<Notebook> result) {
                List<String> namesList = new ArrayList<>(result.size());
                for (Notebook notebook : result) {
                    namesList.add(notebook.getName());
                }
                String notebookNames = TextUtils.join(", ", namesList);
                Toast.makeText(getApplicationContext(), notebookNames + " notebooks have been retrieved", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onException(Exception exception) {
                Log.e("Estemos", "Error retrieving notebooks", exception);
            }
        });
    }

    public void findNotes(){
        if (!EvernoteSession.getInstance().isLoggedIn()) {
            return;
        }

        NoteFilter filter = new NoteFilter();
        // Este parámetro me vale para la creación modificacion
        filter.setOrder(NoteSortOrder.TITLE.getValue());
        final EvernoteNoteStoreClient noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();
        noteStoreClient.findNotesAsync(filter, 0, 100, new EvernoteCallback<NoteList>() {
            @Override
            public void onSuccess(NoteList noteList) {

            int a = 0;
            }

            @Override
            public void onException(Exception e) {
            }
        });
    }

}

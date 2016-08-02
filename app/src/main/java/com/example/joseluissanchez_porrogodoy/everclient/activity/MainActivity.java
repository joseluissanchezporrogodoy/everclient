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
import android.widget.AdapterView;
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
import com.example.joseluissanchez_porrogodoy.everclient.activity.DetailNoteActivity;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity implements EvernoteLoginFragment.ResultCallback {

    private ListView listViewiew;
    private NoteListAdapter adapter;
    public static final int SORT_ALPHABETICAL = 4;
    public static final int SORT_EDIT = 2;
    private static final int REQUEST_CODE_ADD=10;

    public static void launch(Activity activity) {
        activity.startActivity(new Intent(activity, MainActivity.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        //.setAction("Action", null).show();
                Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);
                startActivityForResult(intent,REQUEST_CODE_ADD);
            }
        });
        if (!EvernoteSession.getInstance().isLoggedIn()) {
            // LoginChecker will call finish
            return;
        }
        listViewiew = (ListView)findViewById(R.id.listView);
        findNotes(SORT_ALPHABETICAL);
        listViewiew.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Note note = adapter.getItem(i);
                goToDetail(note);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_alphabetics) {
            findNotes(SORT_ALPHABETICAL);
            return true;
        }
        if (id == R.id.action_edition) {
            findNotes(SORT_EDIT);
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
        if (!EvernoteSession.getInstance().isLoggedIn()) {
            return;
        }

        NoteFilter filter = new NoteFilter();

        if(sortMode == 4)
            filter.setOrder(NoteSortOrder.TITLE.getValue());
        else
            filter.setOrder(NoteSortOrder.UPDATED.getValue());


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



    private void goToDetail(final Note note){
        EvernoteNoteStoreClient noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();
        noteStoreClient.getNoteContentAsync(note.getGuid(), new EvernoteCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Intent intent = new Intent(getApplicationContext(), DetailNoteActivity.class);
                intent.putExtra(DetailNoteActivity.EXTRA_TITLE,note.getTitle());
                intent.putExtra(DetailNoteActivity.EXTRA_CONTENT,getContentString(result));
                startActivity(intent);
            }

            @Override
            public void onException(Exception exception) {
                //mostrar mensaje de error
            }
        });

    }
    public String getContentString(String contentXml){
        try{
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource src = new InputSource();
            src.setCharacterStream(new StringReader(contentXml));
            Document doc = builder.parse(src);
            String content = doc.getElementsByTagName("en-note").item(0).getTextContent();
            return content;
        }catch (Exception e){

        }
       return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Comprobamos que el codigo de resultado es OK
        if (resultCode == RESULT_OK) {
            //En funcion del request code que introdujimos la respuesta sera de un tipo u otro
            switch (requestCode) {
                case REQUEST_CODE_ADD: {
                    ////Recargar Lista
                    findNotes(SORT_ALPHABETICAL);
                    break;
                }
            }
        }
    }

}

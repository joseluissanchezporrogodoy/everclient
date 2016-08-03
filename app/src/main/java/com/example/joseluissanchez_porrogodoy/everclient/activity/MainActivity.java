package com.example.joseluissanchez_porrogodoy.everclient.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.login.EvernoteLoginFragment;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.Note;
import com.example.joseluissanchez_porrogodoy.everclient.R;
import com.example.joseluissanchez_porrogodoy.everclient.adapter.NoteListAdapter;
import com.example.joseluissanchez_porrogodoy.everclient.presenter.MainPresenter;
import com.example.joseluissanchez_porrogodoy.everclient.presenter.MainPresenterImpl;
import com.example.joseluissanchez_porrogodoy.everclient.view.MainView;

public class MainActivity extends AppCompatActivity implements MainView, EvernoteLoginFragment.ResultCallback {

    private ListView listViewiew;
    private NoteListAdapter adapter;
    public static final int SORT_ALPHABETICAL = 4;
    public static final int SORT_EDIT = 2;
    private static final int REQUEST_CODE_ADD=10;
    private MainPresenter presenter;


    public static void launch(Activity activity) {
        activity.startActivity(new Intent(activity, MainActivity.class));
    }

    @Override protected void onResume() {
        super.onResume();
       presenter.onResume();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);
//                startActivityForResult(intent,REQUEST_CODE_ADD);
                showDialog();
            }
        });
        if (!EvernoteSession.getInstance().isLoggedIn()) {
            // LoginChecker will call finish
            return;
        }
        presenter = new MainPresenterImpl(this);
        listViewiew = (ListView)findViewById(R.id.listView);
        listViewiew.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Note note = adapter.getItem(i);
                presenter.onItemClicked(note);
            }
        });

    }
    private void showDialog(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog);
        Button keyButton =(Button) dialog.findViewById(R.id.btKey);
        keyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);
                startActivityForResult(intent,REQUEST_CODE_ADD);
            }
        });
        Button handButton= (Button)dialog.findViewById(R.id.btHand);
        handButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Llamar a la activity de dibujo
                Intent intent = new Intent(getApplicationContext(), HandWritingActivity.class);
                startActivity(intent);
            }
        });
        dialog.show();
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
            presenter.onOptionSortSelected(SORT_ALPHABETICAL);
            return true;
        }
        if (id == R.id.action_edition) {
            presenter.onOptionSortSelected(SORT_EDIT);
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

    @Override
    public void setItems(NoteList noteList) {
        adapter = new NoteListAdapter(getApplicationContext(),noteList.getNotes());
        listViewiew.setAdapter(adapter);
    }

    @Override
    public void goToDetail(String title, String content) {
        Intent intent = new Intent(getApplicationContext(), DetailNoteActivity.class);
        intent.putExtra(DetailNoteActivity.EXTRA_TITLE,title);
        intent.putExtra(DetailNoteActivity.EXTRA_CONTENT,content);
        startActivity(intent);
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
                    presenter.onAddNoteResult();
                    break;
                }
            }
        }
    }

}

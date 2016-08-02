package com.example.joseluissanchez_porrogodoy.everclient.activity;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.EvernoteUtil;
import com.evernote.client.android.asyncclient.EvernoteCallback;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.client.conn.mobile.FileData;
import com.evernote.clients.NoteStoreClient;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;
import com.evernote.edam.type.Resource;
import com.evernote.edam.type.ResourceAttributes;
import com.example.joseluissanchez_porrogodoy.everclient.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class CreateNoteActivity extends AppCompatActivity {
    private EditText etTitle;
    private EditText etContent;
    private Button btAccept;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        etTitle = (EditText)findViewById(R.id.etTitle);
        etContent = (EditText)findViewById(R.id.etContent);
        btAccept = (Button)findViewById(R.id.btAccept);
        btAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
            }
        });
       // recognitionHandWriting();
    }

    private void saveNote(){
        Note note= new Note();
        note.setTitle(etTitle.getText().toString());
        String nBody = EvernoteUtil.NOTE_PREFIX+ etContent.getText().toString() + EvernoteUtil.NOTE_SUFFIX;
        note.setContent(nBody);
        EvernoteNoteStoreClient noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();
        noteStoreClient.createNoteAsync(note, new EvernoteCallback<Note>() {
            @Override
            public void onSuccess(Note result) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onException(Exception exception) {
                //mostrar mensaje de error

            }
        });
    }


}

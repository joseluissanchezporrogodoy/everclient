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

public class CreateNoteActivity extends AppCompatActivity implements CreateNoteView {
    private EditText etTitle;
    private EditText etContent;
    private Button btAccept;
    private CreateNotePresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        presenter = new CreateNotePresenterImpl(this);
        etTitle = (EditText)findViewById(R.id.etTitle);
        etContent = (EditText)findViewById(R.id.etContent);
        btAccept = (Button)findViewById(R.id.btAccept);
        btAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onAddButtonCliked(etTitle.getText().toString(),etContent.getText().toString());
            }
        });

    }





    @Override
    public void addNote() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}

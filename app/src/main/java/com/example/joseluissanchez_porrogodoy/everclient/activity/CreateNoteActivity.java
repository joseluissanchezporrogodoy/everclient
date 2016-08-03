package com.example.joseluissanchez_porrogodoy.everclient.activity;

import android.content.Intent;
import android.graphics.Bitmap;
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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
        recognitionHandWriting();
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
    private void saveImageNote( final ImageData mImageData){

        InputStream in = null;
        final Note note = new Note();
        note.setTitle(etTitle.getText().toString());
        try {
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
//            byte[] bitmapdata = bos.toByteArray();
//            in = new ByteArrayInputStream(bitmapdata);
            // Hash the data in the image file. The hash is used to reference the file in the ENML note content.
            in = new BufferedInputStream(new FileInputStream(mImageData.getPath()));
            FileData data = new FileData(EvernoteUtil.hash(in), new File(mImageData.getPath()));

            ResourceAttributes attributes = new ResourceAttributes();
            attributes.setFileName(mImageData.getFileName());

            // Create a new Resource
            Resource resource = new Resource();
            resource.setData(data);
            resource.setMime(mImageData.getMimeType());
            resource.setAttributes(attributes);

            note.addToResources(resource);

            // Set the note's ENML content

        } catch (Exception e){

        }
        finally
        {
            if (in != null) {
                try{
                    in.close();
                }catch (Exception e){

                }

            }
        }
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
    private void recognitionHandWriting(){

        EvernoteNoteStoreClient noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();

        noteStoreClient.getResourceRecognitionAsync("3a9f1886-e7ae-44b7-bc7a-559d64fa58f9", new EvernoteCallback<byte[]>() {
            @Override
            public void onSuccess(byte[] result) {
                String s = new String(result);
                int a = 0;
            }

            @Override
            public void onException(Exception exception) {

            }
        });
    }
    public static class ImageData implements Parcelable {

        private final String mPath;
        private final String mFileName;
        private final String mMimeType;

        public ImageData(String path, String fileName, String mimeType) {
            mPath = path;
            mFileName = fileName;
            mMimeType = mimeType;
        }

        public String getPath() {
            return mPath;
        }

        public String getFileName() {
            return mFileName;
        }

        public String getMimeType() {
            return mMimeType;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mPath);
            dest.writeString(mFileName);
            dest.writeString(mMimeType);
        }

        public static final Creator<ImageData> CREATOR = new Creator<ImageData>() {
            @Override
            public ImageData createFromParcel(final Parcel source) {
                return new ImageData(source.readString(), source.readString(), source.readString());
            }

            @Override
            public ImageData[] newArray(final int size) {
                return new ImageData[size];
            }
        };
    }

}

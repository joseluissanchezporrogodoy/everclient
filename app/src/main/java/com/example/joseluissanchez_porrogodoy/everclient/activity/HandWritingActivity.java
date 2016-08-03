package com.example.joseluissanchez_porrogodoy.everclient.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.EvernoteUtil;
import com.evernote.client.android.asyncclient.EvernoteCallback;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.client.conn.mobile.FileData;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Resource;
import com.evernote.edam.type.ResourceAttributes;
import com.example.joseluissanchez_porrogodoy.everclient.R;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class HandWritingActivity extends AppCompatActivity implements View.OnClickListener {

    //Vista custom para dibujar
    private HandWritingView drawView;
    //Botones
    private ImageButton currPaint, drawBtn, eraseBtn, newBtn, saveBtn, colorBtn;
    //Tamaños
    private float smallBrush, mediumBrush, largeBrush;
    private EditText etTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hand_writing);
        drawView = (HandWritingView) findViewById(R.id.view);
        //Tamaño Pincel
        drawView.setBrushSize(1);
        //Boton nuevo
        newBtn = (ImageButton) findViewById(R.id.new_btn);
        newBtn.setOnClickListener(this);
        //Boton guardar
        saveBtn = (ImageButton) findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(this);
        etTitle = (EditText)findViewById(R.id.editText);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.save_btn) {
            //Acción de guardar
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            saveDialog.setTitle("Guardar imagen");
            saveDialog.setMessage("¿Guardar esta imagen en la galería?");
            saveDialog.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    drawView.setDrawingCacheEnabled(true);
                    String path = getCacheDir().toString();
                    OutputStream fOut = null;
                    String filename = UUID.randomUUID().toString() + ".png";
                    File file = new File(path, filename); // the File to save to
                    try{
                        fOut = new FileOutputStream(file);
                    }catch (Exception e){

                    }
                    Bitmap pictureBitmap = drawView.getDrawingCache();
                    pictureBitmap.compress(Bitmap.CompressFormat.PNG,0, fOut);
                    try {
                        fOut.flush();
                    }catch (Exception e){

                    }
                    try {
                        fOut.close();
                    }catch (Exception e){

                    }
                    ImageData imageData= new ImageData(path+"/"+filename,filename,"image/png");
                    saveImageNote(imageData);
                    drawView.destroyDrawingCache();
                }
            });
            saveDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            saveDialog.show();
        } else if (view.getId() == R.id.new_btn) {
            drawView.startNew();
        }

    }

    private void saveImageNote(ImageData mImageData) {

        InputStream in = null;
        final Note note = new Note();
        note.setTitle(etTitle.getText().toString());
        try {
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
            String content = EvernoteUtil.NOTE_PREFIX
                    + ""
                    + EvernoteUtil.createEnMediaTag(resource)
                    + EvernoteUtil.NOTE_SUFFIX;

            note.setContent(content);
        } catch (Exception e) {

        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {

                }

            }
        }
        EvernoteNoteStoreClient noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();
        noteStoreClient.createNoteAsync(note, new EvernoteCallback<Note>() {
            @Override
            public void onSuccess(Note result) {
                ///Vuelve a main
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


    ///Si se llama a este metodo con el guid de la imagen guardada en la nota obtenemos el resultado del OCR de evernote, este no es un resultado
    ///instantáneo pero como aproximación incluyo el método. Lo he probado con alguna imagen y funciona reguleras. Una vez tenemos la respuesta
    // hay que elegir el que mejor coheficiente tenga. Este proceso se de recoger la info del ocr se puede hacer cuando se va amostrar la nota
    ///https://dev.evernote.com/doc/articles/image_recognition.php

    private void recognitionHandWriting(String guid) {

        EvernoteNoteStoreClient noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();

        noteStoreClient.getResourceRecognitionAsync(guid, new EvernoteCallback<byte[]>() {
            @Override
            public void onSuccess(byte[] result) {
                String s = new String(result);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
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

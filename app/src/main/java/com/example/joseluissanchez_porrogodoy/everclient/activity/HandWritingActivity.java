package com.example.joseluissanchez_porrogodoy.everclient.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;

public class HandWritingActivity extends AppCompatActivity implements View.OnClickListener {

    //Vista custom para dibujar
    private HandWritingView drawView;
    //Botones
    private ImageButton currPaint, drawBtn, eraseBtn, newBtn, saveBtn, colorBtn;
    //Tamaños
    private float smallBrush, mediumBrush, largeBrush;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hand_writing);
        drawView = (HandWritingView)findViewById(R.id.view);
        //Tamaño Pincel
        drawView.setBrushSize(1);
        //Boton nuevo
        newBtn = (ImageButton)findViewById(R.id.new_btn);
        newBtn.setOnClickListener(this);
        //Boton guardar
        saveBtn = (ImageButton)findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
         if(view.getId()==R.id.save_btn){
            //Acción de guardar
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            saveDialog.setTitle("Guardar imagen");
            saveDialog.setMessage("¿Guardar esta imagen en la galería?");
            saveDialog.setPositiveButton("Guardar", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){

                    drawView.setDrawingCacheEnabled(true);

//                    String imgSaved = MediaStore.Images.Media.insertImage(
//                            getContentResolver(), drawView.getDrawingCache(),
//                            UUID.randomUUID().toString()+".png", "drawing");

//                    if(imgSaved!=null){
//                        Toast savedToast = Toast.makeText(getApplicationContext(),
//                                "Imagen guardada.", Toast.LENGTH_SHORT);
//                        savedToast.show();
//                    }
//                    else{
//                        Toast unsavedToast = Toast.makeText(getApplicationContext(),
//                                "No se ha podido guardar la imagen", Toast.LENGTH_SHORT);
//                        unsavedToast.show();
//                    }
                    saveImageNote();
                    drawView.destroyDrawingCache();
                }
            });
            saveDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            saveDialog.show();
        }else if(view.getId()==R.id.new_btn){
             drawView.startNew();
         }

    }
    private void saveImageNote(){

        InputStream in = null;
        final Note note = new Note();
        note.setTitle("Título");
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            drawView.getDrawingCache().compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();
            in = new ByteArrayInputStream(bitmapdata);
            // Hash the data in the image file. The hash is used to reference the file in the ENML note content.
           // in = new BufferedInputStream(new FileInputStream(mImageData.getPath()));
            FileData data = new FileData(EvernoteUtil.hash(in), new File("Imagen"));

            ResourceAttributes attributes = new ResourceAttributes();


            // Create a new Resource
            Resource resource = new Resource();
            resource.setData(data);
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

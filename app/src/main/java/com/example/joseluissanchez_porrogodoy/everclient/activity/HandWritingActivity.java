package com.example.joseluissanchez_porrogodoy.everclient.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.joseluissanchez_porrogodoy.everclient.R;

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

                    String imgSaved = MediaStore.Images.Media.insertImage(
                            getContentResolver(), drawView.getDrawingCache(),
                            UUID.randomUUID().toString()+".png", "drawing");

                    if(imgSaved!=null){
                        Toast savedToast = Toast.makeText(getApplicationContext(),
                                "Imagen guardada.", Toast.LENGTH_SHORT);
                        savedToast.show();
                    }
                    else{
                        Toast unsavedToast = Toast.makeText(getApplicationContext(),
                                "No se ha podido guardar la imagen", Toast.LENGTH_SHORT);
                        unsavedToast.show();
                    }
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
}

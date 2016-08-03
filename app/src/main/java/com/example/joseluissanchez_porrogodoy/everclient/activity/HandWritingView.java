package com.example.joseluissanchez_porrogodoy.everclient.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.joseluissanchez_porrogodoy.everclient.R;

/**
 * Created by jlsanchez on 7/12/15.
 */
public class HandWritingView extends View {

    // path
    private Path drawPath;
    //
    private Paint drawPaint, canvasPaint;
    //
    private int paintColor = 0xFF000000;
    //canvas
    private Canvas drawCanvas;
    //
    private Bitmap canvasBitmap;
    //
    private float brushSize, lastBrushSize;
    //
    private boolean erase=false;

    public HandWritingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupDrawing();
    }

    //Prepara inicialmente el pincel y la vita
    private void setupDrawing(){

        brushSize = 1;
        lastBrushSize = brushSize;
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(brushSize);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }
    //Toma los puntos pulsados en la pantalla para dibujar
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawPath.lineTo(touchX, touchY);
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                break;
            default:
                return false;
        }

        invalidate();
        return true;

    }

    //Actualiza color
    public void setColor(String newColor){
        invalidate();

        if(newColor.startsWith("#")){
            paintColor = Color.parseColor(newColor);
            drawPaint.setColor(paintColor);
            drawPaint.setShader(null);
        }

    }
    //Cambia el tamaño del pincel
    public void setBrushSize(float newSize){
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                newSize, getResources().getDisplayMetrics());
        brushSize=pixelAmount;
        drawPaint.setStrokeWidth(brushSize);
    }



    //Inicializa el cuadro para pintar
    public void startNew(){
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }
//    private void muestraDialogo(){
//        Dialog dialog = new Dialog(this);
//        dialog.setTitle("Diálogo personalizado");
//        dialog.setContentView(R.layout.dialog);
//        Button keyButton =(Button) dialog.findViewById(R.id.btKey);
//        keyButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);
//                startActivityForResult(intent,REQUEST_CODE_ADD);
//            }
//        });
//        Button handButton= (Button)dialog.findViewById(R.id.btHand);
//        handButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //Llamar a la activity de dibujo
//                Intent intent = new Intent(getApplicationContext(), HandWritingActivity.class);
//                startActivity(intent);
//            }
//        });
//        dialog.show();
//    }
}

package com.example.yesaude;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ImagemZoom extends AppCompatActivity {

    private ImageView imageView;
    private ScaleGestureDetector scaleGestureDetector;
    private float scaleFactor = 1.0f;

    private float previousX;
    private float previousY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagem_zoom);

        // Deixa o menu no topo em verde escuro.
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.verdebom));


        imageView = findViewById(R.id.imgZoom);

        try{
            Uri uri = Info.getUri();
            InputStream inputStream = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
            imageView.setImageBitmap(bitmap);
        }
        catch (Exception e){
            ;
        }

        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);

        float currentX = event.getX();
        float currentY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                previousX = currentX;
                previousY = currentY;
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaX = currentX - previousX;
                float deltaY = currentY - previousY;

                imageView.setTranslationX(imageView.getTranslationX() + deltaX);
                imageView.setTranslationY(imageView.getTranslationY() + deltaY);

                previousX = currentX;
                previousY = currentY;
                break;
        }

        return true;
    }


    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 10.0f));
            imageView.setScaleX(scaleFactor);
            imageView.setScaleY(scaleFactor);
            return true;
        }
    }

}

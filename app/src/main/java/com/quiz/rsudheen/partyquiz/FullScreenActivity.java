package com.quiz.rsudheen.partyquiz;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class FullScreenActivity extends Activity {

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        byte[] bitmap = extras.getByteArray("imageBitmap");
        mImageView = (ImageView)findViewById(R.id.questionImage);
        Bitmap bm = BitmapFactory.decodeByteArray(bitmap,0,bitmap.length);
        mImageView.setImageBitmap(bm);

    }
}

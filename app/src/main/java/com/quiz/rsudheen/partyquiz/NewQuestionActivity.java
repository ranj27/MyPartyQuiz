package com.quiz.rsudheen.partyquiz;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.quiz.rsudheen.partyquiz.database.DatabaseHelper;
import com.quiz.rsudheen.partyquiz.database.Questions;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class NewQuestionActivity extends Activity implements View.OnClickListener {

    private static final int SELECT_PHOTO = 1;
    private static final int CAPTURE_PHOTO = 2;
    private int quizId;
    private int roundId;
    private String categoryName;
    private String score;
    private DatabaseHelper db;
    private int questionID;
    private Questions mQuestion = null;
    private TextView questionTxt;
    private TextView answerTxt;
    private TextView choice2Txt;
    private TextView choice3Txt;
    private TextView choice4Txt;
    private int categoryId;
    private ImageView imageView;
    private ImageButton imageButton;
    private Bitmap bp;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 100;
    private ImageButton cameraButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        questionID = extras.getInt("questionID");
        quizId = extras.getInt("quizID");
        roundId = extras.getInt("roundID");
        categoryName = extras.getString("categoryName");
        categoryId = extras.getInt("categoryID");
//        score = extras.getString("score");

        db = new DatabaseHelper(getApplicationContext());

        if (questionID != 0) {
            mQuestion = db.getQuestionById(questionID);
        }

        setContentView(R.layout.activity_new_question);
        Button saveBtn = findViewById(R.id.saveBtn);
        Button cancelBtn = findViewById(R.id.cancelBtn);
        questionTxt = (TextView) findViewById(R.id.questionEditText);
        answerTxt = (TextView) findViewById(R.id.answerTxt);
        choice2Txt = (TextView) findViewById(R.id.choice2Txt);
        choice3Txt = (TextView) findViewById(R.id.choice3Txt);
        choice4Txt = (TextView) findViewById(R.id.choice4Txt);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        cameraButton = (ImageButton) findViewById(R.id.cameraButton);
        imageButton.setOnClickListener(this);
        cameraButton.setOnClickListener(this);
        fillQuestionfromDB();
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] photo = null;
                long rowData = 0;
                boolean result = ValidateQuestionInput();
                if (result) {
                    if (bp != null) {
                        photo = profileImage(bp);
                    }
                    if (questionID > 0) {
                        rowData = UpdateQuestiontoDB(questionID, questionTxt.getText().toString(), answerTxt.getText().toString(), choice2Txt.getText().toString(),
                                choice3Txt.getText().toString(), choice4Txt.getText().toString(), quizId, roundId, categoryName, categoryId, score, photo);
                        rowData = questionID;
                    }
                    if (questionID < 0) {
                        rowData = InsertQuestiontoDB(questionTxt.getText().toString(), answerTxt.getText().toString(), choice2Txt.getText().toString(),
                                choice3Txt.getText().toString(), choice4Txt.getText().toString(), quizId, roundId, categoryName, categoryId, score, photo);
                    }
                    Log.d("Ranj", "Row data result " + rowData);
                    if (rowData > 0) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("questionID", rowData);
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    }
                }
            }

            private long UpdateQuestiontoDB(int questionId, String questionTxt, String answerTxt, String choice2Txt, String choice3Txt, String choice4Txt, int quizId, int roundId, String categoryName, int categoryId, String score, byte[] photo) {
                long rowData = db.UpdateQuestion(questionId, questionTxt, answerTxt, choice2Txt, choice3Txt, choice4Txt, quizId, roundId, categoryName, categoryId, score, photo);
                return rowData;
            }

            private boolean ValidateQuestionInput() {
                boolean result = true;
                if (questionTxt.getText().toString().isEmpty() || answerTxt.getText().toString().isEmpty()) {
                    result = false;
                    final AlertDialog.Builder dialog1 = new AlertDialog.Builder(NewQuestionActivity.this);
                    dialog1.setMessage("Enter valid input");
                    dialog1.setCancelable(true);
                    dialog1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    dialog1.show();
                }
                return result;
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Check read external storage permission
        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE);
        checkAndGetPermission();

    }

    private void checkAndGetPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    private void fillQuestionfromDB() {
        if (mQuestion != null && mQuestion.getQuestion() != null && mQuestion.getAnswer() != null) {
            questionTxt.setText(mQuestion.getQuestion().toString());
            answerTxt.setText(mQuestion.getAnswer().toString());
            if (mQuestion.getPhoto() != null) {
                bp = convertToBitmap(mQuestion.getPhoto());
                imageView.setImageBitmap(bp);
            }

        }
    }

    private long InsertQuestiontoDB(String questionTxt, String answerTxt, String choice2Txt, String choice3Txt, String choice4Txt, int quizId, int roundId, String categoryName, int categoryId, String score, byte[] photo) {
        long rowData = db.insertNewQuestion(questionTxt, answerTxt, choice2Txt, choice3Txt, choice4Txt, quizId, roundId, categoryName, categoryId, score, photo);
        Log.d("Ranj", "Row data " + rowData);
        return rowData;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButton:
                selectImageFromGallery();
                break;
            case R.id.cameraButton:
                captureImage();
        }
    }

    private void captureImage() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAPTURE_PHOTO);
    }

    private void selectImageFromGallery() {
        // To open up a gallery browser
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        photoPickerIntent.setType("image/*.jpg");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            switch (requestCode) {
                case SELECT_PHOTO:
                    if (data != null) {
                        Uri currImageURI = data.getData();
                        Uri tempURI = Uri.fromFile(new File(currImageURI.getPath()));
                        String s = tempURI.getPath().toString();
                        File file = new File(s);
                        if (!currImageURI.equals(null)) {
                            String defaultFile = file.getAbsolutePath();
                            bp = decodeUri(currImageURI, 400);
                            imageView.setImageBitmap(bp);
                        }
                    }
                    break;
                case CAPTURE_PHOTO:
                    if (data != null){
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        imageView.setImageBitmap(photo);
                    }
                    break;
            }
    }

    public String getRealPathFromURI(Uri contentUri) {

        String[] proj = {MediaStore.Images.Media.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    //COnvert and resize our image to 400dp for faster uploading our images to DB
    protected Bitmap decodeUri(Uri selectedImage, int REQUIRED_SIZE) {

        try {

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

            // The new size we want to scale to
            // final int REQUIRED_SIZE =  size;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //Convert bitmap to bytes
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    private byte[] profileImage(Bitmap b) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 0, bos);
        return bos.toByteArray();

    }

    private Bitmap convertToBitmap(byte[] b) {

        return BitmapFactory.decodeByteArray(b, 0, b.length);

    }


}

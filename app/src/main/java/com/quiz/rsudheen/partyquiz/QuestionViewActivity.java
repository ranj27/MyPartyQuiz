package com.quiz.rsudheen.partyquiz;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quiz.rsudheen.partyquiz.database.DatabaseHelper;
import com.quiz.rsudheen.partyquiz.database.Questions;

public class QuestionViewActivity extends Activity {

    private int mQuestionID;
    private DatabaseHelper db;
    private Questions mQuestion;
    private TextView mQuestionTxt;
    private String mQuestionCategory;
    private int mCategoryColor;
    private TextView mCategoryTxt;
    private Button mShowAnswerBtn;
    private TextView mAnswerTxt;
    private Bitmap bp;
    private ViewGroup questionLayout;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_view);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        mQuestionID = extras.getInt("questionID");
        mQuestionCategory = extras.getString("categoryName");
        mCategoryColor = extras.getInt("categoryColor");

        db = new DatabaseHelper(getApplicationContext());
        mQuestion = db.getQuestionById(mQuestionID);


        mQuestionTxt = (TextView) findViewById(R.id.questionTxt);
        mCategoryTxt = (TextView)findViewById(R.id.categoryTxt);
        mShowAnswerBtn = (Button)findViewById(R.id.answerBtn);
        mAnswerTxt = (TextView)findViewById(R.id.answerTxt);
        mCategoryTxt.setText(mQuestionCategory);
//        Animation animate = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
//        animate.setStartOffset(150);
//        animate.setDuration(800);
//        mCategoryTxt.startAnimation(animate);
        mCategoryTxt.setBackgroundColor(mCategoryColor);
        mCategoryTxt.setTextColor(Color.BLACK);

        questionLayout = (ViewGroup)findViewById(R.id.questionViewLayout);
//        Transition transition = new Slide(Gravity.LEFT);
//        transition.setDuration(300);
//        transition.setStartDelay(200);
//        TransitionManager.beginDelayedTransition(questionLayout,transition);

        if ((mQuestion != null) && (mQuestion.getPhoto() !=null)){

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200,200);
            layoutParams.gravity=Gravity.CENTER_HORIZONTAL;
            ImageView imageView = new ImageView(getApplicationContext());
            bp = convertToBitmap(mQuestion.getPhoto());
            imageView.setImageBitmap(bp);
            imageView.setId(2);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(layoutParams);
            Animation animate1 = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
            animate1.setStartOffset(800);
            animate1.setDuration(800);
            imageView.startAnimation(animate1);
            questionLayout.addView(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(),FullScreenActivity.class);
                    intent.putExtra("imageBitmap",mQuestion.getPhoto());
                    startActivity(intent);
                }
            });

        }
        if (mQuestion !=null) {
            mQuestionTxt.setText(mQuestion.getQuestion());
            mQuestionTxt.setBackgroundColor(mCategoryColor);
            mQuestionTxt.setTextColor(Color.BLACK);
            Animation animate2 = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_in_left);
            animate2.setStartOffset(500);
            animate2.setDuration(800);
            mQuestionTxt.startAnimation(animate2);
        }

        mShowAnswerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mQuestion !=null){
                    String correctAns = mQuestion.getAnswer();
                    mAnswerTxt.setText(correctAns);

                }
            }
        });

    }

    private Bitmap convertToBitmap(byte[] b) {

        return BitmapFactory.decodeByteArray(b, 0, b.length);

    }
}

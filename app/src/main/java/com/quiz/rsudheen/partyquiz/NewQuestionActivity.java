package com.quiz.rsudheen.partyquiz;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.quiz.rsudheen.partyquiz.database.DatabaseHelper;
import com.quiz.rsudheen.partyquiz.database.Questions;

public class NewQuestionActivity extends Activity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        questionID = extras.getInt("questionID");
        quizId = extras.getInt("quizID");
        roundId = extras.getInt("roundID");
        categoryName = extras.getString("categoryName");
        score = extras.getString("score");

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
        fillQuestionfromDB();
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long rowData = 0;
                boolean result = ValidateQuestionInput();
                if (result) {
                    if (questionID > 0) {
                        rowData = UpdateQuestiontoDB(questionID,questionTxt.getText().toString(), answerTxt.getText().toString(), choice2Txt.getText().toString(),
                                choice3Txt.getText().toString(), choice4Txt.getText().toString(), quizId, roundId, categoryName, score);
                    }
                    if (questionID < 0){
                        rowData = InsertQuestiontoDB(questionTxt.getText().toString(), answerTxt.getText().toString(), choice2Txt.getText().toString(),
                                choice3Txt.getText().toString(), choice4Txt.getText().toString(), quizId, roundId, categoryName, score);
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

            private long UpdateQuestiontoDB(int questionId,String questionTxt, String answerTxt, String choice2Txt, String choice3Txt, String choice4Txt, int quizId, int roundId, String categoryName, String score) {
                long rowData = db.UpdateQuestion(questionId,questionTxt, answerTxt, choice2Txt, choice3Txt, choice4Txt, quizId, roundId, categoryName, score);
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


    }

    private void fillQuestionfromDB() {
        if (mQuestion != null && mQuestion.getQuestion() != null && mQuestion.getAnswer() != null) {
            questionTxt.setText(mQuestion.getQuestionId() + " " + mQuestion.getQuestion().toString());
            answerTxt.setText(mQuestion.getAnswer().toString());

        }
    }

    private long InsertQuestiontoDB(String questionTxt, String answerTxt, String choice2Txt, String choice3Txt, String choice4Txt, int quizId, int roundId, String categoryName, String score) {
        long rowData = db.insertNewQuestion(questionTxt, answerTxt, choice2Txt, choice3Txt, choice4Txt, quizId, roundId, categoryName, score);
        Log.d("Ranj", "Row data " + rowData);
        return rowData;
    }
}

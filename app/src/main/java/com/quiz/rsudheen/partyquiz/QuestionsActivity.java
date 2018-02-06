package com.quiz.rsudheen.partyquiz;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.quiz.rsudheen.partyquiz.database.Categories;
import com.quiz.rsudheen.partyquiz.database.DatabaseHelper;
import com.quiz.rsudheen.partyquiz.database.Questions;
import com.quiz.rsudheen.partyquiz.database.Scores;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class QuestionsActivity extends Activity implements View.OnClickListener {

    private int ROWS_COUNT = 6;
    private int COLS_COUNT = 6;
    private Button mCatBtn1;
    private Button mCatBtn2;
    private DatabaseHelper db;
    private int roundId;
    private int quizId;
    private Button mCatBtn3;
    private Button mCatBtn4;
    private Button mCatBtn5;
    private Button mCatBtn6;
    private TableLayout mTableLayout;
    private View mDialogView;
    private String newCategoryName;
    List<Questions> questions = new ArrayList<>();
    static final ArrayList<Categories> defaultCategories = new ArrayList<Categories>() {
        {
            add(new Categories("Movies"));
            add(new Categories("History"));
            add(new Categories("Fun Stuff"));
            add(new Categories("Science"));
            add(new Categories("Math"));
            add(new Categories("Epics"));
        }

    };
    private Button[][] buttons = new Button[ROWS_COUNT][COLS_COUNT];
    private Button mButton;
    private List<Categories> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        roundId = extras.getInt("roundID");
        quizId = extras.getInt("quizID");


        setContentView(R.layout.activity_questions);
        mTableLayout = (TableLayout) findViewById(R.id.tableForQuestions);
        TableRow headerRow = (TableRow) mTableLayout.getChildAt(0).findViewById(R.id.row1);

        db = new DatabaseHelper(getApplicationContext());
        questions = db.getQuestionsById(quizId, roundId);
        categories = db.getCategoriesFoQuizId(quizId,roundId);
        /*if (questions.isEmpty()) {
            db.createNewQuestionsforQuiz(quizId, roundId);
            questions = db.getQuestionsById(quizId, roundId);
        }
        List<Categories> categories = getCategories();*/

        if (categories.isEmpty()){
            categories = defaultCategories;
        }

        if (!categories.isEmpty()) {
            for (int i = 0; i < categories.size(); i++) {
                final Button button = (Button) headerRow.getChildAt(i);
                String category = categories.get(i).getCategoryName().toLowerCase();
                category = category.substring(0, 1).toUpperCase() + category.substring(1);
                button.setText(category);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button button = (Button) v;
                        changeCategoryName(button, button.getText().toString());
                    }
                });

            }

        }

        populateQuestions();
        fillQuestions();
        // getScores();
    }

    private void fillQuestions() {
        Categories category = null;
        Iterator iter = null;
        Questions q;

        int rows = buttons.length;
        int col = 0;
        if (rows > 1) {
            col = buttons[0].length;
        }
        for (int i = 0; i < col; i++) {//
            View view = mTableLayout.getChildAt(0);
            if (view instanceof View) {
                TableRow tableRow = (TableRow) view;
                View child = tableRow.getChildAt(i);
                if (child instanceof Button) {
                    Button button = (Button) child;
                    category = db.getCategoryByName(button.getText().toString());
                }
            }
            questions = db.getQuestionsByCategory(category.getCategoryName(), quizId, roundId);
            if (!questions.isEmpty()) {
                iter = questions.iterator();
            }

            for (int j = 0; j < rows; j++) {
                if (iter != null && iter.hasNext()) {
                    q = (Questions) iter.next();
                    if (q != null) {
                        if (q.getCategoryName() != null && q.getCategoryName().equals(category.getCategoryName()) ) {
                            Log.d("Ranj", "Question ID " + q.getQuestionId());
                            buttons[j][i].setId(q.getQuestionId());
                            buttons[j][i].setText(buttons[j][i].getText().toString() + " - " + q.getQuestionId());
                            buttons[j][i].setBackgroundColor(Color.MAGENTA);
                        }
                    }
                }
            }
        }
    }

    private List<Scores> getScores() {
        Scores scores = null;
        List<Scores> scoresList = new ArrayList<Scores>();
        scoresList = db.getScoresbyQuizId(quizId, roundId);
        if (scoresList.isEmpty()) {
            scoresList = db.getAllScore();
        }
        if (!scoresList.isEmpty()) {
            for (Scores c : scoresList) {
                Log.d("Ranj", "Complexity name " + c.getComplexity());
            }
        }
        /*for(Questions q: questionsList){
            category = db.getCategoryById(q.getCategoryId());
            Log.d("Ranj"," Category name " + category.getCategoryName());
        }*/
        return scoresList;

    }

    private void changeCategoryName(final Button button, final String currentCategoryName) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Change Category Name");
        View view = getLayoutInflater().inflate(R.layout.change_category_dialog, null);
        alertDialogBuilder.setView(view);
        final Dialog dialog = alertDialogBuilder.create();
        final TextView categoryTxt = (TextView) view.findViewById(R.id.categoryNameTxt);
        Button okBtn = (Button) view.findViewById(R.id.okBtn);
        Button cancelBtn = (Button) view.findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newCategoryName = categoryTxt.getText().toString();
                if (newCategoryName.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter valid name ", Toast.LENGTH_LONG).show();
                } else {
                    button.setText(newCategoryName);
                    dialog.dismiss();
                }
            }


        });
        categoryTxt.setText(currentCategoryName);
        categoryTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });
        categoryTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryTxt.setText(currentCategoryName);
            }
        });
        dialog.show();

    }

    private List<Categories> getCategories() {
        Categories category = null;
        List<Categories> categoriesList = new ArrayList<Categories>();
//        categoriesList = db.getCategoriesByQuizId(quizId, roundId);
        categoriesList = db.getCategoriesFoQuizId(quizId, roundId);
        if (categoriesList.isEmpty()) {
            categoriesList = db.getAllCategories();
        }
        if (!categoriesList.isEmpty()) {
            for (Categories c : categoriesList) {
                Log.d("Ranj", "Category name " + c.getCategoryName());
            }
        }
        return categoriesList;
    }

    private void populateQuestions() {
        int count = 5;
        Categories colCategory = null;

        List<Scores> scorelist = getScores();
        Collections.sort(scorelist, new Scores());


        for (int row = 0; row < ROWS_COUNT; row++) {
//            while (count < scorelist.size()) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT, 1.0f));
            mTableLayout.addView(tableRow);


            for (int col = 0; col < COLS_COUNT; col++) {
                final int FINAL_ROW = row;
                final int FINAL_COL = col;
                View view = mTableLayout.getChildAt(0);
                if (view instanceof View) {
                    TableRow tr = (TableRow) view;
                    View child = tr.getChildAt(FINAL_COL);
                    if (child instanceof Button) {
                        Button button = (Button) child;
                        colCategory = db.getCategoryByName(button.getText().toString());
                    }
                }

                Button button = new Button(this);
//                ViewGroup.LayoutParams params = button.getLayoutParams();
                button.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT, 1.0f));
//                    button.setText(String.valueOf(scorelist.get(count).getScore()));
                button.setText(String.valueOf(count));
               /* if (iter!=null && iter.hasNext()) {
                    q = (Questions) iter.next();
                    if (q.getCategoryName().equals(colCategory.getCategoryName())) {
                        Log.d("Ranj", "Question ID " + q.getQuestionId());
                        button.setId(q.getQuestionId());
                        button.setBackgroundColor(Color.MAGENTA);
                    }
                }*/
                tableRow.addView(button);
                buttons[row][col] = button;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button b = (Button) v;
                        Categories category = new Categories();
                        Scores scores = new Scores();
                        View view = mTableLayout.getChildAt(0);
                        if (view instanceof View) {
                            TableRow tableRow = (TableRow) view;
                            View child = tableRow.getChildAt(FINAL_COL);
                            if (child instanceof Button) {
                                Button button = (Button) child;
                                category = db.getCategoryByName(button.getText().toString());
                            }
                        }
                        if (v instanceof View) {

                            String scoreValue = b.getText().toString();
                            scores = db.getScoresbyValue(scoreValue);
                        }
//                            createNewQuestion(v,FINAL_ROW, FINAL_COL);
                        Log.d("Ranj","Button ID on Click " + v.getId());
                        launchNewQuestionActivity(v, category.getCategoryName(), scores.getScore(), v.getId());
                    }
                });

            }
            count = count + 5;
//            }


        }
    }

    private void launchNewQuestionActivity(View view, String categoryName, int score, int questionID) {
        Intent intent = new Intent(getApplicationContext(), NewQuestionActivity.class);
        intent.putExtra("quizID", quizId);
        intent.putExtra("roundID", roundId);
        intent.putExtra("categoryName", categoryName);
        intent.putExtra("questionID", questionID);
        mButton = (Button) view;
        intent.putExtra("score", score);
//        startActivity(intent);
        startActivityForResult(intent, 5);
    }

   /* private void createNewQuestion(View buttonView, int final_row, int final_col) {
        View view = mTableLayout.getChildAt(0);
        Categories category = new Categories();
        Scores scores = new Scores();
        if (view instanceof View) {
            TableRow tableRow = (TableRow) view;
            View child = tableRow.getChildAt(final_col);
            if (child instanceof Button) {
                Button button = (Button) child;
//                Toast.makeText(getApplicationContext(), "Click on the category " + button.getText().toString(), Toast.LENGTH_SHORT).show();
                category = db.getCategoryByName(button.getText().toString());
            }
        }
        if (buttonView instanceof View) {
            Button button = (Button) buttonView;
            String scoreValue = button.getText().toString();
            scores = db.getScoresbyValue(scoreValue);
        }
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(QuestionsActivity.this);

        final View dialogView = getLayoutInflater().inflate(R.layout.question_dialog, null);
        Button saveBtn = dialogView.findViewById(R.id.saveBtn);
        Button cancelBtn = dialogView.findViewById(R.id.cancelBtn);
        final TextView questionTxt = (TextView) dialogView.findViewById(R.id.questionEditText);
        final TextView answerTxt = (TextView) dialogView.findViewById(R.id.answerTxt);
        final TextView choice2Txt = (TextView) dialogView.findViewById(R.id.choice2Txt);
        final TextView choice3Txt = (TextView) dialogView.findViewById(R.id.choice3Txt);
        final TextView choice4Txt = (TextView) dialogView.findViewById(R.id.choice4Txt);

        alertDialog.setView(dialogView);
        alertDialog.setTitle("Add New Question");
        final AlertDialog dialog = alertDialog.create();
        dialog.show();

        final Categories finalCategory = category;
        final Scores finalScore = scores;
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = ValidateQuestionInput();
//                InsertQuestiontoDB(questionTxt, answerTxt, choice2Txt, choice3Txt, choice4Txt, quizId, roundId, finalCategory.getCategoryId(), finalScore.getScoreId());
            }

            private boolean ValidateQuestionInput() {
                boolean result = false;
                if (questionTxt.getText().toString().isEmpty() || answerTxt.getText().toString().isEmpty()) {
                    final AlertDialog.Builder dialog1 = new AlertDialog.Builder(dialog.getContext());
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
                dialog.dismiss();
            }
        });


    }
*/

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle extras = data.getExtras();
                long questionID = extras.getLong("questionID");
                Log.d("Ranj", "QUestion ID is " + questionID);
                mButton.setId((int) questionID);
                mButton.setBackgroundColor(Color.CYAN);
                Questions q = db.getQuestionById((int) questionID);
                Log.d("Ranj", "Question is " + q.getQuestion());

            }
        }

    }
}

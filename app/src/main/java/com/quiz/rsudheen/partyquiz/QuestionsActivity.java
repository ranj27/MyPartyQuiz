package com.quiz.rsudheen.partyquiz;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewParent;
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
    private int categoryColor;
    private Button mStartQuizBtn;
    private boolean viewOnly = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        roundId = extras.getInt("roundID");
        quizId = extras.getInt("quizID");
        viewOnly = extras.getBoolean("viewOnly");


        setContentView(R.layout.activity_questions);
        mTableLayout = (TableLayout) findViewById(R.id.tableForQuestions);
        TableRow headerRow = (TableRow) mTableLayout.getChildAt(0).findViewById(R.id.row1);

        db = new DatabaseHelper(getApplicationContext());
//        db.insertDefaultCategories(quizId,roundId);
        questions = db.getQuestionsById(quizId, roundId);
        categories = db.getCategoriesFoQuizId(quizId, roundId);
        /*if (questions.isEmpty()) {
            db.createNewQuestionsforQuiz(quizId, roundId);
            questions = db.getQuestionsById(quizId, roundId);
        }
        List<Categories> categories = getCategories();*/

        if (categories.isEmpty()) {
            //categories = defaultCategories;
            db.insertDefaultCategories(quizId, roundId);
            categories = db.getCategoriesFoQuizId(quizId, roundId);
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
                    ColorDrawable colorDrawable = (ColorDrawable) button.getBackground();
                    categoryColor = colorDrawable.getColor();
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
                        if (q.getCategoryName() != null && q.getCategoryName().equals(category.getCategoryName())) {
                            Log.d("Ranj", "Question ID " + q.getQuestionId());
                            buttons[j][i].setId(q.getQuestionId());
                            buttons[j][i].setBackgroundColor(categoryColor);
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
                    updateCategoryName(currentCategoryName, newCategoryName);
                    updateQuestionsCategory(currentCategoryName, newCategoryName);
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

    private void updateQuestionsCategory(String currentCategoryName, String newCategoryName) {
        db.updateQuestionCategory(quizId, roundId, currentCategoryName, newCategoryName);
    }

    private void updateCategoryName(String currentCategoryName, String newCategoryName) {
        db.updateQuizCategoryName(quizId, roundId, currentCategoryName, newCategoryName);
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
        int count = 1;
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
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                layoutParams.setMargins(1, 1, 1, 1);
                button.setLayoutParams(layoutParams);


                /*button.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT, 1.0f));*/
//                    button.setText(String.valueOf(scorelist.get(count).getScore()));
                button.setText(Html.fromHtml("<color:'black'> " + count + "</color>&nbsp&nbsp&nbsp<b align=right>  10 </b>"));
//                button.setText("10");
                button.setBackgroundColor(Color.LTGRAY);
                button.setPadding(1, 1, 1, 1);
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
                                ColorDrawable categoryCol = (ColorDrawable) button.getBackground();
                                categoryColor = categoryCol.getColor();
                            }
                        }
                        /*if (v instanceof View) {

                            String scoreValue = b.getText().toString();
                            scores = db.getScoresbyValue(scoreValue);
                        }*/
//                            createNewQuestion(v,FINAL_ROW, FINAL_COL);
//                        Log.d("Ranj","Button ID on Click " + v.getId());
                        if (viewOnly) {
                            launchQuestionViewActivity(v.getId(),category.getCategoryName(),categoryColor);
                        } else {
                            launchNewQuestionActivity(v, category.getCategoryName(), category.getCategoryId(), v.getId());
                        }


                    }
                });

            }
            count++;
//            }


        }
    }

    private void launchQuestionViewActivity(int id, String categoryName, int colorId) {
        Intent intent = new Intent(getApplicationContext(), QuestionViewActivity.class);
        intent.putExtra("questionID", id);
        intent.putExtra("categoryName",categoryName);
        intent.putExtra("categoryColor",colorId);
        startActivityForResult(intent, 5);
    }

    private void launchNewQuestionActivity(View view, String categoryName, int categoryId,int questionID) {
        Intent intent = new Intent(getApplicationContext(), NewQuestionActivity.class);
        intent.putExtra("quizID", quizId);
        intent.putExtra("roundID", roundId);
        intent.putExtra("categoryName", categoryName);
        intent.putExtra("questionID", questionID);
        intent.putExtra("categoryID", categoryId);
        mButton = (Button) view;
//        intent.putExtra("score", score);
//        startActivity(intent);
        startActivityForResult(intent, 5);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startBtn:
                startQuiz();
                break;

        }

    }

    private void startQuiz() {
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
//                mButton.setBackgroundColor(Color.CYAN);
                mButton.setBackgroundColor(categoryColor);
                Questions q = db.getQuestionById((int) questionID);
                Log.d("Ranj", "Question is " + q.getQuestion());

            }
        }

    }
}

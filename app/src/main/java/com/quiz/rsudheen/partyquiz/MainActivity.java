package com.quiz.rsudheen.partyquiz;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.quiz.rsudheen.partyquiz.database.DatabaseHelper;
import com.quiz.rsudheen.partyquiz.database.Quiz;
import com.quiz.rsudheen.partyquiz.database.Scores;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener, QuizDetailsFragment.QuizDetailsListener, SettingsDialogFragment.DatabaseListener {

    private Button mNewQuizBtn;
    private Button mOpenQuizBtn;
    private DatabaseHelper db;
    private boolean mCreateResult = false;
    private QuizDetailsFragment mQuizDetailsFragment;
    private FragmentManager mFragmentManager;
    private Button mSettingsBtn;
    private SettingsDialogFragment mSettingFragment;
    private ListView quizList;
    private AlertDialog dialog;
    private TextView mQuizName;
    private TextView mQuizRounds;
    private Button mQuizOkBtn;
    private Button mQuizCancelBtn;
    private AlertDialog mDialog;
    private TableLayout mSettingsTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNewQuizBtn = (Button) findViewById(R.id.newQuiz);
        mOpenQuizBtn = (Button) findViewById(R.id.openQuiz);
        mSettingsBtn = (Button)findViewById(R.id.settingsBtn);
        mNewQuizBtn.setOnClickListener(this);
        mOpenQuizBtn.setOnClickListener(this);
        mSettingsBtn.setOnClickListener(this);

        db = new DatabaseHelper(getApplicationContext());
//        mFragmentManager = getSupportFragmentManager();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.newQuiz:
                createNewQuizWindow();
                break;
            case R.id.openQuiz:
                openQuizWindow();
                break;
            case R.id.settingsBtn:
                showSettingsWindow();
                break;

        }
    }

    private void showSettingsWindow() {
        /*mSettingFragment = new SettingsDialogFragment();
        mSettingFragment.setCancelable(false);
        mSettingFragment.show(mFragmentManager, "Input Dialog");
*/
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.fragment_quiz_settings,null);
        alertDialog.setTitle("Settings");
        alertDialog.setView(view);
        dialog = alertDialog.create();
        dialog.show();
        mSettingsTable = (TableLayout)view.findViewById(R.id.tableforSettings);
        Button mSettingsCancelBtn = (Button) view.findViewById(R.id.cancelBtn);
        Button mSettingsSaveBtn = (Button) view.findViewById(R.id.saveBtn);
        mSettingsCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        mSettingsSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        populateScoreSettings();



    }

    private void populateScoreSettings() {

        List<Scores> scores = db.getAllScore();
        if (!scores.isEmpty() && scores.size()>0){
            for(Scores s: scores){
                TableRow tableRow = new TableRow(this);
                tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.MATCH_PARENT, 1.0f));
                mSettingsTable.addView(tableRow);
                TextView textView = new TextView(this);
                textView.setText(s.getComplexity());
                tableRow.addView(textView);
                EditText editText = new EditText(this);
                editText.setText(String.valueOf(s.getScore()));
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                editText.setTypeface(Typeface.DEFAULT);
                editText.requestFocus();
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText ,
                        InputMethodManager.SHOW_IMPLICIT);
                tableRow.addView(editText);

            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void openQuizWindow() {
        ArrayList<String> quizNameList = new ArrayList<String>();
        List<Quiz> quizes = db.getAllQuiz();
        for (Quiz q: quizes){
            quizNameList.add(q.getQuizName());
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.quiz_list,null);
//        LayoutInflater inflater = getLayoutInflater();
//        View quizLayout = inflater.inflate(R.layout.quiz_list, null);
        quizList = (ListView)view.findViewById(R.id.quizList);
        if (quizNameList!=null){
            ArrayAdapter<String> quizArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, quizNameList);
            quizList.setAdapter(quizArrayAdapter);
        }
//        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this,R.style.Theme_AppCompat));

        alertDialogBuilder.setView(view);
        alertDialogBuilder.setTitle("Quiz Names");
        mDialog = alertDialogBuilder.create();
        mDialog.show();
        quizList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String quizName = adapterView.getItemAtPosition(i).toString();
                Quiz quiz = db.getQuizByName(quizName);
                if (quiz != null) {
//                    Toast.makeText(getApplicationContext(), "Selected Quiz " + adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_LONG).show();
                    mDialog.dismiss();
                    launchQuizRoundsWindow(quiz.getQuizId(),quiz.getQuizRounds());
                }
            }
        });

    }

    private void launchQuizRoundsWindow(int quizID,String roundsCount) {
        Intent intent = new Intent(getApplicationContext(), RoundsActivity.class);
        intent.putExtra("roundsCount",roundsCount);
        intent.putExtra("quizID",quizID);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void createNewQuizWindow() {
        /*mQuizDetailsFragment = new QuizDetailsFragment();
        mQuizDetailsFragment.setCancelable(false);
        mQuizDetailsFragment.setDialogTitle("Enter Quiz Details");
        mQuizDetailsFragment.show(mFragmentManager, "Input Dialog");*/
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.fragment_quiz_details,null);
        alertDialog.setTitle("Quiz Details");
        alertDialog.setView(view);
        dialog = alertDialog.create();
        dialog.show();
        mQuizName = (TextView)view.findViewById(R.id.nameTxt);
        mQuizRounds = (TextView)view.findViewById(R.id.roundsTxt);
        mQuizName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });
        mQuizRounds.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });
        mQuizOkBtn = (Button)view.findViewById(R.id.okBtn);
        mQuizCancelBtn = (Button)view.findViewById(R.id.cancelBtn);
        mQuizName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuizName.setText("");
            }
        });
        mQuizRounds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuizRounds.setText("");
            }
        });
        mQuizOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = ValidateInput();
                if (result) {
                    getQuizDetails(mQuizName.getText().toString(), mQuizRounds.getText().toString());
                    if (mCreateResult) {
                        dialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), RoundsActivity.class);
                        intent.putExtra("roundsCount", mQuizRounds.getText().toString());
                        startActivity(intent);
                    }
                }
            }

            private boolean ValidateInput() {
                boolean result = false;
                String quizName = mQuizName.getText().toString();
                String rounds = mQuizRounds.getText().toString();
                if (!quizName.isEmpty() && !rounds.isEmpty()) {
                    result = true;
                }
                return result;
            }
        });
        mQuizCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    @Override
    public void getQuizDetails(String quizName, String noOfRounds) {
        // Toast.makeText(getApplicationContext(),"Quiz Name  "+ quizName + " Round " + noOfRounds,Toast.LENGTH_LONG).show();
        Quiz dbQuiz = db.getQuizByName(quizName);
        if (dbQuiz == null) {
            Quiz quiz = new Quiz(quizName, noOfRounds);
            long rowData = db.createQuiz(quiz);
            if (rowData == -1) {
                Toast.makeText(getApplicationContext(), "Failed to create quiz " + quizName, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), quizName + " created", Toast.LENGTH_LONG).show();
                mCreateResult = true;
//                updateCreateResult();
            }
        } else {
            if (dbQuiz.getQuizName().equals(quizName)) {
                Toast.makeText(getApplicationContext(), quizName + " already exists. Choose a different name", Toast.LENGTH_LONG).show();
            }
        }
        /*//quiz = db.getQuiz(1);
        // Toast.makeText(getApplicationContext(),"From DB - Quiz Name  "+ quiz.getQuizName() + " ID " + quiz.getQuizId(),Toast.LENGTH_LONG).show();
        List<Quiz> quizList = db.getAllQuiz();
        for (Quiz q : quizList) {
            Log.d("Ranj", "Quiz Name " + q.getQuizName() + " id " + q.getQuizId());
        }*/
    }

    /*public void updateCreateResult(){
        mQuizDetailsFragment.updateCreateResult(mCreateResult);
    }*/

    @Override
    public void getDefaultSettings() {
        Scores scores = new Scores();
        List<Scores> scoresData = db.getAllScore();
        for (Scores s:scoresData){
            Log.d("Ranj","Complexity " + s.getComplexity() + " Scores " + s.getScore());
        }
        mSettingFragment.getDefaultScores(scoresData);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.closeDB();
    }
}

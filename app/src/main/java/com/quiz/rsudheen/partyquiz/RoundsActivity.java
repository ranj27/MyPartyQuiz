package com.quiz.rsudheen.partyquiz;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class RoundsActivity extends Activity {

    private static final int COL_COUNT = 4;
    private static final int ROW_COUNT = 4;
    private int roundsCount;
    private int quizId;
    private boolean viewOnly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rounds);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        quizId = extras.getInt("quizID");
        roundsCount = Integer.parseInt(intent.getStringExtra("roundsCount"));
        viewOnly = extras.getBoolean("viewOnly");

        populateButtons(roundsCount);

    }

    private void populateButtons(int roundsCount) {
        int rows = 1;
        if ((roundsCount / 4) == 0) {
            rows = roundsCount / 4;
        } else {
            rows = (roundsCount / 4) + 1;
        }
        int cols = 0;
        int buttonCount = 0;
        TableLayout tableLayout = (TableLayout) findViewById(R.id.tableForRounds);
        for (int row = 0; row <= rows; row++) {
            TableRow tableRow = new TableRow(this);

            tableLayout.addView(tableRow);
            if ((roundsCount - buttonCount) < COL_COUNT) {
                cols = (roundsCount - buttonCount);
            } else {
                cols = 4;
            }
            for (int col = 0; col < cols; col++) {
                Button button = new Button(this);
                button.setId(buttonCount + 1);
                button.setText("Round " + (button.getId()));
                tableRow.addView(button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showQuestions(view.getId());
                    }
                });
                buttonCount++;
            }

        }
    }

    private void showQuestions(int id) {
        Intent intent = new Intent(getApplicationContext(), QuestionsActivity.class);
        intent.putExtra("roundID", id);
        intent.putExtra("quizID",quizId);
        intent.putExtra("viewOnly",viewOnly);
        startActivity(intent);

    }


}

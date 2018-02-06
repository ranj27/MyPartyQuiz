package com.quiz.rsudheen.partyquiz;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuizDetailsFragment extends DialogFragment implements TextView.OnEditorActionListener, View.OnClickListener {


    private TextView mQuizNameTxt;
    private TextView mNoOfRoundsTxt;
    private String dialogTitle;
    private Button mOkBtn;
    private Button mCancelBtn;
    private QuizDetailsListener mQuizDetailsListenerCallback;
    private Activity mActivity;
    private boolean mCreateResult = false;

    public void updateCreateResult(boolean createResult) {
        mCreateResult = createResult;
    }


    public interface QuizDetailsListener {
        public void getQuizDetails(String quizName, String noOfRounds);
    }

    public QuizDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mQuizDetailsListenerCallback = (QuizDetailsListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity = getActivity();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quiz_details, container);
        getDialog().setTitle(dialogTitle);
        mQuizNameTxt = (TextView) view.findViewById(R.id.nameTxt);
        mNoOfRoundsTxt = (TextView) view.findViewById(R.id.roundsTxt);
        mOkBtn = (Button) view.findViewById(R.id.okBtn);
        mCancelBtn = (Button) view.findViewById(R.id.cancelBtn);
        mQuizNameTxt.setOnEditorActionListener(this);
        mNoOfRoundsTxt.setOnEditorActionListener(this);
        mQuizNameTxt.setOnClickListener(this);
        mNoOfRoundsTxt.setOnClickListener(this);
        mNoOfRoundsTxt.setShowSoftInputOnFocus(false);
        mOkBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);

        return view;

//        return inflater.inflate(R.layout.fragment_quiz_details, container, false);
    }

    public void setDialogTitle(String dialogTitle) {
        this.dialogTitle = dialogTitle;
    }

   /* @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.CustomDialog);
    }*/

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//        mQuizDetailsListenerCallback = (QuizDetailsListener)getActivity();
        //quizDetailsListener.getQuizDetails(mQuizNameTxt.getText().toString(),mNoOfRoundsTxt.getText().toString());
        //this.dismiss();
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nameTxt:
                mQuizNameTxt.setText("");
                break;
            case R.id.roundsTxt:
                mNoOfRoundsTxt.setText("");
                break;
            case R.id.okBtn:
                boolean result = ValidateInput();
                if (result) {
                    //Toast.makeText(getActivity(),"Data entered " + mQuizNameTxt.getText().toString() + " " + mNoOfRoundsTxt.getText().toString(),Toast.LENGTH_LONG).show();
                    mQuizDetailsListenerCallback.getQuizDetails(mQuizNameTxt.getText().toString(), mNoOfRoundsTxt.getText().toString());
                    if (mCreateResult) {
                        this.dismiss();
                        Intent intent = new Intent(getContext(), RoundsActivity.class);
                        intent.putExtra("roundsCount",mNoOfRoundsTxt.getText().toString());
                        startActivity(intent);

                    }

                } else {
                    Toast.makeText(getActivity(), "Enter valid data", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.cancelBtn:
                this.dismiss();
                break;
        }
    }

    private boolean ValidateInput() {
        boolean result = false;
        String quizName = mQuizNameTxt.getText().toString();
        String rounds = mNoOfRoundsTxt.getText().toString();
        if (!quizName.isEmpty() && !rounds.isEmpty()) {
            result = true;
        }
        return result;
    }
}

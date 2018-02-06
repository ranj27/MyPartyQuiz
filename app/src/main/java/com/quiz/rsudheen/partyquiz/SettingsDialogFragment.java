package com.quiz.rsudheen.partyquiz;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.quiz.rsudheen.partyquiz.database.Scores;

import java.util.List;


/**
 * Created by rsudheen on 1/10/2018.
 */

public class SettingsDialogFragment extends DialogFragment implements View.OnClickListener {

    private Activity mActivity;
    private DatabaseListener mDatabaseListener;
    private Button mSaveBtn;
    private Button mCanelBtn;

    public void getDefaultScores(List<Scores> scoresData) {
        
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.saveBtn:
                break;
            case R.id.cancelBtn:
                this.dismiss();
                break;
        }
    }

    public interface DatabaseListener{
        public void getDefaultSettings();
    }

    public SettingsDialogFragment() {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
        //getDefaultSettings();
    }

    private void getDefaultSettings() {
        mDatabaseListener.getDefaultSettings();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mDatabaseListener = (DatabaseListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mActivity = getActivity();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quiz_settings, container);
        mSaveBtn = (Button)view.findViewById(R.id.saveBtn);
        mCanelBtn = (Button)view.findViewById(R.id.cancelBtn);
        mSaveBtn.setOnClickListener(this);
        mCanelBtn.setOnClickListener(this);
        getDialog().setTitle("Settings:");
//        setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog);
        getDefaultSettings();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
       }
    }

}

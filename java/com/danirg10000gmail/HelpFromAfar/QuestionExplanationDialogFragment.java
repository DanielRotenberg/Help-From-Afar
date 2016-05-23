package com.danirg10000gmail.HelpFromAfar;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.danirg10000gmail.HelpFromAfar.model.QuestionM;
import com.danirg10000gmail.HelpFromAfar.model.QuestionnaireM;

import org.w3c.dom.Text;


public class QuestionExplanationDialogFragment extends DialogFragment {
    private static final String EXPLANATION_POSITION = "explanationPosition";
    private int mIndex = 0;
    private TextView mExplanationText;
    private View v;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mIndex = getArguments().getInt(EXPLANATION_POSITION);
        return createExplanation(mIndex);
    }

    public static QuestionExplanationDialogFragment createDialogWithBundle(int position){
        Bundle args = new Bundle();
        args.putInt(EXPLANATION_POSITION, position);
        QuestionExplanationDialogFragment questionExplanationDialogFragment = new QuestionExplanationDialogFragment();
        questionExplanationDialogFragment.setArguments(args);
        return questionExplanationDialogFragment;
    }

    private AlertDialog createExplanation(int position){
        QuestionnaireM questionnaireM = new QuestionnaireM(getActivity());
        QuestionM questionM = questionnaireM.getQuestion(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        v = layoutInflater.inflate(R.layout.fragment_dialog_explanation,null);
        mExplanationText = (TextView) v.findViewById(R.id.fragment_dialog_explanation_text);
        mExplanationText.setText(questionM.getExplanation());
        builder.setTitle("Explanation");
        builder.setView(v);
        builder.setPositiveButton(R.string.fragment_dialog_ok_button,null);
        AlertDialog dialog = builder.create();
        return dialog;
    }

}

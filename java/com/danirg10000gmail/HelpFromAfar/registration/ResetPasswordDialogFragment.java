package com.danirg10000gmail.HelpFromAfar.registration;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.danirg10000gmail.HelpFromAfar.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

public class ResetPasswordDialogFragment extends DialogFragment {
    private View mView;
    private EditText mEmail;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return buildDialog();
    }

    private AlertDialog buildDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        mView = inflater.inflate(R.layout.fragment_dialog_reset_password,null);
        mEmail = (EditText)mView.findViewById(R.id.fragment_dialog_reset_password_edit_text);
        builder.setTitle(R.string.fragment_dialog_reset_password_title);
        builder.setView(mView);
        builder.setPositiveButton(R.string.fragment_dialog_reset_password_positive_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage(getString(R.string.fragment_dialog_reset_password_progress_dialog_message));
                progressDialog.show();
                ParseUser.requestPasswordResetInBackground(mEmail.getText().toString(), new RequestPasswordResetCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null){
                            toastWithProgressDismiss(progressDialog, Integer.toString(R.string.fragment_dialog_reset_password_progress_dialog_message));
                        }else {
                            toastWithProgressDismiss(progressDialog,e.getMessage());
                        }
                    }
                });
            }
        });
        builder.setNegativeButton(R.string.fragment_dialog_reset_password_negative_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }


    private void toastWithProgressDismiss(ProgressDialog pDialog, String toast){
        Toast.makeText(getActivity(),toast,Toast.LENGTH_SHORT);
        pDialog.dismiss();
    }
}

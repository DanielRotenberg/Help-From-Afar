package com.danirg10000gmail.HelpFromAfar.registration;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.danirg10000gmail.HelpFromAfar.R;
import com.danirg10000gmail.HelpFromAfar.dataBase.ParseData;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class CreateAccountFragment extends Fragment implements TextWatcher {
    private static final String EXTRA_NAME = "name";
    private static final String EXTRA_PASSWORD = "password";
    private EditText mCreateAccountUserName, mCreateAccountUserPassword, mCreateAccountVerifyPassword, mCreateAccountMail;
    private Button mCreateAccountButton;
    private Boolean mValid=true;
    String mUserName,mPass;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_account,container,false);
        mCreateAccountUserName = (EditText)v.findViewById(R.id.create_account_user_name);
        mCreateAccountUserPassword = (EditText)v.findViewById(R.id.create_account_user_password);
        mCreateAccountVerifyPassword = (EditText)v.findViewById(R.id.create_account_verify_password);
        mCreateAccountMail = (EditText)v.findViewById(R.id.create_account_user_mail);
        mCreateAccountButton = (Button)v.findViewById(R.id.create_account_create_account_button);
        mCreateAccountUserName.addTextChangedListener(this);
        mCreateAccountUserPassword.addTextChangedListener(this);
        mCreateAccountVerifyPassword.addTextChangedListener(this);

        mCreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Please wait while creating your account...");
                progressDialog.show();

                final String email = mCreateAccountMail.getText().toString();
                if (mValid) {
                    ParseUser user = new ParseUser();
                    user.setUsername(mUserName);
                    user.setPassword(mPass);
                    user.setEmail(email);
                    user.put(ParseData.UserClass.UserClassCols.IS_THERAPIST,false);

                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), R.string.create_account_successfully_toast, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                intent.putExtra(EXTRA_NAME, mUserName);
                                intent.putExtra(EXTRA_PASSWORD, mPass);
                                getActivity().setResult(Activity.RESULT_OK, intent);

                                //clean up -> destroy this activity vs fragment
                                CreateAccountFragment.this.getActivity().finish();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(getActivity(),"fix errors first",Toast.LENGTH_SHORT).show();
                }
            }
        });


        return v;
    }
    public static String getName(Intent data){
        return data.getStringExtra(EXTRA_NAME);
    }
    public static String getPassword (Intent data){
        return data.getStringExtra(EXTRA_PASSWORD);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        mUserName = mCreateAccountUserName.getText().toString();
        mPass = mCreateAccountUserPassword.getText().toString();
        mValid = validate(mPass, mUserName);
    }
    private Boolean validate(String passChecker,String nameChecker){
        mValid = true;
        String cName=("^(\\w*)$");
        final String verifyPass = mCreateAccountVerifyPassword.getText().toString();
        if((nameChecker.length()<4||nameChecker.length()>16)){
            mCreateAccountUserName.setError("username must be 4-16 chars");
            mValid=false;
        }
        if(!(nameChecker.matches(cName))){
            mCreateAccountUserName.setError("username can't contain non-word characters");
            mValid=false;
        }
        if((passChecker.length()<4||passChecker.length()>16)){
            mCreateAccountUserPassword.setError("password must be 4-16 chars");
            mValid=false;
        }
        if(!(passChecker.matches(cName))){
            mCreateAccountUserPassword.setError("password can't contain non-word characters");
            mValid=false;
        }
        if(!(passChecker.equals(verifyPass))){
            mCreateAccountVerifyPassword.setError("passwords don't match");
            mValid=false;
        }
        return mValid;
    }
}

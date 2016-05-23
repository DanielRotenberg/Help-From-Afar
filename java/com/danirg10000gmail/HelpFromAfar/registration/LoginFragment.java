package com.danirg10000gmail.HelpFromAfar.registration;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.danirg10000gmail.HelpFromAfar.R;
import com.danirg10000gmail.HelpFromAfar.dataBase.SharedPrefData;
import com.danirg10000gmail.HelpFromAfar.userData.ProfileActivity;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * Created by USER on 04/01/2016.
 */
public class LoginFragment extends Fragment {

    private static final int REQUEST_CODE_CREATE_ACCOUNT = 0;
    private final String EMAIL_VERIFIED = "emailVerified";
    private final String SHOW_FRAGMENT_DIALOG_RESET_PASSWORD = "showFragmentDialogResetPassword";
    private EditText mLoginUserName, mLoginUserPassword;
    private Button mLoginButton, mLoginCreateAccountButton, mResetPasswordButton;
    private SharedPreferences prefs;
    private static String userName;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if(ParseUser.getCurrentUser()!=null) {
            userName = ParseUser.getCurrentUser().getUsername();
            prefs = getActivity().getSharedPreferences(userName, Context.MODE_PRIVATE);
            if (prefs.getBoolean(SharedPrefData.EMAIL_VERIFIED, false)) {
                toProfile();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.login_fragment, container, false);
        mLoginUserName = (EditText) fragmentView.findViewById(R.id.login_user_name);
        mLoginUserPassword = (EditText) fragmentView.findViewById(R.id.login_user_password);
        mLoginButton = (Button) fragmentView.findViewById(R.id.login_login_button);
        mLoginCreateAccountButton = (Button) fragmentView.findViewById(R.id.login_create_account_button);
        mResetPasswordButton = (Button) fragmentView.findViewById(R.id.login_reset_password_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validation by parse.com goes here
                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Please wait while we logging you in...");
                progressDialog.show();
                ParseUser.logInInBackground(mLoginUserName.getText().toString(), mLoginUserPassword.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if (parseUser != null) {
                            boolean verified = parseUser.getBoolean(EMAIL_VERIFIED);
                            if (!verified) {
                                Toast.makeText(getActivity(), R.string.login_fragment_toast_verify_account_first, Toast.LENGTH_SHORT).show();
                                LoginFragment.this.dismissProgressDialog(progressDialog);
                                ParseUser.logOut();
                            } else {
                                LoginFragment.this.dismissProgressDialog(progressDialog);
                                userName= mLoginUserName.getText().toString();
                                toProfile();
                            }
                        } else {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            LoginFragment.this.dismissProgressDialog(progressDialog);
                        }
                    }
                });
            }
        });

        mLoginCreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CreateAccountActivity.createAccountActivityIntent(getActivity());
                startActivityForResult(intent, REQUEST_CODE_CREATE_ACCOUNT);

            }
        });
        mResetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                final ResetPasswordDialogFragment resetPasswordDialogFragment = new ResetPasswordDialogFragment();
                resetPasswordDialogFragment.show(fm, SHOW_FRAGMENT_DIALOG_RESET_PASSWORD);
            }
        });

        return fragmentView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != getActivity().RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CREATE_ACCOUNT && data != null) {
            String name = CreateAccountFragment.getName(data);
            String password = CreateAccountFragment.getPassword(data);
            mLoginUserName.setText(name);
            mLoginUserPassword.setText(password);
        }
    }
    private void toProfile(){
        Intent intent= ProfileActivity.newIntent(getActivity(),userName);
        startActivity(intent);
        getActivity().finish();
    }

    private void dismissProgressDialog(ProgressDialog progressDialog) {
        progressDialog.dismiss();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.login_screen,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.toolbar_contact_us:
                Intent mIntent = new Intent(Intent.ACTION_SEND);
                mIntent.setData(Uri.parse("mailto:"));
                String [] ourMails = {"danirg10000@gmail.com"};
                mIntent.putExtra(Intent.EXTRA_EMAIL,ourMails);
                mIntent.setType("message/rfc822");
                PackageManager pm = getActivity().getPackageManager();
                if (pm.resolveActivity(mIntent,PackageManager.MATCH_DEFAULT_ONLY) == null){
                    Toast.makeText(getActivity(), "you don't have responding app for mail", Toast.LENGTH_LONG).show();
                } else{
                    getActivity().startActivity(mIntent);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}



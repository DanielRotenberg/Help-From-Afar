package com.danirg10000gmail.HelpFromAfar.registration;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.danirg10000gmail.HelpFromAfar.SingleFragmentActivity;

public class CreateAccountActivity extends SingleFragmentActivity {
    public static Intent createAccountActivityIntent(Context context){
        Intent intent = new Intent(context,CreateAccountActivity.class);
        return  intent;
    }
    @Override
    protected Fragment getFragment() {
        return new CreateAccountFragment();
    }
}

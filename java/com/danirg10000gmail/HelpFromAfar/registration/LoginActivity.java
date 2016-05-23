package com.danirg10000gmail.HelpFromAfar.registration;


import android.support.v4.app.Fragment;
import com.danirg10000gmail.HelpFromAfar.SingleFragmentActivity;

public class LoginActivity extends SingleFragmentActivity {

    @Override
    protected Fragment getFragment() {
        return new LoginFragment();
    }


}

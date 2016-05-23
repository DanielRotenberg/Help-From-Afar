package com.danirg10000gmail.HelpFromAfar.userData;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.danirg10000gmail.HelpFromAfar.SingleFragmentActivity;

public class EditProfileActivity extends SingleFragmentActivity {
    public static Intent editProfileIntent(Context context){
        Intent intent = new Intent(context,EditProfileActivity.class);
        return intent;
    }
    @Override
    protected Fragment getFragment() {
        return EditProfileFragment.newInstance();
    }
}

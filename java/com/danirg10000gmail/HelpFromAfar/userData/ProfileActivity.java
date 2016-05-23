package com.danirg10000gmail.HelpFromAfar.userData;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;

import com.danirg10000gmail.HelpFromAfar.SingleFragmentActivity;
import com.danirg10000gmail.HelpFromAfar.dataBase.SharedPrefData;

public class ProfileActivity extends SingleFragmentActivity {
    String name;
    public static Intent newIntent(Context context){
        Intent intent = new Intent(context,ProfileActivity.class);
        return intent;

    }public static Intent newIntent(Context context,String name){
        Intent intent = new Intent(context,ProfileActivity.class);
        intent.putExtra(SharedPrefData.USER_NAME, name);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = getIntent().getStringExtra(SharedPrefData.USER_NAME);
    }

    @Override
    protected Fragment getFragment() {
        return ProfileFragment.newInstanceWithName(name);
    }
}

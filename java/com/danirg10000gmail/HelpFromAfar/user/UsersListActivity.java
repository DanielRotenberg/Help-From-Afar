package com.danirg10000gmail.HelpFromAfar.user;

import android.support.v4.app.Fragment;

import com.danirg10000gmail.HelpFromAfar.SingleFragmentActivity;

public class UsersListActivity extends SingleFragmentActivity {


    @Override
    protected Fragment getFragment() {
        return new UsersListFragment();
    }
}

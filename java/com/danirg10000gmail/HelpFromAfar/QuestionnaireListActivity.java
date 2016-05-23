package com.danirg10000gmail.HelpFromAfar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class QuestionnaireListActivity extends AppCompatActivity {
    private static final String TAG = "QuestionnaireList";
    private static final String USER_NAME = "UserName"; //-> key for userName extra
    //static factory for Intent
    public static Intent newInstance (Context context,String userName){
        Intent intent = new Intent(context,QuestionnaireListActivity.class);
        intent.putExtra(USER_NAME,userName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire_list);
        String userName = getIntent().getStringExtra(USER_NAME);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_questionnaire_list_container);
        if (fragment == null){
            fragment = QuestionnaireListFragment.newInstance(userName);
            fm.beginTransaction().replace(R.id.fragment_questionnaire_list_container,fragment).commit();
        }
    }
}

package com.danirg10000gmail.HelpFromAfar;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.danirg10000gmail.HelpFromAfar.dataBase.CentralInternalData;
import com.danirg10000gmail.HelpFromAfar.model.QuestionnaireM;
import com.danirg10000gmail.HelpFromAfar.user.SingleUserM;
import com.danirg10000gmail.HelpFromAfar.user.UserM;

import java.util.UUID;

public class QuestionViewPagerActivity extends AppCompatActivity {
    private static final String SOURCE_OF_QUESTIONNAIRE = "UserNewOrUserExistingOrTherapist"; //-->key
    private static final String UUID_EXISTING_QUESTIONNAIRE = "uuidOfExistingQuestionnaire";
    private QuestionnaireM mQuestionnaireM;
    private ViewPager mViewPager;
    private String mCheckIntentSource;
    private UserM mUserM;

    //static factory when its users new Questionnaire or User
    public static Intent newInstance(Context context, String source){
        Intent intent = new Intent(context,QuestionViewPagerActivity.class);
        intent.putExtra(SOURCE_OF_QUESTIONNAIRE,source);
        return intent;
    }
    //static factory when users existing questionnaire
    public static Intent newInstanceExistingQuestionnaire(Context context,String source,UUID uuid){
        Intent intent = new Intent(context,QuestionViewPagerActivity.class);
        intent.putExtra(SOURCE_OF_QUESTIONNAIRE,source);
        intent.putExtra(UUID_EXISTING_QUESTIONNAIRE, uuid);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_view_pager);
        mViewPager = (ViewPager)findViewById(R.id.activity_view_pager);
        mCheckIntentSource = getIntent().getStringExtra(SOURCE_OF_QUESTIONNAIRE);
        SingleUserM singleUserM = SingleUserM.getSingleUser();
        mUserM = singleUserM.getUserType(singleUserM,mCheckIntentSource);
        switch (mCheckIntentSource){
            case CentralInternalData.USER_NEW_QUESTIONNAIRE:
                mQuestionnaireM = new QuestionnaireM(getApplicationContext());
                mQuestionnaireM.setIsAnswered(false);
                mUserM.getQuestionnaireBankM().addQuestionnaire(mQuestionnaireM);
                break;
            default:
                UUID id = (UUID) getIntent().getSerializableExtra(QuestionViewPagerActivity.UUID_EXISTING_QUESTIONNAIRE);
                mUserM = singleUserM.getUserType(singleUserM,mCheckIntentSource);
                mQuestionnaireM = mUserM.getQuestionnaireBankM().getQuestionnaire(id);
                mQuestionnaireM.setIsAnswered(true);
        }

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {

            @Override
            public Fragment getItem(int position) {
                return QuestionFragment.newInstance(position, mCheckIntentSource, mQuestionnaireM.isAnswered(), mQuestionnaireM.getId());
            }

            @Override
            public int getCount() {
                return mQuestionnaireM.getQuestionsList().size();
            }
        });
    }

}

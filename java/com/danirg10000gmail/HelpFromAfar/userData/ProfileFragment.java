package com.danirg10000gmail.HelpFromAfar.userData;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.danirg10000gmail.HelpFromAfar.QuestionViewPagerActivity;
import com.danirg10000gmail.HelpFromAfar.QuestionnaireListActivity;
import com.danirg10000gmail.HelpFromAfar.R;
import com.danirg10000gmail.HelpFromAfar.SingleFragment;
import com.danirg10000gmail.HelpFromAfar.dataBase.CentralInternalData;
import com.danirg10000gmail.HelpFromAfar.dataBase.ParseData.UserClass;
import com.danirg10000gmail.HelpFromAfar.dataBase.ParseData.UserClass.UserClassCols;
import com.danirg10000gmail.HelpFromAfar.dataBase.SharedPrefData;
import com.danirg10000gmail.HelpFromAfar.user.SingleUserM;
import com.danirg10000gmail.HelpFromAfar.user.UserM;
import com.danirg10000gmail.HelpFromAfar.user.UsersListActivity;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ProfileFragment extends SingleFragment {
    private static final int REQUEST_CODE_EDIT = 0;
    private static final String ASSET_PATH="text_font.ttf";
    private TextView mTitle,mName,mAge,mGender,mCountry,mProfNameTitle,mProfAgeTitle,mProfGenderTitle,mProfCountryTitle;
    private Button mEdit, mAddQuestionnaire,mShowQuestionnaireList;
    private Button  goToTherapist;
    private ParseUser mParseUser;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private SingleUserM mSingleUser;
    private UserM mUser;
    private String name,country,userName;
    private int age;
    private Boolean gender;
    private Intent intent;


    public static ProfileFragment newInstance(){
        return new ProfileFragment();
    }
    public static ProfileFragment newInstanceWithName(String name) {
        Bundle args = new Bundle();
        args.putString(SharedPrefData.USER_NAME, name);
        ProfileFragment profileFragment = new ProfileFragment();
        profileFragment.setArguments(args);
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mParseUser = ParseUser.getCurrentUser();
        decideWhereToGo(this);
        // here we check if its user or therapist
        mSingleUser = SingleUserM.getSingleUser();
        mSingleUser.getUser().setName(userName);
        if (!(mSingleUser.getUser().isTherapist())) {
            mUser = mSingleUser.getUser();
            mUser.getQuestionnaireBankM();
        }
        mPreferences = getActivity().getSharedPreferences(userName, Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
            mEditor.putBoolean(SharedPrefData.EMAIL_VERIFIED, true);
            mEditor.commit();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile,container,false);
        mAge = (TextView)v.findViewById(R.id.profile_age_text);
        mGender = (TextView)v.findViewById(R.id.profile_gender_text);
        mCountry = (TextView)v.findViewById(R.id.profile_country_text);
        mTitle = (TextView)v.findViewById(R.id.profile_title);
        mName = (TextView)v.findViewById(R.id.profile_name_text);
        mProfNameTitle=(TextView)v.findViewById(R.id.profile_name_title);
        mProfGenderTitle=(TextView)v.findViewById(R.id.profile_gender_title);
        mProfCountryTitle=(TextView)v.findViewById(R.id.profile_country_title);
        mProfAgeTitle=(TextView)v.findViewById(R.id.profile_age_title);
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), ASSET_PATH);
        mProfNameTitle.setTypeface(tf);
        mProfGenderTitle.setTypeface(tf);
        mProfAgeTitle.setTypeface(tf);
        mProfCountryTitle.setTypeface(tf);
        mName.setTypeface(tf);
        mCountry.setTypeface(tf);
        mGender.setTypeface(tf);
        mAge.setTypeface(tf);
        mEdit = (Button)v.findViewById(R.id.profile_button_go_to_edit);
        load();
        mAddQuestionnaire = (Button)v.findViewById(R.id.fragment_status_button_add_questionnaire);
        mShowQuestionnaireList = (Button)v.findViewById(R.id.fragment_status_button_show_questionnaire_list);
        goToTherapist = (Button)v.findViewById(R.id.go_to_therapist);
        if(mUser.isTherapist()){
            mTitle.setText("Welcome Therapist");
            goToTherapist.setText("Show Users");
            mAddQuestionnaire.setVisibility(View.INVISIBLE);
            mAddQuestionnaire.setEnabled(false);
            mShowQuestionnaireList.setVisibility(View.INVISIBLE);
            mShowQuestionnaireList.setEnabled(false);
        }else{
            goToTherapist.setText("Show Therapists");
            mTitle.setText("Welcome User");
        }




        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = EditProfileActivity.editProfileIntent(getActivity());
                startActivityForResult(intent, REQUEST_CODE_EDIT);
            }
        });

        mAddQuestionnaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = QuestionViewPagerActivity.newInstance(getActivity(), CentralInternalData.USER_NEW_QUESTIONNAIRE);
                startActivity(intent);
            }
        });
        mShowQuestionnaireList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = QuestionnaireListActivity.newInstance(getActivity(), mSingleUser.getUser().getName());
                startActivity(intent);
            }
        });


        goToTherapist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(getActivity(), UsersListActivity.class);
                startActivity(i);
            }
        });
        return v;
    }
    public void load() {
        if (mPreferences.getString(SharedPrefData.NAME, "").isEmpty()) {
            final String id = mParseUser.getObjectId();
            ParseQuery<ParseObject> query = ParseQuery.getQuery(UserClass.NAME);
            ParseObject user = null;
            try {
                user = query.get(id);
                country=user.getString(UserClassCols.COUNTRY);
                name=user.getString(UserClassCols.NAME);
                age=user.getInt(UserClassCols.AGE);
                gender=user.getBoolean(UserClassCols.GENDER);
                mUser.setPersonalName(name);
                mUser.setAge(age);
                mUser.setGender(gender);
                mUser.setCountry(country);
                mName.setText(name);
                mAge.setText(""+age);
                mGender.setText(SharedPrefData.genderChecker(gender));
                mCountry.setText(country);
                mEditor.putString(SharedPrefData.NAME, name);
                mEditor.putInt(SharedPrefData.AGE, age);
                mEditor.putBoolean(SharedPrefData.GENDER, gender);
                mEditor.putString(SharedPrefData.COUNTRY, country);
                mEditor.commit();

            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            name=mPreferences.getString(SharedPrefData.NAME, "");
            age=mPreferences.getInt(SharedPrefData.AGE, 0);
            gender=mPreferences.getBoolean(SharedPrefData.GENDER, true);
            country=mPreferences.getString(SharedPrefData.COUNTRY, "");
            mUser.setPersonalData(name,age,true,country);
            mName.setText(name);
            mAge.setText(""+age);
            mGender.setText(SharedPrefData.genderChecker(mPreferences.getBoolean(SharedPrefData.GENDER, true)));
            mCountry.setText(country);
            }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != getActivity().RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_EDIT && data != null) {
            /*
            name = mUser.getPersonalName();
            age = mUser.getAge();
            country = mUser.getCountry();
            gender = mUser.getGender();*/
            name=mPreferences.getString(SharedPrefData.NAME,"");
            age=mPreferences.getInt(SharedPrefData.AGE,0);
            country=mPreferences.getString(SharedPrefData.COUNTRY,"");
            gender=mPreferences.getBoolean(SharedPrefData.GENDER,false);
            mGender.setText(SharedPrefData.genderChecker(gender));
            mCountry.setText(country);
            mName.setText(name);
            mAge.setText(""+age);
           /* mEditor.putString(SharedPrefData.NAME, name);
            mEditor.putInt(SharedPrefData.AGE, age);
            mEditor.putString(SharedPrefData.COUNTRY, country);
            mEditor.putBoolean(SharedPrefData.GENDER, gender);
            mEditor.commit();*/
        }
    }

    private void decideWhereToGo(ProfileFragment fragment) {
        //initializing the user only once ,type -> patient or therapist
        boolean isTherapist = mParseUser.getBoolean(UserClassCols.IS_THERAPIST);
        userName = mParseUser.getUsername();
        mUser = new UserM(getActivity(), isTherapist, userName);
        mSingleUser = SingleUserM.setSingleUser(mUser);
        if (mParseUser.getString(UserClassCols.COUNTRY) == null) {
            fragment.intent = EditProfileActivity.editProfileIntent(getActivity());
            startActivityForResult(intent, REQUEST_CODE_EDIT);
        }
    }
}

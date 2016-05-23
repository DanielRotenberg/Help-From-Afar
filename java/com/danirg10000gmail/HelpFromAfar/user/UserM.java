package com.danirg10000gmail.HelpFromAfar.user;

import android.content.Context;

import com.danirg10000gmail.HelpFromAfar.dataBase.ParseMethods;
import com.danirg10000gmail.HelpFromAfar.model.QuestionnaireBankM;

import java.util.ArrayList;
import java.util.List;

public class UserM {

    private List<UserM> mUsersList;
    private Context mContext;
    private String mName;
    private String mPersonalName;
    private int mAge;
    private boolean mGender;
    private String mCountry;
    private boolean mIsTherapist;
    private QuestionnaireBankM mQuestionnaireBankM;
    private ParseMethods mParseMethods;


    public UserM(Context context, boolean isTherapist, String name){
        mContext = context.getApplicationContext();
        mIsTherapist = isTherapist;
        mParseMethods = new ParseMethods(mContext);
        mName = name;
        mUsersList = new ArrayList<>();
        if (!isTherapist){
            mQuestionnaireBankM = new QuestionnaireBankM(context);
        }
    }


    public QuestionnaireBankM getQuestionnaireBankM() {
        if (mQuestionnaireBankM.getBankQuestionnaireMList().isEmpty()) {
            mParseMethods.loadUserQuestionnaires(getName(), mQuestionnaireBankM);
        }
        return mQuestionnaireBankM;
    }

    public List<UserM> getUsersList(){
        populateUsersList(mUsersList);
        return mUsersList;
    }

    public UserM getPatient(String name){
        populateUsersList(mUsersList);
        for (UserM userM : mUsersList){
            if (userM.getName().equalsIgnoreCase(name)){
                return userM;
            }
        }
        return null;
    }

    public void populateUsersList(List<UserM> userMs){
        if (userMs.isEmpty()){
            mParseMethods.loadUsers(userMs,isTherapist());
        }
    }

    public Context getContext() {
        return mContext;
    }


    public void setUsersList(List<UserM> usersList) {
        mUsersList = usersList;
    }

    public String getPersonalName() {
        return mPersonalName;
    }

    public void setPersonalName(String PersonalName) {
        mPersonalName = PersonalName;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getAge() {
        return mAge;
    }

    public void setAge(int age) {
        mAge = age;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        mCountry = country;
    }

    public void setGender(Boolean gender){ mGender=gender;}

    public boolean getGender(){return mGender;}

    public boolean isTherapist() {
        return mIsTherapist;
    }

    public void setIsTherapist(boolean isTherapist) {
        mIsTherapist = isTherapist;
    }

    public void setPersonalData(String name,int age,boolean gender,String country){
        setPersonalName(name);
        setAge(age);
        setGender(gender);
        setCountry(country);

    }
}



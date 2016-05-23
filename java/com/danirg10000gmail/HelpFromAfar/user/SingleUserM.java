package com.danirg10000gmail.HelpFromAfar.user;

/*
 using lazy singleton design pattern to to verify that its the same User in all parts of application.
*
 */
import android.content.Context;

public class SingleUserM {
    private UserM mUserM;
    private static SingleUserM mSingleUser;
    private Context mContext;

    private SingleUserM(){}

    public static SingleUserM setSingleUser(UserM user){
        if (mSingleUser == null && user != null){
            mSingleUser = new SingleUserM();
            mSingleUser.setUser(user);
            mSingleUser.mContext = user.getContext();
        } else if (mSingleUser != null && user == null) {
            mSingleUser = null;
            return mSingleUser;
        }
        return mSingleUser;
    }

    public UserM getUserType(SingleUserM singleUser,String userName){
        UserM user;
        if (!singleUser.getUser().isTherapist()){
            user = singleUser.getUser();
        } else {
            user = singleUser.getUser().getPatient(userName);
        }
        return user;
    }

    public static SingleUserM getSingleUser(){
        return mSingleUser;
    }

    public UserM getUser() {
        return mUserM;
    }

    public void setUser(UserM user) {
        mUserM = user;
    }

    @Override
    public String toString() {
        boolean therapist = mSingleUser.getUser().isTherapist();
        return mSingleUser.getUser().getName()+" he is therapist "+therapist;
    }

}

package com.danirg10000gmail.HelpFromAfar;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.danirg10000gmail.HelpFromAfar.dataBase.ParseData.UserClass;
import com.danirg10000gmail.HelpFromAfar.dataBase.SharedPrefData;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class UserDetailDialogFragment extends DialogFragment {
    private static final String USER = "username";
    private String mUserName,name,gender,age,country;
    private TextView mName,mGender,mAge,mCountry;
    private View v;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mUserName = getArguments().getString(USER);
        return createShow(mUserName);
    }

    public static UserDetailDialogFragment createDialogWithBundle(String userName){
        Bundle args = new Bundle();
        args.putString(USER, userName);
        UserDetailDialogFragment userDetailDialogFragment = new UserDetailDialogFragment();
        userDetailDialogFragment.setArguments(args);
        return userDetailDialogFragment;
    }

    private AlertDialog createShow(String userName){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        v = layoutInflater.inflate(R.layout.fragment_show_user_detail,null);
        ParseQuery<ParseObject> query=ParseQuery.getQuery(UserClass.NAME);
        query.whereEqualTo(UserClass.UserClassCols.USER_NAME,userName);
        ParseObject user=null;
        try {
            user=query.getFirst();
            name=user.getString(UserClass.UserClassCols.NAME);
            country=user.getString(UserClass.UserClassCols.COUNTRY);
            age=String.valueOf(user.getInt(UserClass.UserClassCols.AGE));
            gender=SharedPrefData.genderChecker(user.getBoolean(UserClass.UserClassCols.GENDER));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mName = (TextView) v.findViewById(R.id.fragment_name);
        mAge = (TextView) v.findViewById(R.id.fragment_age);
        mGender = (TextView) v.findViewById(R.id.fragment_gender);
        mCountry = (TextView) v.findViewById(R.id.fragment_country);
        mName.setText("Name: "+name);
        mAge.setText("Age: "+age);
        mCountry.setText("Country: "+country);
        mGender.setText("Gender: "+gender);
        builder.setTitle(userName);
        builder.setView(v);
        builder.setPositiveButton(R.string.fragment_dialog_ok_button,null);
        AlertDialog dialog = builder.create();
        return dialog;
    }
}

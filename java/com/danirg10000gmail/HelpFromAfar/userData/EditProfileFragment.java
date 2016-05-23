package com.danirg10000gmail.HelpFromAfar.userData;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.danirg10000gmail.HelpFromAfar.R;
import com.danirg10000gmail.HelpFromAfar.SingleFragment;
import com.danirg10000gmail.HelpFromAfar.dataBase.SharedPrefData;
import com.danirg10000gmail.HelpFromAfar.user.SingleUserM;
import com.danirg10000gmail.HelpFromAfar.user.UserM;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import static com.danirg10000gmail.HelpFromAfar.dataBase.ParseData.UserClass.NAME;
import static com.danirg10000gmail.HelpFromAfar.dataBase.ParseData.UserClass.UserClassCols;

public class EditProfileFragment extends SingleFragment implements TextWatcher {
    private static final String ASSET_PATH = "text_font.ttf";

    private TextView mGenderTitle, mCountryTitle;
    private EditText mName, mAge;
    private RadioGroup mGender;
    private RadioButton mMale, mFemale;
    private Button mDone;
    private String mCountryInput, mNameInput;
    private int mAgeInput;
    private Spinner mCountrySpinner;
    private boolean mGenInput, mValid = true;
    private SingleUserM mSingleUser;
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;
    private UserM mUser;

    public static EditProfileFragment newInstance() {
        return new EditProfileFragment();
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
        mSingleUser = SingleUserM.getSingleUser();
        mUser = mSingleUser.getUser();
        mPref = getActivity().getSharedPreferences(mUser.getName(), Context.MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), ASSET_PATH);
        mName = (EditText) v.findViewById(R.id.edit_profile_name);
        mAge = (EditText) v.findViewById(R.id.edit_profile_age);
        mCountrySpinner = (Spinner) v.findViewById(R.id.edit_profile_country_spinner);
        mGender = (RadioGroup) v.findViewById(R.id.edit_profile_pick_gender);
        mGenderTitle = (TextView) v.findViewById(R.id.edit_profile_gender);
        mMale = (RadioButton) v.findViewById(R.id.edit_profile_male);
        mFemale = (RadioButton) v.findViewById(R.id.edit_profile_female);
        mCountryTitle = (TextView) v.findViewById(R.id.edit_profile_country);
        mDone = (Button) v.findViewById(R.id.edit_profile_button_confirm);
        mName.setTypeface(tf);
        mGenderTitle.setTypeface(tf);
        mCountryTitle.setTypeface(tf);
        mAge.setTypeface(tf);
        ArrayAdapter<CharSequence> countryAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.country, R.layout.edit_profile_spinner_item);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCountrySpinner.setAdapter(countryAdapter);
        mCountrySpinner.setSelection(0);

        if (mUser.getPersonalName() != null) {
            mName.setText(mUser.getPersonalName());
            mAge.setText(String.valueOf(mUser.getAge()));
            if (mUser.getCountry().equals("Israel")) {
                mCountrySpinner.setSelection(1);
            } else {
                mCountrySpinner.setSelection(0);
            }
            mGenInput = mUser.getGender();
            if (mGenInput) {
                mMale.setChecked(true);
            } else {
                mFemale.setChecked(true);
            }
        }
        mName.addTextChangedListener(this);
        mAge.addTextChangedListener(this);
        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mValid) {
                    int id = mGender.getCheckedRadioButtonId();
                    if (id == R.id.edit_profile_male) {
                        mGenInput = true;
                    } else {
                        mGenInput = false;
                    }
                    send();
                    Intent intent = ProfileActivity.newIntent(getActivity());
                    mCountryInput = mCountrySpinner.getSelectedItem().toString();
                    mUser.setPersonalData(mNameInput, mAgeInput, mGenInput, mCountryInput);
                    mEditor = mPref.edit();
                    mEditor.putString(SharedPrefData.NAME, mNameInput);
                    mEditor.putBoolean(SharedPrefData.GENDER, mGenInput);
                    mEditor.putInt(SharedPrefData.AGE, mAgeInput);
                    mEditor.putString(SharedPrefData.COUNTRY, mCountryInput);
                    mEditor.commit();
                    getActivity().setResult(Activity.RESULT_OK, intent);
                    //clean up -> destroy this activity vs fragment
                    EditProfileFragment.this.getActivity().finish();
                }
            }
        });
        return v;
    }

    public void send() {
        final String id = ParseUser.getCurrentUser().getObjectId();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(NAME);
        mAgeInput = Integer.valueOf(mAge.getText().toString());
        mCountryInput = mCountrySpinner.getSelectedItem().toString();
        mNameInput = mName.getText().toString();
        // Retrieve the object by id

        query.getInBackground(id, new GetCallback<ParseObject>() {
            public void done(ParseObject user, ParseException e) {
                if (e == null) {
                    user.put(UserClassCols.NAME, mNameInput);
                    user.put(UserClassCols.AGE, mAgeInput);
                    user.put(UserClassCols.COUNTRY, mCountryInput);
                    user.put(UserClassCols.GENDER, mGenInput);
                    user.saveEventually();
                } else {
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void afterTextChanged(Editable s) {
        mNameInput = mName.getText().toString();
        String age = mAge.getText().toString();
        mValid = checker(age, mNameInput);

    }

    private boolean checker(String ageChecker, String nameChecker) {
        mValid = true;
        String cName = ("^(\\w*)$");
        String cAge = ("^([1][8-9]|[2-7][0-9])$");
        if (nameChecker.length() < 2) {
            mName.setError("name too short");
            mValid = false;
        }
        if (!(nameChecker.matches(cName))) {
            mName.setError("name can contain only letters");
            mValid = false;
        }
        if (nameChecker.matches("^(((\\d+|(\\D+\\d+)+|(\\D+\\d+\\D+)+|(\\d+\\D+)+|(\\d+\\D+\\d+)+)+)$)")) {
            mName.setError("name can't contain numbers");
            mValid = false;
        }

        if (ageChecker.matches("^(\\D+|(\\d+\\D+)+|(\\d+\\D+\\d+)+)$")) {
            mAge.setError("age can't contain chars");
            mValid = false;
        }
        if (!(ageChecker.matches(cAge))) {
            mAge.setError("age must be 18-80");
            mValid = false;
        }
        if (ageChecker.matches("^([0]+[0-9]+)")) {
            mAge.setError("age can't start with 0");
            mValid = false;
        }
        return mValid;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

}

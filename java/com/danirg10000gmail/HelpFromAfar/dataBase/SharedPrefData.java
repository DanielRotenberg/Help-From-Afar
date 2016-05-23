package com.danirg10000gmail.HelpFromAfar.dataBase;

public class SharedPrefData {
    public static final String USER_NAME = "UserName";
    public static final String USER_PASSWORD = "UserPassword";
    public static final String EMAIL_VERIFIED = "Email_verified";
    public static final String GENDER = "Gender";
    public static final String AGE = "Age";
    public static final String COUNTRY = "Country";
    public static final String NAME = "Name";
    public static final String MALE = "Male";
    public static final String FEMALE = "Female";


    public static String genderChecker(Boolean g){
        if(g){
            return MALE;
        }else{
            return FEMALE;
        }
    }
}

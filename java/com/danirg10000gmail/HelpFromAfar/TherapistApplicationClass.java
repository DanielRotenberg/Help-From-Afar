package com.danirg10000gmail.HelpFromAfar;

import android.app.Application;

import com.danirg10000gmail.HelpFromAfar.dataBase.ParseData.AppData;
import com.parse.Parse;

public class TherapistApplicationClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, AppData.APP_ID, AppData.CLIENT_KEY);
    }
}

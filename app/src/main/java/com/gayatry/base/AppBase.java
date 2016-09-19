package com.gayatry.base;

import android.app.Application;

import com.gayatry.R;
import com.gayatry.storage.DatabaseHelper;
import com.gayatry.storage.DatabaseManager;
import com.gayatry.storage.SharedPreferenceUtil;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

/**
 * Created by Admin on 05-Apr-16.
 */
@ReportsCrashes(formKey = "", // will not be used
        mailTo = "reports@yourdomain.com", // my email here
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.crash_toast_text)
public class AppBase extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
       // ACRA.init(this);
        SharedPreferenceUtil.init(this);
        DatabaseManager.initializeInstance(new DatabaseHelper(this));
    }
}

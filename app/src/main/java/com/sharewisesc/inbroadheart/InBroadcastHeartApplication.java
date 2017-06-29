package com.sharewisesc.inbroadheart;

import android.app.ActivityManager;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.DisplayMetrics;

import com.sharewisesc.inbroadheart.crash.CrashHandler;
import com.tencent.qcloud.suixinbo.presenters.InitBusinessHelper;
import com.tencent.qcloud.suixinbo.utils.SxbLogImpl;

import org.litepal.LitePal;

import java.util.List;

/**
 * Created by wenlin on 2017/6/15.
 */

public class InBroadcastHeartApplication extends MultiDexApplication {
    public static InBroadcastHeartApplication instance;
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        if (instance == null) {
            instance = this;
        }
        if(mContext == null){
            mContext = getApplicationContext();
        }
        if (shouldInit()) {
            SxbLogImpl.init(mContext);

            //初始化APP
            InitBusinessHelper.initApp(mContext);
        }
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        IConstants.width = displayMetrics.widthPixels;
        IConstants.height = displayMetrics.heightPixels;
        IConstants.density = displayMetrics.density;

        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
        crashHandler.scaneCrashFile();

        LitePal.initialize(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();

        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    public static Context getContext() {
        return mContext;
    }

    public static InBroadcastHeartApplication getInstance() {
        return instance;
    }
}

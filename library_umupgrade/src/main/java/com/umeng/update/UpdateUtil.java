package com.umeng.update;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liumeng on 3/23/16.
 */
class UpdateUtil {
    private static final int NETTYPE_NO = 0;
    public static final int NETTYPE_WIFT = 1;
    private static final int NETTYPE_NOWIFT = 2;
    public static final String JSON_PATTERN = "\\/\\*\\*.*?\\*\\/";
    public static String getPatternStr(String ori, String pat, int a, int b) {
        if (TextUtils.isEmpty(ori))
            return null;
        Pattern atPeoplePtn = Pattern.compile(pat, Pattern.DOTALL);
        Matcher matcher = atPeoplePtn.matcher(ori);
        while (matcher.find()) {
            int i = matcher.start();
            int j = matcher.end();
            String id = ori.substring(i + a, j - b);
            return id;
        }
        return null;
    }

    public static int getCurrentNetType(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                NetworkInfo info = connectivityManager.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    if (info.getTypeName().equalsIgnoreCase("WIFI")) {
                        return NETTYPE_WIFT;
                    }
                    return NETTYPE_NOWIFT;
                }
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }
        return NETTYPE_NO;
    }

    public static int getVersionCode(Context con) {
        int version = 1;
        PackageManager packageManager = con.getPackageManager();
        try {
            PackageInfo e = packageManager.getPackageInfo(con.getPackageName(), 0);
            version = e.versionCode;
        } catch (Exception var4) {
            var4.printStackTrace();
        }
        return version;
    }

    public static void setLogTextViewFromNewLog(TextView localTextView,String log) {
        try {
            String json = UpdateUtil.getPatternStr(log, UpdateUtil.JSON_PATTERN, 0, 0);
            if(!TextUtils.isEmpty(json)){
                log = log.replace(json,"");
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }
        localTextView.setText(log);
    }

}

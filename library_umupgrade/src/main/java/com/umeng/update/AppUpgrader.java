package com.umeng.update;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;

/**
 * Created by liumeng on 3/4/16.
 */
public class AppUpgrader {
    static final String KEY_UPGRADE_TYPE = "KEY_UPGRADE_TYPE";
    static final String KEY_FORCE_UPGRADE = "KEY_FORCE_UPGRADE";
    private static final int NETTYPE_NO = 0;
    private static final int NETTYPE_WIFT = 1;
    private static final int NETTYPE_NOWIFT = 2;
    //0、不更新
    //1、wifi 下静默更新,弹窗可取消  +   非 wifi 弹窗 可取消
    //2、wifi 下静默更新,弹窗不可取消 +   非 wifi 弹窗 可取消
    //3、wifi 下静默更新,弹窗不可取消 +   非 wifi 弹窗 不可取消
    private static final int UPGRADETYPE_NO = 0;
    private static final int UPGRADETYPE_NORMAL = 1;
    static final int UPGRADETYPE_WIFT_FORCE = 2;
    private static final int UPGRADETYPE_ALL_FORCE = 3;

    public static void start(Application context, String umengKey, @Nullable String upgradeTypeStr, @Nullable String rule) {
        UmengUpdateAgent.setUpdateCheckConfig(false);
        UpdateConfig.setAppkey(umengKey);
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        SharedPre.getInstance(context).set(KEY_FORCE_UPGRADE, false);
        //处理 rules
        upgradeTypeStr = handleRule(context, upgradeTypeStr, rule);
        //处理 upgradeType
        int upgradeType = UPGRADETYPE_NORMAL;
        try {
            upgradeType = Integer.parseInt(upgradeTypeStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SharedPre.getInstance(context).set(KEY_UPGRADE_TYPE, upgradeType);
        //各种情况
        if (upgradeType == UPGRADETYPE_NO) {
            return;
        }
        if (getCurrentNetType(context) == NETTYPE_WIFT) {
            if (upgradeType != UPGRADETYPE_NORMAL) {
                SharedPre.getInstance(context).set(KEY_FORCE_UPGRADE, true);
            }
            UmengUpdateAgent.silentUpdate(context);
        } else {
            if (upgradeType == UPGRADETYPE_ALL_FORCE) {
                SharedPre.getInstance(context).set(KEY_FORCE_UPGRADE, true);
            }
            UmengUpdateAgent.update(context);
        }
    }

    /**
     * 实现伪灰度发布:即指定某个 versionCode 采用指定的 upgradeType
     * rule 的格式为versionCode:upgradeType#versionCode:upgradeType#versionCode:upgradeType
     *
     * @param upgradeTypeStr
     * @param rule
     * @return
     */
    private static String handleRule(Context context, String upgradeTypeStr, String rule) {
        try {
            String[] codeTypes = rule.split("#");
            for (String codeType : codeTypes) {
                if (getVersionCode(context) == Integer.parseInt(codeType.split(":")[0])) {
                    return codeType.split(":")[1];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return upgradeTypeStr;
    }

    public static void check(Context context, String umengKey) {
        SharedPre.getInstance(context).set(KEY_FORCE_UPGRADE, false);
        UmengUpdateAgent.setUpdateCheckConfig(false);
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.update(context);
    }

    private static int getCurrentNetType(Context context) {
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

    private static int getVersionCode(Context con) {
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
}

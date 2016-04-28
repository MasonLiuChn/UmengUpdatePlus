package com.umeng.update;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by liumeng on 3/4/16.
 */
public class AppUpgrader {
    static final String KEY_UPGRADE_TYPE = "KEY_UPGRADE_TYPE";
    static final String KEY_FORCE_UPGRADE = "KEY_FORCE_UPGRADE";
    static final int UPGRADETYPE_WIFT_FORCE = 2;
    //0、不更新
    //1、wifi 下静默更新,弹窗可取消  +   非 wifi 弹窗 可取消
    //2、wifi 下静默更新,弹窗不可取消 +   非 wifi 弹窗 可取消
    //3、wifi 下静默更新,弹窗不可取消 +   非 wifi 弹窗 不可取消
    private static final int UPGRADETYPE_NO = 0;
    private static final int UPGRADETYPE_NORMAL = 1;
    private static final int UPGRADETYPE_ALL_FORCE = 3;

    public static void start(Application context, String umengKey) {
        //init
        init(context, umengKey);
        //获取 upgradeType 和 rules
        getUpdateResponse(context);
    }

    private static void init(Application context, String umengKey) {
        UmengUpdateAgent.setUpdateCheckConfig(false);
        UpdateConfig.setAppkey(umengKey);
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.setDeltaUpdate(false);
        SharedPre.getInstance(context).set(KEY_FORCE_UPGRADE, false);
    }

    public static void getUpdateResponse(Context context) {
        new MyTask(context).execute();
    }

    private static void handleTypeAndRule(Context context, String upgradeTypeStr, String rule) {
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
        if (UpdateUtil.getCurrentNetType(context) == UpdateUtil.NETTYPE_WIFT) {
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
                if (UpdateUtil.getVersionCode(context) == Integer.parseInt(codeType.split(":")[0])) {
                    return codeType.split(":")[1];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return upgradeTypeStr;
    }

    public static void check(Application context, String umengKey) {
        init(context, umengKey);
        UmengUpdateAgent.update(context);
    }

    private static class MyTask extends AsyncTask<Void, Void, UpdateInfo> {
        private Context context;

        MyTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected UpdateInfo doInBackground(Void... params) {
            UpdateResponse var1 = (new com.umeng.update.b(context)).b();
            return UpdateInfo.parseUpdateInfoFromUmengLog(var1);
        }

        @Override
        protected void onPostExecute(UpdateInfo result) {
            if (result != null) {
                handleTypeAndRule(context, result.getUpdateType(), result.getUpdateRule());
            }
        }

    }

}

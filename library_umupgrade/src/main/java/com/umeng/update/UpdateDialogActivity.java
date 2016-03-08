package com.umeng.update;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.io.File;

import u.upd.a;
import u.upd.c;

public class UpdateDialogActivity
        extends Activity {
    int a2 = 6;
    UpdateResponse b;
    boolean c2 = false;
    File d = null;
    ViewGroup e;

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        try {
            requestWindowFeature(1);
            setContentView(c.a(this).d("umeng_update_dialog"));
            this.b = ((UpdateResponse) getIntent().getExtras().getSerializable("response"));
            String str1 = getIntent().getExtras().getString("file");
            boolean bool1 = getIntent().getExtras().getBoolean("force");
            boolean downloaded = str1 != null;
            if (downloaded) {
                this.d = new File(str1);
            }
            int contentResId = c.a(this).b("umeng_update_content");
            int wifiIndicatorResId = c.a(this).b("umeng_update_wifi_indicator");

            final int k = c.a(this).b("umeng_update_id_ok");
            int cancelBtnResId = c.a(this).b("umeng_update_id_cancel");
            final int n = c.a(this).b("umeng_update_id_ignore");
            int i1 = c.a(this).b("umeng_update_id_close");
            int i2 = c.a(this).b("umeng_update_id_check");

            View.OnClickListener local1 = new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    if (k == paramAnonymousView.getId()) {
                        UpdateDialogActivity.this.a2 = 5;
                    } else if (n == paramAnonymousView.getId()) {
                        UpdateDialogActivity.this.a2 = 7;
                    } else if (UpdateDialogActivity.this.c2) {
                        UpdateDialogActivity.this.a2 = 7;
                    }
                    UpdateDialogActivity.this.finish();
                }
            };
            CompoundButton.OnCheckedChangeListener local2 = new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton paramAnonymousCompoundButton, boolean paramAnonymousBoolean) {
                    UpdateDialogActivity.this.c2 = paramAnonymousBoolean;
                }
            };
            if (wifiIndicatorResId > 0) {
                int i3 = a.k(this) ? View.GONE : View.VISIBLE;

                findViewById(wifiIndicatorResId).setVisibility(i3);
            }
            if (bool1) {
                findViewById(i2).setVisibility(View.GONE);
            }
            findViewById(k).setOnClickListener(local1);
            findViewById(cancelBtnResId).setOnClickListener(local1);
            findViewById(n).setOnClickListener(local1);
            findViewById(i1).setOnClickListener(local1);
            ((CheckBox) findViewById(i2)).setOnCheckedChangeListener(local2);

            String str2 = this.b.a(this, downloaded);
            TextView localTextView = (TextView) findViewById(contentResId);
            localTextView.requestFocus();
            localTextView.setText(str2);
            //以下是自定义的代码
            if (SharedPre.getInstance(this).getBoolean(AppUpgrader.KEY_FORCE_UPGRADE)) {
                this.findViewById(cancelBtnResId).setVisibility(View.GONE);
            } else {
                //如果已经下载过了就不再显示网络信号了
                if (downloaded) {
                    findViewById(wifiIndicatorResId).setVisibility(View.GONE);
                    //如果已经下载过了 并且 wifi 下强制升级 则把取消按钮隐藏掉
                    if (SharedPre.getInstance(this).getInt(AppUpgrader.KEY_UPGRADE_TYPE) == AppUpgrader.UPGRADETYPE_WIFT_FORCE) {
                        findViewById(cancelBtnResId).setVisibility(View.GONE);
                    }
                }
            }
        } catch (Exception localException) {
            Log.d("UpdateDialogActivity", localException.toString());
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        UmengUpdateAgent.a(this.a2, this, this.b, this.d);
    }

    @Override
    public void onBackPressed() {

    }
}
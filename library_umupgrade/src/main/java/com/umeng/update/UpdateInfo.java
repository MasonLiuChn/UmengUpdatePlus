package com.umeng.update;

import android.text.TextUtils;

import org.json.JSONObject;

/**
 * Created by liumeng on 3/23/16.
 */
public class UpdateInfo {
    private String updateType;
    private String updateRule;

    /**
     * 返回的 result 中会有如下结构的 json
     * {
     * "updateType":1,
     * "updateRule":"1:2#2:1"
     * }
     *
     * @param updateResponse
     */
    public static UpdateInfo parseUpdateInfoFromUmengLog(UpdateResponse updateResponse) {
        try {
            String json = UpdateUtil.getPatternStr(updateResponse.updateLog, UpdateUtil.JSON_PATTERN, 3, 2).replaceAll("\\s", "");
            JSONObject jsonObject = new JSONObject(json);
            String type = jsonObject.getString("updateType");
            String rule = jsonObject.getString("updateRule");
            UpdateInfo info = new UpdateInfo();
            info.setUpdateType(type);
            info.setUpdateRule(rule);
            return info;
        } catch (Exception e) {
            e.printStackTrace();
        }
        //如果 updatelog 里没有 json，则使用友盟默认的升级策略
        if (updateResponse != null && !TextUtils.isEmpty(updateResponse.updateLog)) {
            UpdateInfo info = new UpdateInfo();
            info.setUpdateType("1");
            info.setUpdateRule("");
            return info;
        }
        return null;
    }

    public String getUpdateType() {
        return updateType;
    }

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }

    public String getUpdateRule() {
        return updateRule;
    }

    public void setUpdateRule(String updateRule) {
        this.updateRule = updateRule;
    }

}

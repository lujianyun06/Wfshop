package com.tobyli16.profilelibrary.personal.sign;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.bupt.wfshop.util.storage.WangfuPreference;

/**
 * Created by tobyli on 2017/4/22
 */

public class SignHandler {
    private static final String nickName = "nickname";
    private static final String headImgUrl = "headimgurl";
    private static final String openId = "openid";
    private static final String unionId = "unionid";

    public static void onSignIn(String response, ISignListener signListener) {

        JSONObject jsonObject = JSONObject.parseObject(response);
        String nickname = jsonObject.getString(nickName);
        String headimgurl = jsonObject.getString(headImgUrl);
        String openid = jsonObject.getString(openId);
        String unionid = jsonObject.getString(unionId);

        WangfuPreference.addCustomAppProfile(nickName,nickname);
        WangfuPreference.addCustomAppProfile(headImgUrl,headimgurl);
        WangfuPreference.addCustomAppProfile(openId,openid);
        WangfuPreference.addCustomAppProfile(unionId,unionid);

        //已经注册并登录成功了
        AccountManager.setSignState(true);
        signListener.onSignInSuccess();
    }


    public static void onSignUp(String response, ISignListener signListener) {
        final JSONObject profileJson = JSON.parseObject(response).getJSONObject("data");
        final long userId = profileJson.getLong("userId");
        final String name = profileJson.getString("name");
        final String avatar = profileJson.getString("avatar");
        final String gender = profileJson.getString("gender");
        final String address = profileJson.getString("address");

        //已经注册并登录成功了
        AccountManager.setSignState(true);
        signListener.onSignUpSuccess();
    }

    public static void onSignOut(ISignListener signListener){
        WangfuPreference.removeCustomAppProfile(nickName);
        WangfuPreference.removeCustomAppProfile(headImgUrl);
        WangfuPreference.removeCustomAppProfile(openId);
        WangfuPreference.removeCustomAppProfile(unionId);
        AccountManager.setSignState(false);
        signListener.onSignOutSuccess();
        //TODO 需要告诉视图，已经退出登录，并且清空头像
    }
}

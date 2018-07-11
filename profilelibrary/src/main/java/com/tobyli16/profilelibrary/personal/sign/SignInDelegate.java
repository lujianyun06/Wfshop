package com.tobyli16.profilelibrary.personal.sign;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.tobyli16.profilelibrary.R;

import cn.bupt.wfshop.delegates.WangfuDelegate;
import cn.bupt.wfshop.util.storage.WangfuPreference;
import cn.bupt.wfshop.wechat.WangfuWeChat;
import cn.bupt.wfshop.wechat.callbacks.IWeChatSignInCallback;


/**
 * Created by tobyli on 2017/4/22
 */

public class SignInDelegate extends WangfuDelegate implements View.OnClickListener {

    private ISignListener mISignListener = null;

    public void setmISignListener(ISignListener mISignListener) {
        this.mISignListener = mISignListener;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ISignListener) {
            mISignListener = (ISignListener) activity;
        }
    }

    public static SignInDelegate create(ISignListener iSignListener) {
        SignInDelegate signInDelegate = new SignInDelegate();
        signInDelegate.setmISignListener(iSignListener);
        return signInDelegate;
    }


    private void onClickWeChat() {
        WangfuWeChat
                .getInstance()
                .onSignSuccess(new IWeChatSignInCallback() {
                    @Override
                    public void onSignInSuccess(String userInfo) {
                        Toast.makeText(getContext(), userInfo, Toast.LENGTH_LONG).show();
                        JSONObject jsonObject = JSONObject.parseObject(userInfo);
                        String nickname = jsonObject.getString("nickname");
                        String headimgurl = jsonObject.getString("headimgurl");
                        String openid = jsonObject.getString("openid");
                        String unionid = jsonObject.getString("unionid");
                        WangfuPreference.addCustomAppProfile("nickname",nickname);
                        WangfuPreference.addCustomAppProfile("headimgurl",headimgurl);
                        WangfuPreference.addCustomAppProfile("openid",openid);
                        WangfuPreference.addCustomAppProfile("unionid",unionid);
                        mISignListener.onSignInSuccess();
                        getSupportDelegate().pop();
                    }
                })
                .signIn();
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_sign_in;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        $(R.id.signin_back).setOnClickListener(this);
        $(R.id.icon_sign_in_wechat).setOnClickListener(this);
//        final ISupportFragment personalDelegate =
//                SupportHelper.getTopFragment(this.getFragmentManager());
//        mISignListener = (ISignListener) personalDelegate;
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.signin_back) {
            getSupportDelegate().pop();
        }
        else if (i == R.id.icon_sign_in_wechat) {
            onClickWeChat();
        }
    }
}

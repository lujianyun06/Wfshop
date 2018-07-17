package cn.bupt.wfshop.wechat;

import android.app.Activity;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import cn.bupt.wfshop.app.ConfigKeys;
import cn.bupt.wfshop.app.Wangfu;
import cn.bupt.wfshop.wechat.callbacks.IWeChatPayCallback;
import cn.bupt.wfshop.wechat.callbacks.IWeChatSignInCallback;

/**
 * Created by tobyli on 2017/4/25
 */

public class WangfuWeChat {
    public static final String APP_ID = Wangfu.getConfiguration(ConfigKeys.WE_CHAT_APP_ID);
    public static final String APP_SECRET = Wangfu.getConfiguration(ConfigKeys.WE_CHAT_APP_SECRET);
    private final IWXAPI WXAPI;
    private IWeChatSignInCallback mSignInCallback = null;
    private IWeChatPayCallback mPayCallback = null;

    private static final class Holder {
        private static final WangfuWeChat INSTANCE = new WangfuWeChat();
    }

    public static WangfuWeChat getInstance() {
        return Holder.INSTANCE;
    }

    private WangfuWeChat() {
        final Activity activity = Wangfu.getConfiguration(ConfigKeys.ACTIVITY);
        WXAPI = WXAPIFactory.createWXAPI(activity, APP_ID, true);
        WXAPI.registerApp(APP_ID);
    }

    public final IWXAPI getWXAPI() {
        return WXAPI;
    }

    public WangfuWeChat onSignSuccess(IWeChatSignInCallback callback) {
        this.mSignInCallback = callback;
        return this;
    }

    public IWeChatSignInCallback getSignInCallback() {
        return mSignInCallback;
    }

    public WangfuWeChat onPayCallback(IWeChatPayCallback iWeChatPayCallback) {
        this.mPayCallback = iWeChatPayCallback;
        return this;
    }

    public IWeChatPayCallback getPayCallback() {
        return mPayCallback;
    }

    public final void signIn() {
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "random_state";
        WXAPI.sendReq(req);
    }

    public final void startPay(PayReq payReq) {
        WXAPI.sendReq(payReq);
    }

}

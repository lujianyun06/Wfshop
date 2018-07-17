package cn.bupt.wfshop.wechat.templates;


import cn.bupt.wfshop.wechat.BaseWXEntryActivity;
import cn.bupt.wfshop.wechat.WangfuWeChat;

public class WXEntryTemplate extends BaseWXEntryActivity {

    @Override
    protected void onResume() {
        super.onResume();
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onSignInSuccess(String userInfo) {
        WangfuWeChat.getInstance().getSignInCallback().onSignInSuccess(userInfo);
    }
}

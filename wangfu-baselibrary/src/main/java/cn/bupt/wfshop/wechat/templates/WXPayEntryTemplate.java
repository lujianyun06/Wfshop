package cn.bupt.wfshop.wechat.templates;

import android.widget.Toast;

import com.tencent.mm.opensdk.modelbase.BaseReq;

import cn.bupt.wfshop.wechat.BaseWXPayEntryActivity;
import cn.bupt.wfshop.wechat.WangfuWeChat;

/**
 * Created by tobyli on 2017/1/2
 */

public class WXPayEntryTemplate extends BaseWXPayEntryActivity {

    @Override
    protected void onPaySuccess() {
        Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
        finish();
        overridePendingTransition(0, 0);  //activity切换的动画
        WangfuWeChat.getInstance().getPayCallback().onPaySuccess();  //这个onPaySuccess函数是在ConfirmOrderDelegate的232行
    }

    @Override
    protected void onPayFail() {
        Toast.makeText(this, "支付失败", Toast.LENGTH_SHORT).show();
        finish();
        overridePendingTransition(0, 0);
        WangfuWeChat.getInstance().getPayCallback().onPayFailure();
    }

    @Override
    protected void onPayCancel() {
        Toast.makeText(this, "支付取消", Toast.LENGTH_SHORT).show();
        finish();
        overridePendingTransition(0, 0);
        WangfuWeChat.getInstance().getPayCallback().onPayCancel();
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }
}

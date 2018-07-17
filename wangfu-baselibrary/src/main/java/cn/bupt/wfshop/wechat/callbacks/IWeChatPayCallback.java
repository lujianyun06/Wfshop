package cn.bupt.wfshop.wechat.callbacks;

/**
 * Created by Toby on 2018/1/18 0018.
 */

public interface IWeChatPayCallback {
    void onPaySuccess();
    void onPayFailure();
    void onPayCancel();
}

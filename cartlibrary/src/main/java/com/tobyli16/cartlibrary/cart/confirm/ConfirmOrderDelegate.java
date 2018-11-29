package com.tobyli16.cartlibrary.cart.confirm;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tobyli16.cartlibrary.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.bupt.wfshop.pay.PayEvent;
import cn.bupt.wfshop.pay.alipay.AliPayManager;
import cn.bupt.wfshop.pay.alipay.OrderInfoUtil2_0;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.bupt.wfshop.app.ConfigKeys;
import cn.bupt.wfshop.app.Wangfu;
import cn.bupt.wfshop.delegates.WangfuDelegate;
import cn.bupt.wfshop.net.RestClient;
import cn.bupt.wfshop.net.callback.ISuccess;
import cn.bupt.wfshop.pay.alipay.PayResult;
import cn.bupt.wfshop.ui.loader.WangfuLoader;
import cn.bupt.wfshop.util.log.WangfuLogger;
import cn.bupt.wfshop.wechat.WangfuWeChat;
import cn.bupt.wfshop.wechat.callbacks.IWeChatPayCallback;


/**
 * Created by Toby on 2018/1/10 0010.
 */

@Route(path = "/profileOrderConfirm/profileOrderConfirm/")
public class ConfirmOrderDelegate extends WangfuDelegate implements View.OnClickListener  {

    private List<TextView> checks;
    public static int payWay = 1;
    private TextView rechargeNum = null;
    private Activity activity;

    @Autowired
    String orderId;

    @Autowired
    String cost;

    @Override
    public Object setLayout() {
        return R.layout.delegate_confirm_order_new;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        activity = getProxyActivity();
        ARouter.getInstance().inject(this);  //获得传递的参数
        $(R.id.tv_create_order_pay).setOnClickListener(this);
        $(R.id.dialog_zhifubao).setOnClickListener(this);
        $(R.id.dialog_wechat).setOnClickListener(this);
        checks = new ArrayList<>();
        rechargeNum = $(R.id.tv_create_order_total_price);
        rechargeNum.setText(cost+"元");
        TextView textview0 = $(R.id.recharge_zhifubao_cb);  //这是选择支付方式的勾勾
        TextView textView1 = $(R.id.recharge_wechat_cb);
        checks.add(textview0);
        checks.add(textView1);
        checks.get(0).setVisibility(View.VISIBLE);
        payWay = 0;
        //测试数据
//        cost = "0.01";
    }

    @Override
    public void onClick(View view) {

        int viewId = view.getId();
        if (viewId == R.id.dialog_zhifubao) {
            checkChanges(0);
        } else if (viewId == R.id.dialog_wechat) {
            checkChanges(1);
        } else if (viewId == R.id.tv_create_order_pay) {

            setOrderCreateClickable(false);

            int type = 0;
            for (int i = 0; i < 2; i++) {
                if (checks.get(i).getVisibility()==View.VISIBLE) {
                    type = i;
                }
            }
            if (type == 0) {

                //支付宝支付
                AliPayManager.getInstance().pay(getProxyActivity(), orderId, cost, "网服商城");
            } else if (type == 1) {
                wechatPay(orderId);         //微信付款
            }
        }
    }

    /**
     * 改变选中
     */
    private void checkChanges(int index) {
        for (int i = 0; i < 2; i++) {
            if (i != index) {
                checks.get(i).setVisibility(View.GONE);
            }
        }
        payWay = index;
        checks.get(index).setVisibility(View.VISIBLE);
    }

    private void setOrderCreateClickable(boolean clickable){
        if (clickable){
            $(R.id.tv_create_order_pay).setOnClickListener(this);
        } else {
            $(R.id.tv_create_order_pay).setOnClickListener(null);
        }
    }

    //获得支付结果
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPayEvent(PayEvent event){
        if (event == null) return;
        setOrderCreateClickable(true);
        if (event.tag == PayEvent.ALIPAY){
            handleAliPayResult(event);
        }
        else if (event.tag == PayEvent.WECHATPAY){

        }
    }

    private void handleAliPayResult(PayEvent event){
        int resultStatus = event.status;

        switch (resultStatus) {
            case PayEvent.PAY_SUCCESS:
                Toast.makeText(getContext(), "支付成功", Toast.LENGTH_SHORT).show();
                getSupportDelegate().startWithPop(new PaySuccessDelegate());
                //支付成功后还要做一次网络请求？此项举动看支付宝的支付流程，在支付宝调用完成后还需要把数据返给app服务端
                //TODO 这里没有写完，必须要处理服务器返回的数据
                RestClient.builder()
                        .url("https://wfshop.andysheng.cn/order/" + orderId + "/paid")
                        .build()
                        .post();
                break;
            case PayEvent.PAY_FAIL:
                Toast.makeText(getContext(), "支付失败", Toast.LENGTH_SHORT).show();
                getSupportDelegate().start(new PayFailDelegate());
                break;
            case PayEvent.PAY_PAYING:
                break;
            case PayEvent.PAY_CANCEL:
                Toast.makeText(getContext(), "支付取消", Toast.LENGTH_SHORT).show();
                getSupportDelegate().start(new PayFailDelegate());
                break;
            case PayEvent.PAY_CONNECT_ERROR:
                break;
            default:
                break;
        }
    }

    //支付宝支付
//    class AliPaymentTask extends AsyncTask<String, Void, String> {
//
//        //订单支付成功
//        private static final String AL_PAY_STATUS_SUCCESS = "9000";
//        //订单处理中
//        private static final String AL_PAY_STATUS_PAYING = "8000";
//        //订单支付失败
//        private static final String AL_PAY_STATUS_FAIL = "4000";
//        //用户取消
//        private static final String AL_PAY_STATUS_CANCEL = "6001";
//        //支付网络错误
//        private static final String AL_PAY_STATUS_CONNECT_ERROR = "6002";
//
//        @Override
//        protected void onPreExecute() {
//            $(R.id.tv_create_order_pay).setOnClickListener(null);   //处理的时候屏蔽订单按钮
//        }
//
//        @Override
//        protected String doInBackground(String... param) {
//            boolean rsa2 = (AliPayManager.getInstance().RSA2_PRIVATE.length() > 0);
//            String amount = param[0];
//            String subject = param[1];
//            OrderInfoUtil2_0.amount = amount;   //价格
//            OrderInfoUtil2_0.subject = subject;  //商品名称
//
//            Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(AliPayManager.getInstance().APP_ID, rsa2);
//            String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
//
//            String privateKey = AliPayManager.getInstance().RSA2_PRIVATE;
//            String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
//            String orderInfo = orderParam + "&" + sign;
//
//            PayTask payTask = new PayTask(getActivity());
//
//
//            PayTask alipay = new PayTask(activity);
//            Map<String, String>  result = alipay.payV2(orderInfo,true);
//
//            Message msg = new Message();
//            msg.what = AliPayManager.getInstance().SDK_PAY_FLAG;
//            msg.obj = result;
//            mHandler.sendMessage(msg);
//            return payTask.pay(orderInfo, true);
//        }
//
//        /**
//         * 获得服务端的charge，调用ping++ sdk。处理支付结果
//         * 要请求服务器，验证支付是否成功。都成功，则此次支付成功，有一方未成功，则支付失败。
//         */
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            $(R.id.tv_create_order_pay).setOnClickListener(ConfirmOrderDelegate.this);
//            final PayResult payResult = new PayResult(result);
//            // 支付宝返回此次支付结构及加签，建议对支付宝签名信息拿签约是支付宝提供的公钥做验签
//            final String resultInfo = payResult.getResult();
//            final String resultStatus = payResult.getResultStatus();
//            WangfuLogger.d("AL_PAY_RESULT", resultInfo);
//            WangfuLogger.d("AL_PAY_RESULT", resultStatus);
//
//            switch (resultStatus) {
//                case AL_PAY_STATUS_SUCCESS:
//                    Toast.makeText(getContext(),"支付成功",Toast.LENGTH_SHORT).show();
//                    getSupportDelegate().startWithPop(new PaySuccessDelegate());
//                    //支付成功后还要做一次网络请求？此项举动看支付宝的支付流程，在支付宝调用完成后还需要把数据返给app服务端
//                    //TODO 这里没有写完，必须要处理服务器返回的数据
//                    RestClient.builder()
//                            .url("https://wfshop.andysheng.cn/order/"+orderId+"/paid")
//                            .build()
//                            .post();
//                    break;
//                case AL_PAY_STATUS_FAIL:
//                    Toast.makeText(getContext(),"支付失败",Toast.LENGTH_SHORT).show();
//                    getSupportDelegate().start(new PayFailDelegate());
//                    break;
//                case AL_PAY_STATUS_PAYING:
//                    break;
//                case AL_PAY_STATUS_CANCEL:
//                    Toast.makeText(getContext(),"支付取消",Toast.LENGTH_SHORT).show();
//                    getSupportDelegate().start(new PayFailDelegate());
//                    break;
//                case AL_PAY_STATUS_CONNECT_ERROR:
//                    break;
//                default:
//                    break;
//            }
//        }
//    }

    /*
    微信支付顺序：
    步骤1：用户进入商户APP，选择商品下单、确认购买，进入支付环节。商户服务后台生成支付订单，签名后将数据传输到APP端。以微信提供的DEMO为例。
    步骤2：用户点击后发起支付操作，进入到微信界面，调起微信支付，出现确认支付界面。
    步骤3：用户确认收款方和金额，点击立即支付后出现输入密码界面，可选择零钱或银行卡支付。
    第四步：输入正确密码后，支付完成，用户端微信出现支付详情页面。
    第五步：回跳到商户APP中，商户APP根据支付结果个性化展示订单处理结果。
     */
    private void wechatPay(String orderId) {
        final String weChatPrePayUrl = "http://tobyli16.com:8080/pay/wechat/" + orderId;
        WangfuLogger.d("WX_PAY", weChatPrePayUrl);

        WangfuLoader.showLoading(getContext());
        final String appId = Wangfu.getConfiguration(ConfigKeys.WE_CHAT_APP_ID);
        RestClient.builder()  //向服务器发送数据，使得服务器生成订单
                .params("amount","1")
                .params("body","商品测试")
                .params("callbackUrl","https://wfshop.andysheng.cn/payment/wechat_callback")
                .url(weChatPrePayUrl)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        $(R.id.tv_create_order_pay).setOnClickListener(ConfirmOrderDelegate.this);
                        WangfuLoader.stopLoading();  //服务器订单回传到app,此时服务器已经获得从微信服务器获得的预订单信息了
                        final JSONObject result =
                                JSON.parseObject(response).getJSONObject("result");
                        final String prepayId = result.getString("prepayid");
                        final String partnerId = result.getString("partnerid");
                        final String packageValue = result.getString("package");
                        final String timestamp = result.getString("timestamp");
                        final String nonceStr = result.getString("noncestr");
                        final String paySign = result.getString("sign");

                        final PayReq payReq = new PayReq();  //构建要发给微信的请求体
                        payReq.appId = appId;
                        payReq.prepayId = prepayId;
                        payReq.partnerId = partnerId;
                        payReq.packageValue = packageValue;
                        payReq.timeStamp = timestamp;
                        payReq.nonceStr = nonceStr;
                        payReq.sign = paySign;
                        WangfuWeChat.getInstance()
                                .onPayCallback(new IWeChatPayCallback() {
                                    @Override
                                    public void onPaySuccess() {
                                        getSupportDelegate().startWithPop(new PaySuccessDelegate());
                                    }

                                    @Override
                                    public void onPayFailure() {
                                        getSupportDelegate().startWithPop(new PayFailDelegate());
                                    }

                                    @Override
                                    public void onPayCancel() {
                                        getSupportDelegate().startWithPop(new PayFailDelegate());
                                    }
                                })
                                .startPay(payReq); //向微信发送请求信息
                    }
                })
                .build()
                .post();
    }
}

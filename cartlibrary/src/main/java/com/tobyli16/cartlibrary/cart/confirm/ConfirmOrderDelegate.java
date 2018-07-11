package com.tobyli16.cartlibrary.cart.confirm;

import android.os.AsyncTask;
import android.os.Bundle;
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
import com.tobyli16.cartlibrary.cart.confirm.alipaytest.util.OrderInfoUtil2_0;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.bupt.wfshop.app.ConfigKeys;
import cn.bupt.wfshop.app.Wangfu;
import cn.bupt.wfshop.delegates.WangfuDelegate;
import cn.bupt.wfshop.net.RestClient;
import cn.bupt.wfshop.net.callback.ISuccess;
import cn.bupt.wfshop.ui.loader.WangfuLoader;
import cn.bupt.wfshop.util.log.WangfuLogger;
import cn.bupt.wfshop.wechat.WangfuWeChat;
import cn.bupt.wfshop.wechat.callbacks.IWeChatPayCallback;

import static com.tobyli16.cartlibrary.cart.confirm.alipaytest.PayDemoActivity.APPID;
import static com.tobyli16.cartlibrary.cart.confirm.alipaytest.PayDemoActivity.RSA2_PRIVATE;

/**
 * Created by Toby on 2018/1/10 0010.
 */

@Route(path = "/profileOrderConfirm/profileOrderConfirm/")
public class ConfirmOrderDelegate extends WangfuDelegate implements View.OnClickListener  {

    private List<TextView> checks;
    public static int payWay = 1;
    private TextView rechargeNum = null;

    @Autowired
    String orderId;

    @Autowired
    String cost;

    @Override
    public Object setLayout() {
        return R.layout.delegate_confirm_order_new;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
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
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.dialog_zhifubao) {
            checkChanges(0);
        } else if (viewId == R.id.dialog_wechat) {
            checkChanges(1);
        } else if (viewId == R.id.tv_create_order_pay) {
            int type = 0;
            for (int i = 0; i < 2; i++) {
                if (checks.get(i).getVisibility()==View.VISIBLE) {
                    type = i;
                }
            }
            if (type == 0) {
                new AliPaymentTask().execute(cost,"网服商城");
            } else if (type == 1) {
                wechatPay(orderId);
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

    class AliPaymentTask extends AsyncTask<String, Void, String> {

        //订单支付成功
        private static final String AL_PAY_STATUS_SUCCESS = "9000";
        //订单处理中
        private static final String AL_PAY_STATUS_PAYING = "8000";
        //订单支付失败
        private static final String AL_PAY_STATUS_FAIL = "4000";
        //用户取消
        private static final String AL_PAY_STATUS_CANCEL = "6001";
        //支付网络错误
        private static final String AL_PAY_STATUS_CONNECT_ERROR = "6002";

        @Override
        protected void onPreExecute() {
            $(R.id.tv_create_order_pay).setOnClickListener(null);
        }

        @Override
        protected String doInBackground(String... param) {
            boolean rsa2 = (RSA2_PRIVATE.length() > 0);
            String amount = param[0];
            String subject = param[1];
            OrderInfoUtil2_0.amount = amount;
            OrderInfoUtil2_0.subject = subject;

            Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2);
            String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

            String privateKey = RSA2_PRIVATE;
            String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
            String orderInfo = orderParam + "&" + sign;

            PayTask payTask = new PayTask(getActivity());
            return payTask.pay(orderInfo, true);
        }

        /**
         * 获得服务端的charge，调用ping++ sdk。
         */
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            $(R.id.tv_create_order_pay).setOnClickListener(ConfirmOrderDelegate.this);
            final PayResult payResult = new PayResult(result);
            // 支付宝返回此次支付结构及加签，建议对支付宝签名信息拿签约是支付宝提供的公钥做验签
            final String resultInfo = payResult.getResult();
            final String resultStatus = payResult.getResultStatus();
            WangfuLogger.d("AL_PAY_RESULT", resultInfo);
            WangfuLogger.d("AL_PAY_RESULT", resultStatus);

            switch (resultStatus) {
                case AL_PAY_STATUS_SUCCESS:
                    Toast.makeText(getContext(),"支付成功",Toast.LENGTH_SHORT).show();
                    getSupportDelegate().startWithPop(new PaySuccessDelegate());
                    //支付成功后还要做一次网络请求？
                    RestClient.builder()
                            .url("https://wfshop.andysheng.cn/order/"+orderId+"/paid")
                            .build()
                            .post();
                    break;
                case AL_PAY_STATUS_FAIL:
                    Toast.makeText(getContext(),"支付失败",Toast.LENGTH_SHORT).show();
                    getSupportDelegate().start(new PayFailDelegate());
                    break;
                case AL_PAY_STATUS_PAYING:
                    break;
                case AL_PAY_STATUS_CANCEL:
                    Toast.makeText(getContext(),"支付取消",Toast.LENGTH_SHORT).show();
                    getSupportDelegate().start(new PayFailDelegate());
                    break;
                case AL_PAY_STATUS_CONNECT_ERROR:
                    break;
                default:
                    break;
            }
        }
    }

    private void wechatPay(String orderId) {
        final String weChatPrePayUrl = "http://tobyli16.com:8080/pay/wechat/" + orderId;
        WangfuLogger.d("WX_PAY", weChatPrePayUrl);

        WangfuLoader.showLoading(getContext());
        final String appId = Wangfu.getConfiguration(ConfigKeys.WE_CHAT_APP_ID);
        RestClient.builder()
                .params("amount","1")
                .params("body","商品测试")
                .params("callbackUrl","https://wfshop.andysheng.cn/payment/wechat_callback")
                .url(weChatPrePayUrl)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        $(R.id.tv_create_order_pay).setOnClickListener(ConfirmOrderDelegate.this);
                        WangfuLoader.stopLoading();
                        final JSONObject result =
                                JSON.parseObject(response).getJSONObject("result");
                        final String prepayId = result.getString("prepayid");
                        final String partnerId = result.getString("partnerid");
                        final String packageValue = result.getString("package");
                        final String timestamp = result.getString("timestamp");
                        final String nonceStr = result.getString("noncestr");
                        final String paySign = result.getString("sign");

                        final PayReq payReq = new PayReq();
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
                                .startPay(payReq);
                    }
                })
                .build()
                .post();
    }
}

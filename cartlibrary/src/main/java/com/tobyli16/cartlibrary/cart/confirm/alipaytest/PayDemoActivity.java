package com.tobyli16.cartlibrary.cart.confirm.alipaytest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;

import java.util.Map;

import com.tobyli16.cartlibrary.cart.confirm.alipaytest.util.OrderInfoUtil2_0;

/**
 * 重要说明:
 * <p>
 * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
 * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
 * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
 */
public class PayDemoActivity extends FragmentActivity {

    public static final String APPID = "2016091200496204";

    public static final String RSA2_PRIVATE = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDAsmDoZn6vTh3u4g+feZlYsEMCVVLqVVeqWqLg6l2tvDeyCjxS5Rhac5f5S5XIruKH3enovUAViQxu5mDyWlZDsfKO4Xv2lP22rLal4ePtzrKhaA8ZXiXMA8rMUo4a3yk+Bq9fAf5ObHI3SXfGICzjdTps6Q3eipGLR3B+DowXsT8Wjb937upgu+GG1D5X+DMbG2aDnyibhCgLo1KCAwLboCP3vU7MhcKnbeUCJDvgMCS8t//RdiQvPAeU2jS2ve4Q2GYDxqKUx1Rg4B349UuJwUN7W2K+lF1zBMaccc1lL9TNwpeh4F4fuI+HFe9gTmGtmkII9lVkKqnTAXeu0t5BAgMBAAECggEBALx3OnTSmiEXRAt6KlGNer/VVRSZIwlyAtXo49kbIBhmOAr4G9vvmYwN9puqo4quEX1VPAuX3W+ypOhqhnNCi5CWIGhCfR3Exwi9j75aWrUiLm/S+MGU5V7ObrC8JaR+kzXJEAjaSEGnq2YLgwvSOFUXONeAcUIxQPx6GLoRvE8GJ/P9dT/hFdpDaih8oWbMkKTzouDybg/48mMHC80KUpYjppuyIizQrG1Bl7GSDwwNWRhm7rJwQSah7sh6jTzYX0VjTWaBeYIS79RIUZIW6mzJOHy2VBSpq0ER6xdQLApo58+2bNZTz6Mz5Khfjj0m74jf4F3HCuGaQAHNT2mCfIkCgYEA4GCfvrXt7A0EDcrs6pcs1tc4Pc36hwfAGUZ1QeOKjQLLrusAD5KFZbcZ9It8fxcnP/Khx/WMXbAVW0/qn0U2bRtv+EwRo9F8VS1ZfuNZc8C8UuPAO62O/XiXvBeic+dPmXlsYubTjt9o6IN2KXqTKwz9rJICyp6fzJ2ts6a6GS8CgYEA29q9XEjJxGDcYqri80dzyvAJ4mgoENx3a+6Cy716g8KE5WRxJeTAp9ciZ0LMs859Ipd9/auv7foH05VcALzS1f9TkcJZ6Rwde6NKRkA0oYJw12xLCkRBa1eEyb/BMJmZAQIeITCIE7PM2GKvidevIhVnNm10zCo5R1FPo6NQw48CgYAsdbjxlycP0PxupzkOuundoEO6q2fq/bQozfdKGQZJLrahcFtQup3IK+HuvPzsSfAzEabQ8mzf25Y/w7Qf3x0LvZYQQzJznXzfe97x76a38wbd/dcHfDqLKywVzzEXekwhgxfg+RAOjWJCQFpXmbwzmZ1iZoQ7FfdgGhZjNLRHbQKBgQCYFWV+bB+ex/5MBrg94ASaspez1ac5mrjFodU6yRTyBmJWKLgYpXgb4rc6xTvDeiiYAEG4c8ackaROWfitzfDu9dPCdbYqijde6qMbm5iMzSXv70qtjG/QqFcEFLC4RNhRmTxsax0GvA8M9J9HdDsDEdk3DlqITQEYc4fqw7O/wwKBgQDSz0kVK//fcVECtSxx7Eo7J4rwdQ+jV8RgCSbxD1JpitXyDcRxZ+o3vvoRK8AggVlXTCUQEvIvzd1flX5o68EDQZrL39aaaEFHk/90WMU8tex1mhocMAxUJvwy+np+VxYUkpkvhrh4zfZtfe9TlqvajQXGEsWZ0IK24gAQubT0Aw==";

    private static final int SDK_PAY_FLAG = 1;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(PayDemoActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(PayDemoActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
//        setContentView(R.layout.pay_main);
    }

    /**
     * 支付宝支付业务
     *
     * @param v
     */
    public void payV2(View v) {

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = RSA2_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(PayDemoActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * get the sdk version. 获取SDK版本号
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(this);
        String version = payTask.getVersion();
        Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
    }

    /**
     * 原生的H5（手机网页版支付切natvie支付） 【对应页面网页支付按钮】
     *
     * @param v
     */
    public void h5Pay(View v) {
        Intent intent = new Intent(this, H5PayDemoActivity.class);
        Bundle extras = new Bundle();
        /**
         * url 是要测试的网站，在 Demo App 中会使用 H5PayDemoActivity 内的 WebView 打开。
         *
         * 可以填写任一支持支付宝支付的网站（如淘宝或一号店），在网站中下订单并唤起支付宝；
         * 或者直接填写由支付宝文档提供的“网站 Demo”生成的订单地址
         * （如 https://mclient.alipay.com/h5Continue.htm?h5_route_token=303ff0894cd4dccf591b089761dexxxx）
         * 进行测试。
         *
         * H5PayDemoActivity 中的 MyWebViewClient.shouldOverrideUrlLoading() 实现了拦截 URL 唤起支付宝，
         * 可以参考它实现自定义的 URL 拦截逻辑。
         */
        String url = "http://m.taobao.com";
        extras.putString("url", url);
        intent.putExtras(extras);
        startActivity(intent);
    }

}

package cn.bupt.wfshop.pay.alipay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import cn.bupt.wfshop.net.RestClient;
import cn.bupt.wfshop.pay.PayEvent;
import cn.bupt.wfshop.util.log.WangfuLogger;

public class AliPayManager {

    final public String APP_ID = "2016091900550807";
    /**
     *  pkcs8 格式的商户私钥。
     *
     * 	如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个，如果两个都设置了，将优先
     * 	使用 RSA2_PRIVATE。RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议商户使用
     * 	RSA2_PRIVATE。
     *
     * 	建议使用支付宝提供的公私钥生成工具生成和获取 RSA2_PRIVATE。
     * 	工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1
     */
    public static final String RSA2_PRIVATE = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCips5kzcW9EarlurjQVhJreDWuIhW8I/M/1slMJfknnl2iEBpRujzVcK5oYH6rD3Z2AHVuVD0Dg3pC4ALe1vfJBRtPg6gsLVR/danFrzWm0i2dLWKzk3NbhXt8HfPg7aq1pFhnFYi6nIDKLKCqbDHja+qLiH/+hf0DFSuU1a3Ey/n40z/zK/FRdLN0LoySVwlFTDnmMf6+jEX54P0LIiMLHU45+eV4HEDy1/VbDdHmGMmQ5Dkm1RklNuz/AD/T8oMCPaZ/owwqkiz93tdwmsrsUS48nFUeZZDNLh6div7bLUt+p0o8ptHdVIxxVd0IJDmRkCxvneIJCer48G9AjqTpAgMBAAECggEAXHzhi+x+bl5ngS5F/SGUY8v9KkFzfoIqxbhKyqQlI2Qn9gmWDbcK9IuYgP2tyXOHdt4X6F66Ow8+LFzZBQHT8FiZCQPiUwyLqdasbR6OMYf+LAwsvdQwmnAyoYcesF403gjPw/44u3RWv5/U1FOlop4wxpsdvQtPrbsSBxXiM66oeMyVm7HxCmOATLR4Zz4xvl6S/63KRMpttCRyF/f8tbYHqsdeeyxUOdPQnhFivoBbuqY67sh10sqTkylluDIshj6Q+ZNVVdjmRn17DUY1gQicjbgAuDI+1L1m5MpZ2Pcz9aKuPj8npGacg6OJPFo4NW3bz1aGtxBWjlzKdVWO6QKBgQDceqN5/P4fGJ74RnQUYbXiTBXIR1kgg9junHQRutQhXS5Ft0daHW7Rc62mODbql4NXdGH+IjwFe/y4tYfwlPVZ2n+jBeiglLEQ2v2bIYFkxOXeo6n1B9bHOBMzrN8+qtqg0J9KDctKzJcBletRWxI+5FTPYTpEKXdV82hzQHUd/wKBgQC82ybQB8uqC/WZ36vmAh5ghcZdaJmSk/jikLLZczcSRz4srhCGTulJesvEfvk+4WtZAM+gJO4eERCA8GSWg2yGUSTwMtij0zj/dAnE2zqg5MivOBUh1cV9RNcFlC9IvvtuWIdVz/A/MwuL6cz3XRXLeL+J3WSwHyLRQzldIn0NFwKBgCFQyskRKJksUEE17C50QVarppL37QjTw4Dy1Tsknw9XlDlmo3qDfg5aS4AVeH7kXC1n4bLUG3u8q4iBrcP1zklAkg7hQrCZJ1RjsvSxEGtYsV9+HZ1wDM1XtNcXJTjD+ntYaul2wiRQW/UaYU+O0mf8erBHgdVw6p5rRHo94DSjAoGAEhFtby2vwH8Qaj5J9eFyAo4J4lnlh258u0BYvBEXzbnfKuskTZUzIIKNCCmVsvxiBsvbEVk3Js0/JPbayMOeGGOfAxP6QgItS9NQM/M9crMwuALHksse79MsNy5sL6uF5pK1r/IK7CD4E9S0dV4Efcj5mjkCLw9rh1XFITHxgNUCgYBjIJP3yv2SNbwobrgx0JjQcYZIiQn1I6Bgf0KdGidE7uPXU6t0H07ZYhIvyfDCLCRHvEbeN/Oh7G7R6cCCeMAw1YnHopDFm566uWIE8qzQ1+aOJDZToCceoNuJJ320bdqt0hiKj5PZ6nU9kVwScBovLp1mwP9qPzPap2Y6ifEsTg==";
    public static final String RSA_PRIVATE = "";

    final public String ALIPAY_NETGATE = "https://openapi.alipaydev.com/gateway.do";

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

    public final int SDK_PAY_FLAG = 1;

    private static AliPayManager aliPayManager = null;

    private AliPayManager() {

    }

    //单例
    public static AliPayManager getInstance() {
        if (aliPayManager == null) {
            aliPayManager = new AliPayManager();
        }
        return aliPayManager;
    }

    class OrderInfoBundle{
        public String name;
        public String cost;
        public OrderInfoBundle(String name, String cost){
            this.cost = cost;
            this.name = name;
        }
    }

    //这里的orderinfo在真实环境中必须是由服务器生成的
    public void pay(Activity activity, String...params) {
        OrderInfoBundle infoBundle = new OrderInfoBundle(params[1], params[0]);
//        AliPayThread payThread = new AliPayThread(activity, payInfo);
//        payThread.start();

        SimSign(activity, infoBundle);

    }

    //真实的付款线程,这里的orderinfo在真实环境中必须是由服务器生成的,
    // TODO 这里payinfo还没有和orderBundle做匹配，只是先试了一下模拟的环境
    class AliPayThread extends Thread {
        Activity activity = null;
        String payInfo = null;

        public AliPayThread(Activity activity, String payInfo) {
            this.activity = activity;
            this.payInfo = payInfo;
        }

        @Override
        public void run() {
            PayTask alipay = new PayTask(activity);
            Map<String, String> result = alipay.payV2(payInfo, true);
            Message msg = new Message();
            msg.what = AliPayManager.getInstance().SDK_PAY_FLAG;
            msg.obj = result;
            mHandler.sendMessage(msg);
        }
    }

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
                    // 判断resultStatus 为9000则代表支付成功
                    // 支付宝返回此次支付结构及加签，建议对支付宝签名信息拿签约是支付宝提供的公钥做验签
                    final String resultInfo = payResult.getResult();
                    final String resultStatus = payResult.getResultStatus(); // 同步返回需要验证的信息
                    WangfuLogger.d("AL_PAY_RESULT", resultInfo);
                    WangfuLogger.d("AL_PAY_RESULT", resultStatus);

                    switch (resultStatus) {
                        case AL_PAY_STATUS_SUCCESS:
                            EventBus.getDefault().post(new PayEvent(PayEvent.ALIPAY, PayEvent.PAY_SUCCESS, payResult));
                            break;
                        case AL_PAY_STATUS_FAIL:
                            EventBus.getDefault().post(new PayEvent(PayEvent.ALIPAY, PayEvent.PAY_FAIL, payResult));
                            break;
                        case AL_PAY_STATUS_PAYING:
                            break;
                        case AL_PAY_STATUS_CANCEL:
                            EventBus.getDefault().post(new PayEvent(PayEvent.ALIPAY, PayEvent.PAY_CANCEL, payResult));
                            break;
                        case AL_PAY_STATUS_CONNECT_ERROR:
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    };

    //整个函数都是模拟整个付款线程，后台签字必须由后台执行，这里为了方便模拟一下
    private void SimSign(final Activity activity, OrderInfoBundle infoBundle){
        /*
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * 真实环境中orderInfo 的获取必须来自服务端；
         */
//        OrderInfoUtil2_0.subject = infoBundle.name;
//        OrderInfoUtil2_0.amount = infoBundle.cost;

        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APP_ID, rsa2);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;

//        ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
//        cm.setText(orderInfo);
        Log.d("orderInfo", "内容已复制到剪贴板");
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
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


}

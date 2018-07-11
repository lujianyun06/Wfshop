package cn.bupt.wfshop.mainapp;

import android.app.Application;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import cn.bupt.wfshop.app.Wangfu;
import cn.bupt.wfshop.icon.FontEcModule;
import cn.bupt.wfshop.mainapp.event.ShareEvent;
import cn.bupt.wfshop.mainapp.event.TestEvent;
import cn.bupt.wfshop.net.interceptors.DebugInterceptor;
import cn.bupt.wfshop.util.callback.CallbackManager;
import cn.bupt.wfshop.util.callback.CallbackType;
import cn.bupt.wfshop.util.callback.IGlobalCallback;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by tobyli on 2017/3/29
 */
public class ExampleApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (true) {
            //开启InstantRun之后，一定要在ARouter.init之前调用openDebug
            ARouter.openDebug();
            ARouter.openLog();
        }
        ARouter.init(this);

        Wangfu.init(this)
                .withIcon(new FontAwesomeModule()) //添加字体库
                .withIcon(new FontEcModule()) //添加图标库
                .withLoaderDelayed(1000) //设置网络读取等待时间
                .withApiHost("http://tobyli16.com/") //设置服务器基本URL
                .withInterceptor(new DebugInterceptor("test", R.raw.test)) //设置拦截器
                .withWeChatAppId("wxe7798aead7625419") //设置微信AppID
                .withWeChatAppSecret("d8dfb107972ae477b58f9a600c989541") //设置微信AppSecret
                                .withJavascriptInterface("wangfu")
                                .withWebEvent("test", new TestEvent())
                                .withWebEvent("share", new ShareEvent())
                                //添加Cookie同步拦截器
                                .withWebHost("https://www.baidu.com/")
                .withApiField("productURL", "product") //设置API地址
                .withApiField("cartUrl", "cart") //设置API地址
                .withSpanCount(120) //设置首页布局一行的SpanSize的最大值，方便布局
                .configure();

        //开启极光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        CallbackManager.getInstance()
                .addCallback(CallbackType.TAG_OPEN_PUSH, new IGlobalCallback() {
                    @Override
                    public void executeCallback(@Nullable Object args) {
                        if (JPushInterface.isPushStopped(Wangfu.getApplicationContext())) {
                            //开启极光推送
                            JPushInterface.setDebugMode(true);
                            JPushInterface.init(Wangfu.getApplicationContext());
                        }
                    }
                })
                .addCallback(CallbackType.TAG_STOP_PUSH, new IGlobalCallback() {
                    @Override
                    public void executeCallback(@Nullable Object args) {
                        if (!JPushInterface.isPushStopped(Wangfu.getApplicationContext())) {
                            JPushInterface.stopPush(Wangfu.getApplicationContext());
                        }
                    }
                });
//        RestClient.builder()
//                .url("http://admin.swczyc.com/hyapi/ymmall/login/submit")
//                .params("wechat_id",3)
//                .params("webusiness_id",2)
//                .success(new ISuccess() {
//                    @Override
//                    public void onSuccess(String response) {
//                        Log.e("sss",response);
//                        RestClient.builder()
//                                .url("http://admin.swczyc.com/hyapi/business/usermanagement/info")
//                                .success(new ISuccess() {
//                                    @Override
//                                    public void onSuccess(String response) {
//                                        Log.e("sssddd",response);
//                                    }
//                                })
//                                .build()
//                                .get();
//                    }
//                })
//                .build()
//                .post();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    FormBody requestBody = new FormBody.Builder()
//                            .add("wechat_id","8")
//                            .add("webusiness_id","2")
//                            .build();
//                    final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
//                    OkHttpClient client = new OkHttpClient.Builder()
//                            .cookieJar(new CookieJar() {
//                                @Override
//                                public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
//                                    cookieStore.put(httpUrl.host(), list);
//                                }
//
//                                @Override
//                                public List<Cookie> loadForRequest(HttpUrl httpUrl) {
//                                    List<Cookie> cookies = cookieStore.get(httpUrl.host());
//                                    return cookies != null ? cookies : new ArrayList<Cookie>();
//                                }
//                            })
//                            .build();
//
//                    Request.Builder builder = new Request.Builder();
//                    Request request = builder
//                            .url("http://admin.swczyc.com/hyapi/ymmall/login/submit")
//                            .post(requestBody)
//                            .build();
//                    Response response = client.newCall(request).execute();
//                    String responseData = response.body().string();
//                    Log.e("test_dddd",responseData);
//
//                    request = builder.
//                            url("http://admin.swczyc.com/hyapi/business/usermanagement/info")
//                            .build();
//                    response = client.newCall(request).execute();
//                    responseData = response.body().string();
//                    Log.e("test_dddd_eeee",responseData);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
    }

}

package debug;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.tobyli16.indexlibrary.R;

import cn.bupt.wfshop.app.Wangfu;
import cn.bupt.wfshop.icon.FontEcModule;
import cn.bupt.wfshop.net.interceptors.DebugInterceptor;


public class IndexApplication extends Application {

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
                .withWeChatAppId("wxe7798aead7625419") //设置微信AppID
                .withWeChatAppSecret("d8dfb107972ae477b58f9a600c989541") //设置微信AppSecret
                .withInterceptor(new DebugInterceptor("test", R.raw.test)) //设置拦截器
                .withJavascriptInterface("wangfu")
                //添加Cookie同步拦截器
                .withWebHost("https://www.baidu.com/")
                .withApiField("productURL", "product") //设置API地址
                .withApiField("cartUrl", "cart") //设置API地址
                .withSpanCount(120) //设置首页布局一行的SpanSize的最大值，方便布局
                .configure();
    }



}

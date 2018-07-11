package cn.bupt.wfshop.mainapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.util.Log;

import com.alipay.sdk.app.EnvUtils;

import cn.bupt.wfshop.activities.ProxyActivity;
import cn.bupt.wfshop.app.Wangfu;
import cn.bupt.wfshop.delegates.WangfuDelegate;
import cn.bupt.wfshop.ui.launcher.ILauncherListener;
import cn.bupt.wfshop.ui.launcher.OnLauncherFinishTag;
import cn.jpush.android.api.JPushInterface;

public class ExampleActivity extends ProxyActivity implements
        ILauncherListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);  //支付宝支付环境
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        Wangfu.getConfigurator().withActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    public WangfuDelegate setRootDelegate() {
        return new EcBottomDelegate();
    }

    @Override
    public void onLauncherFinish(OnLauncherFinishTag tag) {
        Log.e("ExampleActivity", tag.name());
        switch (tag) {
            case SIGNED:
                getSupportDelegate().startWithPop(new EcBottomDelegate());
                break;
            case NOT_SIGNED:
                getSupportDelegate().startWithPop(new EcBottomDelegate());
                break;
            default:
                break;
        }
    }
}

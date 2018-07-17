package cn.bupt.wfshop.app;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import java.util.HashMap;

/**
 * Created by tobyli on 2017/3/29
 */

public final class Wangfu {

    public static Configurator init(Context context) {
        Configurator.getInstance()
                .getWangfuConfigs()
                .put(ConfigKeys.APPLICATION_CONTEXT, context.getApplicationContext());
        return Configurator.getInstance();
    }

    public static Configurator getConfigurator() {
        return Configurator.getInstance();
    }

    public static <T> T getConfiguration(Object key) {
        return getConfigurator().getConfiguration(key);
    }

    public static Application getApplicationContext() {
        return getConfiguration(ConfigKeys.APPLICATION_CONTEXT);
    }

    public static Handler getHandler() {
        return getConfiguration(ConfigKeys.HANDLER);
    }

    public static HashMap<String, String> getApiFields() {
        return getConfiguration(ConfigKeys.API_FIELDS);
    }

    public static void test(){
    }
}
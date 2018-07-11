package cn.bupt.wfshop.app;

import android.app.Activity;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.blankj.utilcode.util.Utils;
import com.joanzapata.iconify.IconFontDescriptor;
import com.joanzapata.iconify.Iconify;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;

import cn.bupt.wfshop.delegates.web.event.Event;
import cn.bupt.wfshop.delegates.web.event.EventManager;
import okhttp3.Interceptor;

/**
 * Created by tobyli on 2017/3/29
 * 配置全局的变量，即初始化
 */

public final class Configurator {

    private static final HashMap<Object, Object> WANGFU_CONFIGS = new HashMap<>();
    private static final Handler HANDLER = new Handler();
    private static final ArrayList<IconFontDescriptor> ICONS = new ArrayList<>();
    private static final ArrayList<Interceptor> INTERCEPTORS = new ArrayList<>();
    private static final HashMap<String, String> API_FIELDS = new HashMap<>();

    private Configurator() {
        WANGFU_CONFIGS.put(ConfigKeys.CONFIG_READY, false);
        WANGFU_CONFIGS.put(ConfigKeys.HANDLER, HANDLER);
        WANGFU_CONFIGS.put(ConfigKeys.API_FIELDS,API_FIELDS);
    }

    static Configurator getInstance() {
        return Holder.INSTANCE;
    }

    final HashMap<Object, Object> getWangfuConfigs() {
        return WANGFU_CONFIGS;
    }

    private static class Holder {
        private static final Configurator INSTANCE = new Configurator();
    }

    public final void configure() {
        initIcons();
        Logger.addLogAdapter(new AndroidLogAdapter());
        WANGFU_CONFIGS.put(ConfigKeys.CONFIG_READY, true);
        Utils.init(Wangfu.getApplicationContext());
    }

    public final Configurator withApiHost(String host) {
        WANGFU_CONFIGS.put(ConfigKeys.API_HOST, host);
        return this;
    }

    public final Configurator withLoaderDelayed(long delayed) {
        WANGFU_CONFIGS.put(ConfigKeys.LOADER_DELAYED, delayed);
        return this;
    }

    private void initIcons() {
        if (ICONS.size() > 0) {
            final Iconify.IconifyInitializer initializer = Iconify.with(ICONS.get(0));
            for (int i = 1; i < ICONS.size(); i++) {
                initializer.with(ICONS.get(i));
            }
        }
    }

    public final Configurator withIcon(IconFontDescriptor descriptor) {
        ICONS.add(descriptor);
        return this;
    }

    public final Configurator withInterceptor(Interceptor interceptor) {
        INTERCEPTORS.add(interceptor);
        WANGFU_CONFIGS.put(ConfigKeys.INTERCEPTOR, INTERCEPTORS);
        return this;
    }

    public final Configurator withInterceptors(ArrayList<Interceptor> interceptors) {
        INTERCEPTORS.addAll(interceptors);
        WANGFU_CONFIGS.put(ConfigKeys.INTERCEPTOR, INTERCEPTORS);
        return this;
    }

    public final Configurator withWeChatAppId(String appId) {
        WANGFU_CONFIGS.put(ConfigKeys.WE_CHAT_APP_ID, appId);
        return this;
    }

    public final Configurator withWeChatAppSecret(String appSecret) {
        WANGFU_CONFIGS.put(ConfigKeys.WE_CHAT_APP_SECRET, appSecret);
        return this;
    }

    public final Configurator withActivity(Activity activity) {
        WANGFU_CONFIGS.put(ConfigKeys.ACTIVITY, activity);
        return this;
    }

    public Configurator withJavascriptInterface(@NonNull String name) {
        WANGFU_CONFIGS.put(ConfigKeys.JAVASCRIPT_INTERFACE, name);
        return this;
    }

    public Configurator withWebEvent(@NonNull String name, @NonNull Event event) {
        final EventManager manager = EventManager.getInstance();
        manager.addEvent(name, event);
        return this;
    }

    //浏览器加载的HOST
    public Configurator withWebHost(String host) {
        WANGFU_CONFIGS.put(ConfigKeys.WEB_HOST, host);
        return this;
    }

    public Configurator withApiField(String name, String url) {
        API_FIELDS.put(name, url);
        return this;
    }

    public Configurator withApiFields(HashMap<String, String> urls) {
        API_FIELDS.putAll(urls);
        return this;
    }

    public Configurator withSpanCount(int spanCount) {
        WANGFU_CONFIGS.put(ConfigKeys.SPAN_COUNT, spanCount);
        return this;
    }

    private void checkConfiguration() {
        final boolean isReady = (boolean) WANGFU_CONFIGS.get(ConfigKeys.CONFIG_READY);
        if (!isReady) {
            throw new RuntimeException("Configuration is not ready,call configure");
        }
    }

    @SuppressWarnings("unchecked")
    final <T> T getConfiguration(Object key) {
        checkConfiguration();
        final Object value = WANGFU_CONFIGS.get(key);
        if (value == null) {
            throw new NullPointerException(key.toString() + " IS NULL");
        }
        return (T) WANGFU_CONFIGS.get(key);
    }
}

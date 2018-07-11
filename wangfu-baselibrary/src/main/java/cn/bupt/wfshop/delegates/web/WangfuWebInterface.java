package cn.bupt.wfshop.delegates.web;

import android.webkit.JavascriptInterface;

import com.alibaba.fastjson.JSON;
import cn.bupt.wfshop.delegates.web.event.Event;
import cn.bupt.wfshop.delegates.web.event.EventManager;
import cn.bupt.wfshop.util.log.WangfuLogger;

/**
 * Created by tobyli
 */

final class WangfuWebInterface {
    private final WebDelegate DELEGATE;

    private WangfuWebInterface(WebDelegate delegate) {
        this.DELEGATE = delegate;
    }

    static WangfuWebInterface create(WebDelegate delegate) {
        return new WangfuWebInterface(delegate);
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public String event(String params) {
        final String action = JSON.parseObject(params).getString("action");
        final Event event = EventManager.getInstance().createEvent(action);
        WangfuLogger.d("WEB_EVENT",params);
        if (event != null) {
            event.setAction(action);
            event.setDelegate(DELEGATE);
            event.setContext(DELEGATE.getContext());
            event.setUrl(DELEGATE.getUrl());
            return event.execute(params);
        }
        return null;
    }
}

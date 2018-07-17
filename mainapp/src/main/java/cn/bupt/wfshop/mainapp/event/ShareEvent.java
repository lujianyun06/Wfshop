package cn.bupt.wfshop.mainapp.event;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import cn.bupt.wfshop.delegates.web.event.Event;
import cn.bupt.wfshop.util.log.WangfuLogger;

import cn.sharesdk.framework.ShareSDK;
import onekeyshare.OnekeyShare;

/**
 * Created by tobyli
 */

public class ShareEvent extends Event {

    @Override
    public String execute(String params) {

        WangfuLogger.json("ShareEvent", params);

        final JSONObject object = JSON.parseObject(params).getJSONObject("params");
        final String title = object.getString("title");
        final String url = object.getString("url");
        final String imageUrl = object.getString("imageUrl");
        final String text = object.getString("text");

        ShareSDK.initSDK(getContext());
        final OnekeyShare oks = new OnekeyShare();
        oks.disableSSOWhenAuthorize();
        oks.setTitle(title);
        oks.setText(text);
        oks.setImageUrl(imageUrl);
        oks.setUrl(url);
        oks.show(getContext());

        return null;
    }
}

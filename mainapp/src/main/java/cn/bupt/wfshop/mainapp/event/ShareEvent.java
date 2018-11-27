package cn.bupt.wfshop.mainapp.event;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import cn.bupt.wfshop.delegates.web.event.Event;
import cn.bupt.wfshop.util.log.WangfuLogger;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import onekeyshare.OnekeyShare;

/**
 * Created by tobyli
 */
//用于分享的SDK：sharedSDK，实际上并没有使用，只是留个接口，使用的时候直接调用WangfuWebInterface.event() 里面是调用本类的execute
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
        oks.disableSSOWhenAuthorize();   //关闭sso授权
        oks.setTitle(title);
        oks.setText(text);
        oks.setImageUrl(imageUrl);
        oks.setUrl(url);
        oks.show(getContext());  //在上面设置各种参数后，这里启动分享\

        //微信分享
        /*
        Wechat.ShareParams sp;
        Platform wx = ShareSDK.getPlatform (Wechat.NAME);
        wx. setPlatformActionListener (mPlatformActionListener); // 设置分享事件回调
        // 执行图文分享
        wx.share(sp);
        */

        return null;
    }
}

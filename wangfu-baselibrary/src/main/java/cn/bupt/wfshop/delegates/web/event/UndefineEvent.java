package cn.bupt.wfshop.delegates.web.event;

import cn.bupt.wfshop.util.log.WangfuLogger;

/**
 * Created by tobyli
 */

public class UndefineEvent extends Event {
    @Override
    public String execute(String params) {
        WangfuLogger.e("UndefineEvent", params);
        return null;
    }
}

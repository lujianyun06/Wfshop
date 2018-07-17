package cn.bupt.wfshop.mainapp.generators;

import cn.bupt.wfshop.annotations.PayEntryGenerator;
import cn.bupt.wfshop.wechat.templates.WXPayEntryTemplate;

/**
 * Created by tobyli on 2017/4/22
 */
@SuppressWarnings("unused")
@PayEntryGenerator(
        packageName = "cn.bupt.wfshop.mainapp",
        payEntryTemplate = WXPayEntryTemplate.class
)
public interface WeChatPayEntry {
}

package cn.bupt.wfshop.mainapp.generators;

import cn.bupt.wfshop.annotations.EntryGenerator;
import cn.bupt.wfshop.wechat.templates.WXEntryTemplate;

/**
 * Created by tobyli on 2017/4/22
 */

@SuppressWarnings("unused")
@EntryGenerator(
        packageName = "cn.bupt.wfshop.mainapp",
        entryTemplate = WXEntryTemplate.class
)
public interface WeChatEntry {
}

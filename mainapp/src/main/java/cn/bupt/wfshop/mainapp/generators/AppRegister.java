package cn.bupt.wfshop.mainapp.generators;

import cn.bupt.wfshop.annotations.AppRegisterGenerator;
import cn.bupt.wfshop.wechat.templates.AppRegisterTemplate;

/**
 * Created by tobyli on 2017/4/22
 */
@SuppressWarnings("unused")
@AppRegisterGenerator(
        packageName = "cn.bupt.wfshop.mainapp",
        registerTemplate = AppRegisterTemplate.class
)
public interface AppRegister {
}

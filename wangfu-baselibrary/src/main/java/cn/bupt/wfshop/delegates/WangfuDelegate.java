package cn.bupt.wfshop.delegates;

/**
 * Created by tobyli on 2017/4/2
 */

public abstract class WangfuDelegate extends PermissionCheckerDelegate {

    @SuppressWarnings("unchecked")
    public <T extends WangfuDelegate> T getParentDelegate() {
        return (T) getParentFragment();
    }
}

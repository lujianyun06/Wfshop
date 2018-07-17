package cn.bupt.wfshop.util.callback;

import android.support.annotation.Nullable;

/**
 * Created by tobyli
 */

public interface IGlobalCallback<T> {

    void executeCallback(@Nullable T args);
}

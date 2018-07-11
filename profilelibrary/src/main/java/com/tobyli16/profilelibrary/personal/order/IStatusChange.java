package com.tobyli16.profilelibrary.personal.order;

import android.support.v4.app.Fragment;

/**
 * Created by Toby on 2018/3/15 0015.
 */

public interface IStatusChange {
    void statusChange(int targetPos);
    void gotoFragment(Fragment fragment);
}

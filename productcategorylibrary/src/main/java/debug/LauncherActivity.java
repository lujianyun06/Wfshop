package debug;

import android.os.Bundle;

import cn.bupt.wfshop.activities.ProxyActivity;
import cn.bupt.wfshop.delegates.WangfuDelegate;


public class LauncherActivity extends ProxyActivity {

    @Override
    public WangfuDelegate setRootDelegate() {
        return new EcBottomDelegate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}

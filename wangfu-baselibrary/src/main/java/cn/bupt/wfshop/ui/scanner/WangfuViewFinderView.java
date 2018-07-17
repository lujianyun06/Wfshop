package cn.bupt.wfshop.ui.scanner;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import me.dm7.barcodescanner.core.ViewFinderView;

/**
 * Created by tobyli
 */

public class WangfuViewFinderView extends ViewFinderView {

    public WangfuViewFinderView(Context context) {
        this(context, null);
    }

    public WangfuViewFinderView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mSquareViewFinder = true;
        mBorderPaint.setColor(Color.YELLOW);
        mLaserPaint.setColor(Color.YELLOW);
    }
}

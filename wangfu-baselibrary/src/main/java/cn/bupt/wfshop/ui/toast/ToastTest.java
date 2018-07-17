package cn.bupt.wfshop.ui.toast;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.bupt.wfshop.R;

/**
 * Created by Toby on 2018/3/15 0015.
 */

public class ToastTest {

    public static void showToast(Context context, String text, int duration) {
        //自定义Toast控件
        View toastView = LayoutInflater.from(context).inflate(R.layout.toast_clear_layout, null);
        LinearLayout relativeLayout = (LinearLayout) toastView.findViewById(R.id.toast_linear);
        //动态设置toast控件的宽高度，宽高分别是130dp
        //这里用了一个将dp转换为px的工具类PxUtil
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) dpToPx(context, 200), (int)dpToPx(context, 130));
        relativeLayout.setLayoutParams(layoutParams);
        TextView textView = (TextView) toastView.findViewById(R.id.tv_toast_clear);
        textView.setText(text);
        Toast toast = new Toast(context);
        toast.setDuration(duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(toastView);
        toast.show();
    }

    public static float dpToPx(Context context, int dp) {
        //获取屏蔽的像素密度系数
        float density = context.getResources().getDisplayMetrics().density;
        return dp * density;
    }

    public static float pxTodp(Context context, int px) {
        //获取屏蔽的像素密度系数
        float density = context.getResources().getDisplayMetrics().density;
        return px / density;
    }

}

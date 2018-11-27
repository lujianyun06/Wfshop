package com.tobyli16.indexlibrary.index.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.tobyli16.indexlibrary.R;
import com.tobyli16.indexlibrary.index.bean.GridViewBean;
import com.tobyli16.indexlibrary.index.bean.ImageObject;

import java.util.ArrayList;

public class  GridLayoutHelper{
    private Context mContext;
    private static final RequestOptions RECYCLER_OPTIONS =
            new RequestOptions()
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate();

    private GridLayoutHelper(Context context){
        mContext = context;
    }

    public static GridLayoutHelper builder(Context context){
        return new GridLayoutHelper(context);
    }

    public void initGridLayout(ViewGroup viewGroup, GridViewBean bean){
        addViewIntoVLinearLayout((LinearLayout) viewGroup, bean.subGridView);
    }

    //动态设置宽高比
    private void setLayoutRatio(final View view, final float ratio){
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {

                view.getViewTreeObserver().removeOnPreDrawListener(this);
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = (int) (view.getWidth() * ratio);
                view.setLayoutParams(layoutParams);
                return true;

            }
        });
    }

    private void addViewIntoVLinearLayout(LinearLayout suplayout, ArrayList<GridViewBean> gridObjs){
        int len = gridObjs.size();
        for(int i=0; i<len; i++){
            GridViewBean gridObj = gridObjs.get(i);
            LinearLayout ll = new LinearLayout(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.weight = Integer.parseInt(gridObj.weight);
            ll.setLayoutParams(params);
            addViewIntoHLinearLayout(ll, gridObj.imgs);
            suplayout.addView(ll);
        }
    }

    private void addViewIntoHLinearLayout(LinearLayout suplayout, ArrayList<ImageObject> imageObjs){
        int len = imageObjs.size();
        for(int i=0; i<len; i++){
            ImageObject imageObj = imageObjs.get(i);
            ImageView iv = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.weight = Integer.parseInt(imageObj.weight);
            iv.setLayoutParams(params);

            Glide.with(mContext)
                    .load(imageObj.imgUrl)
                    .apply(RECYCLER_OPTIONS)
                    .into(iv);
            suplayout.addView(iv);
        }
    }
}

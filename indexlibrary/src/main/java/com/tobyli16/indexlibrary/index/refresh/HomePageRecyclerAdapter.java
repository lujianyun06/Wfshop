package com.tobyli16.indexlibrary.index.refresh;

import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tobyli16.indexlibrary.R;
import com.tobyli16.indexlibrary.index.bean.BannerBean;
import com.tobyli16.indexlibrary.index.bean.GridViewBean;
import com.tobyli16.indexlibrary.index.bean.ImageObject;
import com.tobyli16.indexlibrary.index.bean.SeparatorViewBean;
import com.tobyli16.indexlibrary.index.ui.GridLayoutHelper;

import java.util.ArrayList;
import java.util.List;

import cn.bupt.wfshop.ui.banner.BannerCreator;
import cn.bupt.wfshop.ui.recycler.DataConverter;
import cn.bupt.wfshop.ui.recycler.ItemType;
import cn.bupt.wfshop.ui.recycler.MultipleFields;
import cn.bupt.wfshop.ui.recycler.MultipleItemEntity;
import cn.bupt.wfshop.ui.recycler.MultipleViewHolder;

public class HomePageRecyclerAdapter extends
        BaseMultiItemQuickAdapter<HomeMultiItemEntity, BaseViewHolder>
        implements
//        BaseQuickAdapter.SpanSizeLookup,
        OnItemClickListener {


    //确保初始化一次Banner，防止重复Item加载
    private boolean mIsInitBanner = false;
    private GridLayoutHelper gridHelper = null;
    //设置图片加载策略
    private static final RequestOptions RECYCLER_OPTIONS =
            new RequestOptions()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate();

    protected HomePageRecyclerAdapter(List<HomeMultiItemEntity> data) {
        super(data);
        init();
    }

    public static HomePageRecyclerAdapter create(List<HomeMultiItemEntity> data) {
        return new HomePageRecyclerAdapter(data);
    }

    public static HomePageRecyclerAdapter create(HomeDataConvert converter) {
        return new HomePageRecyclerAdapter(converter.convert());
    }

    public void refresh(List<HomeMultiItemEntity> data) {
        getData().clear();
        setNewData(data);
        notifyDataSetChanged();
    }

    private void init() {
        //设置不同的item布局，绑定type和layout的关系

        addItemType(HomeMultiItemEntity.BANNER, cn.bupt.wfshop.R.layout.item_multiple_banner);
        addItemType(HomeMultiItemEntity.GRID, R.layout.item_linear_vertical);
        addItemType(HomeMultiItemEntity.SEPARATOR, R.layout.item_text);
        //设置宽度监听
//        setSpanSizeLookup(this);
        openLoadAnimation();
        //多次执行动画
        isFirstOnly(false);

    }

    @Override
    protected MultipleViewHolder createBaseViewHolder(View view) {
        return MultipleViewHolder.create(view);
    }

    @Override
    protected void convert(BaseViewHolder holder, HomeMultiItemEntity entity) {
        gridHelper = GridLayoutHelper.builder(mContext);
        final String text;
        final ArrayList<String> bannerImages;
        switch (holder.getItemViewType()) {
            case HomeMultiItemEntity.SEPARATOR:
                text = ((SeparatorViewBean) entity.getDataBean()).getTitle();
                holder.setText(R.id.tv, text);
                break;
            case HomeMultiItemEntity.BANNER:
                if (!mIsInitBanner) {
                    bannerImages = new ArrayList<>();
                    BannerBean bannerBean = (BannerBean) entity.getDataBean();
                    int len = bannerBean.getBannImgs().size();
                    ArrayList<ImageObject> imageObjects = bannerBean.getBannImgs();
                    for (int i = 0;i<len;i++){
                        bannerImages.add(imageObjects.get(i).imgUrl);
                    }
                    final ConvenientBanner<String> convenientBanner = holder.getView(R.id.banner_recycler_item);
                    BannerCreator.setDefault(convenientBanner, bannerImages, this, bannerBean.getDuration());
                    mIsInitBanner = true;
                }
                break;
            case HomeMultiItemEntity.GRID:
                GridViewBean gridViewBean = (GridViewBean) entity.getDataBean();
                gridHelper.initGridLayout((ViewGroup) holder.itemView, gridViewBean);
                break;
            default:
                break;
        }
    }

//    @Override
//    public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
//        return getData().get(position);
//    }

    @Override
    public void onItemClick(int position) {

    }


}

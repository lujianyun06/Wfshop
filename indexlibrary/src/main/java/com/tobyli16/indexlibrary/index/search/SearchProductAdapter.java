package com.tobyli16.indexlibrary.index.search;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.tobyli16.indexlibrary.R;

import java.util.List;

import cn.bupt.wfshop.ui.recycler.MultipleFields;
import cn.bupt.wfshop.ui.recycler.MultipleItemEntity;
import cn.bupt.wfshop.ui.recycler.MultipleRecyclerAdapter;
import cn.bupt.wfshop.ui.recycler.MultipleViewHolder;

/**
 * Created by tobyli
 */

public class SearchProductAdapter extends MultipleRecyclerAdapter {

    private static final RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .dontAnimate();

    protected SearchProductAdapter(List<MultipleItemEntity> data) {
        super(data);
        addItemType(SearchProductItemType.ITEM_SEARCH, R.layout.item_search_product_list);
    }

    @Override
    protected void convert(MultipleViewHolder holder, MultipleItemEntity entity) {
        super.convert(holder, entity);
        switch (entity.getItemType()) {
            case SearchProductItemType.ITEM_SEARCH:
                final AppCompatImageView imageView = holder.getView(R.id.iv_search_product_iv);
                final AppCompatTextView name = holder.getView(R.id.tv_search_product_name);
                final AppCompatTextView price = holder.getView(R.id.tv_search_product_price);
                final AppCompatTextView description = holder.getView(R.id.tv_search_product_description);

                final String nameVal = entity.getField(MultipleFields.NAME);
                final String descriptionVal = entity.getField(MultipleFields.DESCRIPTION);
                final double priceVal = entity.getField(MultipleFields.PRICE);
                final String imageUrl = entity.getField(MultipleFields.IMAGE_URL);

                Glide.with(mContext)
                        .load(imageUrl)
                        .apply(OPTIONS)
                        .into(imageView);

                name.setText(nameVal);
                price.setText("价格：" + String.valueOf(priceVal));
                description.setText(descriptionVal);
                break;
            default:
                break;
        }
    }
}

package com.tobyli16.profilelibrary.personal.coupon;

import android.annotation.SuppressLint;

import com.tobyli16.profilelibrary.personal.fencheng.FenchengListItemType;

import java.util.List;

import cn.bupt.wfshop.ui.recycler.MultipleItemEntity;
import cn.bupt.wfshop.ui.recycler.MultipleRecyclerAdapter;
import cn.bupt.wfshop.ui.recycler.MultipleViewHolder;

/**
 * Created by tobyli
 */

public class CouponListAdapter extends MultipleRecyclerAdapter {


    protected CouponListAdapter(List<MultipleItemEntity> data) {
        super(data);
//        addItemType(FenchengListItemType.ITEM_FENCHENGT_LIST, R.layout.item_fencheng_list);
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(MultipleViewHolder holder, MultipleItemEntity entity) {
        super.convert(holder, entity);
        switch (holder.getItemViewType()) {
            case FenchengListItemType.ITEM_FENCHENGT_LIST:
//                final AppCompatTextView dateTV = holder.getView(R.id.tv_fencheng_date);
//                final AppCompatTextView usernameTV = holder.getView(R.id.tv_fencheng_user);
//                final AppCompatTextView productTV = holder.getView(R.id.tv_fencheng_product);
//                final AppCompatTextView shopTV = holder.getView(R.id.tv_fencheng_shop);
//                final AppCompatTextView totalPriceTV = holder.getView(R.id.tv_fencheng_totalprice);
//                final AppCompatTextView fenchengPriceTV = holder.getView(R.id.tv_fencheng_price);
//
//                final String date = entity.getField(FenchengItemFields.DATE);
//                final String username = entity.getField(FenchengItemFields.USERNAME);
//                final String product = entity.getField(FenchengItemFields.PRODUCT);
//                final String shop = entity.getField(FenchengItemFields.SHOP);
//                final double totalPrice = entity.getField(FenchengItemFields.TOTAL_PRICE);
//                final double fenchengPrice = entity.getField(FenchengItemFields.FENCHENG_PRICE);
//
//                dateTV.setText(date);
//                usernameTV.setText(username);
//                productTV.setText(product);
//                shopTV.setText(shop);
//                totalPriceTV.setText("￥"+String.format("%.2f",totalPrice));
//                fenchengPriceTV.setText("￥"+String.format("%.2f",fenchengPrice));
                break;
            default:
                break;
        }
    }
}

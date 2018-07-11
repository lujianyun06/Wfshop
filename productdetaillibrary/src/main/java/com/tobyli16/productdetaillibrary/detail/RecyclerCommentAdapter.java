package com.tobyli16.productdetaillibrary.detail;

import android.support.v7.widget.AppCompatImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.tobyli16.productdetaillibrary.R;

import java.util.List;

import cn.bupt.wfshop.ui.recycler.ItemType;
import cn.bupt.wfshop.ui.recycler.MultipleFields;
import cn.bupt.wfshop.ui.recycler.MultipleItemEntity;
import cn.bupt.wfshop.ui.recycler.MultipleRecyclerAdapter;
import cn.bupt.wfshop.ui.recycler.MultipleViewHolder;

/**
 * Created by tobyli
 */

public class RecyclerCommentAdapter extends MultipleRecyclerAdapter {

    private static final RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .dontAnimate();

    protected RecyclerCommentAdapter(List<MultipleItemEntity> data) {
        super(data);
        addItemType(ItemType.COMMENT, R.layout.item_comment);
    }

    @Override
    protected void convert(MultipleViewHolder holder, MultipleItemEntity entity) {
        super.convert(holder, entity);
        final int type = holder.getItemViewType();
        switch (type) {
            case ItemType.COMMENT:
                final AppCompatImageView imageView = holder.getView(R.id.iv_comment_user_pic);
                final String url = entity.getField(MultipleFields.IMAGE_URL);
                Glide.with(mContext)
                        .load(url)
                        .into(imageView);

                final TextView tv_comment = holder.getView(R.id.tv_comment);
                final TextView tv_buyer_name = holder.getView(R.id.tv_buyer_name);
                final TextView tv_star_text = holder.getView(R.id.tv_star_text);
                String comment = entity.getField(CommentFileds.COMMENT);
                String buyer = entity.getField(CommentFileds.BUYER);
                String star = entity.getField(CommentFileds.STAR);
                tv_comment.setText(comment);
                tv_buyer_name.setText(buyer);
                tv_star_text.setText(star);
                break;
            default:
                break;
        }
    }
}

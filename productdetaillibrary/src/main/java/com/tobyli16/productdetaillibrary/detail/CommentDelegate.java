package com.tobyli16.productdetaillibrary.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tobyli16.productdetaillibrary.R;

import java.util.ArrayList;

import cn.bupt.wfshop.delegates.WangfuDelegate;
import cn.bupt.wfshop.ui.recycler.ItemType;
import cn.bupt.wfshop.ui.recycler.MultipleFields;
import cn.bupt.wfshop.ui.recycler.MultipleItemEntity;

/**
 * Created by tobyli
 */

public class CommentDelegate extends WangfuDelegate {

    private RecyclerView mRecyclerView = null;

    private static final String USER_PICTURES = "USER_PICTURES";
    private static final String BUYER_NAME = "BUYER_NAME";
    private static final String USER_COMMENT = "USER_COMMENT";
    private static final String STARS = "STARS";

    @Override
    public Object setLayout() {
        return R.layout.delegate_comment;
    }

    private void initComments() {
        final Bundle arguments = getArguments();
        if (arguments != null) {
            final ArrayList<String> user_pics = arguments.getStringArrayList(USER_PICTURES);
            final ArrayList<String> buy_names = arguments.getStringArrayList(BUYER_NAME);
            final ArrayList<String> comments = arguments.getStringArrayList(USER_COMMENT);
            final ArrayList<String> stars = arguments.getStringArrayList(STARS);
            final ArrayList<MultipleItemEntity> entities = new ArrayList<>();
            final int size;
            if (user_pics != null) {
                size = user_pics.size();
                for (int i = 0; i < size; i++) {
                    final String imageUrl = user_pics.get(i);
                    final String buy_name = buy_names.get(i);
                    final String comment = comments.get(i);
                    final String star = stars.get(i);
                    final MultipleItemEntity entity = MultipleItemEntity
                            .builder()
                            .setItemType(ItemType.COMMENT)
                            .setField(MultipleFields.IMAGE_URL, imageUrl)
                            .setField(CommentFileds.BUYER, buy_name)
                            .setField(CommentFileds.COMMENT, comment)
                            .setField(CommentFileds.STAR, star)
                            .build();
                    entities.add(entity);
                }
                final RecyclerCommentAdapter adapter = new RecyclerCommentAdapter(entities);
                mRecyclerView.setAdapter(adapter);
            }
        }
    }

    public static CommentDelegate create(ArrayList<ArrayList<String>> data) {
        final Bundle args = new Bundle();
        args.putStringArrayList(USER_PICTURES, data.get(0));
        args.putStringArrayList(BUYER_NAME, data.get(1));
        args.putStringArrayList(USER_COMMENT, data.get(2));
        args.putStringArrayList(STARS, data.get(3));
        final CommentDelegate delegate = new CommentDelegate();
        delegate.setArguments(args);
        return delegate;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mRecyclerView = $(R.id.rv_comment_container);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        initComments();
    }
}

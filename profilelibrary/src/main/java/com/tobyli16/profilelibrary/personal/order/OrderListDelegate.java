package com.tobyli16.profilelibrary.personal.order;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tobyli16.profilelibrary.R;
import com.tobyli16.profilelibrary.personal.order.orderAdapter.OrderAdapter;
import com.tobyli16.profilelibrary.personal.order.orderBean.GoodsOrderInfo;
import com.tobyli16.profilelibrary.personal.order.orderBean.OrderGoodsItem;
import com.tobyli16.profilelibrary.personal.order.orderBean.OrderPayInfo;

import java.util.ArrayList;
import java.util.List;

import cn.bupt.wfshop.delegates.WangfuDelegate;
import cn.bupt.wfshop.net.RestClient;
import cn.bupt.wfshop.net.callback.ISuccess;

/**
 * Created by tobyli
 */

@Route(path = "/profile/order/list/")
public class OrderListDelegate extends WangfuDelegate implements IStatusChange {

    private String mType = null;

    private RecyclerView mRecyclerView = null;

    private TabLayout mTitles;
    private OrderAdapter mAllOrderAdapter;
    private List<Object> mAllOrderList = new ArrayList<>();
    private String titles[] = new String[]{"全部", "待付款", "待收货", "待评价", "退换修"};
    private static int mListStyle = 0;

    @Autowired
    String currentTabPos;

    @Override
    public Object setLayout() {
        return R.layout.delegate_order_list_new;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public static OrderListDelegate create(int pos) {
        mListStyle = pos;
        return new OrderListDelegate();
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        //currentTabPos是由路由传过来的
        ARouter.getInstance().inject(this);
        mTitles = $(R.id.order_tab);
        mRecyclerView = $(R.id.order_list);
        initTag();
        //如果当前tabpos不空，则选中它，反之选中当前tabpos
        TabLayout.Tab tab = (currentTabPos==null)?mTitles.getTabAt(mListStyle):mTitles.getTabAt(Integer.parseInt(currentTabPos));
        if (tab != null) {
            tab.select();
        }
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        RestClient.builder()
                .loader(getContext())
                .url("https://wfshop.andysheng.cn/order/")
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        //订单列表有三种：1.商品订单信息(物品代码，物品状态)、2.物品信息(数量，价格，图片)、3.交易信息(状态和花费)
                        mAllOrderList = new ArrayList<>();
                        JSONObject responseJsonObject = JSON.parseObject(response);
                        JSONArray jsonArray = responseJsonObject.getJSONArray("data");
                        for(int i = 0; i < jsonArray.size(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            GoodsOrderInfo goodsOrderInfo = new GoodsOrderInfo();
                            goodsOrderInfo.setOrderCode(jsonObject.getString("id"));
                            goodsOrderInfo.setStatus(jsonObject.getString("state"));
                            mAllOrderList.add(goodsOrderInfo);

                            JSONArray productsJSONArray = jsonObject.getJSONArray("products");
                            for (int j = 0 ; j <productsJSONArray.size();j++) {
                                JSONObject productObject = productsJSONArray.getJSONObject(j);
                                OrderGoodsItem orderGoodsItem = new OrderGoodsItem();
                                orderGoodsItem.setCount(Integer.parseInt(productObject.getString("amount")));
                                orderGoodsItem.setTotalPrice(Double.parseDouble(productObject.getString("price")));
                                orderGoodsItem.setOrder(goodsOrderInfo);
                                orderGoodsItem.setOrderid(Integer.parseInt(productObject.getString("id")));
                                orderGoodsItem.setProductName(productObject.getString("name"));
                                orderGoodsItem.setProductPic(productObject.getString("cover_img"));
                                mAllOrderList.add(orderGoodsItem);
                            }

                            OrderPayInfo orderPayInfo = new OrderPayInfo();
                            orderPayInfo.setId(i);
                            orderPayInfo.setStatus(jsonObject.getString("state"));
                            orderPayInfo.setTotalAmount(Double.parseDouble(jsonObject.getString("cost")));
                            mAllOrderList.add(orderPayInfo);
                        }
                        mRecyclerView.setAdapter(new OrderAdapter(getActivity(), mAllOrderList, OrderListDelegate.this));
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                    }
                })
                .build()
                .get();
//        final String defaultPic = "http://swczyc.com/upload/image/201803/82de696b-7c86-4a7c-b0fd-943b28c1f686-thumbnail.jpg";
//        RestClient.builder()
//                        .loader(getContext())
//                        .url("http://admin.swczyc.com/hyapi/ymmall/order/list_by_account")
//                        .params("wechat_id",1)
//                        .params("page",1)
//                        .params("rows",100)
//                        .success(new ISuccess() {
//                            @Override
//                            public void onSuccess(String response) {
//                                mAllOrderList = new ArrayList<>();
//                                JSONObject responseJsonObject = JSON.parseObject(response);
//                                JSONArray jsonArray = responseJsonObject.getJSONObject("obj").getJSONArray("rows");
//                                for(int i = 0; i < jsonArray.size(); i++) {
//                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                    GoodsOrderInfo goodsOrderInfo = new GoodsOrderInfo();
//                                    goodsOrderInfo.setOrderCode(jsonObject.getString("orderCode"));
//                                    goodsOrderInfo.setStatus(jsonObject.getString("orderState"));
//                                    mAllOrderList.add(goodsOrderInfo);
//
//                                    JSONArray productsJSONArray = jsonObject.getJSONArray("orderItems");
//                                    for (int j = 0 ; j <productsJSONArray.size();j++) {
//                                        JSONObject productObject = productsJSONArray.getJSONObject(j);
//                                        OrderGoodsItem orderGoodsItem = new OrderGoodsItem();
//                                        orderGoodsItem.setCount(productObject.getInteger("quantity"));
//                                        orderGoodsItem.setTotalPrice(productObject.getDouble("salePrice"));
//                                        orderGoodsItem.setOrder(goodsOrderInfo);
//                                        orderGoodsItem.setOrderid(Integer.parseInt(productObject.getString("id")));
//                                        String name = productObject.getString("name");
//                                        String specificationName = productObject.getString("specification");
//                                        orderGoodsItem.setProductName(name + ((specificationName == null) ? "":specificationName));
//                                        JSONObject iconURLJSONObject = productObject.getJSONObject("iconURL");
//                                        String defPic = defaultPic;
//                                        if (iconURLJSONObject != null) {
//                                            defPic = iconURLJSONObject.getString("thumbnailPath");
//                                            if (!defPic.startsWith("http")) {
//                                                defPic = defaultPic;
//                                            }
//                                        }
//                                        orderGoodsItem.setProductPic(defPic);
//                                        mAllOrderList.add(orderGoodsItem);
//                                    }
//
//                                    OrderPayInfo orderPayInfo = new OrderPayInfo();
//                                    orderPayInfo.setId(i);
//                                    orderPayInfo.setStatus(jsonObject.getString("orderState"));
//                                    orderPayInfo.setTotalAmount(Double.parseDouble(jsonObject.getString("payMoney")));
//                                    mAllOrderList.add(orderPayInfo);
//                                }
//                                mRecyclerView.setAdapter(new OrderAdapter(getActivity(), mAllOrderList, OrderListDelegate.this));
//                                mRecyclerView.getAdapter().notifyDataSetChanged();
//                            }
//                        })
//                        .build()
//                        .get();
    }


    private void initTag(){
        mTitles.setOnTabSelectedListener(tabSelectedListener);
        mTitles.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTitles.addTab(mTitles.newTab().setText(titles[0].toUpperCase()).setTag(0));
        mTitles.addTab(mTitles.newTab().setText(titles[1]).setTag(1));
        mTitles.addTab(mTitles.newTab().setText(titles[2]).setTag(2));
        mTitles.addTab(mTitles.newTab().setText(titles[3]).setTag(3));
        mTitles.addTab(mTitles.newTab().setText(titles[4]).setTag(4));
    }

    /**
     * 标题选择监听
     */
    private TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            mListStyle = (int) tab.getTag();
            //当前没有对订单的几种不同情况做处理，而是只有一种情况
            String[] url_tags = new String[]{"orderAll","orderUnpay","orderUnrecieve","orderComment","orderRepair"};
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            RestClient.builder()
                    .loader(getContext())
                    .url("https://wfshop.andysheng.cn/order/")
                    .success(new ISuccess() {
                        @Override
                        public void onSuccess(String response) {
                            mAllOrderList = new ArrayList<>();
                            JSONObject responseJsonObject = JSON.parseObject(response);
                            JSONArray jsonArray = responseJsonObject.getJSONArray("data");
                            for(int i = 0; i < jsonArray.size(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String state = jsonObject.getString("state");
                                if (mListStyle != 0 && mListStyle!= Integer.parseInt(state)) {
                                    continue;
                                }

                                GoodsOrderInfo goodsOrderInfo = new GoodsOrderInfo();
                                goodsOrderInfo.setOrderCode(jsonObject.getString("id"));
                                goodsOrderInfo.setStatus(jsonObject.getString("state"));
                                mAllOrderList.add(goodsOrderInfo);

                                JSONArray productsJSONArray = jsonObject.getJSONArray("products");
                                for (int j = 0 ; j <productsJSONArray.size();j++) {
                                    JSONObject productObject = productsJSONArray.getJSONObject(j);
                                    OrderGoodsItem orderGoodsItem = new OrderGoodsItem();
                                    orderGoodsItem.setCount(Integer.parseInt(productObject.getString("amount")));
                                    orderGoodsItem.setTotalPrice(Double.parseDouble(productObject.getString("price")));
                                    orderGoodsItem.setOrder(goodsOrderInfo);
                                    orderGoodsItem.setOrderid(Integer.parseInt(productObject.getString("id")));
                                    orderGoodsItem.setProductName(productObject.getString("name"));
                                    orderGoodsItem.setProductPic(productObject.getString("cover_img"));
                                    mAllOrderList.add(orderGoodsItem);
                                }

                                OrderPayInfo orderPayInfo = new OrderPayInfo();
                                orderPayInfo.setId(i);
                                orderPayInfo.setStatus(jsonObject.getString("state"));
                                orderPayInfo.setTotalAmount(Double.parseDouble(jsonObject.getString("cost")));
                                mAllOrderList.add(orderPayInfo);
                            }
                            mRecyclerView.setAdapter(new OrderAdapter(getActivity(), mAllOrderList,OrderListDelegate.this));
                            mRecyclerView.getAdapter().notifyDataSetChanged();
                        }
                    })
                    .build()
                    .get();
//            final String defaultPic = "http://swczyc.com/upload/image/201803/82de696b-7c86-4a7c-b0fd-943b28c1f686-thumbnail.jpg";
//            RestClient.builder()
//                    .loader(getContext())
//                    .url("http://admin.swczyc.com/hyapi/ymmall/order/list_by_account")
//                    .params("wechat_id",1)
//                    .params("page",1)
//                    .params("rows",100)
//                    .success(new ISuccess() {
//                        @Override
//                        public void onSuccess(String response) {
//                            mAllOrderList = new ArrayList<>();
//                            JSONObject responseJsonObject = JSON.parseObject(response);
//                            JSONArray jsonArray = responseJsonObject.getJSONObject("obj").getJSONArray("rows");
//
//                            for(int i = 0; i < jsonArray.size(); i++) {
//                                JSONObject jsonObject = jsonArray.getJSONObject(i);
//
//                                String state = jsonObject.getString("orderState");
//                                if (mListStyle != 0 && mListStyle!= Integer.parseInt(state)) {
//                                    continue;
//                                }
//
//                                GoodsOrderInfo goodsOrderInfo = new GoodsOrderInfo();
//                                goodsOrderInfo.setOrderCode(jsonObject.getString("orderCode"));
//                                goodsOrderInfo.setStatus(jsonObject.getString("orderState"));
//                                mAllOrderList.add(goodsOrderInfo);
//
//                                JSONArray productsJSONArray = jsonObject.getJSONArray("orderItems");
//                                for (int j = 0 ; j <productsJSONArray.size();j++) {
//                                    JSONObject productObject = productsJSONArray.getJSONObject(j);
//                                    OrderGoodsItem orderGoodsItem = new OrderGoodsItem();
//                                    orderGoodsItem.setCount(productObject.getInteger("quantity"));
//                                    orderGoodsItem.setTotalPrice(productObject.getDouble("salePrice"));
//                                    orderGoodsItem.setOrder(goodsOrderInfo);
//                                    orderGoodsItem.setOrderid(Integer.parseInt(productObject.getString("id")));
//                                    String name = productObject.getString("name");
//                                    String specificationName = productObject.getString("specification");
//                                    orderGoodsItem.setProductName(name + ((specificationName == null) ? "":specificationName));
//                                    JSONObject iconURLJSONObject = productObject.getJSONObject("iconURL");
//                                    String defPic = defaultPic;
//                                    if (iconURLJSONObject != null) {
//                                        defPic = iconURLJSONObject.getString("thumbnailPath");
//                                        if (!defPic.startsWith("http")) {
//                                            defPic = defaultPic;
//                                        }
//                                    }
//                                    orderGoodsItem.setProductPic(defPic);
//                                    mAllOrderList.add(orderGoodsItem);
//                                }
//
//                                OrderPayInfo orderPayInfo = new OrderPayInfo();
//                                orderPayInfo.setId(i);
//                                orderPayInfo.setStatus(jsonObject.getString("orderState"));
//                                orderPayInfo.setTotalAmount(Double.parseDouble(jsonObject.getString("payMoney")));
//                                mAllOrderList.add(orderPayInfo);
//                            }
//                            mRecyclerView.setAdapter(new OrderAdapter(getActivity(), mAllOrderList,OrderListDelegate.this));
//                            mRecyclerView.getAdapter().notifyDataSetChanged();
//                        }
//                    })
//                    .build()
//                    .get();
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            onTabSelected(tab);
        }
    };

    @Override
    public void statusChange(int targetPos){
        TabLayout.Tab tab = mTitles.getTabAt(targetPos);
        if (tab != null) {
            tab.select();
        }
    }

    @Override
    public void gotoFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportDelegate().start((WangfuDelegate)fragment);
        }
    }
}

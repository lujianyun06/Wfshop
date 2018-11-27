package com.tobyli16.cartlibrary.cart;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tobyli16.cartlibrary.R;
import com.tobyli16.cartlibrary.cart.adapter.ShopCartAdapter;
import com.tobyli16.cartlibrary.cart.bean.ShopCartBean;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import cn.bupt.wfshop.delegates.WangfuDelegate;
import cn.bupt.wfshop.delegates.bottom.BottomItemDelegate;
import cn.bupt.wfshop.net.URLManager;
import cn.bupt.wfshop.net.RestClient;
import cn.bupt.wfshop.net.callback.ISuccess;

/**
 * Created by tobyli
 */

public class ShopCartDelegate extends BottomItemDelegate implements View.OnClickListener, ISuccess {

    private TextView tvShopCartSubmit, tvShopCartSelect, tvShopCartTotalNum;
    private View mEmtryView;   //TODO 这个是干啥的？

    private RecyclerView rlvShopCart, rlvHotProducts;
    private ShopCartAdapter mShopCartAdapter;
    private LinearLayout llPay;
    private RelativeLayout rlHaveProduct;
    private List<ShopCartBean> mAllOrderList = new ArrayList<>();
    private ArrayList<ShopCartBean.CartlistBean> mGoPayList = new ArrayList<>();
    private List<String> mHotProductsList = new ArrayList<>();
    private TextView tvShopCartTotalPrice;
    private int mCount, mPosition;
    private float mTotalPrice1;
    private boolean mSelect;


    //    private SwipeRefreshLayout cart_swipe_refresh;


    private boolean hasLazyLoad = false;

    @Override
    public Object setLayout() {
        return R.layout.delegate_shopcart;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        tvShopCartSelect = $(R.id.tv_shopcart_addselect);
        tvShopCartTotalPrice = $(R.id.tv_shopcart_totalprice);
        tvShopCartTotalNum = $(R.id.tv_shopcart_totalnum);

        rlHaveProduct = $(R.id.rl_shopcart_have);
        rlvShopCart = $(R.id.rv_shopcart);
        mEmtryView = $(R.id.emtryview);
        mEmtryView.setVisibility(View.GONE);
        llPay = $(R.id.ll_pay);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        llPay.setLayoutParams(lp);

        tvShopCartSubmit = $(R.id.tv_shopcart_submit);
        tvShopCartSubmit.setOnClickListener(this);

        //        cart_swipe_refresh =  $(R.id.cart_swipe_refresh);
        //        cart_swipe_refresh.setColorSchemeResources(R.color.colorPrimary);
        //        cart_swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
        //            @Override
        //            public void onRefresh() {
        //                RestClient.builder()
        //                        .url("https://wfshop.andysheng.cn/cart")
        //                        .loader(getContext())
        //                        .success(ShopCartDelegate.this)
        //                        .build()
        //                        .get();
        //                cart_swipe_refresh.setRefreshing(false);
        //            }
        //        });
        //
        //        rlvShopCart.setOnScrollListener(new RecyclerView.OnScrollListener(){
        //            @Override
        //            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        //                int topRowVerticalPosition =
        //                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
        //                cart_swipe_refresh.setEnabled(topRowVerticalPosition >= 0);
        //
        //            }
        //
        //            @Override
        //            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        //                super.onScrollStateChanged(recyclerView, newState);
        //            }
        //        });
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        RestClient.builder()
//                .url("http://admin.swczyc.com/hyapi/ymmall/shopping_cart/get_items?wechat_id=8")
                .url(URLManager.CART_URL)
                .loader(getContext())
                .success(this)  //回调成功的接口是本类的方法onSuccess
                .build()
                .get();
        hasLazyLoad = true;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        RestClient.builder()
//                .url("http://admin.swczyc.com/hyapi/ymmall/shopping_cart/get_items?wechat_id=8")
                .url(URLManager.CART_URL)
                .loader(getContext())
                .success(this)
                .build()
                .get();

    }

    //这个只有在onLazyInitView中的请求成功时才会调用
    @Override
    public void onSuccess(String response) {
        mAllOrderList = new ArrayList<>();
        JSONObject responseJsonObject = JSON.parseObject(response);

        JSONArray jsonArray = responseJsonObject.getJSONArray("data");
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject shopAllObject = jsonArray.getJSONObject(i);
            JSONObject shopObject = shopAllObject.getJSONObject("shop");
            JSONArray cartObjects = shopAllObject.getJSONArray("cart_items");
            ShopCartBean shopCartBean = new ShopCartBean();
            shopCartBean.setShopId(shopObject.getInteger("id"));
            shopCartBean.setShopName(shopObject.getString("shop_name"));
            for (int j = 0; j < cartObjects.size(); j++) {
                JSONObject cartObject = cartObjects.getJSONObject(j);
                ShopCartBean.CartlistBean cartlistBean = new ShopCartBean.CartlistBean();
                cartlistBean.setCount(Integer.parseInt(cartObject.getString("amount")));
                cartlistBean.setAttr("");
                cartlistBean.setDefaultPic(cartObject.getJSONObject("product").getString("cover_img"));
                cartlistBean.setIsFirst((j == 0) ? 1 : 2);
                cartlistBean.setSelect(true);
                cartlistBean.setShopSelect(true);
                cartlistBean.setPrice(cartObject.getJSONObject("product").getString("price"));
                cartlistBean.setProductId(Integer.parseInt(cartObject.getJSONObject("product").getString("id")));
                cartlistBean.setShopId(shopCartBean.getShopId());
                cartlistBean.setShopName(shopObject.getString("shop_name"));
                cartlistBean.setProductName(cartObject.getJSONObject("product").getString("name"));
                cartlistBean.setId(j + 1);
                shopCartBean.getCartlist().add(cartlistBean);
            }
            mAllOrderList.add(shopCartBean);
        }
        rlvShopCart.setLayoutManager(new LinearLayoutManager(getContext()));
        mShopCartAdapter = new ShopCartAdapter(getContext(), convertData(mAllOrderList));
        rlvShopCart.setAdapter(mShopCartAdapter);
        mShopCartAdapter.notifyDataSetChanged();

        //删除商品接口
        mShopCartAdapter.setOnDeleteClickListener(new ShopCartAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(View view, int position, int cartid) {
                RestClient.builder()
//                        .url("http://admin.swczyc.com/hyapi/ymmall/shopping_cart/delete_items")
                        .url(URLManager.CART_URL + "/modify/" + cartid)
                        .params("quantity", 0)
//                        .params("id", cartid)
                        .loader(getContext())
                        .success(new ISuccess() {
                            @Override
                            public void onSuccess(String response) {
                                Log.d("modify_cart", response);
                            }
                        })
                        .build()
                        .post();
                mShopCartAdapter.notifyDataSetChanged();
            }
        });
        //修改数量接口
        mShopCartAdapter.setOnEditClickListener(new ShopCartAdapter.OnEditClickListener() {
            @Override
            public void onEditClick(int position, int cartid, int count) {
                mCount = count;
                mPosition = position;
                RestClient.builder()
//                        .url("http://admin.swczyc.com/hyapi/ymmall/shopping_cart/edit_items")
//                        .params("id",cartid)
                        .url(URLManager.CART_URL + "/modify/" + cartid)
                        .params("quantity", count)
                        .loader(getContext())
                        .success(new ISuccess() {
                            @Override
                            public void onSuccess(String response) {
                                Log.d("modify_cart", response);
                            }
                        })
                        .build()
                        .post();
            }
        });
        //实时监控全选按钮
        mShopCartAdapter.setResfreshListener(new ShopCartAdapter.OnResfreshListener() {
            @Override
            public void onResfresh(boolean isSelect) {
                mSelect = isSelect;
                Drawable left = mSelect ? getResources().getDrawable(R.drawable.shopcart_selected)
                        : getResources().getDrawable(R.drawable.shopcart_unselected);
                tvShopCartSelect.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
                float mTotalPrice = 0;
                int mTotalNum = 0;
                mTotalPrice1 = 0;
                mGoPayList.clear();
                for (int i = 0; i < mAllOrderList.size(); i++) {
                    int len = mAllOrderList.get(i).getCartlist().size();
                    for (int j = 0; j < len; j++) {
                        ShopCartBean.CartlistBean cartlistBean = mAllOrderList.get(i).getCartlist().get(j);
                        if (cartlistBean.getIsSelect()) {
                            mTotalPrice += Float.parseFloat(cartlistBean.getPrice()) * cartlistBean.getCount();
                            //                        mTotalNum += 1;
                            mTotalNum += cartlistBean.getCount();
                            mGoPayList.add(cartlistBean);
                        }

                    }


                }
                mTotalPrice1 = mTotalPrice;
                tvShopCartTotalPrice.setText("总价：" + mTotalPrice);
                tvShopCartTotalNum.setText("共" + mTotalNum + "件商品");
            }
        });

        //全选
        tvShopCartSelect.setOnClickListener(this);
    }

    private List<ShopCartBean.CartlistBean> convertData(List<ShopCartBean> shopCartBeans){
        List<ShopCartBean.CartlistBean> cartlistBeans = new ArrayList<>();
        int shopSize = shopCartBeans.size();
        for (int i=0;i<shopSize;i++){
            ShopCartBean shopCartBean = shopCartBeans.get(i);
            cartlistBeans.addAll(shopCartBean.getCartlist());
        }
        return cartlistBeans;
    }

    private void submitBuy() {
        WeakHashMap<String, Object> params = new WeakHashMap<>();
        int len = mGoPayList.size();
        for (int j = 0; j < len; j++) {
            ShopCartBean.CartlistBean cartlistBean = mGoPayList.get(j);
            if (cartlistBean.getIsSelect()) {
                String s1 = "products[" + j + "]";
                String s2 = "amounts[" + j + "]";
                Float amount = cartlistBean.getCount() * Float.parseFloat(cartlistBean.getPrice());
                params.put(s1, cartlistBean.getProductId());
                params.put(s2, amount.toString());
            }
        }

        if (params.size() > 0) {
            RestClient.builder()
                    .url(URLManager.ORDER_URL + "/create")  //先给服务器提交想要交易的请求，请求成功后才跳到下一个页面
                    .loader(getContext())
                    .params(params)
                    .success(new ISuccess() {
                        @Override
                        public void onSuccess(String response) {
                            JSONObject jsonObject = JSON.parseObject(response).getJSONObject("data");
                            Fragment fragment = (Fragment) ARouter.getInstance()
                                    .build("/profileOrderConfirm/profileOrderConfirm/")  //指向 ConfirmOrderDelegate
                                    .withString("orderId", jsonObject.getString("id"))
                                    .withString("cost", jsonObject.getString("cost"))
                                    .navigation();
                            getParentDelegate().getSupportDelegate().start((WangfuDelegate) fragment);

                            int len = mGoPayList.size();
                            for (int i = 0; i < len; i++) {
                                ShopCartBean.CartlistBean buyingItem = mGoPayList.get(i);
                                RestClient.builder()
                                        .url(URLManager.CART_URL + "/modify/" + buyingItem.getId())
                                        .loader(getContext())
                                        .params("amount", 0)  //为啥给0？
                                        .build()
                                        .post();

                            }
                        }
                    })
                    .build()
                    .post();
        }
    }

    private void transSelectStatus() {
        //全选
        Log.d("tvShopCartSelect", "click!");
        mSelect = !mSelect;
        Drawable left = mSelect ?
                getResources().getDrawable(R.drawable.shopcart_selected)
                : getResources().getDrawable(R.drawable.shopcart_unselected);
        tvShopCartSelect.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
        for (int i = 0; i < mAllOrderList.size(); i++) {
            int len = mAllOrderList.get(i).getCartlist().size();
            for (int j = 0; j < len; j++) {
                mAllOrderList.get(i).getCartlist().get(j).setSelect(mSelect);
                mAllOrderList.get(i).getCartlist().get(j).setShopSelect(mSelect);

            }
        }
        mShopCartAdapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.tv_shopcart_submit) {   //提交购买
            submitBuy();
        } else if (viewId == tvShopCartSelect.getId()) {
            transSelectStatus();
        }
    }
}

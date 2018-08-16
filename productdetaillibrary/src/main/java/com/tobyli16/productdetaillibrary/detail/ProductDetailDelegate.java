package com.tobyli16.productdetaillibrary.detail;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.ToxicBakery.viewpager.transforms.DefaultTransformer;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.daimajia.androidanimations.library.YoYo;
import com.joanzapata.iconify.widget.IconTextView;
import com.tobyli16.productdetaillibrary.R;

import java.util.ArrayList;
import java.util.List;

import cn.bupt.wfshop.delegates.WangfuDelegate;
import cn.bupt.wfshop.net.NetManager;
import cn.bupt.wfshop.net.RestClient;
import cn.bupt.wfshop.net.callback.IError;
import cn.bupt.wfshop.net.callback.IFailure;
import cn.bupt.wfshop.net.callback.ISuccess;
import cn.bupt.wfshop.ui.animation.BezierAnimation;
import cn.bupt.wfshop.ui.animation.BezierUtil;
import cn.bupt.wfshop.ui.banner.HolderCreator;
import cn.bupt.wfshop.ui.widget.CircleTextView;
import cn.sharesdk.framework.ShareSDK;
import de.hdodenhof.circleimageview.CircleImageView;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;
import onekeyshare.OnekeyShare;

/**
 * Created by tobyli
 */

@Route(path="/productDetail/productId/")
public class ProductDetailDelegate extends WangfuDelegate implements
        BezierUtil.AnimationListener {

    private final static String TAG = "ProductDetailDelegate";
    @Autowired
    public String goodsId;

    private String spId = "";

    private TabLayout mTabLayout = null;
    private ViewPager mViewPager = null;
    private ConvenientBanner<String> mBanner = null;

    private CircleTextView mCircleTextView = null;
    private RelativeLayout mRlAddShopCart = null;
    private IconTextView mIconShopCart = null;

    private static final String ARG_GOODS_ID = "ARG_GOODS_ID";
    private int mGoodsId = -1;

    private String mGoodsThumbUrl = null;
    private int mShopCount = 0;

    private String sharePicUrl = "";
    private String shareTitle = "WF电商通用客户端";
    private String shareContent = "商品";

    private static final RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .dontAnimate()
            .override(100, 100);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        final Bundle args = getArguments();
        if (args != null) {
            mGoodsId = args.getInt(ARG_GOODS_ID);
        }
        if (goodsId != null) {
            mGoodsId = Integer.parseInt(goodsId);
        }
    }

    private void onClickAddShopCart() {
        final CircleImageView animImg = new CircleImageView(getContext());
        Glide.with(this)
                .load(sharePicUrl)
                .apply(OPTIONS)
                .into(animImg);
        //物品图片进入购物车的 贝塞尔曲线？动画
        BezierAnimation.addCart(this, mRlAddShopCart, mIconShopCart, animImg, this);
    }

    private void setShopCartCount(JSONArray data) {
        mShopCount = 0;
        for (int i = 0; i < data.size(); i++) {
            JSONArray cart_items = data.getJSONObject(i).getJSONArray("cart_items");
            for (int j = 0; j < cart_items.size(); j++) {
                mShopCount+=cart_items.getJSONObject(j).getInteger("amount");
            }
        }
        if (mShopCount == 0) {
            mCircleTextView.setVisibility(View.GONE);
        } else {
            mCircleTextView.setVisibility(View.VISIBLE);
            mCircleTextView.setText(String.valueOf(mShopCount));
        }
    }

    public static ProductDetailDelegate create(int goodsId) {
        final Bundle args = new Bundle();
        args.putInt(ARG_GOODS_ID, goodsId);
        final ProductDetailDelegate delegate = new ProductDetailDelegate();
        delegate.setArguments(args);
        return delegate;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_product_detail;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mTabLayout = $(R.id.tab_layout);
        mViewPager = $(R.id.view_pager);
        mBanner = $(R.id.detail_banner);
        final CollapsingToolbarLayout mCollapsingToolbarLayout = $(R.id.collapsing_toolbar_detail);
        final AppBarLayout mAppBar = $(R.id.app_bar_detail);

        $(R.id.icon_goods_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportDelegate().pop();
            }
        });

        $(R.id.icon_goods_share).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ShareSDK.initSDK(getContext());
                        OnekeyShare oks = new OnekeyShare();
                        oks.disableSSOWhenAuthorize();
                        oks.setTitle(shareTitle);
                        oks.setText(shareContent);
                        oks.setImageUrl(sharePicUrl);
                        oks.setUrl("http://ymymmall.swczyc.com");
                        oks.show(getContext());
                    }
                }
        );

        //底部
        mCircleTextView = $(R.id.tv_shopping_cart_amount);  //购物车中的物品数目，即带圆圈的文字
        mRlAddShopCart = $(R.id.rl_add_shop_cart);   //加入购物车的按钮
        mIconShopCart = $(R.id.icon_shop_cart);

        $(R.id.rl_add_shop_cart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickAddShopCart();
            }
        });

        mCollapsingToolbarLayout.setContentScrimColor(Color.WHITE);
//        mCircleTextView.setCircleBackground(Color.RED);
        if (mShopCount==0)
            mCircleTextView.setVisibility(View.GONE);
        initData();
        initTabLayout();
    }
    /*
    @data 图片的url
    @data2  其他评价信息 //TODO 这一页还没看太明白
     */
    private void initPager(JSONObject data, JSONArray data2) {
        final PagerAdapter adapter = new TabPagerAdapter(getFragmentManager(), data, data2);
        mViewPager.setAdapter(adapter);
    }

    private void initTabLayout() {
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        final Context context = getContext();
        if (context != null) {
            mTabLayout.setSelectedTabIndicatorColor
                    (ContextCompat.getColor(context, R.color.app_main));
        }
        mTabLayout.setTabTextColors(ColorStateList.valueOf(Color.BLACK));
        mTabLayout.setBackgroundColor(Color.WHITE);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initData() {
        RestClient.builder()
//                .url("http://admin.swczyc.com/hyapi/ymmall/product/search?specialty_id="+mGoodsId)
                .url( NetManager.PRODUCT_URL + "/"+mGoodsId)
                .loader(getContext())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final JSONObject data =
                                JSON.parseObject(response).getJSONObject("data");
//                                JSON.parseObject(response).getJSONObject("obj").getJSONArray("rows").getJSONObject(0);
//                        spId = String.valueOf(data.getJSONObject("specification").getString("id"));
                        initBanner(data);
                        initGoodsInfo(data);
                        //获取该商品评论
                        RestClient.builder()
//                                .url("http://admin.swczyc.com/hyapi/ymmall/product/appraisedetail?id="+mGoodsId)
                                .url("https://wfshop.andysheng.cn/product/"+mGoodsId+"/comment")
                                .loader(getContext())
                                .success(new ISuccess() {
                                    @Override
                                    public void onSuccess(String response) {
                                        final JSONArray data2 =
//                                                JSON.parseObject(response).getJSONObject("obj").getJSONArray("rows");
                                                JSON.parseObject(response).getJSONArray("data");
                                        initPager(data,data2);
//                                        setShopCartCount(data);
                                    }
                                })
                                .build()
                                .get();
                        //TODO 获取购物车？交互的时候不需要传入user吗？
                        RestClient.builder()
                                .url("https://wfshop.andysheng.cn/cart")
                                .loader(getContext())
                                .success(new ISuccess() {
                                    @Override
                                    public void onSuccess(String response) {
                                        final JSONArray data2 =
                                                JSON.parseObject(response).getJSONArray("data");
                                        setShopCartCount(data2);
                                    }
                                })
                                .build()
                                .get();

                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String msg) {
                        Log.d(TAG, msg);
                    }
                })
                .build()
                .get();
    }

    private void initGoodsInfo(JSONObject data) {
        shareContent = data.getString("name");
        String goodsData = data.toJSONString();
        getSupportDelegate().
                loadRootFragment(R.id.frame_goods_info, ProductInfoDelegate.create(goodsData));
    }

    private void initBanner(JSONObject data) {
        final JSONArray array = data.getJSONArray("img_urls");
        final List<String> images = new ArrayList<>();
        sharePicUrl = array.getString(0);
        final int size = array.size();
        for (int i = 0; i < size; i++) {
            images.add(array.getString(i));
        }
        mBanner
                .setPages(new HolderCreator(), images)
                .setPageIndicator(new int[]{R.drawable.dot_normal, R.drawable.dot_focus})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .setPageTransformer(new DefaultTransformer())
                .startTurning(3000)
                .setCanLoop(true);
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
    }

    @Override
    public void onAnimationEnd() {
        YoYo.with(new ScaleUpAnimator())  //购物车小图片放大动画
                .duration(500)
                .playOn(mIconShopCart);
        RestClient.builder()
//        spId
//                .url("http://admin.swczyc.com/hyapi/ymmall/shopping_cart/add_items")
                .url("https://wfshop.andysheng.cn/cart/add/"+goodsId)
//                .params("specialtySpecificationId",spId)
//                .params("specialtyId",goodsId)
//                .params("isGroupPromotion",0)
//                .params("quantity",1)
//                .params("wechat_id",8)
                .params("amount",1)
                .loader(getContext())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        mShopCount++;
                        mCircleTextView.setVisibility(View.VISIBLE);
                        mCircleTextView.setText(String.valueOf(mShopCount));
                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String msg) {
                        Log.e("error_add",code+" "+msg);
                    }
                })
                .build()
                .post();
    }
}

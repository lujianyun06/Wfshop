package com.tobyli16.profilelibrary.personal.order.orderAdapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.tobyli16.profilelibrary.R;
import com.tobyli16.profilelibrary.personal.order.IStatusChange;
import com.tobyli16.profilelibrary.personal.order.orderBean.GoodsOrderInfo;
import com.tobyli16.profilelibrary.personal.order.orderBean.OrderGoodsItem;
import com.tobyli16.profilelibrary.personal.order.orderBean.OrderPayInfo;

import java.util.List;

import cn.bupt.wfshop.net.RestClient;
import cn.bupt.wfshop.net.callback.IError;
import cn.bupt.wfshop.net.callback.ISuccess;
import cn.bupt.wfshop.ui.toast.ToastTest;


/**
 * Created by Administrator on 2016/11/9.
 */

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Object> data;
    private View headerView;
    private int ITEM_HEADER = 1,ITEM_CONTENT = 2,ITEM_FOOTER = 3;
    private IStatusChange statusChange;

    public OrderAdapter(Context context, List<Object> data, IStatusChange statusChange){
        this.context = context;
        this.data = data;
        this.statusChange = statusChange;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType == ITEM_HEADER) {
            view = LayoutInflater.from(context).inflate(R.layout.item_allorder_header, parent, false);
            return new OrderAdapter.MyViewHolderHeader(view);
        }else if(viewType == ITEM_CONTENT){
            view = LayoutInflater.from(context).inflate(R.layout.item_allorder_content, parent, false);
            return new OrderAdapter.MyViewHolderContent(view);
        }else if(viewType == ITEM_FOOTER){
            view = LayoutInflater.from(context).inflate(R.layout.item_allorder_footer, parent, false);
            return new OrderAdapter.MyViewHolderFooter(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if(holder instanceof MyViewHolderHeader){
            GoodsOrderInfo datas = (GoodsOrderInfo)data.get(position);
            ((OrderAdapter.MyViewHolderHeader) holder).tvAllOrderItemid.setText("订单编号：" + datas.getOrderCode());
            if(datas.getStatus().equals("1")){
                ((OrderAdapter.MyViewHolderHeader) holder).tvAllOrderItemState.setText("待付款");
            }else if(datas.getStatus().equals("2")){
                ((OrderAdapter.MyViewHolderHeader) holder).tvAllOrderItemState.setText("待收货");
            }else if(datas.getStatus().equals("3")){
                ((OrderAdapter.MyViewHolderHeader) holder).tvAllOrderItemState.setText("待评价");
            }else if(datas.getStatus().equals("4")){
                ((OrderAdapter.MyViewHolderHeader) holder).tvAllOrderItemState.setText("退换修");
            }
        }else if(holder instanceof MyViewHolderContent) {
            OrderGoodsItem datas = (OrderGoodsItem)data.get(position);
            Glide.with(context).load(datas.getProductPic())
                    .into(((OrderAdapter.MyViewHolderContent) holder).ivAllOrderItemPic);
            ((OrderAdapter.MyViewHolderContent) holder).tvAllOrderItemTitle.setText(datas.getProductName());
            ((OrderAdapter.MyViewHolderContent) holder).tvAllOrderItemNum.setText("共" + datas.getCount() + "件");
            ((OrderAdapter.MyViewHolderContent) holder).tvAllOrderItemPrice.setText("¥" + datas.getTotalPrice());

            final int pos = datas.getOrderid();

            ((MyViewHolderContent) holder).llAllOrderItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Intent intent = new Intent(context, OrderDetailActivity.class);
                    Bundle bundle = new Bundle();
                    //类型是确认订单
                    bundle.putString("mType", "orderdetail");
                    bundle.putInt("mOrderId",pos);
                    intent.putExtras(bundle);
                    context.startActivity(intent);*/
                }
            });

        }else if(holder instanceof MyViewHolderFooter) {
            OrderPayInfo datas = (OrderPayInfo)data.get(position);
            ((OrderAdapter.MyViewHolderFooter) holder).tvAllOrderTotal.setText(datas.getTotalAmount() + "");
            final int pos = datas.getId();
            final String thisPrice = String.valueOf(datas.getTotalAmount());


            if(datas.getStatus().equals("1")){
                ((OrderAdapter.MyViewHolderFooter) holder).tvAllOrderSubmit.setText("确认付款");
                ((OrderAdapter.MyViewHolderFooter) holder).tvAllOrderCancel.setText("取消付款");
                ((MyViewHolderFooter) holder).tvAllOrderSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(context,"确认付款",Toast.LENGTH_LONG).show();
                        //一个物品在列表中的显示，在下面的是物品详情(postion大)，下面是订单详情(psotion小)
                        //虽然是同一个物品，但是这是两个item的显示，如果图片没有，则没有物品详情，则testPos不用减1，该项就是订单详情
                        int testPos = position;
                        String picUrl = "";
                        String orderNo = "";
                        while (testPos >= 0) {
                            if (data.get(testPos) instanceof  OrderGoodsItem) {
                                OrderGoodsItem orderGoodsItem = (OrderGoodsItem)(data.get(testPos));
                                picUrl = orderGoodsItem.getProductPic();
                                break;
                            }
                            testPos--;
                        }
                        if (picUrl.equals("")) {
                            testPos = position;
                        }
                        while (testPos >= 0) {
                            if (data.get(testPos) instanceof  GoodsOrderInfo) {
                                GoodsOrderInfo goodsOrderInfo = (GoodsOrderInfo)(data.get(testPos));
                                orderNo = goodsOrderInfo.getOrderCode();
                                break;
                            }
                            testPos--;
                        }
                        //转到支付页面
                        Fragment fragment = (Fragment) ARouter.getInstance()
                                .build("/profileOrderConfirm/profileOrderConfirm/")
                                .withString("orderId",orderNo)
                                .withString("cost",thisPrice)
                                .navigation();
                        statusChange.gotoFragment(fragment);
                    }
                });
                ((MyViewHolderFooter) holder).tvAllOrderCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int testPos = position;
                        String picUrl = "";
                        String orderNo = "";
                        while (testPos >= 0) {
                            if (data.get(testPos) instanceof  OrderGoodsItem) {
                                OrderGoodsItem orderGoodsItem = (OrderGoodsItem)(data.get(testPos));
                                picUrl = orderGoodsItem.getProductPic();
                                break;
                            }
                            testPos--;
                        }
                        if (picUrl.equals("")) {
                            testPos = position;
                        }
                        while (testPos >= 0) {
                            if (data.get(testPos) instanceof  GoodsOrderInfo) {
                                GoodsOrderInfo goodsOrderInfo = (GoodsOrderInfo)(data.get(testPos));
                                orderNo = goodsOrderInfo.getOrderCode();
                                break;
                            }
                            testPos--;
                        }

                        RestClient.builder()
                                .url("https://wfshop.andysheng.cn/order/"+orderNo+"/delete/")
                                .success(new ISuccess() {
                                    @Override
                                    public void onSuccess(String response) {
                                    }
                                })
                                .error(new IError() {
                                    @Override
                                    public void onError(int code, String msg) {
                                        Log.e("test_error", code + " " + msg);
                                    }
                                })
                                .build()
                                .post();
                        Toast.makeText(context,"取消付款",Toast.LENGTH_LONG).show();
                        statusChange.statusChange(0);
                    }
                });
            }else if (datas.getStatus().equals("2")) {
                ((OrderAdapter.MyViewHolderFooter) holder).tvAllOrderSubmit.setText("确认收货");
                ((OrderAdapter.MyViewHolderFooter) holder).tvAllOrderCancel.setText("联系客服");
                ((MyViewHolderFooter) holder).tvAllOrderSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int testPos = position;
                        String picUrl = "";
                        String orderNo = "";
                        while (testPos >= 0) {
                            if (data.get(testPos) instanceof  OrderGoodsItem) {
                                OrderGoodsItem orderGoodsItem = (OrderGoodsItem)(data.get(testPos));
                                picUrl = orderGoodsItem.getProductPic();
                                break;
                            }
                            testPos--;
                        }
                        if (picUrl.equals("")) {
                            testPos = position;
                        }
                        while (testPos >= 0) {
                            if (data.get(testPos) instanceof  GoodsOrderInfo) {
                                GoodsOrderInfo goodsOrderInfo = (GoodsOrderInfo)(data.get(testPos));
                                orderNo = goodsOrderInfo.getOrderCode();
                                break;
                            }
                            testPos--;
                        }
                        RestClient.builder()
                                .url("https://wfshop.andysheng.cn/order/"+orderNo+"/confirm/")
                                .build()
                                .post();
                        ToastTest.showToast(context,"确认收货",Toast.LENGTH_SHORT);
                        statusChange.statusChange(3);
                    }
                });
                ((MyViewHolderFooter) holder).tvAllOrderCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context,"联系客服",Toast.LENGTH_LONG).show();
                    }
                });
            }else if (datas.getStatus().equals("3")) {
                ((OrderAdapter.MyViewHolderFooter) holder).tvAllOrderSubmit.setText("填写评价");
                ((OrderAdapter.MyViewHolderFooter) holder).tvAllOrderCancel.setText("删除订单");
                ((MyViewHolderFooter) holder).tvAllOrderSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int testPos = position;
                        String picUrl = "";
                        String orderNo = "";
                        while (testPos >= 0) {
                            if (data.get(testPos) instanceof  OrderGoodsItem) {
                                OrderGoodsItem orderGoodsItem = (OrderGoodsItem)(data.get(testPos));
                                picUrl = orderGoodsItem.getProductPic();
                                break;
                            }
                            testPos--;
                        }
                        while (testPos >= 0) {
                            if (data.get(testPos) instanceof  GoodsOrderInfo) {
                                GoodsOrderInfo goodsOrderInfo = (GoodsOrderInfo)(data.get(testPos));
                                orderNo = goodsOrderInfo.getOrderCode();
                                break;

                            }
                            testPos--;
                        }
                        Fragment fragment = (Fragment) ARouter.getInstance().build("/profile/order/comment/")
                                .withString("imgUrl",picUrl)
                                .withString("orderId",orderNo)
                                .navigation();
                        statusChange.gotoFragment(fragment);
                    }
                });
                ((MyViewHolderFooter) holder).tvAllOrderCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int testPos = position;
                        String picUrl = "";
                        String orderNo = "";
                        while (testPos >= 0) {
                            if (data.get(testPos) instanceof  OrderGoodsItem) {
                                OrderGoodsItem orderGoodsItem = (OrderGoodsItem)(data.get(testPos));
                                picUrl = orderGoodsItem.getProductPic();
                                break;

                            }
                            testPos--;
                        }
                        while (testPos >= 0) {
                            if (data.get(testPos) instanceof  GoodsOrderInfo) {
                                GoodsOrderInfo goodsOrderInfo = (GoodsOrderInfo)(data.get(testPos));
                                orderNo = goodsOrderInfo.getOrderCode();
                                break;

                            }
                            testPos--;
                        }
                        RestClient.builder()
                                .url("https://wfshop.andysheng.cn/order/"+orderNo+"/delete/")
                                .success(new ISuccess() {
                                    @Override
                                    public void onSuccess(String response) {
                                    }
                                })
                                .build()
                                .post();
                        Toast.makeText(context,"删除订单",Toast.LENGTH_LONG).show();
                        statusChange.statusChange(0);
                    }
                });
            } else if (datas.getStatus().equals("3")) {
                ((OrderAdapter.MyViewHolderFooter) holder).tvAllOrderSubmit.setText("填写评价");
                ((OrderAdapter.MyViewHolderFooter) holder).tvAllOrderCancel.setVisibility(View.INVISIBLE);
                ((MyViewHolderFooter) holder).tvAllOrderSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context,"填写评价",Toast.LENGTH_LONG).show();
                    }
                });
            } else if (datas.getStatus().equals("4")) {
                ((OrderAdapter.MyViewHolderFooter) holder).tvAllOrderSubmit.setVisibility(View.INVISIBLE);
                ((OrderAdapter.MyViewHolderFooter) holder).tvAllOrderCancel.setText("退换货");
            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(data.get(position) instanceof GoodsOrderInfo) {
            return ITEM_HEADER;
        }else if(data.get(position) instanceof OrderGoodsItem){
            return ITEM_CONTENT;
        }else if(data.get(position) instanceof OrderPayInfo){
            return ITEM_FOOTER;
        }
        return ITEM_CONTENT;
    }

    @Override
    public int getItemCount() {
        int count = (data == null ? 0 : data.size());
        if(headerView != null){
            count++;
        }
        return count;
    }


    class MyViewHolderContent extends RecyclerView.ViewHolder
    {
        private ImageView ivAllOrderItemPic;
        private TextView tvAllOrderItemTitle;
        private TextView tvAllOrderItemPrice;
        private LinearLayout llAllOrderItem;
        private TextView tvAllOrderItemNum;

        public MyViewHolderContent(View view)
        {
            super(view);
            llAllOrderItem = (LinearLayout) view.findViewById(R.id.ll_item_allorder);
            ivAllOrderItemPic = (ImageView) view.findViewById(R.id.iv_item_allorder_pic);
            tvAllOrderItemTitle = (TextView) view.findViewById(R.id.tv_item_allorder_title);
            tvAllOrderItemPrice = (TextView) view.findViewById(R.id.tv_item_allorder_item_price);
            tvAllOrderItemNum = (TextView) view.findViewById(R.id.tv_item_allorder_item_num);
        }
    }

    class MyViewHolderHeader extends RecyclerView.ViewHolder
    {
        private LinearLayout llAllOrderItem;
        private TextView tvAllOrderItemState,tvAllOrderItemid;

        public MyViewHolderHeader(View view)
        {
            super(view);
            llAllOrderItem = (LinearLayout) view.findViewById(R.id.ll_item_allorder_header);
            tvAllOrderItemid = (TextView) view.findViewById(R.id.tv_item_allorder_orderid);
            tvAllOrderItemState = (TextView) view.findViewById(R.id.tv_item_allorder_state);
        }
    }

    class MyViewHolderFooter extends RecyclerView.ViewHolder
    {
        private TextView tvAllOrderTotal,tvAllOrderSubmit,tvAllOrderCancel;

        public MyViewHolderFooter(View view)
        {
            super(view);
            tvAllOrderTotal = (TextView) view.findViewById(R.id.tv_item_allorder_total);
            tvAllOrderSubmit = (TextView) view.findViewById(R.id.tv_item_allorder_submit);
            tvAllOrderCancel = (TextView) view.findViewById(R.id.tv_item_allorder_cancel);
        }
    }


    public View getHeaderView(){
        return headerView;
    }

    private OrderAdapter.OnItemClickListener mOnItemClickListener;
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OrderAdapter.OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }
}

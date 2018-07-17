package com.tobyli16.profilelibrary.personal.order.orderBean;

/**
 * Created by admin on 2016/11/8.
 */

/**
 * orderDetailList字段中每一项的order字段
 */
public class GoodsOrderInfo {

    private String orderCode;
    private String status;

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

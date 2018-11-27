package cn.bupt.wfshop.pay;

public class PayEvent {
    public static int ALIPAY = 0;

    public static int WECHATPAY = 1;

    final public static int PAY_SUCCESS = 10;
    final public static int PAY_FAIL = 11;
    final public static int PAY_PAYING = 12;
    final public static int PAY_CANCEL = 13;
    final public static int PAY_CONNECT_ERROR = 4;
    public int tag;
    public int status;
    public Object data;

    public PayEvent(int tag, int status, Object data){
        this.tag = tag;
        this.status = status;
        this.data = data;
    }


}

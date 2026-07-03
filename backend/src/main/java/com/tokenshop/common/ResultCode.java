package com.tokenshop.common;

/**
 * 统一返回码枚举
 */
public enum ResultCode {

    // 成功
    SUCCESS(200, "操作成功"),

    // 客户端错误 400-499
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未登录或登录已过期"),
    FORBIDDEN(403, "没有权限"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不允许"),
    CONFLICT(409, "资源冲突"),
    VALIDATION_ERROR(422, "参数校验失败"),
    TOO_MANY_REQUESTS(429, "请求过于频繁，请稍后再试"),

    // 业务错误 1000-1999
    USER_NOT_FOUND(1001, "用户不存在"),
    USER_ALREADY_EXISTS(1002, "用户已存在"),
    PASSWORD_ERROR(1003, "密码错误"),
    USER_DISABLED(1004, "用户已被禁用"),
    PHONE_ALREADY_EXISTS(1005, "手机号已被注册"),
    EMAIL_ALREADY_EXISTS(1006, "邮箱已被注册"),
    VERIFICATION_CODE_ERROR(1007, "验证码错误"),
    VERIFICATION_CODE_EXPIRED(1008, "验证码已过期"),

    // Token 商品错误 2000-2099
    TOKEN_NOT_FOUND(2001, "Token 不存在"),
    TOKEN_OFFLINE(2002, "Token 已下架"),
    STOCK_INSUFFICIENT(2003, "库存不足"),
    TOKEN_CREATE_FAILED(2004, "Token 创建失败"),

    // 订单错误 3000-3099
    ORDER_NOT_FOUND(3001, "订单不存在"),
    ORDER_STATUS_ERROR(3002, "订单状态异常"),
    ORDER_CANCEL_FAILED(3003, "订单取消失败"),
    ORDER_CONFIRM_FAILED(3004, "订单确认失败"),

    // 钱包错误 4000-4099
    WALLET_NOT_FOUND(4001, "钱包不存在"),
    BALANCE_INSUFFICIENT(4002, "余额不足"),
    RECHARGE_FAILED(4003, "充值失败"),
    WITHDRAW_FAILED(4004, "提现失败"),
    WITHDRAW_AMOUNT_INSUFFICIENT(4005, "提现金额不足"),

    // 支付错误 5000-5099
    PAYMENT_FAILED(5001, "支付失败"),
    PAYMENT_TIMEOUT(5002, "支付超时"),
    PAYMENT_CALLBACK_ERROR(5003, "支付回调异常"),

    // 服务器错误 500-599
    INTERNAL_ERROR(500, "服务器内部错误"),
    SERVICE_UNAVAILABLE(503, "服务不可用"),

    // 数据错误 6000-6099
    DATA_ERROR(6001, "数据异常"),
    DATA_NOT_FOUND(6002, "数据不存在"),
    DATA_DUPLICATE(6003, "数据重复");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static ResultCode getByCode(int code) {
        for (ResultCode resultCode : values()) {
            if (resultCode.code == code) {
                return resultCode;
            }
        }
        return INTERNAL_ERROR;
    }
}

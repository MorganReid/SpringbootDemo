package com.example.demo.exception;

/**
 * API 统一返回错误码
 */
public class AjaxCode {
    /**
     * SUCCESS CODE
     */
    public static final int SUCCESS_CODE = 200;
    /**
     * 参数错误[状态码]
     */
    public static final int ILLEGAL_ARGU_CODE = 201;
    /**
     * ERROR CODE
     */
    public static final int SERVER_ERROR = 500;
    /**
     * 用户未登录[状态码]
     */
    public static final int NOT_LOGIN_CODE = 1001;
    /**
     * 动态验证码[动态码]
     */
    public static final int NEED_VERIFY_CODE = 1002;
    /**
     * 没有权限[权限检查]
     */
    public static final int NO_ACCESS_CODE = 1003;
    /**
     * 无效的参数[参数验证]
     */
    public static final int ILLEGAL_PARAMS_CODE = 1004;
    /**
     * 业务规则限制[业务规则检查]
     */
    public static final int BUSINESS_RULES_CODE = 1005;
    /**
     * 业务数据限制[业务数据检查]
     */
    public static final int NO_PERMISSION = 1006;

    public static final int NO_DATA = 1007;

    /**
     * 分配给在线闸机使用的号段
     */
    public static final int GATE_NO_ERROR_CODE = 3001;
    public static final int GATE_PASSWORD_ERROR_CODE = 3002;
    public static final int GATE_NO_ALREADY_USED_CODE = 3003;
    public static final int GATE_DISABLE_ERROR_CODE = 3004;
    public static final int GATE_STOP_JOB_ERROR_CODE = 3005;
    public static final String GATE_NO_ERROR_MSG = "请输入正确的设备编号";
    public static final String GATE_PASSWORD_ERROR_MSG = "请输入正确的设备密码";
    public static final String GATE_NO_ALREADY_USED_MSG = "该设备编号已绑其他设备，请核实设备编号";

    public static final String ILLEGAL_MSG = "请求参数验证错误";
    public static final String NOTLOGIN_MSG = "您的账号还未登录";
    public static final String ERROR_MSG = "刚系统君走神了, 请稍候再试";
    public static final String PERMISSION_MSG = "您无权操作此数据";
    public static final String FORBID_MSG = "当前用户权限不足, 请联系管理员";
}

package com.example.demo.exception;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @author: hujun
 * @date: 2021/03/06  09:50
 */
@Data
public class AjaxResult<T> implements Serializable {
    private static final long serialVersionUID = 6790935501322497972L;
    /**
     * 返回代码
     */
    private int code;
    /**
     * 返回 msg
     */
    private String msg;
    /**
     * 返回的数据
     */
    private T data;


    public AjaxResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }


    public static <T> AjaxResult<T> createSuccessResult(T data) {
        return new AjaxResult<T>(AjaxCode.SUCCESS_CODE, StringUtils.EMPTY, data);
    }

    public static<T> AjaxResult<T> createFailedResult(T data, int code, String msg) {
        return new AjaxResult<T>(code, msg, data);
    }

    public static<T> AjaxResult<T> createFailedResult(int code, String msg) {
        return new AjaxResult<T>(code, msg, null);
    }


    public void showkey(T genericObj){

    }

}

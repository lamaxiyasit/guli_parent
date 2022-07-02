package com.atguigu.guli.service.base.exception;

import com.atguigu.guli.common.base.result.ResultCodeEnum;
import lombok.Data;

/**
 * @Description TODO
 * @Auther chengjiahui
 * @Date 2022/4/14 10:34
 * @Version 1.0
 */
@Data
public class GuliException extends RuntimeException{
    private Integer code;

    public GuliException(String message, Integer code){
        super(message);
        this.code=code;
    }

    public GuliException(ResultCodeEnum resultCodeEnum){
        super(resultCodeEnum.getMessage());
        this.code=resultCodeEnum.getCode();
    }

    @Override
    public String toString() {
        return "GuliException{" +
                "code=" + code +
                ",message=" + this.getMessage() +
                '}';
    }
}

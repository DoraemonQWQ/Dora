package top.doraemonqwq.dora.security.pojo;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import lombok.Data;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Doraemon
 * @date 2021-09-12
 * 返回响应体通用类
 */
@Data
public class ResponseResult {
    /**
     * code 响应码
     */
    private Integer statusCode;
    /**
     * 响应内容
     */
    private JSON json;
    /**
     * msg 响应内容的说明
     */
    private String msg;
    /**
     * path 请求的url
     */
    private String path;
    /**
     * 请求时间
     */
    private String date;

    public static ResponseResult create() {
        return new ResponseResult();
    }

    public String error(Integer statusCode, String msg, String path) {
        this.statusCode = statusCode;
        /**
         * 当error时，msg会保存对应的错误提示
         */
        this.msg = msg;
        this.path = path;
        this.date = DateUtil.date().toString();
        return JSONUtil.parse(this).toString();
    }

    public JSON ok(JSON json, String path, String msg) {
        this.msg = msg;
        this.json = json;
        this.path = path;
        this.statusCode = HttpStatus.HTTP_OK;
        this.date = DateUtil.date().toString();

        return JSONUtil.parse(this);

    }


}

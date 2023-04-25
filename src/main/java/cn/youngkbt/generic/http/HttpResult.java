package cn.youngkbt.generic.http;


import java.util.HashMap;
import java.util.Map;

/**
 * @author kele-Bingtang
 * @date 2022/4/30 14:55
 * @note 请求响应封装
 */
public class HttpResult {

    private HttpResult() {
    }

    public static <T> Response<T> response(T data, ResponseStatusEnum responseStatusEnum) {
        Response<T> response = new Response<>();
        response.setData(data);
        response.setCode(responseStatusEnum.getCode());
        response.setStatus(responseStatusEnum.getStatus());
        response.setMessage(responseStatusEnum.getMessage());
        return response;
    }

    public static <T> Response<T> response(T data, ResponseStatusEnum responseStatusEnum, String message) {
        Response<T> response = new Response<>();
        response.setData(data);
        response.setCode(responseStatusEnum.getCode());
        response.setStatus(responseStatusEnum.getStatus());
        response.setMessage(message);
        return response;
    }

    public static <T> Response<Map<String, T>> response(String key, T data, ResponseStatusEnum status) {
        Map<String, T> map = new HashMap<>(16);
        Response<Map<String, T>> response = new Response<>();
        map.put(key, data);
        response.setData(map);
        response.setCode(status.getCode());
        response.setStatus(status.getStatus());
        response.setMessage(status.getMessage());
        return response;
    }

    public static <T> Response<T> response(T data, Integer code, String status, String message) {
        Response<T> response = new Response<>();
        response.setData(data);
        response.setCode(code);
        response.setStatus(status);
        response.setMessage(message);
        return response;
    }

    public static <T> Response<Map<String, T>> response(String key, T data, Integer code, String status, String message) {
        Map<String, T> map = new HashMap<>(16);
        Response<Map<String, T>> response = new Response<>();
        map.put(key, data);
        response.setData(map);
        response.setCode(code);
        response.setStatus(status);
        response.setMessage(message);
        return response;
    }

    public static <T> Response<T> ok(T data) {
        Response<T> response = new Response<>();
        response.setData(data);
        response.setCode(ResponseStatusEnum.SUCCESS.getCode());
        response.setStatus(ResponseStatusEnum.SUCCESS.getStatus());
        response.setMessage(ResponseStatusEnum.SUCCESS.getMessage());
        return response;
    }

    public static <T> Response<Map<String, T>> ok(String key, T data) {
        Map<String, T> map = new HashMap<>(16);
        Response<Map<String, T>> response = new Response<>();
        map.put(key, data);
        response.setData(map);
        response.setCode(ResponseStatusEnum.SUCCESS.getCode());
        response.setStatus(ResponseStatusEnum.SUCCESS.getStatus());
        response.setMessage(ResponseStatusEnum.SUCCESS.getMessage());
        return response;
    }

    public static Response<String> okMessage(String message) {
        Response<String> response = new Response<>();
        response.setData(null);
        response.setCode(ResponseStatusEnum.SUCCESS.getCode());
        response.setStatus(ResponseStatusEnum.SUCCESS.getStatus());
        response.setMessage(message);
        return response;
    }

    public static <T> Response<T> fail(T data) {
        Response<T> response = new Response<>();
        response.setData(data);
        response.setCode(ResponseStatusEnum.FAIL.getCode());
        response.setStatus(ResponseStatusEnum.FAIL.getStatus());
        response.setMessage(ResponseStatusEnum.FAIL.getMessage());
        return response;
    }

    public static <T> Response<Map<String, T>> fail(String key, T data) {
        Map<String, T> map = new HashMap<>(16);
        Response<Map<String, T>> response = new Response<>();
        map.put(key, data);
        response.setData(map);
        response.setCode(ResponseStatusEnum.FAIL.getCode());
        response.setStatus(ResponseStatusEnum.FAIL.getStatus());
        response.setMessage(ResponseStatusEnum.FAIL.getMessage());
        return response;
    }
    
    public static Response<String> failMessage(String message) {
        Response<String> response = new Response<>();
        response.setData(null);
        response.setCode(ResponseStatusEnum.FAIL.getCode());
        response.setStatus(ResponseStatusEnum.FAIL.getStatus());
        response.setMessage(message);
        return response;
    }

    public static <T> Response<T> error(T data) {
        Response<T> response = new Response<>();
        response.setData(data);
        response.setCode(ResponseStatusEnum.ERROR.getCode());
        response.setStatus(ResponseStatusEnum.ERROR.getStatus());
        response.setMessage(ResponseStatusEnum.ERROR.getMessage());
        return response;
    }

    public static <T> Response<Map<String, T>> error(String key, T data) {
        Map<String, T> map = new HashMap<>(16);
        Response<Map<String, T>> response = new Response<>();
        map.put(key, data);
        response.setData(map);
        response.setCode(ResponseStatusEnum.ERROR.getCode());
        response.setStatus(ResponseStatusEnum.ERROR.getStatus());
        response.setMessage(ResponseStatusEnum.ERROR.getMessage());
        return response;
    }

    public static Response<String> errorMessage(String message) {
        Response<String> response = new Response<>();
        response.setData(null);
        response.setCode(ResponseStatusEnum.ERROR.getCode());
        response.setStatus(ResponseStatusEnum.ERROR.getStatus());
        response.setMessage(message);
        return response;
    }

    public static <T> Response<T> okOrFail(T data, String message) {
        if (null == data) {
            return response(null, ResponseStatusEnum.FAIL, ResponseStatusEnum.FAIL.getMessage());
        }
        return response(data, ResponseStatusEnum.SUCCESS, message);
    }

    public static <T> Response<T> okOrFail(T data) {
        if (null == data) {
            return response(null, ResponseStatusEnum.FAIL);
        }
        return response(null, ResponseStatusEnum.SUCCESS);
    }

}

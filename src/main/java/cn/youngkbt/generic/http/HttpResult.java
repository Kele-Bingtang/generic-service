package cn.youngkbt.generic.http;



import java.util.HashMap;
import java.util.Map;

/**
 * @author kele-Bingtang
 * @date 2022/4/30 14:55
 * @note 请求响应封装
 */
public class HttpResult {
    
    private HttpResult() {}
    
    public static Response processResult(Object data, ResponseStatusEnum status) {
        Response response = new Response();
        response.setData(data);
        response.setCode(status.getCode());
        response.setStatus(status.getStatus());
        response.setMessage(status.getMessage());
        return response;
    }

    public static Response processResult(String key, Object data, ResponseStatusEnum status) {
        Map<String, Object> map = new HashMap<>();
        Response response = new Response();
        map.put(key, data);
        response.setData(map);
        response.setCode(status.getCode());
        response.setStatus(status.getStatus());
        response.setMessage(status.getMessage());
        return response;
    }

    public static Response processResult(Object data, Integer code, String codeMessage, String message) {
        Response response = new Response();
        response.setData(data);
        response.setCode(code);
        response.setStatus(codeMessage);
        response.setMessage(message);
        return response;
    }

    public static Response processResult(String key, Object data, Integer code, String codeMessage, String message) {
        Map<String, Object> map = new HashMap<>();
        Response response = new Response();
        map.put(key, data);
        response.setData(map);
        response.setCode(code);
        response.setStatus(codeMessage);
        response.setMessage(message);
        return response;
    }

    public static Response ok(Object data) {
        Response response = new Response();
        response.setData(data);
        response.setCode(ResponseStatusEnum.SUCCESS.getCode());
        response.setStatus(ResponseStatusEnum.SUCCESS.getStatus());
        response.setMessage(ResponseStatusEnum.SUCCESS.getMessage());
        return response;
    }

    public static Response ok(String key, Object data) {
        Map<String, Object> map = new HashMap<>();
        Response response = new Response();
        map.put(key, data);
        response.setData(map);
        response.setCode(ResponseStatusEnum.SUCCESS.getCode());
        response.setStatus(ResponseStatusEnum.SUCCESS.getStatus());
        response.setMessage(ResponseStatusEnum.SUCCESS.getMessage());
        return response;
    }

    public static Response fail(Object data) {
        Response response = new Response();
        response.setData(data);
        response.setCode(ResponseStatusEnum.FAIL.getCode());
        response.setStatus(ResponseStatusEnum.FAIL.getStatus());
        response.setMessage(ResponseStatusEnum.FAIL.getMessage());
        return response;
    }

    public static Response fail(String key, Object data) {
        Map<String, Object> map = new HashMap<>();
        Response response = new Response();
        map.put(key, data);
        response.setData(map);
        response.setCode(ResponseStatusEnum.FAIL.getCode());
        response.setStatus(ResponseStatusEnum.FAIL.getStatus());
        response.setMessage(ResponseStatusEnum.FAIL.getMessage());
        return response;
    }

    public static Response error(Object data) {
        Response response = new Response();
        response.setData(data);
        response.setCode(ResponseStatusEnum.ERROR.getCode());
        response.setStatus(ResponseStatusEnum.ERROR.getStatus());
        response.setMessage(ResponseStatusEnum.ERROR.getMessage());
        return response;
    }

    public static Response error(String key, Object data) {
        Map<String, Object> map = new HashMap<>();
        Response response = new Response();
        map.put(key, data);
        response.setData(map);
        response.setCode(ResponseStatusEnum.ERROR.getCode());
        response.setStatus(ResponseStatusEnum.ERROR.getStatus());
        response.setMessage(ResponseStatusEnum.ERROR.getMessage());
        return response;
    }

    public static Response okOrFail(Object data) {
        if(null == data) {
            return fail(null);
        }
        return ok(data);
    }
    
}

package cn.youngkbt.intercept;

import cn.youngkbt.utils.SecureUtil;
import cn.youngkbt.utils.StringUtil;
import org.slf4j.MDC;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Kele-Bingtang
 * @date 2022/12/8 23:19
 * @note
 */
public class LogInterceptor implements HandlerInterceptor {

    private static final String TRACE_ID = "TRACE_ID";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String tid = SecureUtil.noBarUUID();
        // 可以考虑让客户端传入链路 ID，但需保证一定的复杂度唯一性；如果没使用默认 UUID 自动生成
        if (!StringUtil.isEmpty(request.getHeader(TRACE_ID))) {
            tid = request.getHeader(TRACE_ID);
        }
        MDC.put(TRACE_ID, tid);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) {
        MDC.remove(TRACE_ID);
    }

}

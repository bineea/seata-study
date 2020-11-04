package com.tryimpl.globaltransaction.interceptor;

import com.tryimpl.globaltransaction.transaction.GlobalTransantionManager;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 因为服务之间通信为HTTP协议
 * 所以通过创建Http拦截器，服务调用者将groupId传递给服务提供者
 */
@Component
public class RequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String groupIdStr = request.getHeader("groupId");
        String transactionCountStr = request.getHeader("transactionCount");
        if(StringUtils.hasText(groupIdStr) && StringUtils.hasText(transactionCountStr)) {
            int transactionCount = Integer.parseInt(transactionCountStr);
            System.out.println("拦截器groupId:"+groupIdStr+"拦截器transactionCount:"+transactionCount);
            GlobalTransantionManager.setCurrentGroupId(groupIdStr);
            GlobalTransantionManager.setCurrentTransactionCount(transactionCount);
        }
        return true;
    }
}

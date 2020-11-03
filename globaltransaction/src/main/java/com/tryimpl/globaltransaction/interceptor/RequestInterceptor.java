package com.tryimpl.globaltransaction.interceptor;

import com.tryimpl.globaltransaction.transaction.GlobalTransantionManager;
import org.springframework.stereotype.Component;
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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String groupId = request.getHeader("groupId");
        System.out.println("拦截器groupId:"+groupId);
        GlobalTransantionManager.setCurrentGroupId(groupId);
        return true;
    }
}

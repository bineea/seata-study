package com.tryimpl.globaltransaction.transaction;

import com.alibaba.fastjson.JSONObject;
import com.tryimpl.globaltransaction.netty.NettyClient;
import netscape.javascript.JSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class GlobalTransantionManager {

    private static NettyClient nettyClient = new NettyClient();

    private static ThreadLocal<DefaultTransaction> currentTransaction = new ThreadLocal<>();
    private static ThreadLocal<String> currentGroupId = new ThreadLocal<>();

    //通过set方法实现依赖注入
    @Autowired
    public void setNettyClient(NettyClient nettyClient) {
        GlobalTransantionManager.nettyClient = nettyClient;
    }

    public static Map<String, DefaultTransaction> DEFAULTTRANSACION_MAP = new HashMap<>();

    /**
     * 获取并创建全局事务Id
     * @return
     */
    public static String getOrCreateGroupId() {
        if (currentGroupId.get() != null) {
            return currentGroupId.get();
        } else {
            String groupId = UUID.randomUUID().toString();
            JSONObject obj = new JSONObject();
            obj.put("command", "create");
            obj.put("groupId", groupId);
            nettyClient.send(obj);
            currentGroupId.set(groupId);
            System.out.println("创建全局事务");
            return groupId;
        }
    }

    /**
     * 创建分支事务
     */
    public static DefaultTransaction getOrCreateTransaction(String groupId) {
        if(currentTransaction.get() != null) {
            return currentTransaction.get();
        } else {
            String transactionId = UUID.randomUUID().toString();
            DefaultTransaction defaultTransaction = new DefaultTransaction(groupId, transactionId);
            currentTransaction.set(defaultTransaction);
            DEFAULTTRANSACION_MAP.put(groupId, defaultTransaction);
            return defaultTransaction;
        }
    }

    /**
     * 注册分支事务
     */
    public static DefaultTransaction registerTransaction(DefaultTransaction defaultTransaction, TransactionType transactionType) {
        JSONObject obj = new JSONObject();
        obj.put("command", "register");
        obj.put("groupId", defaultTransaction.getGroupId());
        obj.put("transactionId", defaultTransaction.getTransactionId());
        obj.put("transactionType", transactionType);
        nettyClient.send(obj);
        System.out.println("注册分支事务");
        return defaultTransaction;
    }

    /**
     * 提交全局事务
     */
    public static void commitGlobalTransaction(String groupId) {
        JSONObject obj = new JSONObject();
        obj.put("command", "commit");
        obj.put("groupId", groupId);
        nettyClient.send(obj);
        System.out.println("提交全局事务");
    }

    public static void setCurrentGroupId(String groupId) {
        currentGroupId.set(groupId);
    }

    public static String getCurrentGroupId() {
        return currentGroupId.get();
    }

    public static DefaultTransaction getCurrentDefaultTransaction() {
        return currentTransaction.get();
    }
}

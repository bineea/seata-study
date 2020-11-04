package com.example.seatatc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 事务协调者
 * 1.创建并保存全局事务信息（即保存全局事务与分支事务对应关系）
 * 2.接收所有分支事务的操作指令，并根据分支事务操作指令推断全局事务操作指令
 * 3.根据全局事务操作指令，通知各个分支事务执行响应操作
 */

public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private static ConcurrentHashMap<String, HashMap<String, String>>  transactionRelateMap = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, Boolean> transactionIsEndMap = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, Integer> transactionCountMap = new ConcurrentHashMap<>();

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.add(channel);
    }

    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("接收操作数据："+msg.toString());

        JSONObject jsonObject = JSON.parseObject((String) msg);
        String command = jsonObject.getString("command"); // create-创建全局事务，register-注册分支事务
        String groupId = jsonObject.getString("groupId");   // 全局事务id
        String transactionType = jsonObject.getString("transactionType"); // 分支事务类型，commit-待提交，rollback-待回滚
        String transactionId = jsonObject.getString("transactionId");   // 分支事务id
        Boolean transactionIsEnd = jsonObject.getBoolean("transactionIsEnd"); // 分支事务是否为最后的结束事务
        Integer transactionCount = jsonObject.getInteger("transactionCount"); // 分支事务的数量

        if("create".equals(command)) {
            transactionRelateMap.put(groupId, new HashMap<>());
            transactionIsEndMap.put(groupId, false);
        } else if("register".equals(command)) {
            transactionRelateMap.get(groupId).put(transactionId, transactionType);

            if(transactionIsEnd) {
                transactionIsEndMap.put(groupId, true);
                transactionCountMap.put(groupId, transactionCount);
            }

            if(transactionIsEndMap.get(groupId) && transactionCountMap.get(groupId).equals(transactionRelateMap.get(groupId).size())) {
                transactionRelateMap.get(groupId).keySet().forEach(k -> System.out.println(transactionRelateMap.get(groupId).get(k)));
                if(transactionRelateMap.get(groupId).containsValue("rollback")) {
                    responseResult(groupId, "rollback");
                } else {
                    responseResult(groupId, "commit");
                }
            }

        }

    }

    private void responseResult(String groupId, String command) {
        JSONObject result = new JSONObject();
        result.put("groupId", groupId);
        result.put("command", command);
        for (Channel channel : channelGroup) {
            System.out.println("发送操作数据:" + result.toJSONString());
            channel.writeAndFlush(result.toJSONString());
        }
    }
}

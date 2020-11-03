package com.example.seatatc;

public class SeataTcApplication {

    public static void main(String[] args) {
        NettyServer nettyServer = new NettyServer();
        nettyServer.startServer("localhost", 8080);
        System.out.println("Netty Server启动成功");
    }

}

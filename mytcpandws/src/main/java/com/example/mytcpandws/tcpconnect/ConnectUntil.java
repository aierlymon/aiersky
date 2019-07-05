package com.example.mytcpandws.tcpconnect;

import android.util.Log;

import com.example.mytcpandws.utils.MyLog;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Created by huanghao on 2017/8/14.
 */
public class ConnectUntil {
    private ChannelFuture channelFuture;
    private Channel channel;
    private String ip;
    private int port;
    private long currentTime;
    private int longtime = 10 * 1000;//服务器回应的时间-客户端ping的时间大于10秒，再次重新发送ping包

    protected ConnectUntil(String ip, int port) {
        currentTime = System.currentTimeMillis();
        this.ip = ip;
        this.port = port;
    }



    public int start() {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        if (ConnectUntilBox.group == null || ConnectUntilBox.group.isShutdown()) {
            ConnectUntilBox.group = new NioEventLoopGroup(1);
        }
        bootstrap.group(ConnectUntilBox.group);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10 * 1000);
        bootstrap.handler(new SimpleClientInitializer());
        ConnectUntilBox.getMap().put(ip + ":" + port, this);

        if (ip != null) {
            channelFuture = bootstrap.connect(new InetSocketAddress(ip, port));
        }

        channel = channelFuture.awaitUninterruptibly().channel();


        if (channel.isActive()&&channel.isOpen() && channel.isWritable()) {
            return ConnectUntilBox.OpenSocketsuccessful;
        } else {
            reciveMsgListener.onConnectFail(ConnectUntil.this);
            return ConnectUntilBox.NoRouteToHost;
        }

    }


    public int send(byte[] bys) {
        Log.i("mylog", "send: "+Thread.currentThread().getName());
        if (channel != null && channel.isOpen() && channel.isWritable()) {
            ByteBuf byteBuf = channel.alloc().buffer();
            byteBuf.resetWriterIndex();
            byteBuf.writeBytes(bys);
            channel.writeAndFlush(byteBuf);
            return ConnectUntilBox.SendSuccess;
        }
        return ConnectUntilBox.Fail;
    }


    public int restart() {
        closeClient();
        int state = start();
        return state;
    }


    protected void closeClient() {
        ConnectUntilBox.getMap().put(ip + ":" + port, this);
        if (channel != null) {
            channel.closeFuture();
            channelFuture.channel().close();
        }
    }

    public String getIp() {
        return ip;
    }



    public interface ReciveMsgListener {
        //接收到数据
        void onRecive(ConnectUntil connectUntil, byte[] msg);

        //连接成功
        void onConnect(ConnectUntil connectUntil);

        //连接被断开
        void onDisConnect();

        //连接失败，无法找到主机
        void onConnectFail(ConnectUntil connectUntil);

        //停止它的上层调用层线程的其他操作
        void onStopThread();
    }


    public void stopThread(){
        reciveMsgListener.onStopThread();
    }

    private ReciveMsgListener reciveMsgListener;

    public void setReciveMsgListener(ReciveMsgListener reciveMsgListener) {
        this.reciveMsgListener = reciveMsgListener;
    }


    public class SimpleClientInitializer extends ChannelInitializer<SocketChannel> {
        @Override
        public void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            pipeline.addLast("ping", new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS));//每5秒发送一次ping包,在服务器没返回的情况下
            ch.pipeline().addLast(new StringEncoder(Charset.forName("UTF-8")));
            ch.pipeline().addLast(new StringDecoder(Charset.forName("GBK")));
            pipeline.addLast("1", new NettyClientHandler());
        }
    }

    private class NettyClientHandler extends SimpleChannelInboundHandler {

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            //当连接上的时候
            if(ctx.channel().isOpen()&&ctx.channel().isWritable())
            reciveMsgListener.onConnect(ConnectUntil.this);
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            super.channelInactive(ctx);
            //当连接断开的时候
            reciveMsgListener.onDisConnect();
        }


        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (((String) msg).contains("[_heart_]")) {
                currentTime = System.currentTimeMillis();
            }
            byte[] bs = ((String) msg).getBytes("utf-8");
            reciveMsgListener.onRecive(ConnectUntil.this, ((String) msg).getBytes("utf-8"));
            /* ReferenceCountUtil.retain(msg);
            ctx.fireChannelRead(msg);//传给下一个inBound*/
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            super.exceptionCaught(ctx, cause);
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            super.userEventTriggered(ctx, evt);
            if(ctx.channel().isActive()){
                if (System.currentTimeMillis() - currentTime > longtime) {
                    if (ctx.channel().isOpen() && ctx.channel().isWritable())
                        ctx.channel().writeAndFlush("ping");
                }
            }
        }
    }


    //通道正常
    public boolean isActive() {
        if(channel==null)return false;
        if (channel.isOpen() && channel.isWritable()&&channel.isActive()) {
            return true;
        }
        return false;
    }

}

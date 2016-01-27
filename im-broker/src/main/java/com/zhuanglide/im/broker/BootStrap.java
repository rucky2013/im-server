package com.zhuanglide.im.broker;

import com.zhuanglide.im.broker.handle.MqttDispathHandle;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by wwj on 16.1.26.
 */
@Component
public class BootStrap {
    private Logger logger = Logger.getLogger(BootStrap.class);
    @Autowired
    private MqttDispathHandle dispathHandle;
    private int port = 1873;
    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("idle", new IdleStateHandler(180, 200, 200));
                            pipeline.addLast("encode", new MqttEncoder());
                            pipeline.addLast("decode", new MqttDecoder());
                            pipeline.addLast(dispathHandle);
                        }
                    });
            ChannelFuture f = bootstrap.bind(port).sync();
            logger.info("server start port:" + port);
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}

package com.zhuanglide.im.broker.handle;

import com.zhuanglide.im.broker.session.SessionManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.MqttConnectMessage;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttSubscribeMessage;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by wwj on 16.1.26.
 */
@Component
public class MqttDispathHandle extends SimpleChannelInboundHandler<MqttMessage>{
    private Logger logger = Logger.getLogger(MqttDispathHandle.class);
    @Autowired
    MqttHandle mqttHandle;
    @Autowired
    SessionManager sessionManager;

    @Override
    public boolean isSharable() {
        return true;
    }

    protected void channelRead0(ChannelHandlerContext ctx, MqttMessage msg) throws Exception {
        Channel channel = ctx.channel();
        switch (msg.fixedHeader().messageType()){
            case CONNECT:
                mqttHandle.connect(channel, (MqttConnectMessage) msg);
                break;
            case SUBSCRIBE:
                mqttHandle.subscribe(channel, (MqttSubscribeMessage)msg);
                break;
            case PUBLISH:
                mqttHandle.publish(channel, (MqttPublishMessage) msg);
                break;
            case PUBACK:
                break;
            case PINGREQ:
                mqttHandle.pingreq(channel, msg);
                break;
            default:
                System.out.println(msg);

        }
    }

    
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if(logger.isDebugEnabled())
            logger.debug("channel "+ctx.channel().attr(SessionManager.SESSION_KEY).get()+" add");
        sessionManager.addSession(ctx.channel());
    }

    
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if(logger.isDebugEnabled())
            logger.debug("channel "+ctx.channel().attr(SessionManager.SESSION_KEY).get());
        mqttHandle.disconnect(ctx.channel());
        ctx.close();
    }

    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent state = (IdleStateEvent) evt;
        switch (state.state()) {
            case READER_IDLE:
                if(logger.isDebugEnabled())
                    logger.debug("channel "+ctx.channel().attr(SessionManager.SESSION_KEY).get()+" read idle timeout");
                break;
            case WRITER_IDLE:
                if(logger.isDebugEnabled())
                    logger.debug("channel "+ctx.channel().attr(SessionManager.SESSION_KEY).get()+" write idle timeout");
                break;
            case ALL_IDLE:
                logger.warn("channel "+ctx.channel().attr(SessionManager.SESSION_KEY).get()+" idle timeout");
                ctx.close();
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("channel "+ctx.channel().attr(SessionManager.SESSION_KEY).get()+" Exception:", cause);
        mqttHandle.disconnect(ctx.channel());
        ctx.close();
    }
}

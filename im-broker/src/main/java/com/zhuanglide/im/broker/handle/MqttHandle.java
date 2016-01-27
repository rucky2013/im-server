package com.zhuanglide.im.broker.handle;

import com.zhuanglide.core.ClientSession;
import com.zhuanglide.core.ServerSession;
import com.zhuanglide.core.service.IMService;
import com.zhuanglide.im.broker.session.SessionManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.mqtt.*;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

/**
 * Created by wwj on 16.1.26.
 */
@Component
public class MqttHandle {
    @Autowired
    private SessionManager sessionManager;
    @Autowired
    IMService imService;

    void connect(Channel channel, MqttConnectMessage msg){
        MqttFixedHeader fixedHeader = new MqttFixedHeader(MqttMessageType.CONNACK,false,MqttQoS.AT_MOST_ONCE,false,0);
        MqttConnAckVariableHeader header = new MqttConnAckVariableHeader(MqttConnectReturnCode.CONNECTION_ACCEPTED);
        MqttConnAckMessage ackMsg = new MqttConnAckMessage(fixedHeader,header);
        long userId = NumberUtils.toLong(msg.payload().userName());
        ClientSession session = sessionManager.getSession(channel);
        session.setUserId(userId);
        imService.connect(sessionManager.getClientId(),session.getSid(), session.getUserId());
        channel.writeAndFlush(ackMsg).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
    }

    /**
     * publish
     * @param channel
     * @param msg
     */
    void publish(Channel channel, MqttPublishMessage msg){
        String[] message = msg.content().toString(Charset.forName("utf-8")).split(":");
        MqttFixedHeader pheader = new MqttFixedHeader(MqttMessageType.PUBLISH,false,MqttQoS.AT_MOST_ONCE,false,0);
        MqttPublishVariableHeader pvhead = new MqttPublishVariableHeader("chat", msg.variableHeader().messageId()+10);
        MqttPublishMessage pmsg = new MqttPublishMessage(pheader,pvhead,msg.content());

        long userId = NumberUtils.toLong(message[0]);
        imService.publish(userId, message[1]);
        MqttFixedHeader fixedHeader = new MqttFixedHeader(MqttMessageType.PUBACK,false,MqttQoS.AT_MOST_ONCE,false,0);
        MqttPubAckMessage mqttPubAckMessage = new MqttPubAckMessage(fixedHeader, MqttMessageIdVariableHeader.from(msg.variableHeader().messageId()+10));
        channel.writeAndFlush(mqttPubAckMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);

    }

    /**
     * subscribe
     * @param channel
     * @param msg
     */
    void subscribe(Channel channel, MqttSubscribeMessage msg){

    }

    void puback(String clientId, MqttPubAckMessage msg) {
    }

    /**
     * pingreq
     * @param channel
     * @param msg
     */
    void pingreq(Channel channel, MqttMessage msg) {
        MqttFixedHeader pheader = new MqttFixedHeader(MqttMessageType.PINGRESP,false,MqttQoS.AT_MOST_ONCE,false,0);
        MqttMessage pingRes = new MqttMessage(pheader);
        channel.writeAndFlush(pingRes).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
    }


    void disconnect(Channel channel) {
        ClientSession session = sessionManager.getSession(channel);
        if(null!=session) {
            imService.disconnect(session.getUserId());
            sessionManager.removeSession(channel);
        }
    }
}

package com.mgtv.socket.service.server;

import com.mgtv.socket.service.EventDispatcher;
import com.mgtv.socket.service.WrappedChannel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author zhiguang@mgtv.com
 * @date 2018/12/30 16:47
 */
public class ServerDispatchHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ServerDispatchHandler.class);

    protected EventDispatcher eventDispatcher;

    public ServerDispatchHandler(EventDispatcher eventDispatcher) {
        if (eventDispatcher == null) {
            throw new IllegalArgumentException("eventDispatcher cannot be null.");
        }

        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String channelId = ctx.channel().id().asShortText();
        if (logger.isDebugEnabled()) {
            logger.debug("Message received from channel '{} : '{}'.", channelId, msg);
        }
        WrappedChannel channel = ((Server) eventDispatcher.getService()).getChannel(channelId);
        eventDispatcher.dispatchMessageEvent(ctx, channel, msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        WrappedChannel channel = new WrappedChannel(ctx.channel());
        String channelId = channel.id().asShortText();
        if (logger.isDebugEnabled()) {
            logger.debug("Connected on channel '{}'.", channelId);
        }
        ((Server) eventDispatcher.getService()).getChannels().put(channelId, channel);
        eventDispatcher.dispatchChannelEvent(ctx, channel);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        closeChannel(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        String channelId = ctx.channel().id().asShortText();
        WrappedChannel channel = ((Server) eventDispatcher.getService()).getChannel(channelId);
        if (channel != null) {
            eventDispatcher.dispatchExceptionCaught(ctx, channel, cause);
        }

        // 处理IOException，主动关闭channel
        if (cause instanceof IOException) {
            ctx.close();
            closeChannel(ctx);
        }
    }

    private void closeChannel(ChannelHandlerContext ctx) {
        String channelId = ctx.channel().id().asShortText();
        WrappedChannel channel = ((Server) eventDispatcher.getService()).getChannels().remove(channelId);
        if (channel != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Channel '{}' was closed.", channelId);
            }

            eventDispatcher.dispatchChannelEvent(ctx, channel);
        }
        ctx.close();
    }

}
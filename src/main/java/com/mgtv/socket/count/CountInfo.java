package com.mgtv.socket.count;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author zhiguang@mgtv.com
 * @date 2019/1/11 16:55
 */
public class CountInfo {
    private static final long serialVersionUID = 1L;

    /**
     * 最后次接收消息
     */
    private long lastReceive;
    /**
     * 最后次接收消息
     */
    private long lastSent;
    /**
     * 最大连接数
     */
    private long maxChannelNum;
    /**
     * 当前连接数
     */
    private long curChannelNum;
    /**
     * 接收消息数
     */
    private AtomicLong receiveNum = new AtomicLong();
    /**
     * 发送消息数
     */
    private AtomicLong sentNum = new AtomicLong();
    /**
     * 接收心跳消息个数
     */
    private AtomicLong heartbeatReceived = new AtomicLong();
    /**
     * 发送心跳消息数
     */
    private AtomicLong heartbeatSent = new AtomicLong();

    public long getCurChannelNum() {
        return curChannelNum;
    }

    public void setCurChannelNum(long curChannelNum) {
        this.curChannelNum = curChannelNum;
        if (this.maxChannelNum < curChannelNum) {
            this.maxChannelNum = curChannelNum;
        }
    }

    public long getMaxChannelNum() {
        return maxChannelNum;
    }

    public AtomicLong getReceiveNum() {
        return receiveNum;
    }

    public AtomicLong getSentNum() {
        return sentNum;
    }

    public AtomicLong getHeartbeatReceived() {
        return this.heartbeatReceived;
    }

    public AtomicLong getHeartbeatSent() {
        return this.heartbeatSent;
    }

    public long getLastReceive() {
        return lastReceive;
    }

    public void setLastReceive(long lastReceive) {
        if (this.lastReceive < lastReceive) {
            this.lastReceive = lastReceive;
        }
    }

    public long getLastSent() {
        return lastSent;
    }

    public void setLastSent(long lastSent) {
        if (this.lastSent < lastSent) {
            this.lastSent = lastSent;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("StatisticInfo [lastReceive=").append(this.lastReceive);
        sb.append(", lastSent=").append(this.lastSent);
        sb.append(", receiveNum=").append(this.receiveNum);
        sb.append(", heartbeatReceived=").append(this.heartbeatReceived);
        sb.append(", sentNum=").append(this.sentNum);
        sb.append(", heartbeatSent=").append(this.heartbeatSent);
        sb.append(", maxChannelNum=").append(this.maxChannelNum);
        sb.append(", curChannelNum=").append(this.curChannelNum);
        sb.append("]");
        return sb.toString();
    }
}
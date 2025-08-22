package com.matridx.las.netty.channel.util;

import com.matridx.las.netty.channel.domain.protocol.AutoProtocol;
import com.matridx.las.netty.channel.domain.protocol.CommonProtocol;
import com.matridx.las.netty.channel.domain.protocol.CubicsProtocol;
import com.matridx.las.netty.channel.domain.protocol.DefaultProtocol;
import com.matridx.las.netty.channel.domain.protocol.MatridxProtocol;
import com.matridx.las.netty.channel.domain.protocol.PcrProtocol;
import com.matridx.las.netty.channel.domain.protocol.SeqProtocol;
import com.matridx.las.netty.channel.domain.protocol.TextProtocol;
import com.matridx.las.netty.channel.domain.protocol.AvgProtocol;
import com.matridx.las.netty.channel.domain.protocol.CmhProtocol;

public class MsgUtil {

    public static MatridxProtocol buildMatridxProtocol(String channelId, String msgContent) {
        return new MatridxProtocol(channelId, msgContent);
    }

    public static TextProtocol buildTextProtocol(String channelId, String msgContent) {
        return new TextProtocol(channelId, msgContent);
    }
    
    public static CubicsProtocol buildCubicsProtocol(String channelId, String msgContent) {
        return new CubicsProtocol( msgContent);
    }
    
    public static AutoProtocol buildAutoProtocol(String channelId, String msgContent) {
        return new AutoProtocol( msgContent);
    }
    
    public static PcrProtocol buildPcrProtocol(String channelId, String msgContent) {
        return new PcrProtocol( msgContent);
    }
    
    public static SeqProtocol buildSeqProtocol(String channelId, String msgContent) {
        return new SeqProtocol( msgContent);
    }
    
    public static DefaultProtocol buildDefaultProtocol(String channelId, String msgContent) {
        return new DefaultProtocol( msgContent);
    }
    
    public static CommonProtocol buildCommonProtocol(String channelId, String msgContent) {
        return new CommonProtocol( msgContent);
    }
    public static AvgProtocol AvgProtocol(String channelId, String msgContent) {
        return new AvgProtocol( msgContent);
    }
    public static CmhProtocol CmhProtocol(String channelId, String msgContent) {
        return new CmhProtocol( msgContent);
    }
}

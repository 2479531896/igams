package com.matridx.las.netty.channel.server.handler;

import com.matridx.springboot.util.base.StringUtil;
import io.netty.handler.ipfilter.IpFilterRule;
import io.netty.handler.ipfilter.IpFilterRuleType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
@Component
public class IpFilterRuleHandler implements IpFilterRule {
    @Value("${whitelistip:}")
    private String whitelistip;
    @Override
    public boolean matches(InetSocketAddress inetSocketAddress) {
        String ip = inetSocketAddress.getHostString();
        if(StringUtil.isNotBlank(whitelistip)){
            String [] whitelist = whitelistip.split(",");
            boolean ishave = false;
            for(String i:whitelist){
                if(i.equals(ip)){
                    ishave = true;
                    break;
                }
            }
            return ishave;
        }
        return false;
    }

    @Override
    public IpFilterRuleType ruleType() {
        return IpFilterRuleType.ACCEPT;
    }
}

package com.matridx.igams.common.config.rabbit;

import org.springframework.boot.context.properties.ConfigurationProperties; 
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.rabbitmq")
public class RabbitMq {
	private String host;
    private String username;
    private String password;
    private String port;
    private Boolean publisherconfirms;
    private String address;
    private String virtualHost;

	public String getVirtualHost() {
		return virtualHost;
	}

	public void setVirtualHost(String virtualHost) {
		this.virtualHost = virtualHost;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Boolean getPublisherconfirms() {
		return publisherconfirms;
	}
	public void setPublisherconfirms(Boolean publisherconfirms) {
		this.publisherconfirms = publisherconfirms;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
		if(address!=null && !address.isEmpty()) {
			String[] infos =address.split("@");
			if(infos.length == 2) {
				String[] user =infos[0].split(":");
				this.username = user[0];
				this.password = user[1];
				String[] add =infos[1].split(":");
				this.host = add[0];
				this.port = add[1];
			}
		}
	}
}

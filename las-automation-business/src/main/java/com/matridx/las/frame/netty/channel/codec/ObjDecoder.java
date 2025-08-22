package com.matridx.las.frame.netty.channel.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.matridx.las.frame.connect.channel.domain.Command;
import com.matridx.las.frame.connect.channel.domain.PacketClazzMap;
import com.matridx.las.frame.netty.channel.util.SerializationUtil;

public class ObjDecoder extends ByteToMessageDecoder {

	//数据包基础长度
    private final int BASE_LENGTH = 4;
    
    private Logger log = LoggerFactory.getLogger(ObjDecoder.class);

	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {

		if (in.readableBytes() < BASE_LENGTH) {
			return;
		}
		final int magic1 = in.getUnsignedByte(in.readerIndex());
		final int magic2 = in.getUnsignedByte(in.readerIndex() + 1);

		if (isHttp(magic1, magic2)) {
			log.info("this is a http msg");
			in.resetReaderIndex();
			ctx.pipeline().remove(this.getClass());
			return;
		}
		in.markReaderIndex();
		int dataLength = in.readInt();
//		if (in.readableBytes() < dataLength) {
//			int len = in.readableBytes();
//			byte[] data = new byte[len];
//			in.readBytes(data);
//			String infoString  = new String(data, StandardCharsets.UTF_8);
//			in.resetReaderIndex();
//			//ctx.channel().close();
//			log.error("数据发送的长度未：" + dataLength+"可读长度为："+len+"，可读数据:"+infoString);
//			return;
//		}
		
		try {
			byte command = in.readByte();  //读取指令
			
			log.info("数据command：" + command);
			
			byte splitword = 0;
			try {
				splitword = in.readByte();  //分割指令
			}catch(Exception e) {
				log.error("数据分割指令读取出错：" + e.getMessage());
			}
			
			log.info("数据splitword：" + splitword);
			
			byte[] data = null;
			//类方式的协议
			if(splitword == 64) {
				data = new byte[dataLength - 1];

				 //指令占了一位，剔除掉
				in.readBytes(data);
				out.add(SerializationUtil.deserialize(data, PacketClazzMap.packetTypeMap.get(command)));
			}else {
				// 字符串方式的协议
				in.resetReaderIndex();
				// 解决粘包问题
				if(in.readableBytes()==dataLength+4){
					data = new byte[in.readInt()];
				}else{
					in.resetReaderIndex();
					data = new byte[in.readableBytes()];
				}


				// data = new byte[in.writerIndex() - in.readerIndex()];
				in.readBytes(data);
				String infoString  = new String(data, StandardCharsets.UTF_8);
				String[] infos = infoString.split("&");
				if(infos.length > 1) {
					command = (byte) (infos[0].getBytes()[0]-48);
					Class<?> objClass = PacketClazzMap.packetTypeMap.get(command);
					if(objClass!=null) {
						try {
							Method method = objClass.getMethod("setInfo", String.class);
							Object object = SerializationUtil.deserialize(objClass);
							method.invoke(object, new String(infos[1]));
							out.add(object);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							log.error("数据长度大于1时出错：" + e.getMessage());
						}
					}
				}else if(infos.length ==1) {
					command = Command.DEFAULT;
					Class<?> objClass = PacketClazzMap.packetTypeMap.get(command);
					if(objClass!=null) {
						try {
							Object object = SerializationUtil.deserialize(objClass);
							Method method = objClass.getMethod("setInfo", String.class);
							method.invoke(object, new String(infos[0]));
							out.add(object);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							log.error("数据长度为1时出错：" + e.getMessage());
						}
					}
				}else {
					return;
				}
				
			}
		}catch(Exception e) {
			log.error("数据反编译出错：" + e.getMessage());
		}
	}
	/**
	 * 判断请求是否是HTTP请求
     *
	 * @param magic1 报文第一个字节
     * @param magic2 报文第二个字节
     * @return
	*/
	private boolean isHttp(int magic1, int magic2) {
		return magic1 == 'G' && magic2 == 'E' || // GET
				magic1 == 'P' && magic2 == 'O' || // POST
				magic1 == 'P' && magic2 == 'U' || // PUT
				magic1 == 'H' && magic2 == 'E' || // HEAD
				magic1 == 'O' && magic2 == 'P' || // OPTIONS
				magic1 == 'P' && magic2 == 'A' || // PATCH
				magic1 == 'D' && magic2 == 'E' || // DELETE
				magic1 == 'T' && magic2 == 'R' || // TRACE
				magic1 == 'C' && magic2 == 'O';   // CONNECT
	}




}

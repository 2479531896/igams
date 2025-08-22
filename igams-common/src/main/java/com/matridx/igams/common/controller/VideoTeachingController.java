package com.matridx.igams.common.controller;

import java.io.FileInputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/systemmain")
public class VideoTeachingController {

	//获取大图标视频列表
	@RequestMapping(value="/video/pageListVideo")
	public ModelAndView getVideoList() {
        return new ModelAndView("systemmain/video/video_teach_list");
	}
	
	@RequestMapping(value="/video/pagedataVideomenu")
	public ModelAndView pagedataVideomenu() {
        return new ModelAndView("systemmain/video/video_teach_menu");
	}
	
	 /**
     * 根据本地图片全路径，响应给浏览器1个图片流
     */
    @RequestMapping("/showImage")
    public void showImage(HttpServletResponse response, @RequestParam("fileName")String fileName) {
        show(response,fileName,"image");
    }
 
    /**
     * 根据本地视频全路径，响应给浏览器1个视频
     */
    @RequestMapping("/pagedataShowVideo")
    public void showVideo(HttpServletResponse response, @RequestParam("fileName")String fileName) {
        show(response,fileName,"video");
    }
 
    /**
     * 响应文件
     * @param fileName  文件全路径
     * @param type  响应流类型
     */
    public void  show(HttpServletResponse response, String fileName,String type){
        try{
            FileInputStream fis = new FileInputStream(fileName); // 以byte流的方式打开文件
            int i=fis.available(); //得到文件大小
            byte[] data =new byte[i];
            fis.read(data);  //读数据
            response.setContentType(type+"/*"); //设置返回的文件类型
            OutputStream toClient=response.getOutputStream(); //得到向客户端输出二进制数据的对象
            toClient.write(data);  //输出数据
            toClient.flush();
            toClient.close();
            fis.close();
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("文件不存在");
        }
    }

}

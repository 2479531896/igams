package com.matridx.igams.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/common")
public class AudioController extends BaseController{
	
	/**
	 * 打开语音测试页面
	 * @author asus
	 */
	@RequestMapping("/audio/pageAudio")
	public ModelAndView pageAudio(){
		return new ModelAndView("common/audio/audio");
	}
	
}

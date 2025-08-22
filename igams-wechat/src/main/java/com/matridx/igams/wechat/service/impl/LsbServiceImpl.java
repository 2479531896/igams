package com.matridx.igams.wechat.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.LsbDto;
import com.matridx.igams.wechat.dao.entities.LsbModel;
import com.matridx.igams.wechat.dao.post.ILsbDao;
import com.matridx.igams.wechat.service.svcinterface.ILsbService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

@Service
public class LsbServiceImpl extends BaseBasicServiceImpl<LsbDto, LsbModel, ILsbDao> implements ILsbService{

	/**
	 * 增加记录
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean getInfoFromSite(){
		
		try {
			StringBuffer sb = new StringBuffer();
			URL url;
			try {
				url = new URL("https://3g.dxy.cn/newh5/view/pneumonia?scene=2&clicktime=1579579384&enterid=1579579384&from=singlemessage&isappinstalled=0");
				URLConnection conn = url.openConnection();
				InputStream is = conn.getInputStream();
				Scanner sc = new Scanner(is, "UTF-8");
				while (sc.hasNextLine()) {
					sb.append(sc.nextLine()).append("\r\n");
				}
				sc.close();
				is.close();
			}  catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			String str =sb.toString(); 

			int body_start = str.indexOf("<body>");
			int body_end = str.indexOf("</body>",body_start);
			if(body_end==-1)
				return false;
			String body_str = str.substring(body_start, body_end + 7);
			
			body_str = body_str.replace("<div class=\"shareBtn___ladOE\">分享</div>", "");
			
			body_str = body_str.replace("<script src=\"//assets.dxycdn.com/gitrepo/bbs-mobile/dist/umi.bundle.js?t=1579703539758\"></script>", "");
			
			int div_start = body_str.indexOf("<div class=\"tab___2jN_q\">");
			int div_end = body_str.indexOf("</div></div>",div_start);
			
			if(div_start !=-1) {
				String div_str = body_str.substring(div_start, div_end + 12);
					
				body_str = body_str.replace(div_str, "");
			}

			int script_start = body_str.lastIndexOf("<script ");
			int script_end = body_str.lastIndexOf("</script>");
			
			if(script_start !=-1) {
				String script_str = body_str.substring(script_start, script_end + 9);
					
				body_str = body_str.replace(script_str, "");
			}
			
			int pv_start = body_str.indexOf("<div class=\"pv___1ZVWl\">");
			if(pv_start !=-1) {
				int pv_end = body_str.indexOf("</div>",pv_start);
				
				String pv_str = body_str.substring(pv_start, pv_end + 6);
				
				body_str = body_str.replace(pv_str, "");
				
			}
			
			LsbModel lsbModel = new LsbModel();
			lsbModel.setCxid("DXY-TJ");
			lsbModel.setNr(body_str);
		
			LsbModel t_lsbModel = dao.getModelById("DXY-TJ");
			if(t_lsbModel == null) {
				dao.insert(lsbModel);
			}else {
				dao.update(lsbModel);
			}
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}

package com.matridx.igams.production.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.ShxxDto;
import com.matridx.igams.common.dao.entities.XxdlcwglDto;
import com.matridx.igams.common.dao.post.IShxxDao;
import com.matridx.igams.common.service.svcinterface.IXxdlcwglService;
import com.matridx.igams.production.dao.entities.HtglDto;
import com.matridx.igams.production.dao.entities.HtmxDto;
import com.matridx.igams.production.dao.entities.QgglDto;
import com.matridx.igams.production.dao.entities.QgmxDto;
import com.matridx.igams.production.dao.entities.QgqxglDto;
import com.matridx.igams.production.dao.entities.QgqxmxDto;
import com.matridx.igams.production.dao.post.IHtmxDao;
import com.matridx.igams.production.service.svcinterface.IHtglService;
import com.matridx.igams.production.service.svcinterface.IHtmxService;
import com.matridx.igams.production.service.svcinterface.IQgglService;
import com.matridx.igams.production.service.svcinterface.IQgmxService;
import com.matridx.igams.production.service.svcinterface.IQgqxglService;
import com.matridx.igams.production.service.svcinterface.IQgqxmxService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.util.CollectionUtils;

@Component
public class ProductionMqReceive {
	
	@Autowired
	IXxdlcwglService xxdlcwglService;
	@Value("${matridx.rabbit.flg:}")
	private String prefixFlg;
	@Value("${matridx.rabbit.systemreceiveflg:}")
	private String systemreceiveflg;
	@Autowired
	private IHtglService htglService;
	@Autowired
	IHtmxService htmxService;
	@Autowired
	private IQgglService qgglService;
	@Autowired
	private IQgmxService qgmxService;
	@Autowired
	private IShxxDao shxxDao;
	@Autowired
	IHtmxDao htmxDao;
	@Autowired
	private IQgqxglService qgqxglService;
	@Autowired
	IQgqxmxService qgqxmxService;
	
	private final Logger log = LoggerFactory.getLogger(ProductionMqReceive.class);
	
	/**
	 * 合同新增
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.htgl.insert${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void insertHtgl(String str) {
        try {
        	HtglDto htglDto = JSONObject.parseObject(str, HtglDto.class);
        	if("1".equals(systemreceiveflg)) {
        		if(!prefixFlg.equals(htglDto.getPrefixFlg())){
    				htglService.addSaveContract(htglDto);
            	}
        	}       	
		} catch (Exception e) {
			log.error("prefixFlg htgl.insert:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.htgl.insert");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	
	/**
	 * 合同修改
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.htgl.update${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void updateHtgl(String str) {
        try {
        	HtglDto htglDto = JSONObject.parseObject(str, HtglDto.class);
        	if("1".equals(systemreceiveflg)) {
        		if(!prefixFlg.equals(htglDto.getPrefixFlg())){
        			htglService.modSaveContract(htglDto);
            	}
        	}       	
		} catch (Exception e) {
			log.error("prefixFlg htgl.update:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.htgl.update");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}

	
	/**
	 * 合同高级修改
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.htgl.advancedupdate${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void advancedUpdateHtgl(String str) {
        try {
        	HtglDto htglDto = JSONObject.parseObject(str, HtglDto.class);
        	if("1".equals(systemreceiveflg)) {
        		if(!prefixFlg.equals(htglDto.getPrefixFlg())){
        			htglService.advancedModSaveContract(htglDto);
            	}
        	}       	
		} catch (Exception e) {
			log.error("prefixFlg htgl.advancedupdate:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.htgl.advancedupdate");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	/**
	 * 合同删除
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.htgl.del${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void delHtgl(String str) {
        try {
        	HtglDto htglDto = JSONObject.parseObject(str, HtglDto.class);
        	if("1".equals(systemreceiveflg)) {
        		if(!prefixFlg.equals(htglDto.getPrefixFlg())){
        			htglService.deleteHtgl(htglDto);
            	}
        	}       	
		} catch (Exception e) {
			log.error("prefixFlg htgl.del:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.htgl.del");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	/**
	 * 合同外发修改
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.htgl.updateOut${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void updateOutHtgl(String str) {
        try {
        	HtglDto htglDto = JSONObject.parseObject(str, HtglDto.class);
        	if("1".equals(systemreceiveflg)) {
        		if(!prefixFlg.equals(htglDto.getPrefixFlg())){
        			htglService.OutgrowthContract(htglDto);
            	}
        	}       	
		} catch (Exception e) {
			log.error("prefixFlg htgl.updateOut:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.htgl.updateOut");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	/**
	 * 合同明细修改
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.htmx.update${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void updateHtmx(String str) {
        try {
        	HtmxDto htmxDto = JSONObject.parseObject(str, HtmxDto.class);
        	if("1".equals(systemreceiveflg)) {
        		if(!prefixFlg.equals(htmxDto.getPrefixFlg())){
        			htmxService.modSaveMaintenance(htmxDto);
            	}
        	}       	
		} catch (Exception e) {
			log.error("prefixFlg htmx.update:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.htmx.update");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	/**
	 * 请购增加
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.qggl.insert${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void insertQggl(String str) {
        try {
        	QgglDto qgglDto = JSONObject.parseObject(str, QgglDto.class);
        	if(!prefixFlg.equals(qgglDto.getPrefixFlg())){
        		if("1".equals(systemreceiveflg)) {
        			qgglService.addSaveProdution(qgglDto);
            	}
        	}       	
		} catch (Exception e) {
			log.error("sys.igams.qggl.insert:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.qggl.insert");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	
	/**
	 * 请购高级修改
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.qggl.advancedUpdate${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void advancedUpdateQggl(String str) {
        try {
        	QgglDto qgglDto = JSONObject.parseObject(str, QgglDto.class);
        	if("1".equals(systemreceiveflg)) {
        		if(!prefixFlg.equals(qgglDto.getPrefixFlg())){
        			qgglService.advancedModPurchase(qgglDto, qgglDto.getQgmxlist());
            	}
        	}       	
		} catch (Exception e) {
			log.error("sys.igams.qggl.advancedUpdate:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.qggl.advancedUpdate");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	/**
	 * 请购修改
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.qggl.update${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void updateQggl(String str) {
        try {
        	QgglDto qgglDto = JSONObject.parseObject(str, QgglDto.class);
        	if("1".equals(systemreceiveflg)) {
        		if(!prefixFlg.equals(qgglDto.getPrefixFlg())){
        			qgglService.updateProdution(qgglDto, qgglDto.getQgmxlist());
            	}
        	}       	
		} catch (Exception e) {
			log.error("sys.igams.qggl.update:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.qggl.update");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	/**
	 * 请购删除
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.qggl.del${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void delQggl(String str) {
        try {
        	QgglDto qgglDto = JSONObject.parseObject(str, QgglDto.class);
        	if("1".equals(systemreceiveflg)) {
        		if(!prefixFlg.equals(qgglDto.getPrefixFlg())){
        			qgglService.deleteQggl(qgglDto);
            	}
        	}       	
		} catch (Exception e) {
			log.error("sys.igams.qggl.del:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.qggl.del");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	/**
	 * 请购修改（审核时候修改）
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.qggl.updateQgglxx${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void updateQgglxx(String str) {
        try {
        	QgglDto qgglDto = JSONObject.parseObject(str, QgglDto.class);
        	if("1".equals(systemreceiveflg)) {
        		if(!prefixFlg.equals(qgglDto.getPrefixFlg())){
        			qgglService.updateQgglxx(qgglDto);
            	}
        	}       	
		} catch (Exception e) {
			log.error("sys.igams.qggl.updateQgglxx:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.qggl.updateQgglxx");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	/**
	 * 请购明细修改
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.qgmx.update${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void updateQgmx(String str) {
        try {
        	@SuppressWarnings("unchecked")
			List<QgmxDto> qgmxDtos = (List<QgmxDto>)JSONObject.parseObject(str, QgmxDto.class);
        	if("1".equals(systemreceiveflg) && !CollectionUtils.isEmpty(qgmxDtos)) {
        		if(!prefixFlg.equals(qgmxDtos.get(0).getPrefixFlg())){
        			qgmxService.updateGlidDtos(qgmxDtos);
            	}
        	}       	
		} catch (Exception e) {
			log.error("sys.igams.qgmx.update:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.qgmx.update");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	/**
	 * 审核信息新增
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.shxx.insert${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void insertShxx(String str) {
        try {
        	ShxxDto shxx = JSONObject.parseObject(str, ShxxDto.class);
        	if("1".equals(systemreceiveflg)) {
        		if(!prefixFlg.equals(shxx.getPrefixFlg())){
        			shxxDao.insert(shxx);
            	}
        	}       	
		} catch (Exception e) {
			log.error("sys.igams.shxx.insert:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.shxx.insert");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	/**
	 *请购明细修改
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.qgmx.updatePurchase${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void updatePurchase(String str) {
        try {
        	QgglDto qgglDto = JSONObject.parseObject(str, QgglDto.class);
        	List<QgmxDto> qgmxList = qgglDto.getQgmxlist();
        	if("1".equals(systemreceiveflg)) {
        		if(!prefixFlg.equals(qgglDto.getPrefixFlg())){
        			qgmxService.updatePurchaseDetail(qgmxList, qgglDto);
            	}
        	}       	
		} catch (Exception e) {
			log.error("sys.igams.qgmx.updatePurchase:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.qgmx.updatePurchase");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	/**
	 *合同明细数量修改
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.htmx.updateQuantityComp${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void updateQuantityComp(String str) {
        try {
        	HtglDto htglDto = JSONObject.parseObject(str, HtglDto.class);
        	List<HtmxDto> htmxList = JSON.parseArray(htglDto.getHtmxJson(), HtmxDto.class);
        	if("1".equals(systemreceiveflg)) {
        		if(!prefixFlg.equals(htglDto.getPrefixFlg())){
        			htmxService.updateQuantityComp(htmxList);
            	}
        	}       	
		} catch (Exception e) {
			log.error("sys.igams.htmx.updateQuantityComp:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.htmx.updateQuantityComp");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	/**
	 *请购完成标记修改
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.qggl.updateWcbjs${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void updateWcbjs(String str) {
        try {
        	QgglDto qgglDto = JSONObject.parseObject(str, QgglDto.class);
        	if("1".equals(systemreceiveflg)) {
        		if(!prefixFlg.equals(qgglDto.getPrefixFlg())){
        			qgglService.updateWcbjs(qgglDto);
            	}
        	}       	
		} catch (Exception e) {
			log.error("sys.igams.qggl.updateWcbjs:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.qggl.updateWcbjs");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	/**
	 *合同明细U8id修改
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.htmx.updateHtmxDtos${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void updateHtmxDtos(String str) {
        try {
        	HtglDto htglDto = JSONObject.parseObject(str, HtglDto.class);
        	List<HtmxDto> htmxList = JSON.parseArray(htglDto.getHtmxJson(), HtmxDto.class);
        	if("1".equals(systemreceiveflg)) {
        		if(!prefixFlg.equals(htglDto.getPrefixFlg())){
        			htmxDao.updateHtmxDtos(htmxList);
            	}
        	}       	
		} catch (Exception e) {
			log.error("sys.igams.htmx.updateHtmxDtos:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.htmx.updateHtmxDtos");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	/**
	 *取消请购新增
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.qgqxgl.insertQxqg${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void insertQxqg(String str) {
        try {
        	QgqxglDto qgqxglDto = JSONObject.parseObject(str, QgqxglDto.class);
        	if("1".equals(systemreceiveflg)) {
        		if(!prefixFlg.equals(qgqxglDto.getPrefixFlg())){
        			qgqxglService.addSavePurchaseCancel(qgqxglDto);
            	}
        	}       	
		} catch (Exception e) {
			log.error("sys.igams.qgqxgl.insertQxqg:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.qgqxgl.insertQxqg");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	/**
	 *取消请购明细删除
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.qgqxmx.delQgqxmx${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void delQgqxmx(String str) {
        try {
        	QgqxmxDto qgqxmxDto = JSONObject.parseObject(str, QgqxmxDto.class);
        	if("1".equals(systemreceiveflg)) {
        		if(!prefixFlg.equals(qgqxmxDto.getPrefixFlg())){
        			qgqxmxService.deleteQgqxmx(qgqxmxDto);
            	}
        	}       	
		} catch (Exception e) {
			log.error("sys.igams.qgqxmx.delQgqxmx:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.qgqxmx.delQgqxmx");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	/**
	 *取消请购修改
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.qgqxgl.updateQgqxgl${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void updateQgqxgl(String str) {
        try {
        	QgqxglDto qgqxglDto = JSONObject.parseObject(str, QgqxglDto.class);
        	if("1".equals(systemreceiveflg)) {
        		if(!prefixFlg.equals(qgqxglDto.getPrefixFlg())){
        			qgqxglService.modSavePurchaseCancel(qgqxglDto);
            	}
        	}
		} catch (Exception e) {
			log.error("sys.igams.qgqxgl.updateQgqxgl:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.qgqxgl.updateQgqxgl");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	/**
	 *取消请购明细数量修改
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.qgqxmx.updateQgmxByList${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void updateQgmxByList(String str) {
        try {
        	QgqxglDto qgqxglDto = JSONObject.parseObject(str, QgqxglDto.class);
        	if("1".equals(systemreceiveflg)) {
        		if(!prefixFlg.equals(qgqxglDto.getPrefixFlg())){
        			qgqxmxService.updateQgmxByList(qgqxglDto);
            	}
        	}
		} catch (Exception e) {
			log.error("sys.igams.qgqxmx.updateQgmxByList:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.qgqxmx.updateQgmxByList");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	/**
	 *请购取消标记更新
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.qgqxmx.updateQxqg${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void updateQxqg(String str) {
        try {
        	QgqxmxDto qgqxmxDto = JSONObject.parseObject(str, QgqxmxDto.class);
        	if("1".equals(systemreceiveflg)) {
        		if(!prefixFlg.equals(qgqxmxDto.getPrefixFlg())){
        			qgmxService.updateQxqg(qgqxmxDto.getQgmxid());
            	}
        	}
		} catch (Exception e) {
			log.error("sys.igams.qgqxmx.updateQxqg:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.qgqxmx.updateQxqg");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	/**
	 *请购取消修改
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.qgqxgl.updateQgqxglxx${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void updateQgqxglxx(String str) {
        try {
        	QgqxglDto qgqxglDto = JSONObject.parseObject(str, QgqxglDto.class);
        	if("1".equals(systemreceiveflg)) {
        		if(!prefixFlg.equals(qgqxglDto.getPrefixFlg())){
        			qgqxglService.updateQgqxglxx(qgqxglDto);
            	}
        	}
		} catch (Exception e) {
			log.error("sys.igams.qgqxgl.updateQgqxglxx:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.qgqxgl.updateQgqxglxx");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
}

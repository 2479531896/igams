package com.matridx.server.detection.molecule.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.enums.RabbitEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IPayService;
import com.matridx.server.detection.molecule.dao.entities.FzjcxxDto;
import com.matridx.server.detection.molecule.dao.entities.FzjcxxModel;
import com.matridx.server.detection.molecule.dao.entities.HzxxtDto;
import com.matridx.server.detection.molecule.dao.post.IFzjcxxDao;
import com.matridx.server.detection.molecule.enums.DetMQTypeEnum;
import com.matridx.server.detection.molecule.service.svcinterface.IFzjcxmService;
import com.matridx.server.detection.molecule.service.svcinterface.IFzjcxxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class FzjcxxServiceImpl extends BaseBasicServiceImpl<FzjcxxDto, FzjcxxModel, IFzjcxxDao> implements IFzjcxxService, IPayService {

    @Autowired(required = false)
    private AmqpTemplate amqpTempl;
    @Autowired
    private IFzjcxmService fzjcxmService;
    /**
     * 新冠列表查看
     * @param fzjcxxDto
     * @return
     */
    public List<FzjcxxDto> viewCovidDetails(FzjcxxDto fzjcxxDto){
        return dao.viewCovidDetails(fzjcxxDto);
    }
    /*新增预约信息到分子检测信息表*/
    public boolean insertDetectionAppointmentFzjcxx(FzjcxxDto fzjcxxDto){
        return dao.insertDetectionAppointmentFzjcxx(fzjcxxDto);
    };
/**
     * 报告日期查看
     * @param fzjcxxDto
     * @return
     */

    public List<FzjcxxDto> bgrqDetails(FzjcxxDto fzjcxxDto){
        return dao.bgrqDetails(fzjcxxDto);
    }

    @Override
    public List<FzjcxxDto> beforeDayList(FzjcxxDto fzjcxxDto) {
        return dao.beforeDayList(fzjcxxDto);
    }

    /*更新预约信息到分子检测信息表*/
    public boolean updateDetectionAppointmentFzjcxx(FzjcxxDto fzjcxxDto){
        return dao.updateDetectionAppointmentFzjcxx(fzjcxxDto);
    };
    /*取消已预约的患者信息*/
	@Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean cancelDetectionAppointment(FzjcxxDto fzjcxxDto){
        return dao.cancelDetectionAppointment(fzjcxxDto);
    };
    
    @Override
    public FzjcxxDto getSampleAcceptInfo(FzjcxxDto fzjcxxDto) throws BusinessException {
        List<FzjcxxDto> fzjcxxDtos = dao.getFzjcxxByybbh(fzjcxxDto);
        DBEncrypt dbEncrypt=new DBEncrypt();
        fzjcxxDtos.get(0).setSj(dbEncrypt.dCode(fzjcxxDtos.get(0).getSj()));
        if (null == fzjcxxDtos || fzjcxxDtos.size() == 0){
            throw new BusinessException("msg","没有该标本编码！");
        }
        if (StringUtil.isNotBlank(fzjcxxDto.getSfjs()) && fzjcxxDto.getSfjs().equals("1")){
            fzjcxxDtos.get(0).setNbbh(getFzjcxxByybbh());
            fzjcxxDtos.get(0).setSfjs("1");
        }else{
            fzjcxxDtos.get(0).setSfjs("0");
        }
        if (StringUtil.isBlank(fzjcxxDtos.get(0).getCjsj())){
            throw new BusinessException("msg","该用户还未录入！");
        }
        return fzjcxxDtos.get(0);
    }
    
    @Override
    public String getFzjcxxByybbh() {
        String yearLast = new SimpleDateFormat("yy", Locale.CHINESE).format(new Date().getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);// 设置星期一为一周开始的第一天
        calendar.setTimeInMillis(System.currentTimeMillis());// 获得当前的时间戳
        String weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR) + "";// 获得当前日期属于今年的第几周
        if ("1".equals(weekOfYear) && calendar.get(Calendar.MONTH) == 11){
            calendar.add(Calendar.DATE, -7);
            weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR) + "";// 获得当前日期属于今年的第几周
            weekOfYear = (Integer.parseInt(weekOfYear) + 1) + "";
        }
        String yearandweek = null;
        if (weekOfYear.length() == 1){
            yearandweek = yearLast + "0" + weekOfYear;
        } else if (weekOfYear.length() == 2){
            yearandweek = yearLast + weekOfYear;
        }
        String prefix = "JX" + yearandweek;
        // 查询流水号
        String serial = dao.getNbbhSerial(prefix);
        return prefix + serial;
    }
    /**
     * 获取主键id
     * @param
     * @return
     */
    public String getFzjcid(FzjcxxDto fzjcxxDto){
        return dao.getFzjcid(fzjcxxDto);
    }
    /**
     * 通过wxid查询患者信息
     * @param wxid
     * @return
     */
    public List<FzjcxxDto> getDtoListByWxid(String wxid){
        return dao.getDtoListByWxid(wxid);
    }
    /**
     * 报告日期查看
     * @param hzxxtDto
     * @return
     */
    public List<FzjcxxDto> bgrqDetailsByZjh(HzxxtDto hzxxtDto){
        return dao.bgrqDetailsByZjh(hzxxtDto);
    }

    /**
     * 更新分子检测结果信息
     * @param fzjcxxDto
     * @return
     */
    public boolean updateJcjg(FzjcxxDto fzjcxxDto){
        return dao.updateJcjg(fzjcxxDto);
    }
    /**
     * 废弃按钮
     * @param fzjcxxDto
     * @return
     */
    public boolean discardCovidDetails(FzjcxxDto fzjcxxDto){
        return dao.discardCovidDetails(fzjcxxDto);
    }

    @Override
    public boolean paySuccess(String str, Map<String, String> map) {
        FzjcxxModel fzjcxxModel = new FzjcxxModel();
        fzjcxxModel.setFzjcid(str);
        fzjcxxModel.setFkbj("1");
        fzjcxxModel.setSfje(String.valueOf(Double.parseDouble(map.get("txnAmt"))/100));
        fzjcxxModel.setOrderno(map.get("orderId"));
        fzjcxxModel.setPtorderno(map.get("cmbOrderId"));
        //支付方式
        fzjcxxModel.setZffs("银行"+map.get("payType"));
        Boolean success = dao.update(fzjcxxModel)!=0;
        if (success){
            amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECTION.getCode(), RabbitEnum.FKBJ_MOD.getCode()+ JSONObject.toJSONString(fzjcxxModel));
        }
        return success;
    }
    /**
     * 删除分子检测信息
     * @param fzjcxxDto
     * @return
     */
    public boolean delCovidDetails(FzjcxxDto fzjcxxDto){
        return dao.delCovidDetails(fzjcxxDto);
    }

    /**
     * 修改预约日期
     * @param fzjcxxDto
     * @return
     */
    public boolean updateAppDate(FzjcxxDto fzjcxxDto){
        return dao.updateAppDate(fzjcxxDto);
    }
	
    /**
     * 批量更新
     * @param list
     * @return
     */
    public int updateList(List<FzjcxxDto> list){
        return dao.updateList(list);
    }

    /**
     * 更新阿里订单信息
     * @param fzjcxxDto
     * @return
     */
    public boolean updateAliOrder(FzjcxxDto fzjcxxDto){
        return dao.updateAliOrder(fzjcxxDto);
    }

    /**
     * 钉钉混检删除操作回滚数据
     * @param fzjcxxDto
     * @return
     */
    public boolean callbackFzjcxx(FzjcxxDto fzjcxxDto){
        return dao.callbackFzjcxx(fzjcxxDto);
    }

    /**
     * 更新报告完成时间
     * @param fzjcxxDto
     * @return
     */
    public boolean updateBgwcsj(FzjcxxDto fzjcxxDto){
        return dao.updateBgwcsj(fzjcxxDto);
    }
}

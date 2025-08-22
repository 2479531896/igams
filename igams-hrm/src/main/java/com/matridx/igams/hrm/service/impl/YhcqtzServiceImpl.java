package com.matridx.igams.hrm.service.impl;

import java.text.SimpleDateFormat;
import java.util.*;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiAttendanceListRecordRequest;
import com.matridx.igams.common.dao.entities.UserDto;
import com.matridx.igams.common.service.svcinterface.IUserService;
import com.matridx.igams.hrm.dao.entities.YhcqtzDto;
import com.matridx.igams.hrm.dao.entities.YhcqtzModel;
import com.matridx.igams.hrm.dao.post.IYhcqtzDao;
import com.taobao.api.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dingtalk.api.response.OapiAttendanceListRecordResponse;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.hrm.service.svcinterface.IYhcqtzService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.igams.common.util.DingTalkUtil;
import org.springframework.util.CollectionUtils;

@Service
public class YhcqtzServiceImpl extends BaseBasicServiceImpl<YhcqtzDto, YhcqtzModel, IYhcqtzDao> implements IYhcqtzService{

	@Autowired
	DingTalkUtil talkUtil;
	@Autowired
	IUserService userService;
	@Autowired
	IXxglService xxglService;
	
	private final Logger log = LoggerFactory.getLogger(YhcqtzServiceImpl.class);
	
	public boolean insertDto(YhcqtzDto yhcqtzDto) {
		int result=dao.insert(yhcqtzDto);
		return result > 0;
	}
	
	/**
	 * 根据yhid获取用户出勤信息
	 */
	public YhcqtzDto getCqxxByYhid(YhcqtzDto yhcqtzDto) {
		return dao.getCqxxByYhid(yhcqtzDto);
	}
	
	/**
	 * 根据ids删除用户出勤信息
	 */
	public boolean delYhcqtzxx(YhcqtzDto yhcqtzDto) {
		return dao.delYhcqtzxx(yhcqtzDto);
	}



	/**
	 * 更新用户出勤信息
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public Map<String,Object> updateYhcqtz(YhcqtzDto yhcqtzDto){
		log.error("更新用户出勤信息");
		Map<String,Object> map = new HashMap<>();
		map.put("checkresult",false);//初始化map	
		YhcqtzDto checkyh=getCqxxByYhid(yhcqtzDto);
		if(checkyh!=null) {
			map.put("checkresult",true);
			map.put("msg", "用户已存在!");
			return map;
		}else {
			map.put("checkresult",false);
			//先删除用户出勤通知信息
			boolean delyhcqtz = dao.delByYyhid(yhcqtzDto);
			if(!delyhcqtz) {
				map.put("result", false);
				return map;
			}
			yhcqtzDto.setLrry(yhcqtzDto.getXgry());
			boolean insert=insertDto(yhcqtzDto);
			if(!insert) {
				map.put("result", false);
			}else {
				map.put("result", true);
			}
		}
		return map;
	}
	/**
	 * 定时更新出勤信息并通知相关上级人员
	 */

    public void getCheckinListRecordTimeTask() {
		Date date=new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat hms=new SimpleDateFormat("HH:mm:ss");
		String dqtime=hms.format(date);
		String dqdate=sdf.format(date);
		String checkDateFrom;
		String checkDateTo;
		YhcqtzDto yhcqtzDto=new YhcqtzDto();
		//判断传递上班时间还是下班时间
		if(dqtime.compareTo("14:00:00")<=0) {
			//上班
			checkDateFrom=sdf.format(date)+" 00:00:00";
			checkDateTo=sdf.format(date)+" 14:00:00";
			yhcqtzDto.setDuty_flag(true);
		}else{
			//下班
			checkDateFrom=sdf.format(date)+" 12:00:01";
			checkDateTo=sdf.format(date)+" 23:59:59";
			yhcqtzDto.setDuty_flag(false);
		}
		List<YhcqtzDto> yhcqtzlist=dao.getYhcqList(yhcqtzDto);//获取用户出勤列表
		Map<String, Object> map= new HashMap<>();
		int personnum=1;
		List<YhcqtzDto> xgxcqlist= new ArrayList<>();
		for (YhcqtzDto dto : yhcqtzlist) {
			//获取每个上级下有多少人员
			if (map.get(dto.getSjid()) != null) {
				map.put(dto.getSjid(), (int) map.get(dto.getSjid()) + 1);
			} else {
				map.put(dto.getSjid(), personnum);
			}
			//获取已打卡的人数情况
			//上班
			map.put("sbydk" + dto.getSjid(), 0);//设定初始值
			if (yhcqtzDto.isDuty_flag() && dqdate.equals(dto.getCqrq())) {
				if (map.get("sbydk" + dto.getSjid()) != null) {
					map.put("sbydk" + dto.getSjid(), (int) map.get("sbydk" + dto.getSjid()) + 1);
				} else {
					map.put("sbydk" + dto.getSjid(), personnum);
				}
			}
			//下班
			map.put("xbydk" + dto.getSjid(), 0);//设定初始值
			if (!yhcqtzDto.isDuty_flag() && dqdate.equals(dto.getTqrq())) {
				if (map.get("xbydk" + dto.getSjid()) != null) {
					map.put("xbydk" + dto.getSjid(), (int) map.get("xbydk" + dto.getSjid()) + 1);
				} else {
					map.put("xbydk" + dto.getSjid(), personnum);
				}
			}
			//获取需要更新打卡信息的list
			if (yhcqtzDto.isDuty_flag() && !dqdate.equals(dto.getCqrq())) {
				xgxcqlist.add(dto);
			} else if (!yhcqtzDto.isDuty_flag() && !dqdate.equals(dto.getTqrq())) {
				xgxcqlist.add(dto);
			}

		}
		//查询已更新的各上级下人员打卡情况
		List<String> ids= new ArrayList<>();
		if(!CollectionUtils.isEmpty(xgxcqlist)) {
			for (YhcqtzDto dto : xgxcqlist) {
				ids.add(dto.getYhid());
			}
			UserDto xtyhDto=new UserDto();
			xtyhDto.setIds(ids);
			List<UserDto> xtyhlist=userService.getListByIds(xtyhDto);
			List<String> ddids= new ArrayList<>();
			for (UserDto userDto : xtyhlist) {
				if (userDto.getDdid() != null) {
					ddids.add(userDto.getDdid());
				}
			}
			//处理钉钉接口一次请求最多接收50个id的问题	
			List<String> ddidlist= new ArrayList<>();
			for (String ddid : ddids) {
				if (ddidlist.size() < 50) {
					ddidlist.add(ddid);
				} else {
					getddjk(ddidlist, checkDateFrom, checkDateTo, yhcqtzDto, xgxcqlist, map);
					ddidlist = new ArrayList<>();
					ddidlist.add(ddid);
				}
			}
			getddjk(ddidlist,checkDateFrom,checkDateTo,yhcqtzDto,xgxcqlist, map);
		}
    }


    
    /**
     * 封装请求钉钉接口的方法
     */
    public void getddjk(List<String> ddids, String checkDateFrom, String checkDateTo, YhcqtzDto yhcqtzDto, List<YhcqtzDto> yhcqtzlist, Map<String, Object> map) {
		String token = talkUtil.getToken(null);
    	SimpleDateFormat newsdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	//获取签到信息
		DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/attendance/listRecord");
		OapiAttendanceListRecordRequest request = new OapiAttendanceListRecordRequest();
		request.setCheckDateFrom(checkDateFrom);
		request.setCheckDateTo(checkDateTo);
		request.setUserIds(ddids);
		try {
			OapiAttendanceListRecordResponse resp = client.execute(request, token);
			List<YhcqtzDto> list= new ArrayList<>();
			if(!CollectionUtils.isEmpty(resp.getRecordresult())) {
				for(int i=0;i<resp.getRecordresult().size();i++) {
					String ddid = resp.getRecordresult().get(i).getUserId();
					if(StringUtil.isBlank(ddid))
						continue;
					//上班打卡
					if(("OnDuty").equals(resp.getRecordresult().get(i).getCheckType())) {
						if(!yhcqtzDto.isDuty_flag()) {
                            for (YhcqtzDto dto : yhcqtzlist) {
                                if (ddid.equals(dto.getDdid())) {
                                    //出勤时间
                                    dto.setCqsj(newsdf.format(resp.getRecordresult().get(i).getUserCheckTime()));
                                    dto.setKqlx("OnDuty");
                                    dto.setKqlxmc("上班打卡");
                                    dto.setDkdd(resp.getRecordresult().get(i).getUserAddress());
                                    list.add(dto);
                                    break;
                                }
                            }
						}else {
							for(int j=0;j<yhcqtzlist.size();j++) {
								if(ddid.equals(yhcqtzlist.get(j).getDdid())) {
									//出勤时间
									yhcqtzlist.get(j).setCqsj(newsdf.format(resp.getRecordresult().get(i).getUserCheckTime()));
									yhcqtzlist.get(j).setKqlx("OnDuty");
									yhcqtzlist.get(j).setKqlxmc("上班打卡");
									yhcqtzlist.get(j).setDkdd(resp.getRecordresult().get(i).getUserAddress());
									list.add(yhcqtzlist.get(j));

									yhcqtzlist.remove(j);
									break;
								}
							}
						}
					}
					else if(("OffDuty").equals(resp.getRecordresult().get(i).getCheckType())) {
						for(int j=0;j<yhcqtzlist.size();j++) {
							if(ddid.equals(yhcqtzlist.get(j).getDdid())) {
								//退勤时间
								yhcqtzlist.get(j).setTqsj(newsdf.format(resp.getRecordresult().get(i).getUserCheckTime()));
								yhcqtzlist.get(j).setKqlx("OffDuty");
								yhcqtzlist.get(j).setKqlxmc("下班打卡");
								yhcqtzlist.get(j).setDkdd(resp.getRecordresult().get(i).getUserAddress());
								list.add(yhcqtzlist.get(j));

								yhcqtzlist.remove(j);
								break;
							}
						}
					}
				}
				//更新出勤信息
				boolean updateCqAndTqsj=dao.updateCqAndTq(list);

				//通知相应上级人员
				if(updateCqAndTqsj) {
					String ddtitle = xxglService.getMsg("ICOMM_CQ00001");
					String ddinfo = xxglService.getMsg("ICOMM_CQ00002");
					int ycqzrs=0;//该上级人员下应出勤总人数
					int ycqrs=0;//该上级人员下已出勤人数
					String dkqk;//打卡情况
                    for (YhcqtzDto dto : list) {
                        ycqzrs = (int) map.get(dto.getSjid());
                        if (StringUtil.isNotBlank(dto.getSjddid()) && ("OnDuty").equals(dto.getKqlx())) {
                            //上班已打卡人数
                            if ((int) map.get("sbydk" + dto.getSjid()) != 0) {
                                ycqrs = (int) map.get("sbydk" + dto.getSjid()) + 1;
                                map.put("sbydk" + dto.getSjid(), ycqrs);
                            } else {
                                ycqrs = 1;
                                map.put("sbydk" + dto.getSjid(), ycqrs);
                            }
                            dkqk = ycqrs + " / " + ycqzrs;
                            talkUtil.sendWorkMessage(dto.getSjyhm(), dto.getSjddid(), ddtitle, StringUtil.replaceMsg(ddinfo, dto.getYhmc(), dto.getKqlxmc(), dto.getCqsj(), dto.getDkdd(), dkqk));
                        } else if (StringUtil.isNotBlank(dto.getSjddid()) && ("OffDuty").equals(dto.getKqlx())) {
                            //下班已打卡人数
                            if ((int) map.get("xbydk" + dto.getSjid()) != 0) {
                                ycqrs = (int) map.get("xbydk" + dto.getSjid()) + 1;
                                map.put("xbydk" + dto.getSjid(), ycqrs);
                            } else {
                                ycqrs = 1;
                                map.put("xbydk" + dto.getSjid(), ycqrs);
                            }
                            dkqk = ycqrs + " / " + ycqzrs;
                            talkUtil.sendWorkMessage(dto.getSjyhm(), dto.getSjddid(), ddtitle, StringUtil.replaceMsg(ddinfo, dto.getYhmc(), dto.getKqlxmc(), dto.getTqsj(), dto.getDkdd(), dkqk));
                        }
                    }
				}
			}
		} catch (ApiException e) {
			throw new RuntimeException(e);
		}
    }
}

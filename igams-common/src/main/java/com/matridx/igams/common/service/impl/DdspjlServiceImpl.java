package com.matridx.igams.common.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.matridx.igams.common.dao.entities.DdspjlDto;
import com.matridx.igams.common.dao.entities.DdspjlModel;
import com.matridx.igams.common.dao.entities.UserDto;
import com.matridx.igams.common.dao.post.IDdspjlDao;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IDdspjlService;
import com.matridx.igams.common.service.svcinterface.IUserService;
import com.matridx.springboot.util.base.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/**
 * @author : 郭祥杰
 * @date :
 */
@Service
public class DdspjlServiceImpl  extends BaseBasicServiceImpl<DdspjlDto, DdspjlModel, IDdspjlDao> implements IDdspjlService {
    @Autowired
    private IUserService userService;

    @Override
    public List<String> filtrateDdslid(DdspjlDto ddspjlDto) {
        return dao.filtrateDdslid(ddspjlDto);
    }

    @Override
    public boolean insertList(List<DdspjlDto> ddspjlDtoList) {
        return dao.insertList(ddspjlDtoList);
    }

    /**
     * @Description: 获取查看数据
     * @param processinstanceid
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2025/7/11 10:44
     */
    @Override
    public Map<String, Object> queryView(String processinstanceid) {
        DdspjlDto ddspjlDto = dao.getDtoById(processinstanceid);
        Map<String,Object> map = new HashMap<>();
        if (ddspjlDto == null) {
            return null;
        }
        map = assembleData(ddspjlDto);
        return map;
    }

    /**
     * @Description: 组装数据
     * @param ddspjlDto
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2025/7/11 10:44
     */
    private Map<String,Object> assembleData(DdspjlDto ddspjlDto){
       Map<String,Object> map = new HashMap<>();
        assembleFormcomponentValues(ddspjlDto.getFormcomponentvalues(),map);
        assembleOperationRecords(ddspjlDto.getOperationrecords(),map);
        assembleCcuserids(ddspjlDto.getCcuserids(),map);
        return map;
    }

    /**
     * @Description: 组装组件值
     * @param formcomponentvaluesStr
     * @param map
     * @return void
     * @Author: 郭祥杰
     * @Date: 2025/7/11 10:04
     */
    private void assembleFormcomponentValues(String formcomponentvaluesStr,Map<String,Object> map){
        List<Map<String,Object>> formcomponentvalues = new ArrayList<>();
        if(StringUtil.isNotBlank(formcomponentvaluesStr)){
            formcomponentvalues = JSON.parseObject(formcomponentvaluesStr, new TypeReference<>(){});
            if(!CollectionUtils.isEmpty(formcomponentvalues)){
                for (Map<String,Object> formMap : formcomponentvalues){
                    if(formMap!=null && formMap.get("componentType")!=null && formMap.get("value")!=null && StringUtil.isNotBlank(formMap.get("value").toString()) && "TableField".equals(formMap.get("componentType"))){
                        List<Map<String,Object>> valueMap = JSON.parseObject(formMap.get("value").toString(), new TypeReference<>(){});
                        List<List<Map<String,Object>>> lists = new ArrayList<>();
                        formMap.put("value",lists);
                        if(!CollectionUtils.isEmpty(valueMap)) {
                            for (Map<String, Object> value : valueMap) {
                                if(value!=null && value.get("rowValue")!=null && StringUtil.isNotBlank(value.get("rowValue").toString())){
                                    List<Map<String,Object>> rowValueList = JSON.parseObject(value.get("rowValue").toString(), new TypeReference<>(){});
                                    if(!CollectionUtils.isEmpty(rowValueList)){
                                        lists.add(rowValueList);
                                    }
                                }
                            }
                        }
                        formMap.put("value",lists);
                    }
                    if(formMap!=null && formMap.get("componentType")!=null && formMap.get("value")!=null && StringUtil.isNotBlank(formMap.get("value").toString()) && "DDAttachment".equals(formMap.get("componentType"))){
                        List<Map<String,Object>> valueMap = JSON.parseObject(formMap.get("value").toString(), new TypeReference<>(){});
                        formMap.put("value",valueMap);
                    }
                    if(formMap!=null && formMap.get("componentType")!=null && formMap.get("value")!=null && StringUtil.isNotBlank(formMap.get("value").toString()) && "DDPhotoField".equals(formMap.get("componentType"))){
                        List<Map<String,Object>> valueMap = JSON.parseObject(formMap.get("value").toString(), new TypeReference<>(){});
                        formMap.put("value",valueMap);
                    }
                    if(formMap!=null && formMap.get("componentType")!=null && formMap.get("extValue")!=null && StringUtil.isNotBlank(formMap.get("extValue").toString()) && formMap.get("value")!=null && StringUtil.isNotBlank(formMap.get("value").toString()) && "RelateField".equals(formMap.get("componentType"))){
                        Map<String,Object> extValueMap = JSON.parseObject(formMap.get("extValue").toString(), new TypeReference<>(){});
                        List<String> list = JSON.parseArray(formMap.get("value").toString(), String.class);
                        List<Map<String,Object>> extValueList = new ArrayList<>();
                        formMap.put("extValue",extValueList);
                        if(extValueMap!=null && extValueMap.get("list")!=null && StringUtil.isNotBlank(extValueMap.get("list").toString())){
                            extValueList = JSON.parseObject(extValueMap.get("list").toString(), new TypeReference<>(){});
                            List<Map<String,Object>> listMap = new ArrayList<>();
                            for(int i = 0;i<extValueList.size();i++){
                                Map<String,Object> mapT = extValueList.get(i);
                                mapT.put("value",list.get(i));
                                listMap.add(mapT);
                            }
                            if(!CollectionUtils.isEmpty(listMap)){
                                formMap.put("extValue",listMap);
                            }
                        }
                    }
                }
            }
        }
        map.put("formcomponentvalue",formcomponentvalues);
    }

    /**
     * @Description: 组装操作记录
     * @param operationrecordsStr
     * @param map
     * @return void
     * @Author: 郭祥杰
     * @Date: 2025/7/11 10:05
     */
    private void assembleOperationRecords(String operationrecordsStr,Map<String,Object> map){
        List<Map<String,Object>> operationrecords = new ArrayList<>();
        if(StringUtil.isNotBlank(operationrecordsStr)) {
            operationrecords = JSON.parseObject(operationrecordsStr, new TypeReference<>() {});
            if (!CollectionUtils.isEmpty(operationrecords)) {
                StringBuilder ddids = new StringBuilder();
                List<UserDto> userDtoList = new ArrayList<>();
                for (Map<String, Object> formMap : operationrecords) {
                    if(formMap.get("userId")!=null && StringUtil.isNotBlank(formMap.get("userId").toString())) {
                        ddids.append(",").append(formMap.get("userId").toString());
                    }
                }

                if(StringUtil.isNotBlank(ddids.toString())) {
                    UserDto userDto = new UserDto();
                    userDto.setIds(ddids.substring(1));
                    userDtoList = userService.getListByDdids(userDto);
                }
                for (Map<String, Object> formMap : operationrecords) {
                    if(formMap.get("userId")!=null && StringUtil.isNotBlank(formMap.get("userId").toString())) {
                        if(!CollectionUtils.isEmpty(userDtoList)) {
                            for (UserDto userDto : userDtoList) {
                                if(userDto.getDdid().equals(formMap.get("userId").toString())){
                                    formMap.put("userId",userDto.getYhm()+"-"+userDto.getZsxm());
                                    break;
                                }
                            }
                        }
                    }
                    if(formMap.get("type")!=null && StringUtil.isNotBlank(formMap.get("type").toString())){
                        if("EXECUTE_TASK_NORMAL".equals(formMap.get("type").toString())){
                            formMap.put("type","正常执行任务");
                        }
                        if("EXECUTE_TASK_AGENT".equals(formMap.get("type").toString())){
                            formMap.put("type","代理人执行任务");
                        }
                        if("APPEND_TASK_BEFORE".equals(formMap.get("type").toString())){
                            formMap.put("type","前加签任务");
                        }
                        if("APPEND_TASK_AFTER".equals(formMap.get("type").toString())){
                            formMap.put("type","后加签任务");
                        }
                        if("REDIRECT_TASK".equals(formMap.get("type").toString())){
                            formMap.put("type","转交任务");
                        }
                        if("START_PROCESS_INSTANCE".equals(formMap.get("type").toString())){
                            formMap.put("type","发起流程实例");
                        }
                        if("TERMINATE_PROCESS_INSTANCE".equals(formMap.get("type").toString())){
                            formMap.put("type","终止(撤销)流程实例");
                        }
                        if("FINISH_PROCESS_INSTANCE".equals(formMap.get("type").toString())){
                            formMap.put("type","结束流程实例");
                        }
                        if("ADD_REMARK".equals(formMap.get("type").toString())){
                            formMap.put("type","添加评论");
                        }
                        if("REDIRECT_PROCESS".equals(formMap.get("type").toString())){
                            formMap.put("type","审批退回");
                        }
                        if("PROCESS_CC".equals(formMap.get("type").toString())){
                            formMap.put("type","抄送");
                        }
                    }
                    if(formMap.get("result")!=null && StringUtil.isNotBlank(formMap.get("result").toString())){
                        if("AGREE".equals(formMap.get("result").toString())){
                            formMap.put("result","同意");
                        }
                        if("REFUSE".equals(formMap.get("result").toString())){
                            formMap.put("result","拒绝");
                        }
                        if("NONE".equals(formMap.get("result").toString())){
                            formMap.put("result","");
                        }
                    }
                    if(formMap.get("date")!=null && StringUtil.isNotBlank(formMap.get("date").toString())){
                        DateTimeFormatter parser = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm[:ss][X]")
                                .withZone(ZoneId.of("UTC"));
                        Instant instant = Instant.from(parser.parse(formMap.get("date").toString()));
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                                .withZone(ZoneId.of("UTC"));
                        formMap.put("date",formatter.format(instant));
                    }
                    if(formMap.get("images")!=null && StringUtil.isNotBlank(formMap.get("images").toString())){
                        List<Map<String,Object>> imagesList = JSON.parseObject(formMap.get("images").toString(), new TypeReference<>(){});
                        formMap.put("images",imagesList);
                    }
                    if(formMap.get("attachments")!=null && StringUtil.isNotBlank(formMap.get("attachments").toString())){
                        List<Map<String,Object>> attachmentsList = JSON.parseObject(formMap.get("attachments").toString(), new TypeReference<>(){});
                        formMap.put("attachments",attachmentsList);
                    }
                }
                //sortByDate(operationrecords);
            }
        }
        map.put("operationrecords",operationrecords);
    }

    /**
     * @Description: 组装抄送人数据
     * @param ccuseridsStr
     * @param map
     * @return void
     * @Author: 郭祥杰
     * @Date: 2025/7/11 14:58
     */
    private void assembleCcuserids(String ccuseridsStr,Map<String,Object> map){
        List<String> userList = new ArrayList<>();
        if(StringUtil.isNotBlank(ccuseridsStr)) {
            List<String> ccuserids = JSON.parseArray(ccuseridsStr, String.class);
            if(!CollectionUtils.isEmpty(ccuserids)) {
                UserDto userDto = new UserDto();
                userDto.setIds(ccuserids);
                List<UserDto> userDtoList = userService.getListByDdids(userDto);
                for (UserDto dto:userDtoList){
                    userList.add(dto.getYhm()+"-"+dto.getZsxm());
                }
                userList = new ArrayList<>(new LinkedHashSet<>(userList));
            }
        }
        map.put("ccuserids",userList);
    }
    /**
     * @Description: 按date排序
     * @param list
     * @return void
     * @Author: 郭祥杰
     * @Date: 2025/7/11 10:55
     */
    private void sortByDate(List<Map<String, Object>> list) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        list.sort((map1, map2) -> {
            try {
                String d1 = (String) map1.get("date");
                String d2 = (String) map2.get("date");
                if (d1 == null && d2 == null){
                    return 0;
                }
                if (d1 == null){
                    return -1;
                }
                if (d2 == null){
                    return 1;
                }
                LocalDateTime dt1 = LocalDateTime.parse(d1, formatter.withZone(ZoneId.of("UTC")));
                LocalDateTime dt2 = LocalDateTime.parse(d2, formatter.withZone(ZoneId.of("UTC")));
                return dt1.compareTo(dt2);
            } catch (Exception e) {
                return -1;
            }
        });
    }

}

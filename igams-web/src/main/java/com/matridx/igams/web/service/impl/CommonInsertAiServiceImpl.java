package com.matridx.igams.web.service.impl;

import com.matridx.igams.common.dao.entities.LscxszDto;
import com.matridx.igams.common.service.svcinterface.ILscxszService;
import com.matridx.igams.web.dao.matridxsql.ICommonInsertAiDao;
import com.matridx.igams.web.service.svcinterface.ICommonInsertAiService;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : 郭祥杰
 * @date :
 */
@Service
public class CommonInsertAiServiceImpl implements ICommonInsertAiService {
    @Autowired
    private ICommonInsertAiDao commonInsertAiDao;
    @Autowired
    private ILscxszService lscxszService;
    private final Logger log = LoggerFactory.getLogger(CommonInsertAiServiceImpl.class);

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, transactionManager = "mySqlTransactionManager")
    public void assembleAiData(Map<String, String> map) {
        String ids = map.get("ids");
        ids = ids.replace("/", ",");
        LscxszDto lscxszDto = new LscxszDto();
        lscxszDto.setIds(ids);
        List<LscxszDto> lscxszDtoList = lscxszService.selectLscxByQfdm(lscxszDto);
        if(!CollectionUtils.isEmpty(lscxszDtoList)){
            for (LscxszDto lscxszDtoT : lscxszDtoList){
                if("select".equals(lscxszDtoT.getCxqfcskz1())){
                    String cxdmone = lscxszService.dealStatisticsQuerySql(lscxszDtoT, null);
                    lscxszDtoT.setCxdm(cxdmone);
                    String cxdm = lscxszService.dealQuerySql(lscxszDtoT, null);
                    lscxszDto.setCxdm(cxdm);
                    List<LinkedHashMap<String, Object>> listResult = lscxszService.getResult(lscxszDto);
                    if(!CollectionUtils.isEmpty(listResult)){
                        String delCxdm = "";
                        LocalDateTime now = LocalDateTime.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        String formattedDateTime = now.format(formatter);
                        for (LscxszDto lscxszDtoS : lscxszDtoList){
                            if(lscxszDtoS.getCxbm().equals(lscxszDtoT.getCxbm()) && "insert".equals(lscxszDtoS.getCxqfcskz1())){
                                String cxdmS = lscxszDtoS.getCxdm();
                                if(listResult.size()>100){
                                    int length = (int)Math.ceil((double)listResult.size() / 100);
                                    for(int i=0;i<length;i++){
                                        int t_length;
                                        if (i != length - 1) {
                                            t_length = (i + 1) * 100;
                                        } else {
                                            t_length = listResult.size();
                                        }
                                        String insertSql = "";
                                        for (int j = i * 100; j < t_length; j++) {
                                            String insertField = "";
                                            for (Object value : listResult.get(j).values()) {
                                                insertField = insertField + ",'" + value + "'";
                                            }
                                            insertField = insertField.substring(1);
                                            insertSql = insertSql + ",(" + insertField +",'"+formattedDateTime+ "')";
                                        }
                                        insertSql = insertSql.substring(1);
                                        String cxdmX = cxdmS.replace("${sql}", insertSql);
                                        commonInsertAiDao.insertResult(cxdmX);
                                    }
                                }else {
                                    String insertSql = "";
                                    for(LinkedHashMap<String, Object> hashMap:listResult) {
                                        String insertField = "";
                                        for (Object value : hashMap.values()) {
                                            insertField = insertField + ",'" + value + "'";
                                        }
                                        insertField = insertField.substring(1);
                                        insertSql = insertSql + ",(" + insertField +",'"+formattedDateTime+ "')";
                                    }
                                    insertSql = insertSql.substring(1);
                                    cxdmS = cxdmS.replace("${sql}", insertSql);
                                    commonInsertAiDao.insertResult(cxdmS);
                                }
                            }
                            if(lscxszDtoS.getCxbm().equals(lscxszDtoT.getCxbm()) && "del".equals(lscxszDtoS.getCxqfcskz1())){
                                delCxdm = lscxszDtoS.getCxdm();
                            }
                        }
                        if(StringUtil.isNotBlank(delCxdm)){
                            delCxdm = delCxdm.replace("${dateTime}",formattedDateTime );
                            commonInsertAiDao.delResult(delCxdm);
                        }
                    }
                }
            }
        }
    }
}

package com.matridx.igams.common.util;

import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WechatCommonUtils {

    @Autowired
    RedisUtil redisUtil;

    //获取送检模块相关的word业务类型
    public List<String> getInspectionWordYwlxs() {
        List<String> word_ywlxs=new ArrayList<>();
        List<JcsjDto> wordywlxlist = redisUtil.lgetDto("All_matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode());
        if(wordywlxlist!=null&&!wordywlxlist.isEmpty()){
            for(JcsjDto wordDto : wordywlxlist){
                word_ywlxs.add(wordDto.getCskz3()+"_"+wordDto.getCskz1()+"_WORD");
            }
        }
        List<JcsjDto> pdfywzlxlist = redisUtil.lgetDto("All_matridx_jcsj:" + BasicDataTypeEnum.DETECT_SUBTYPE.getCode());
        if(pdfywzlxlist!=null&&!pdfywzlxlist.isEmpty()){
            for(JcsjDto wordDto : pdfywzlxlist){
                if(StringUtil.isBlank(wordDto.getCskz3()))
                    continue;
                word_ywlxs.add(wordDto.getCskz3()+"_"+wordDto.getCskz1()+"_WORD");
            }
        }
        return word_ywlxs;
    }

    //获取送检模块相关的word业务类型
    public List<String> getInspectionPdfYwlxs() {
        List<String> pdf_ywlxs=new ArrayList<>();
        List<JcsjDto> pdfywlxlist = redisUtil.lgetDto("All_matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode());
        if(pdfywlxlist!=null&&!pdfywlxlist.isEmpty()){
            for(JcsjDto wordDto : pdfywlxlist){
                pdf_ywlxs.add(wordDto.getCskz3()+"_"+wordDto.getCskz1());
            }
        }
        List<JcsjDto> pdfywzlxlist = redisUtil.lgetDto("All_matridx_jcsj:" + BasicDataTypeEnum.DETECT_SUBTYPE.getCode());
        if(pdfywzlxlist!=null&&!pdfywzlxlist.isEmpty()){
            for(JcsjDto wordDto : pdfywzlxlist){
                if(StringUtil.isBlank(wordDto.getCskz3()))
                    continue;
                pdf_ywlxs.add(wordDto.getCskz3()+"_"+wordDto.getCskz1());
            }
        }
        return pdf_ywlxs;
    }
}

package com.matridx.igams.common.config.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import com.matridx.springboot.util.base.StringUtil;

/**
 * 默认数据库设置时，Dto文件路径设置只能设置一个，无法设置多个。特增加处理该事务的方法
 * @author linwu
 *
 */
public class IgamsSqlSessionFactoryBean extends SqlSessionFactoryBean{
	
	static final String DEFAULT_RESOURCE_PATTERN = "**/dao/**/*.class";
    
	private final Logger logger = LoggerFactory.getLogger(IgamsSqlSessionFactoryBean.class);
    
	@Override    
    public void setTypeAliasesPackage(String typeAliasesPackage) {    
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resolver);    
        typeAliasesPackage = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +    
                ClassUtils.convertClassNameToResourcePath(typeAliasesPackage) + "/" + DEFAULT_RESOURCE_PATTERN;    
    
        //将加载多个绝对匹配的所有Resource    
        //将首先通过ClassLoader.getResource("META-INF")加载非模式路径部分    
        //然后进行遍历模式匹配    
        try {    
            List<String> result = new ArrayList<>();
            Resource[] resources =  resolver.getResources(typeAliasesPackage);    
            if(resources.length > 0){
                MetadataReader metadataReader;
                for(Resource resource : resources){
                    if(resource.isReadable()){
                    	//System.out.println(resource);
                    	metadataReader =  metadataReaderFactory.getMetadataReader(resource);    
                        try {    
                            result.add(Class.forName(metadataReader.getClassMetadata().getClassName()).getPackage().getName());    
                        } catch (ClassNotFoundException e) {    
                            e.printStackTrace();    
                        }    
                    }    
                }    
            }    
            if(!result.isEmpty()) {
                super.setTypeAliasesPackage(StringUtil.listToString(result, ","));
            }else{    
                logger.warn("参数typeAliasesPackage:"+typeAliasesPackage+"，未找到任何包");    
            }    
            //logger.info("d");    
        } catch (IOException e) {    
            e.printStackTrace();    
        }    
    }
}

package com.matridx.igams.common.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.UserDto;
import com.matridx.igams.common.dao.entities.UserModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author WYX
 * @version 1.0
 * {@code @className} IUserDao
 * TODO
 * {@code @date} 16:17 2023/9/1
 **/
@Mapper
public interface IUserDao extends BaseBasicDao<UserDto, UserModel> {
    /**
     * 用来查询外部程序
     * userDto
     * 
     */
    List<UserDto> getWbcxDtoList(UserDto userDto);
    /**
     * 通过yhid获取用户
     * userDto
     * 
     */
    UserDto getYhByDdid(UserDto userDto);
    /**
     * 获取删除标记不为1的所有用户
     * []
     *  java.util.List<UserDto>
     */
    List<UserDto> getAllUserList(UserDto userDto);
    /**
     * 根据用户Ids查询用户信息
     * userDto
     * 
     */
    List<UserDto> getListByIds(UserDto userDto);
    /**
     * 获取直属主管
     * userDto
     * 
     */
    UserDto getZszgByYh(UserDto userDto);
    /*
    获取外部程序
 */
    UserDto getWbcxDto(UserDto userDto);
    /*
     获取部主管信息
    */
    List<UserDto> getBmzgsInfo(UserDto userDto);

    /**
     * @Description: 根据ddids查询用户信息
     * @param userDto
     * @return java.util.List<com.matridx.igams.common.dao.entities.UserDto>
     * @Author: 郭祥杰
     * @Date: 2025/7/11 10:16
     */
    List<UserDto> getListByDdids(UserDto userDto);
}

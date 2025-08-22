package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.dao.entities.UserDto;
import com.matridx.igams.common.dao.entities.UserModel;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;

/**
 * @author WYX
 * @version 1.0
 * {@code @className} UserService
 * {@code @description} TODO
 * {@code @date} 16:15 2023/9/1
 **/
public interface IUserService extends BaseBasicService<UserDto, UserModel> {
    /**
     * 用来查询外部程序
     */
    List<UserDto> getWbcxDtoList(UserDto userDto);
    /**
     * 通过ddid获取用户
     */
    UserDto getYhByDdid(UserDto userDto);
    /**
     * 获取删除标记不为1的所有用户
     */
    List<UserDto> getAllUserList(UserDto userDto);
    /**
     * 根据用户Ids查询用户信息
     */
    List<UserDto> getListByIds(UserDto userDto);

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

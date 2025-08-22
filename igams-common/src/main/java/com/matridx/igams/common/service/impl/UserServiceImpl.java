package com.matridx.igams.common.service.impl;
import com.matridx.igams.common.dao.entities.UserDto;
import com.matridx.igams.common.dao.entities.UserModel;
import com.matridx.igams.common.dao.post.IUserDao;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IUserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author WYX
 * @version 1.0
 * {@code @className} UserServiceImpl
 * {@code @description} TODO
 * {@code @date} 16:16 2023/9/1
 **/
@Service
public class UserServiceImpl extends BaseBasicServiceImpl<UserDto, UserModel, IUserDao> implements IUserService {
    @Override
    public List<UserDto> getWbcxDtoList(UserDto wbcxDto) {
        return dao.getWbcxDtoList(wbcxDto);
    }

    @Override
    public UserDto getYhByDdid(UserDto xtyhDto) {
        return dao.getYhByDdid(xtyhDto);
    }

    @Override
    public List<UserDto> getAllUserList(UserDto xtyhDto) {
        return dao.getAllUserList(xtyhDto);
    }

    @Override
    public List<UserDto> getListByIds(UserDto userDto) {
        return dao.getListByIds(userDto);
    }

    @Override
    public UserDto getZszgByYh(UserDto userDto) {
        return dao.getZszgByYh(userDto);
    }

    @Override
    public UserDto getWbcxDto(UserDto userDto) {
        return dao.getWbcxDto(userDto);
    }

    @Override
    public List<UserDto> getBmzgsInfo(UserDto userDto) {
        return dao.getBmzgsInfo(userDto);
    }

    @Override
    public List<UserDto> getListByDdids(UserDto userDto) {
        return dao.getListByDdids(userDto);
    }
}

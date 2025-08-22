package com.matridx.igams.wechat.service.svcinterface;



import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.KhhtglDto;
import com.matridx.igams.wechat.dao.entities.KhhtglModel;

import java.util.List;

/**
 * 客户合同管理(IgamsKhhtgl)表服务接口
 *
 * @author makejava
 * @since 2023-04-04 13:50:57
 */
public interface IKhhtglService extends BaseBasicService<KhhtglDto, KhhtglModel> {

    /**
     * 批量保存
     */
    Boolean insertList(List<KhhtglDto> list);

}

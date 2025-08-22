package com.matridx.igams.detection.molecule.service.svcinterface;

import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.detection.molecule.dao.entities.FzjcxxDto;
import com.matridx.igams.detection.molecule.dao.entities.KzbglDto;
import com.matridx.igams.detection.molecule.dao.entities.KzbglModel;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import java.util.Map;

public interface IKzbglService extends BaseBasicService<KzbglDto, KzbglModel>{

    /**
     * 通过jsid获取角色检测单位
     */
    List<Map<String, String>> getJsjcdwByjsid(String dqjs);

    /**
     * 自动生成扩增板号
     */
    String generateKzbh(KzbglDto kzbglDto);

    /**
     * 新增扩增板明细数据
     */
    boolean insertKzbmx(KzbglDto kzbglDto);

    /**
     * 修改扩增板相关信息
     */
    boolean updateData(KzbglDto kzbglDto);

    /**
     * 删除扩增板相关信息
     */
    boolean deleteData(KzbglDto kzbglDto);

    /**
     * 判断填写的ybbh是否存在
     */
    List<String> exitYbbh(KzbglDto kzbglDto);
    /**
     * 新增扩增板
     */
    boolean insertKzb(KzbglDto kzbglDto, FzjcxxDto fzjcxxDto, User user);


    /**
     * 生成Excel
     */
     void exportKzb(HttpServletResponse response, File file, String fileName, String sheetName,
                           List<List<Object>> sheetDataList, Map<Integer, List<String>> selectMap);
}

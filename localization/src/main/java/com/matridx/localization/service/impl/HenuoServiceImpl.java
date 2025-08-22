package com.matridx.localization.service.impl;

import com.matridx.localization.dao.henuosql.IHenuoDao;
import com.matridx.localization.service.svcinterface.IHenuoService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : 郭祥杰
 * @date :
 */
@Service
public class HenuoServiceImpl implements IHenuoService {
    @Autowired
    private IHenuoDao dao;
    @Override
    public String getHospitalName(String barcode) {
        return StringUtil.isNotBlank(barcode)?dao.getHospitalName(barcode):"";
    }
}

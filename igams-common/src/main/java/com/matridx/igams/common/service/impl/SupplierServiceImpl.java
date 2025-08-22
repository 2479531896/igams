package com.matridx.igams.common.service.impl;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.dao.entities.SupplierDto;
import com.matridx.igams.common.dao.post.ISupplierDao;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.ISupplierService;

@Service
public class SupplierServiceImpl extends BaseBasicServiceImpl<SupplierDto, SupplierDto, ISupplierDao> implements ISupplierService{

}

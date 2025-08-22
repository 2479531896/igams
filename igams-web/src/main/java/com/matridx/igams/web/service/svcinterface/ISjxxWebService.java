package com.matridx.igams.web.service.svcinterface;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(name = "MyWebService",targetNamespace="http://service.matridx.com")
public interface ISjxxWebService {
	@WebMethod
    String downloadByCode(@WebParam(name = "organ")String organ, @WebParam(name = "code")String code,
    		@WebParam(name = "lastcode")String lastcode, @WebParam(name = "sign")String sign, @WebParam(name = "type")String type);
	
	@WebMethod
	String receiveInspectInfo(@WebParam(name = "organ")String organ, @WebParam(name = "sign")String sign, @WebParam(name = "param")String param);

	@WebMethod
	String receiveDetectionInfo (@WebParam(name = "param")String param);
}

package com.tonghuafund.tppreconfileproxy.webservices;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface FetchReconFileService {
	
	@WebMethod(operationName="generateFetchTask")
	public boolean generateFetchTask();
	
	@WebMethod(operationName="execFetchTask")
	public boolean execFetchTask();

}

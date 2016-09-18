package com.tonghuafund.tppproxy;

import java.io.InputStream;
import java.util.Properties;

import com.allinpay.util.MySqlImportAndExport;



public class TestTppProxyController {

	public static void main(String[] args) throws Exception {
		InputStream is = MySqlImportAndExport.class.getClassLoader().getResourceAsStream("jdbc.properties");  
        Properties properties = new Properties();  
        properties.load(is);  
//      MySqlImportAndExport.export(properties);//����򵥵��쳣�Ҿ�ֱ��������  
        MySqlImportAndExport.importSql(properties);
		
	}

}

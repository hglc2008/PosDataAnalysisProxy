package com.tonghuafund.tppproxy;

import java.io.InputStream;
import java.util.Properties;

import com.allinpay.util.MySqlImportAndExport;



public class TestTppProxyController {

	public static void main(String[] args) throws Exception {
		InputStream is = MySqlImportAndExport.class.getClassLoader().getResourceAsStream("jdbc.properties");  
        Properties properties = new Properties();  
        properties.load(is);  
//      MySqlImportAndExport.export(properties);//这里简单点异常我就直接往上抛  
        MySqlImportAndExport.importSql(properties);
		
	}

}

package com.tonghuafund.tppreconfileproxy.webservices.impl;

import java.io.InputStream;
import java.util.Properties;

import javax.jws.WebService;

import com.allinpay.util.CogradientImgFileManager;
import com.tonghuafund.tppreconfileproxy.webservices.FetchReconFileService;

@WebService(endpointInterface = "com.tonghuafund.tppreconfileproxy.webservices.FetchReconFileService", targetNamespace = "http://webservices.tppreconfileproxy.tonghuafund.com/")
public class FetchReconFileServiceImpl implements FetchReconFileService {

	/**
	 * hive统计数据将结果保存在。csv文件中
	 */
	@Override
	public boolean generateFetchTask() {
		// 1.读取配置文件
				InputStream is = CogradientImgFileManager.class.getClassLoader()
						.getResourceAsStream("jdbc.properties");
				Properties props = new Properties();
				try {
					props.load(is);
				} catch (Exception e) {
					e.printStackTrace();
				}

				// 2.根据文件信息和表信息导入数据
				try {
					CogradientImgFileManager.versouSshUtil(
							props.getProperty("hive.host"),
							props.getProperty("hive.username"),
							props.getProperty("hive.password"),
							new Integer(props.getProperty("hive.port")));
					// 拼装命令
					//hive -e "select MCHT_CD,sum(TRANS_AMT),count(TXN_NUM)  from POSL.APMS_LOGINFO group by MCHT_CD sort by MCHT_CD" >> /home/hadoop/test4.csv
					//环境变量问题，81环境只能执行sh脚本
//					String cmd = new StringBuffer("hive -e \"")
//							.append(props.getProperty("hive.hql"))
//							.append("\" >> ")
//							.append(props.getProperty("hive.filepath"))
//							.toString();
					String cmd = "sh /home/hadoop/hql.sh";
					CogradientImgFileManager.runCmd(cmd, "UTF-8");
					Thread.sleep(15000);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					CogradientImgFileManager.close();
				}

				return true;
	}

	/**
	 * 将**。csv文件导入到mysql数据库中
	 */
	@Override
	public boolean execFetchTask() {
		// 1.读取配置文件
		InputStream is = CogradientImgFileManager.class.getClassLoader()
				.getResourceAsStream("jdbc.properties");
		Properties props = new Properties();
		try {
			props.load(is);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 2.根据文件信息和表信息导入数据
		try {
			CogradientImgFileManager.versouSshUtil(
					props.getProperty("remote.host"),
					props.getProperty("remote.username"),
					props.getProperty("remote.password"),
					new Integer(props.getProperty("remote.port")));
			// 拼装命令
			//mysql --local-infile -utestUser -ptestUser test -e "load data infile '/home/test/test2.csv' into table pos_dada_demo2 CHARACTER SET utf8 fields terminated by ',' lines terminated by '\n'"
			String cmd = new StringBuffer("mysql --local-infile -u")
					.append(props.getProperty("mysql.username"))
					.append(" -p")
					.append(props.getProperty("mysql.password"))
					.append(" ")
					.append(props.getProperty("mysql.databasename"))
					.append(" -e \"load data infile '")
					.append(props.getProperty("mysql.filepath"))
					.append("' into table ")
					.append(props.getProperty("mysql.tablename"))
					.append(" CHARACTER SET utf8 fields terminated by '\t' lines terminated by '\n'\"")
					.toString();
			CogradientImgFileManager.runCmd(cmd, "UTF-8");
			Thread.sleep(15000);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			CogradientImgFileManager.close();
		}

		return true;
	}

}

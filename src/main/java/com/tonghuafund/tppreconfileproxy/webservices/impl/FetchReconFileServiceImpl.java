package com.tonghuafund.tppreconfileproxy.webservices.impl;

import java.io.InputStream;
import java.util.Properties;

import javax.jws.WebService;

import com.allinpay.util.CogradientImgFileManager;
import com.tonghuafund.tppreconfileproxy.webservices.FetchReconFileService;

@WebService(endpointInterface = "com.tonghuafund.tppreconfileproxy.webservices.FetchReconFileService", targetNamespace = "http://webservices.tppreconfileproxy.tonghuafund.com/")
public class FetchReconFileServiceImpl implements FetchReconFileService {

	/**
	 * hiveͳ�����ݽ���������ڡ�csv�ļ���
	 */
	@Override
	public boolean generateFetchTask() {
		// 1.��ȡ�����ļ�
				InputStream is = CogradientImgFileManager.class.getClassLoader()
						.getResourceAsStream("jdbc.properties");
				Properties props = new Properties();
				try {
					props.load(is);
				} catch (Exception e) {
					e.printStackTrace();
				}

				// 2.�����ļ���Ϣ�ͱ���Ϣ��������
				try {
					CogradientImgFileManager.versouSshUtil(
							props.getProperty("hive.host"),
							props.getProperty("hive.username"),
							props.getProperty("hive.password"),
							new Integer(props.getProperty("hive.port")));
					// ƴװ����
					//hive -e "select MCHT_CD,sum(TRANS_AMT),count(TXN_NUM)  from POSL.APMS_LOGINFO group by MCHT_CD sort by MCHT_CD" >> /home/hadoop/test4.csv
					//�����������⣬81����ֻ��ִ��sh�ű�
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
	 * ��**��csv�ļ����뵽mysql���ݿ���
	 */
	@Override
	public boolean execFetchTask() {
		// 1.��ȡ�����ļ�
		InputStream is = CogradientImgFileManager.class.getClassLoader()
				.getResourceAsStream("jdbc.properties");
		Properties props = new Properties();
		try {
			props.load(is);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 2.�����ļ���Ϣ�ͱ���Ϣ��������
		try {
			CogradientImgFileManager.versouSshUtil(
					props.getProperty("remote.host"),
					props.getProperty("remote.username"),
					props.getProperty("remote.password"),
					new Integer(props.getProperty("remote.port")));
			// ƴװ����
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

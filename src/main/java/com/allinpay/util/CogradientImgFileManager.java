package com.allinpay.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;

import org.slf4j.LoggerFactory;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class CogradientImgFileManager {

	private static final org.slf4j.Logger log = LoggerFactory
			.getLogger(CogradientImgFileManager.class);
	private static ChannelExec channelExec;
	private static Session session = null;
	private static int timeout = 60000;

	// 测试代码
	public static void main(String[] args) {
		try {
			versouSshUtil("192.168.1.81", "hadoop", "hadoop", 22);
			//versouSshUtil("10.56.201.175", "root", "tppdb", 22);
			System.out.println("1....");
			//runCmd("ls", "UTF-8");
			//String str = new StringBuffer("/home/hadoop/apps/apache-hive-2.0.0/bin/hive -e \"select MCHT_CD,sum(TRANS_AMT),count(TXN_NUM)  from POSL.APMS_LOGINFO group by MCHT_CD sort by MCHT_CD\" >> /home/hadoop/test4.csv").toString();
			//String str = "/home/hadoop/apps/apache-hive-2.0.0/bin/hive -e \"select count(1) from POSL.APMS_LOGINFO\"";
			//String str = "/home/hadoop/apps/apache-hive-2.0.0/bin/hive -e \"show tables\"";
//			String str1 = ". /home/hadoop/.bash_profile";
//			runCmd(str1, "UTF-8");
//			String str = "/home/hadoop/apps/apache-hive-2.0.0/bin/hive -e \"select count(1) from test1\"";
//			runCmd(str, "UTF-8");
			runCmd("sh /home/hadoop/hql.sh", "UTF-8");
			Thread.sleep(20000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{			
			session.disconnect();
		}
	}

	/**
	 * 连接远程服务器
	 * 
	 * @param host
	 *            ip地址
	 * @param userName
	 *            登录名
	 * @param password
	 *            密码
	 * @param port
	 *            端口
	 * @throws Exception
	 */
	public static void versouSshUtil(String host, String userName,
			String password, int port) throws Exception {
		log.info("尝试连接到....host:" + host + ",username:" + userName
				+ ",password:" + password + ",port:" + port);
		JSch jsch = new JSch(); // 创建JSch对象
		session = jsch.getSession(userName, host, port); // 根据用户名，主机ip，端口获取一个Session对象
		session.setPassword(password); // 设置密码
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config); // 为Session对象设置properties
		session.setTimeout(timeout); // 设置timeout时间
		session.connect(); // 通过Session建立链接
	}

	/**
	 * 在远程服务器上执行命令
	 * 
	 * @param cmd
	 *            要执行的命令字符串
	 * @param charset
	 *            编码
	 * @throws Exception
	 */
	public static void runCmd(String cmd, String charset) throws Exception {
		channelExec = (ChannelExec) session.openChannel("exec");
		channelExec.setCommand(cmd);
		channelExec.setInputStream(null);
		channelExec.setErrStream(System.err);
		channelExec.connect();
		InputStream in = channelExec.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in,
				Charset.forName(charset)));
		String buf = null;
		while ((buf = reader.readLine()) != null) {
			System.out.println(buf);
		}
		reader.close();
		channelExec.disconnect();
	}
	
	public static void close(){
		if(session != null && session.isConnected()){
			session.disconnect();
		}
	}
}

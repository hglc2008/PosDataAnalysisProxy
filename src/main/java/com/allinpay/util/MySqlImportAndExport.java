package com.allinpay.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Properties;

public class MySqlImportAndExport {

	/** 
     * ���������ļ������õ���ָ��λ�õ�ָ�����ݿ⵽ָ��λ�� 
     * @param properties 
     * @throws IOException 
     */  
    public static void export(Properties properties) throws IOException {  
        Runtime runtime = Runtime.getRuntime();  
        String command = getExportCommand(properties);  
        runtime.exec(command);//�����һ���쳣�Ҿ�ֱ��������  
    } 
    
    /** 
     * ���������ļ������ð�ָ��λ�õ�ָ���ļ����ݵ��뵽ָ�������ݿ��� 
     * ������ڽ���mysql�����ݿ⵼��һ��������ߣ� 
     * ��һ���ǵǵ���mysql�� mysql -uusername -ppassword -hhost -Pport -DdatabaseName;����ڵ�¼��ʱ��ָ�������ݿ������ 
     * ֱ��ת������ݿ⣬�����Ϳ��������ڶ�����ֱ�ӵ�������  
     * �ڶ������л��������Ŀ�����ݿ⣻use importDatabaseName�� 
     * �������ǿ�ʼ��Ŀ���ļ��������ݵ�Ŀ�����ݿ⣻source importPath�� 
     * @param properties 
     * @throws IOException  
     */  
    public static void importSql(Properties properties) throws IOException {  
        Runtime runtime = Runtime.getRuntime();  
        //��Ϊ������ڽ���mysql���ݿ�ĵ���һ��������ߣ�������ִ�е�������ַ����������ʽ����  
        String cmdarray[] = getImportCommand(properties);//���������ļ������û�ȡ���ݿ⵼�������������һ������  
        //runtime.exec(cmdarray);//����Ҳ�Ǽ򵥵�ֱ���׳��쳣  
        System.out.println(cmdarray[0]);
        Process process = runtime.exec(cmdarray[0]);  
        //ִ���˵�һ�������Ժ��Ѿ���¼��mysql�ˣ�����֮���������mysql�������  
        //����ִ�к���Ĵ���  
        OutputStream os = process.getOutputStream();  
        OutputStreamWriter writer = new OutputStreamWriter(os);  
        //����1������2Ҫ����һ��ִ��  
        System.out.println(cmdarray[1]);
        System.out.println(cmdarray[2]);
        writer.write(cmdarray[1] + "\r\n" + cmdarray[2]);  
        writer.flush();  
        writer.close();  
        os.close();  
    }
    
    /** 
     * ���������ļ��ṩ��������ƴװ������� 
     * ��ƴװ��������ʱ����һ������Ҫע��ģ�һ�������������ֱ��ʹ�������� 
     * ���е�����ʱ����Լ�ʹ�á�>������ʾ������ʲô�ط�����mysqldump -uusername -ppassword databaseName > exportPath�� 
     * ����Java������д�ǲ��еģ�����Ҫ����-r��ȷ��ָ��������ʲô�ط����磺 
     * mysqldump -uusername -ppassword databaseName -r exportPath�� 
     * @param properties 
     * @return 
     */  
    private static String getExportCommand(Properties properties) {  
        StringBuffer command = new StringBuffer();  
        String username = properties.getProperty("jdbc.username");//�û���  
        String password = properties.getProperty("jdbc.password");//�û�����  
        String exportDatabaseName = properties.getProperty("jdbc.exportDatabaseName");//��Ҫ���������ݿ���  
        String host = properties.getProperty("jdbc.host");//���ĸ������������ݿ⣬���û��ָ�����ֵ����Ĭ��ȡlocalhost  
        String port = properties.getProperty("jdbc.port");//ʹ�õĶ˿ں�  
        String exportPath = properties.getProperty("jdbc.exportPath");//����·��  
          
        //ע����Щ�ط�Ҫ�ո���Щ��Ҫ�ո�  
        command.append("mysqldump -u").append(username).append(" -p").append(password)//�������õ�Сp�����˿����õĴ�P��  
        .append(" -h").append(host).append(" -P").append(port).append(" ").append(exportDatabaseName).append(" -r ").append(exportPath);  
        return command.toString();  
    }
    
    /** 
     * ���������ļ������ã��������߻�ȡ��Ŀ���ļ��������ݵ�Ŀ�����ݿ���������� 
     * ����ڵ�¼��ʱ��ָ�������ݿ������ 
     * ֱ��ת������ݿ⣬�����Ϳ��������ڶ�����ֱ�ӵ�������  
     * @param properties 
     * @return 
     */  
    private static String[] getImportCommand(Properties properties) {  
        String username = properties.getProperty("jdbc.username");//�û���  
        String password = properties.getProperty("jdbc.password");//����  
        String host = properties.getProperty("jdbc.host");//�����Ŀ�����ݿ����ڵ�����  
        String port = properties.getProperty("jdbc.port");//ʹ�õĶ˿ں�  
        String importDatabaseName = properties.getProperty("jdbc.importDatabaseName");//�����Ŀ�����ݿ������  
        String importPath = properties.getProperty("jdbc.importPath");//�����Ŀ���ļ����ڵ�λ��  
        //��һ������ȡ��¼�������  
        String loginCommand = new StringBuffer().append("mysql -u").append(username).append(" -p").append(password).append(" -h").append(host)  
        .append(" -P").append(port).toString();  
        //�ڶ�������ȡ�л����ݿ⵽Ŀ�����ݿ���������  
        String switchCommand = new StringBuffer("use ").append(importDatabaseName).toString();  
        //����������ȡ������������  
//        String importCommand = new StringBuffer("source ").append(importPath).toString();  
        //��Ҫ���ص������������  
        String importCommand = new StringBuffer("load data infile ").append(importPath).append("into table pos_dada_demo2 CHARACTER SET utf8 fields terminated by ',' optionally enclosed by '//' escaped by '//' lines terminated by '\n'").toString();
        String[] commands = new String[] {loginCommand, switchCommand, importCommand};  
        return commands;  
    }
}

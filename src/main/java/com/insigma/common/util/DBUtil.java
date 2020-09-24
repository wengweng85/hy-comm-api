package com.insigma.common.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.ServletContext;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by admin on 2014-12-25.
 */
public class DBUtil {

	
	 /**
     * ͨ��spring��jdbctemple��ȡ����
     * @param context
     * @return
     */
    public static JdbcTemplate getJdbcTemplate(ServletContext context) {
        JdbcTemplate jdbcTemplate=(JdbcTemplate) SpringUtil.getBean(context,"jdbcTemplate");
        return  jdbcTemplate;
    }
    
    /**
	 * ͨ���������ݿ����ͣ���ȡ��Ӧ���͵����ݿ�����
	 * @param ip ���ݿ�IP��ַ
	 * @param port ���ݿ�˿�
	 * @param serverName ���ݿ������
	 * @param userName �û���
	 * @param passWord ����
	 * @param dbType ���ݿ�����(oracle,sql server,sybase,mysql,db2,informix,postgresql)
	 * @return ���ݿ�����
	 * @throws Exception
	 */
	public static Connection getConnection(String dbdriver,String dburl, String userName,String passWord) throws Exception {
		Connection conn = null;
			try{
				try {
					Class.forName(dbdriver).newInstance();
				} catch (InstantiationException e) {
					throw new Exception("ORACLE���ݿ�����ʵ�����쳣:"+e.getMessage());
				} catch (IllegalAccessException e) {
					throw new Exception("ORACLE��ȫȨ���쳣:"+e.getMessage());
				} catch (ClassNotFoundException e) {
					throw new Exception("ORACLEδ�ҵ����쳣:"+e.getMessage());
				}
				try {
					conn = DriverManager.getConnection(dburl, userName, passWord);
				} catch (SQLException e) {
					throw new Exception("ORACLE��ȡ�����쳣:"+e.getMessage());
				}
			}catch (Exception e) {
				throw new Exception(e.getMessage());
			}
		
		return conn;
	}
	
	/**
	 * ִ�и���
	 * @param conn
	 * @param sql
	 * @return
	 */
	public static int executeSQLUpdate(Connection conn,String sql) throws Exception{
		PreparedStatement ps = null;
		int result=0;
		try{
			 ps = conn.prepareStatement(sql);
			 result=ps.executeUpdate();
		}catch(Exception e){
			throw new Exception(e);
		}finally{
			try{
				if(ps!=null)
					ps.close();
			}catch(Exception e){
				throw new Exception(e);
			}
		}
		return result;
	}
	
	/**
	 * ִ�в�ѯ
	 * @param conn
	 * @param sql
	 * @return
	 */
	public static Vector<HashMap<String,String>> executeSQLQuery(Connection conn,String sql) throws Exception{
		Vector<HashMap<String,String>> vt = new Vector<HashMap<String,String>>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ps = conn.prepareStatement(sql);
		    rs = ps.executeQuery();
		    ResultSetMetaData rsmd = rs.getMetaData();
		    int cols = rsmd.getColumnCount();
			while (rs.next()){
				HashMap<String,String> hm = new HashMap<String,String>();
				for (int i=1;i<=cols;i++){
					hm.put(rsmd.getColumnName(i).toLowerCase(),rs.getString(i));
				}
				vt.add(hm);
			}
		}catch(Exception e){
			throw new Exception(e);
		}finally{
			try{
				if(rs!=null)
					rs.close();
				if(ps!=null)
					ps.close();
			}catch(Exception e){
				throw new Exception(e);
			}
		}
		return vt;
	}
	
	public static void main(String [] args) {
		try {
			//getConnection("oracle.jdbc.driver.OracleDriver","jdbc:oracle:thin:@127.0.0.1:1521:orcl","DATASOURCEMANAGE","DATASOURCEMANAGE");
			getConnection("dm.jdbc.driver.DmDriver","jdbc:dm://127.0.0.1:5237/DAMENG","hy_tymh","hy_tymh123");
			System.out.println("���ӳɹ�");
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

}

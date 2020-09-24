package com.insigma.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
  *  业务流水号生成生成工具类
 * @author wengsh
 *
 */
public class IDGenertor {
	
	private static IDGenertor idGenertor = null;
	//到日期级
	private static SimpleDateFormat yMd = new SimpleDateFormat("yyyyMMdd");
	
	//到毫秒级
	private static SimpleDateFormat yMdHmsS = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	
	private IDGenertor() {
		 
	}
	
	 /**
	     * 取得PrimaryGenerater的单例实现
	*
	* @return
	*/
	public static IDGenertor getInstance() {
	  if (idGenertor == null) {
	      synchronized (SerialNumberTool.class) {
	          if (idGenertor == null) {
	        	  idGenertor = new IDGenertor();
	          }
	      }
	  }
	  return idGenertor;
	}

	public synchronized  String run() {
		//return yMd.format(new Date(System.currentTimeMillis()))+SerialNumberTool.getInstance().generaterNextNumber(9)+UUIDGenerator.generate();
		//生成规则 当前时间到毫秒级+uuid
		return yMdHmsS.format(new Date(System.currentTimeMillis()))+UUIDGenerator.generate();
	}
	
	/**
	 *  业务流水号
	 * @return
	 */
	public static String generate() {
		return IDGenertor.getInstance().run();
	}
	
	/**
	 * uuid
	 * @return
	 */
	public static String uuidgenerate() {
		return UUIDGenerator.generate().toString();
	}
	
    public static void main(String[] args) {
        //  单线程测试
    	for (int i = 0; i <1000000 ; i++) {
        	System.out.println(IDGenertor.generate());
        }
    }

}

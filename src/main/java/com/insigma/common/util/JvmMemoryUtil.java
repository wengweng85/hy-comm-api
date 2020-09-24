package com.insigma.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * java查看当前系统内存使用情况
 * @author admin
 *
 */
public class JvmMemoryUtil {
	
	private static Log log=LogFactory.getLog(JvmMemoryUtil.class);
	public static  void showJvmMemory(){
		Runtime run = Runtime.getRuntime();
		long max = run.maxMemory();
		long total = run.totalMemory();
		long free = run.freeMemory();
		long usable = max - total + free;
		log.info("最大内存 = " + max/1024/1024+"M");
		//log.info("已分配内存 = " + total/1024/1024+"M");
		//log.info("已分配内存中的剩余空间 = " + free/1024/1024+"M");
		log.info("最大可用内存 = " + usable/1024/1024+"M");
	}
	
	public static void main(String []  a){
		showJvmMemory();
	}
}

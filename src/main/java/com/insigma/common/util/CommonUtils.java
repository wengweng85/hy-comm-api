package com.insigma.common.util;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.MalformedURLException;

public class CommonUtils {
	/**
	 * 根据路径下的文件名
	 * @param path
	 * @return
	 */
	public static String getNewFile(String path){
		File savePath = new File(path);
		File[] files = savePath.listFiles();
		if(files.length == 0){
			return "";
		}
		File file = null;
		for(int i=0; i<files.length; i++){
			if(i==0){
				file = files[i];
			}else if(files[i].lastModified() >= files[i-1].lastModified()){
				file = files[i];
			}
		}
		return file.getName();
	}

	/*
	 * 获取项目的根目录
	 * 因为tomcat和weblogic获取的根目录不一致，所以需要此方法
	 */
	public static String getWebRootUrl(HttpServletRequest request) {
		String fileDirPath = request.getSession().getServletContext().getRealPath("/");
		if (fileDirPath == null) {
			//如果返回为空，则表示服务器为weblogic，则需要使用另外的方法
			try {
				fileDirPath = request.getSession().getServletContext().getResource("/").getFile() + "/tymh-api/WEB_INF/";
				System.out.print("Weblogic获取项目的根目录:"+fileDirPath);
				return fileDirPath;
			} catch (MalformedURLException e) {
				System.out.print("获取项目的根目录出错！");
			}
		} else {
			fileDirPath += "/tymh-api/WEB_INF/";
		}
		return fileDirPath;
	}
}

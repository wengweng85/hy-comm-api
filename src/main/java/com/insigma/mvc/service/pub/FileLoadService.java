package com.insigma.mvc.service.pub;

import org.springframework.web.multipart.MultipartFile;

import com.insigma.mvc.repository.main.model.SFileRecord;


public interface FileLoadService {
	
	 /**
     * 文件上传
     *
     * @param file_bus_type 文件类型
     * @param file_bus_id   业务编号
     * @param multipartFile 文件流
     * @return
     * @throws Exception
     */
    String upload(String file_bus_type, String file_bus_id,MultipartFile multipartFile) throws Exception;
    
    /**
     * 得到文件信息
     */
    public SFileRecord getFileInfo(String fileuuid);

    /**
     * 文件下载
     * @param file_path
     * @return
     */
    byte[] download(String file_path);

  

}

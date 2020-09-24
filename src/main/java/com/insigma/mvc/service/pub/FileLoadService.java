package com.insigma.mvc.service.pub;

import org.springframework.web.multipart.MultipartFile;

import com.insigma.mvc.repository.main.model.SFileRecord;


public interface FileLoadService {
	
	 /**
     * �ļ��ϴ�
     *
     * @param file_bus_type �ļ�����
     * @param file_bus_id   ҵ����
     * @param multipartFile �ļ���
     * @return
     * @throws Exception
     */
    String upload(String file_bus_type, String file_bus_id,MultipartFile multipartFile) throws Exception;
    
    /**
     * �õ��ļ���Ϣ
     */
    public SFileRecord getFileInfo(String fileuuid);

    /**
     * �ļ�����
     * @param file_path
     * @return
     */
    byte[] download(String file_path);

  

}

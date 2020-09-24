package com.insigma.mvc.service.pub.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.insigma.common.util.DateUtil;
import com.insigma.common.util.IDGenertor;
import com.insigma.common.util.RandomNumUtil;
import com.insigma.mvc.repository.main.dao.FileUploadMapper;
import com.insigma.mvc.repository.main.model.SFileRecord;
import com.insigma.mvc.repository.main.model.SFiletype;
import com.insigma.mvc.service.pub.FileLoadService;
import com.insigma.mvc.service.pub.FileTypeService;

/**
 * Created by LENOVO on 2017/8/28.
 */
@Service
public class FileLoadServiceImpl  implements FileLoadService {

	
	Log log = LogFactory.getLog(FileLoadServiceImpl.class);
	
    @Value("${localdir}")
    private String localdir;

    @Autowired
	private FileTypeService fileTypeService;

    @Autowired
    private FileUploadMapper fileUploadMapper;
   

    /**
     * �ϴ��ļ�
     *
     * @param
     * @param file_bus_type
     * @param file_bus_id
     * @param multipartFile
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
	public String upload( String file_bus_type, String file_bus_id, MultipartFile multipartFile) throws Exception {
    	String originalFilename=multipartFile.getOriginalFilename();
    	InputStream is =multipartFile.getInputStream();
        SFileRecord sfilerecord = new SFileRecord();

        sfilerecord.setFileUuid(IDGenertor.generate());
        /** ����ͼƬ�����Ŀ¼ **/
        /** ��ǰ�·� **/
        String fileDir = "/fileroot/" + file_bus_type + "/" + DateUtil.dateToString(new Date(), "yyyyMM");//yyyyMM
        /** ������ʵ·������Ŀ¼ **/
        File fileuploadDir = new File(localdir + fileDir);
        if (!fileuploadDir.exists()) {
            fileuploadDir.mkdirs();
        }

        int indexofdoute = originalFilename.lastIndexOf(".");

        /** ���ļ���������+������� */
        String prefix = DateUtil.dateToString(new Date(), "yyyyMMddHHmmss") + RandomNumUtil.getRandomString(6);
        //�ļ���׺
        String endfix = originalFilename.substring(indexofdoute).toLowerCase();
        sfilerecord.setFileType(endfix);//�ļ�����

        String new_file_name = prefix + endfix;//�����ɵ��ļ���
        sfilerecord.setFileName(new_file_name);//�ļ���

        String file_rel_path = fileDir + "/" + prefix + endfix;//���·��
        String filepath = localdir + file_rel_path;//����·��
        File file = new File(filepath);

        sfilerecord.setFilePath(filepath);
        sfilerecord.setFileRelPath(file_rel_path);
        sfilerecord.setFileStatus("1");//��Ч
        sfilerecord.setFileAddtime(new Date());//����ʱ��
        OutputStream os = new FileOutputStream(file);
        int bytesRead = 0;
        byte [] buffer = new byte[8192];
        while ((bytesRead = is.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.flush();
        os.close();
        is.close();

        sfilerecord.setFileLength(file.length());
        sfilerecord.setFileType(sfilerecord.getFileName().substring(sfilerecord.getFileName().lastIndexOf(".") + 1));
       
        sfilerecord.setFileBusName(originalFilename);// �ļ�ԭ��
        sfilerecord.setFileBusType(file_bus_type);
        sfilerecord.setFileBusId(file_bus_id);
        
        //�����ļ���¼
        fileUploadMapper.insert(sfilerecord);
        
        SFiletype sFiletype=new SFiletype();
        sFiletype.setBusinesstype(file_bus_type+Math.random());
        sFiletype.setFilemaxnum(1);
        sFiletype.setTypename("��������"+Math.random());
        sFiletype.setFilemaxsize(1024);
        
        fileTypeService.saveFileType(sFiletype);
        
        //����ҵ���¼
        return sfilerecord.getFileUuid();
    }

    /**
     * ����
     */
    @Override
	public byte[] download(String file_path) {
        InputStream data = null;
        try {
            data = new FileInputStream(file_path);
            int size = data.available();
            byte[] buffer = new byte[size];
            IOUtils.read(data, buffer);
            return buffer;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            IOUtils.closeQuietly(data);
        }
    }

	@Override
	public SFileRecord getFileInfo(String fileuuid) {
		return fileUploadMapper.selectById(fileuuid);
	}
   
}

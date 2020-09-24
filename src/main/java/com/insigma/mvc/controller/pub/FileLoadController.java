package com.insigma.mvc.controller.pub;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.insigma.dto.R;
import com.insigma.mvc.repository.main.model.SFileRecord;
import com.insigma.mvc.service.pub.FileLoadService;
import com.insigma.resolver.AppException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * �����ļ��ϴ��ӿ�
 * @author wengsh
 *
 */
@RequestMapping(value="/api/file")
@Api(value="�ļ��ϴ�����ʾapi",description ="�ļ��ϴ�����ʾapi" )
@RestController
public class FileLoadController {
	
	/**
	 * ��־
	 */
	Log log=LogFactory.getLog(FileLoadController.class);
	
	@Autowired
	private FileLoadService fileloadService;

	/**
	 * �ļ��ϴ�
	 * @param request
	 * @param multipartFile
	 * @return
	 * @throws Exception
	 */
    @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = " �ϴ��ļ�", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R upload(HttpServletRequest request,@RequestParam("uploadFile") MultipartFile multipartFile,@RequestParam("file_bus_type") String file_bus_type,@RequestParam("file_bus_id") String file_bus_id) throws Exception {
        String fileuuid= fileloadService.upload(file_bus_type,file_bus_id,multipartFile);
        log.info("�ļ��ϴ��ɹ���ˮ��:"+fileuuid);
        return R.success(fileuuid);
    }

    /**
     * �ļ�����
     *
     * @param bus_uuid
     * @param response
     * @throws AppException
     */
    @ApiOperation(value = "�ļ�����", notes = "�ļ�����")
    @RequestMapping(value = "/download/{fileuuid}", method = RequestMethod.GET)
    public void download(@PathVariable String fileuuid, HttpServletResponse response) {
        try {
        	log.info("�ļ����� ��ˮ��:"+fileuuid);
        	SFileRecord filerecord = fileloadService.getFileInfo(fileuuid);
            if (filerecord == null) {
            	   throw new Exception("�ļ������ڻ��Ѿ�ɾ��");
            }
            byte[] temp = fileloadService.download(filerecord.getFilePath());
            if (temp == null) {
            	  throw new Exception("���ش���,�����ڵ�id");
            }
            //���д����Ƿ�ֹ��������Ĺؼ�����
            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(filerecord.getFileName(), "utf-8"));
            BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(temp));
            BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
            //�½�һ��2048�ֽڵĻ�����
            byte[] buff = new byte[2048];
            int bytesRead = 0;
            while ((bytesRead = bis.read(buff, 0, buff.length)) != -1) {
                bos.write(buff, 0, bytesRead);
            }
            bos.flush();
            bis.close();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

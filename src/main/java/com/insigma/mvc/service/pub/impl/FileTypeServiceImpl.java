package com.insigma.mvc.service.pub.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.insigma.mvc.repository.main.dao.FileTypeMapper;
import com.insigma.mvc.repository.main.model.SFiletype;
import com.insigma.mvc.service.pub.FileTypeService;

@Service
public class FileTypeServiceImpl implements  FileTypeService  {

	
	@Autowired
	private FileTypeMapper fileTypeMapper;
	
	@Override
	@Transactional
	public int saveFileType(SFiletype sfiletype) {
		// TODO Auto-generated method stub
		return fileTypeMapper.insert(sfiletype);
	}

}

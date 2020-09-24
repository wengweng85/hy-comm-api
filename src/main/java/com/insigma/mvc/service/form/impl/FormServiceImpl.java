package com.insigma.mvc.service.form.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.insigma.common.util.IDGenertor;
import com.insigma.dto.A01DTO;
import com.insigma.mvc.repository.main.dao.A01Mapper;
import com.insigma.mvc.repository.main.model.A01;
import com.insigma.mvc.service.form.FormService;

/**
 * formdemo
 * @author admin
 *
 */
@Service
public class FormServiceImpl implements  FormService{
	
	@Autowired
	private A01Mapper a01Mapper ;

	@Override
	public Integer inertA01(A01 a01) {
		a01.setA0000(IDGenertor.uuidgenerate());
		return a01Mapper.insert(a01);
	}

	@Override
	public void deleteA01(String id) {
		a01Mapper.deleteById(id);
	}

	@Override
	public Integer updateA01(A01 a01) {
		return a01Mapper.updateById(a01);
	}
	
	@Override
	public IPage<A01> findA01(A01DTO a01query) {
		int currpage=Integer.parseInt(a01query.getPage());
		int limit=Integer.parseInt(a01query.getSize());
		Page<A01> page =new Page<A01>(currpage,limit);
		return a01Mapper.selectPage(page, new QueryWrapper<A01>().eq("a0104",a01query.getA0104()) );
	}
}

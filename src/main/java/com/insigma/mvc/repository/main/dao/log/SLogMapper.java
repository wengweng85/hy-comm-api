package com.insigma.mvc.repository.main.dao.log;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.insigma.mvc.repository.main.model.SErrorLog;
import com.insigma.mvc.repository.main.model.SLog;


/**
 * 
 * ���ϵͳ��־��¼mapper
 * @author admin
 *
 */
public interface SLogMapper extends BaseMapper<SLog>{
	void saveSErrorLog(SErrorLog sErrorLog);
}

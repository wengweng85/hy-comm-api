package com.insigma.mvc.service.form;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.insigma.dto.A01DTO;
import com.insigma.mvc.repository.main.model.A01;

/**
 * form ·þÎñ
 * @author admin
 *
 */
public interface FormService  {
	
	Integer inertA01(A01 a01);

    void deleteA01(String id);

    Integer updateA01(A01 player);
    
    IPage<A01> findA01(A01DTO a01query);
	
}

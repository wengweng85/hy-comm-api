package com.insigma.mvc.controller.pub;

import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.insigma.common.annotation.UserLog;
import com.insigma.dto.A01DTO;
import com.insigma.dto.R;
import com.insigma.dto.validgroup.Add;
import com.insigma.dto.validgroup.All;
import com.insigma.dto.validgroup.Query;
import com.insigma.mvc.repository.main.model.A01;
import com.insigma.mvc.service.form.FormService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * �����ӿ�
 * @author wengsh
 *
 */
@RequestMapping(value="/api/v1/form")
@Api(value="����api",description ="����api" )
@RestController
public class FormApiController {
	
	/**
	 * webservice����
	 */
	Log log=LogFactory.getLog(FormApiController.class);
	

	@Autowired
	private FormService formservice;

	/**
	 * ��ѯ
	 * @param a01query
	 * @return
	 */
	@RequestMapping(value="/a01s",method = RequestMethod.POST)
	@ApiOperation(value="��ҳ��ѯ����",notes="��ҳ��ѯ����")
	@UserLog
    public R find(@RequestBody @Validated(Query.class)  A01DTO a01dto,BindingResult bindingResult){
		//�������ݸ�ʽУ�� 
		if (bindingResult.hasErrors()) {
		   List<ObjectError> ObjectErrorList= bindingResult.getAllErrors();
		   String errormsg="";
		   for(ObjectError o:ObjectErrorList) {
			   errormsg+=o.getDefaultMessage();
		   }
		   return R.fail(errormsg);
        }
        return R.success(formservice.findA01(a01dto));
    }

	/**
	 * ����
	 * @param a01
	 * @return
	 */
	@RequestMapping(value="/a01",method = RequestMethod.POST)
	@ApiOperation(value="��������",notes="��������")
	//@AvoidRepeatSubmit
    public R add(@RequestBody @Validated(Add.class) A01DTO a01dto,BindingResult bindingResult) throws Exception{
		//�������ݸ�ʽУ�� 
		if (bindingResult.hasErrors()) {
		 List<ObjectError> ObjectErrorList= bindingResult.getAllErrors();
		 String errormsg="";
		 for(ObjectError o:ObjectErrorList) {
			 errormsg+=o.getDefaultMessage();
		 }
		 return R.fail( errormsg);
        }
		A01 a01=new A01();
		BeanUtils.copyProperties(a01, a01dto);
	    int result=formservice.inertA01(a01);
        return R.success(result);
    }
	
	/**
	 * a01dto
	 * @param a01
	 * @return
	 */
	@RequestMapping(value="/a01",method = RequestMethod.PUT)
	@ApiOperation(value="��������",notes="��������")
    public R update(@RequestBody @Validated(All.class) A01DTO a01dto,BindingResult bindingResult) throws Exception{
		if (bindingResult.hasErrors()) {
			 List<ObjectError> ObjectErrorList= bindingResult.getAllErrors();
			 String errormsg="";
			 for(ObjectError o:ObjectErrorList) {
				 errormsg+=o.getDefaultMessage();
			 }
			 return R.fail( errormsg);
        }
		A01 a01=new A01();
		BeanUtils.copyProperties(a01, a01dto);
		int result = formservice.updateA01(a01);
        return R.success(result);
    }

	/**
	 * ɾ��
	 * @param a0000
	 */
	@RequestMapping(value="/a01/{a0000}",method = RequestMethod.DELETE)
	@ApiOperation(value="ɾ������",notes="ɾ������")
    @ResponseBody
    //@AvoidRepeatSubmit
    public R delete(@PathVariable String a0000){
		formservice.deleteA01(a0000);
		return R.success("ɾ���ɹ�");
    }
	
}

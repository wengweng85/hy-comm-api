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
 * 公共接口
 * @author wengsh
 *
 */
@RequestMapping(value="/api/v1/form")
@Api(value="测试api",description ="测试api" )
@RestController
public class FormApiController {
	
	/**
	 * webservice测试
	 */
	Log log=LogFactory.getLog(FormApiController.class);
	

	@Autowired
	private FormService formservice;

	/**
	 * 查询
	 * @param a01query
	 * @return
	 */
	@RequestMapping(value="/a01s",method = RequestMethod.POST)
	@ApiOperation(value="分页查询例子",notes="分页查询例子")
	@UserLog
    public R find(@RequestBody @Validated(Query.class)  A01DTO a01dto,BindingResult bindingResult){
		//公共数据格式校验 
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
	 * 保存
	 * @param a01
	 * @return
	 */
	@RequestMapping(value="/a01",method = RequestMethod.POST)
	@ApiOperation(value="新增例子",notes="新增例子")
	//@AvoidRepeatSubmit
    public R add(@RequestBody @Validated(Add.class) A01DTO a01dto,BindingResult bindingResult) throws Exception{
		//公共数据格式校验 
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
	@ApiOperation(value="更新例子",notes="更新例子")
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
	 * 删除
	 * @param a0000
	 */
	@RequestMapping(value="/a01/{a0000}",method = RequestMethod.DELETE)
	@ApiOperation(value="删除例子",notes="删除例子")
    @ResponseBody
    //@AvoidRepeatSubmit
    public R delete(@PathVariable String a0000){
		formservice.deleteA01(a0000);
		return R.success("删除成功");
    }
	
}

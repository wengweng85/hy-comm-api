package com.insigma.mvc.controller.pub;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.insigma.common.util.BeanCopyUtil;
import com.insigma.dto.Pub;
import com.insigma.dto.R;
import com.insigma.mvc.service.pub.PubService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 公共接口
 * @author wengsh
 *
 */
@RequestMapping(value="/v1/pub")
@Api(value="公共api",description ="公共api" )
@RestController
public class PubApiController {
	
	/**
	 * webservice测试
	 */
	Log log=LogFactory.getLog(PubApiController.class);
	
	
	@Autowired
	private PubService pubService;


	/**
	 * 分析库数据接口总入口
	 * @param request 数据包
	 * @param interface_id 接口编号
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/{interface_id}",method = RequestMethod.POST)
	@ApiOperation(value="分析库数据接口总入口",notes="分析库数据接口总入口")
	@ApiImplicitParams({
        @ApiImplicitParam(paramType="interface_id", name = "interface_id", value = "接口编号", required = true, dataType = "String")
    })
    @CrossOrigin
	public R call(@RequestBody @ApiParam(value = "公共参数") Pub pub ,@PathVariable("interface_id") String interface_id,HttpServletRequest request,HttpServletResponse response) throws Exception{
		response.setHeader("Access-Control-Allow-Origin", "*");
		HashMap hm=new HashMap();
		BeanCopyUtil.CopytoHashMap(pub, hm);
		return pubService.call(interface_id, hm,request);
	}
	

}
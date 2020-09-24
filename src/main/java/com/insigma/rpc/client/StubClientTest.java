package com.insigma.rpc.client;


import java.util.HashMap;

import com.insigma.mvc.service.rpc.RpcServiceCall;

/**
 * Created by admin
 */
public class StubClientTest {
    public static void main(String[] args) throws Exception {
        //数据访问接口参数
        //String QUERY_PARAM = "[{paramBM:\"AAC002\",paramValue:\"610425198909152612\",paramType:\"String\",paramMC:\"身份证号码\"}]";
        //方案编码
        String INTERFACE_CONFIG_ID = "queryJyCyInfo";
        //脚本编码
        String INTERFACE_SCRIPT_ID = "JB_000001";
        //请求数据
        HashMap map =new HashMap();
        map.put("aac002","610425198909152612");
        new RpcServiceCall().callService(INTERFACE_CONFIG_ID,INTERFACE_SCRIPT_ID,map);
    }
}

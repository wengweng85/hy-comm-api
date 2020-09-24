
package com.insigma.mvc.service.rpc;


import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.insigma.common.struct.Row;
import com.insigma.common.util.JsonUtils;
import com.insigma.dto.R;
import com.insigma.rpc.client.DataSourceSimpleDllServiceStub;
import com.insigma.rpc.client.entity.Column;
import com.insigma.rpc.client.entity.Head;
import com.insigma.rpc.client.entity.Key;
import com.insigma.rpc.client.entity.Table;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * Created by wengsh
 */
public class RpcServiceCall {

    /**
     * 调用省厅就业创业接口相关服务
     * @param INTERFACE_CONFIG_ID 方案编号
     * @param INTERFACE_SCRIPT_ID 脚本编号
     * @param requestmap 请求数据 hashmap格式
     * @return
     */
    public  R callService(String INTERFACE_CONFIG_ID,String INTERFACE_SCRIPT_ID,HashMap requestmap) throws Exception{
        List<HashMap> list=new ArrayList<HashMap>();
	    //数据访问接口参数
	    //String QUERY_PARAM = "[{paramBM:\"AAC002\",paramValue:\"610425198909152612\",paramType:\"String\",paramMC:\"身份证号码\"}]";
	    //将hashmap数据转换成字符串
	    String QUERY_PARAM=parseMapToQueryParam(requestmap);
	    //调用接口
	    DataSourceSimpleDllServiceStub stub = new DataSourceSimpleDllServiceStub();
	    DataSourceSimpleDllServiceStub.DSM_GENERAL dsm_generalSM_GENERAL = new DataSourceSimpleDllServiceStub.DSM_GENERAL();
	    //interface_config_id 方案编号
	    dsm_generalSM_GENERAL.setINTERFACE_CONFIG_ID(INTERFACE_CONFIG_ID);
	    //interface_script_id 脚本编号
	    dsm_generalSM_GENERAL.setINTERFACE_SCRIPT_ID(INTERFACE_SCRIPT_ID.toUpperCase());
	    //query_param 参数价
	    dsm_generalSM_GENERAL.setQUERY_PARAM(QUERY_PARAM);
	    //调用接口
	    DataSourceSimpleDllServiceStub.DSM_GENERALResponse resp = stub.dSM_GENERAL(dsm_generalSM_GENERAL);
	    //接口返回xml
	    String xmlData = resp.getDSM_GENERALReturn();
	    System.out.println("response:"+xmlData);
	
	    //xmlData是调用DataAccessKit.DSM_GENERAL生成的xml信息
	
	    //获取所有的头信息
	    List<Head> heads = getFAHead(xmlData);
	    //打印出头信息中的flag(反馈代码)和Mess(反馈信息)
	    for (Head head : heads) {
	        //System.out.println("head flag : " + head.getFlag());
	        //System.out.println("head Mess : " + head.getMess());
	        if(!head.getFlag().equals("0001")){
	            return  R.error("公共数据中心平台接调用失败");
	        }
	    }
	
	    String tabName = "";
	    //xmlData是调用DataAccessKit.DSM_GENERAL生成的xml信息
	    //解析出来的脚本头信息封装在HashMap中String类型的表示数据集的名称
	    //Head类型表示脚本头信息其中flag:表示脚本头反馈代码0002表示脚本执行成功，具体
	    //请参考附录1，Mess表示反馈信息
	    HashMap<String, Head> head = getScriptHead(xmlData);
	    Set<Map.Entry<String, Head>> entrys = head.entrySet();
	
	    String flag="";
	    String mess="";
	    for (Map.Entry<String, Head> entry : entrys) {
	        //打印出数据集名称
	        tabName = entry.getKey();
	        //System.out.println(tabName);
	        //打印出脚本头信息的反馈参数和反馈信息
	        //System.out.println("flag 1 :" + entry.getValue().getFlag() + ",mess 1 : " + entry.getValue().getMess());
	        flag=entry.getValue().getFlag();
	    }
	    //flag 为0002代表请求成功 脚本执行成功
	    if(flag.equals("0002")){
	        //xmlData是调用DataAccessKit.DSM_GENERAL生成的xml信息，"DY01"为数据集名称
	        //Table中有三个属性分别是name,columns和keys，本接口主要用到name和columns两个属性
	        //name表示数据集的名称，colums表示字段集List
	        //column中有三个属性分别是name,type和value
	
	        //list是返回的结果集
	        List<Table> tableList = praseXml(xmlData, tabName);
	        
	        list=parseTableToHashMap(tableList);
	    }else{
	        return R.error("公共数据中心平台接口调用失败!");
	    }
        //如果返回结果列表大于1 证明为多条结果返回
        if(list.size()>0){
        	if(list.size()==1) {
        		 return  R.success(list.get(0));
        	}else {
        		 return  R.success(list);
        	}
        }else{
            return  R.fail();
        }
    }
   
    /**
     * 将hashmap转换成以下"[{paramBM:\"AAC002\",paramValue:\"610425198909152612\",paramType:\"String\",paramMC:\"身份证号码\"}]";
     * @param map
     * @return
     */
    private  String parseMapToQueryParam(String reqjson){
    	HashMap map=JsonUtils.jsonToPojo(reqjson,HashMap.class);
        JSONArray jsonArray=new JSONArray();
        Set<Map.Entry<String,String>> set=map.keySet();
        Iterator iterator=set.iterator();
        while(iterator.hasNext()){
            String key=(String)iterator.next();
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("paramBM",key.toUpperCase());//key
            jsonObject.put("paramValue",map.get(key));//value
            jsonObject.put("paramType","String");//
            jsonObject.put("paramMC",key);
            jsonArray.add(jsonObject);
        }
        String result=jsonArray.toString();
        System.out.println("request:"+result);
        return result;
    }
    
    /**
     * 将hashmap转换成以下"[{paramBM:\"AAC002\",paramValue:\"610425198909152612\",paramType:\"String\",paramMC:\"身份证号码\"}]";
     * @param map
     * @return
     */
    private  String parseMapToQueryParam(HashMap map){
        JSONArray jsonArray=new JSONArray();
        Set<Map.Entry<String,String>> set=map.keySet();
        Iterator iterator=set.iterator();
        while(iterator.hasNext()){
            String key=(String)iterator.next();
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("paramBM",key.toUpperCase());//key
            jsonObject.put("paramValue",map.get(key));//value
            jsonObject.put("paramType","String");//
            jsonObject.put("paramMC",key);
            jsonArray.add(jsonObject);
        }
        String result=jsonArray.toString();
        System.out.println("request:"+result);
        return result;
    }


    /**
     * 将kit接口返回对象转换成row对象
     * @param tableList
     * @return
     */
    private  List<Row>  parseTableToRow(List<Table> tableList){
        List<Row> rowList=new ArrayList<Row>();
        if(null!=tableList){
            for (Table table : tableList) {
                Row temprow = new Row();
                String name = table.getName();
                //System.out.println("tabName : " + name);   //打印出数据集名称
                List<Column> columns = table.getColumns();//获取结果集每张表的信息
                for (Column column : columns) {               //获取表的具体信息
                    //System.out.println(column.getName().toLowerCase());       //打印出记录的名称
                    //System.out.println(column.getValue());      //打印出记录的值
                    try {
                        //通过对象反射机制设置价值
                        String method_name = "set" + column.getName().substring(0, 1) + column.getName().toLowerCase().substring(1).toLowerCase();
                        //System.out.println("method_name=" + method_name);
                        Method method = temprow.getClass().getMethod(method_name, column.getValue().getClass());
                        method.invoke(temprow, column.getValue());
                    } catch (Exception ex) {
                        //ex.printStackTrace();;
                    }
                }
                rowList.add(temprow);
            }
        }
        return rowList;
    }
    
    /**
     * 将kit接口返回对象转换成row对象
     * @param tableList
     * @return
     */
    private  List<HashMap>  parseTableToHashMap(List<Table> tableList){
        List<HashMap> rowList=new ArrayList<HashMap>();
        if(null!=tableList){
            for (Table table : tableList) {
            	HashMap hashmap = new HashMap();
                String name = table.getName();
                //System.out.println("tabName : " + name);   //打印出数据集名称
                List<Column> columns = table.getColumns();//获取结果集每张表的信息
                for (Column column : columns) {               //获取表的具体信息
                    //System.out.println(column.getName().toLowerCase());       //打印出记录的名称
                    //System.out.println(column.getValue());      //打印出记录的值
                    try {
                        //通过对象反射机制设置价值
                        hashmap.put(column.getName().toLowerCase(), column.getValue());
                    } catch (Exception ex) {
                        //ex.printStackTrace();;
                    }
                }
                rowList.add(hashmap);
            }
        }
        return rowList;
    }
    
    /**
	 * 根据调用数据访问接口服务返回的xml格式的数据集获取数据访问接口方案信息头
	 * @param XMLDATA        客户端传入的XML数据集字符串
	 * @return               List<Head>
	 * @throws Exception
	 */
	public static List<Head> getFAHead(String XMLDATA) throws Exception{
		List<Head> heads = null;
		SAXReader reader = null;
		Document  document = null;
		try {
			reader = new SAXReader();
			document = reader.read(new ByteArrayInputStream(XMLDATA.getBytes("UTF-8")));
			Element rootElm = document.getRootElement();
			List<Element> faHeadElms = rootElm.elements("FAHEAD");
			if(faHeadElms!=null && !faHeadElms.isEmpty()){
				heads = new ArrayList<Head>();
			}else{
				return null;
			}
			for(int i =0;i<faHeadElms.size();i++){
				Head head = new Head();
				Element faHeadElm = faHeadElms.get(i);
				String Flag = faHeadElm.attributeValue("Flag");
				String Mess = faHeadElm.attributeValue("Mess");
				head.setFlag(Flag);
				head.setMess(Mess);
				heads.add(head);
			}
		}catch (Exception e) {
			throw new Exception(e.getMessage());
		}finally{
			if(document!=null){
				document.clearContent();
			}
			if(reader!=null){
				reader=null;
			}
		}
		return heads;
	}

	/**
	 * 根据调用数据访问接口服务返回的xml格式的数据集获取数据访问接口方案脚本信息头
	 * @param XMLDATA        客户端传入的XML数据集字符串
	 * @return               List<Head>
	 * @throws Exception
	 */
	public static HashMap<String,Head> getScriptHead(String XMLDATA) throws Exception{
		HashMap<String,Head> scriptHead = null;
		SAXReader reader = null;
		Document  document = null;
		try {
			reader = new SAXReader();
			document = reader.read(new ByteArrayInputStream(XMLDATA.getBytes("UTF-8")));
			Element rootElm = document.getRootElement();
			Element scriptHeadElms = rootElm.element("SCRIPTHEAD");
			List<Element> script= scriptHeadElms.elements();
			if(script!=null && !script.isEmpty()){
				scriptHead = new HashMap<String,Head>();
			}else{
				return null;
			}
			for(int i =0;i<script.size();i++){
				Head head = new Head();
				Element scriptHeadElm = script.get(i);
				String Flag = scriptHeadElm.attributeValue("Flag");
				String Mess = scriptHeadElm.attributeValue("Mess");
				head.setFlag(Flag);
				head.setMess(Mess);
				scriptHead.put(scriptHeadElm.getName(), head);
			}
		}catch (Exception e) {
			throw new Exception(e.getMessage());
		}finally{
			if(document!=null){
				document.clearContent();
			}
			if(reader!=null){
				reader=null;
			}
		}
		return scriptHead;
	}

	/**
	 * 解析WebServices客户端发过来的表信息XML字符串
	 * @param xmlData 客户端传入的XML字符串
	 * @param tabName 数据集名称
	 * @return 返回解析后表信息
	 * @throws Exception
	 */
	public static List<Table> praseXml(String xmlData, String tabName) throws Exception{
		SAXReader reader = null;
		Document  document = null;
		try {
			reader = new SAXReader();
			document = reader.read(new ByteArrayInputStream(xmlData.getBytes("UTF-8")));
			Element rootElm = document.getRootElement();
			Element DataElm = rootElm.element("Data");
			List<Element> tableElms = DataElm.elements("Table");
			List<Table> tables = null;
			if(tableElms!=null && !tableElms.isEmpty()){
				tables = new ArrayList<Table>();
			}else{
				return null;
			}

			for (int i = 0; i < tableElms.size(); i++) {
				Element tableElm = tableElms.get(i);
				if(tabName.equalsIgnoreCase(tableElm.attributeValue("TabName"))){
					List<Element> rowElms = tableElm.elements("rowData");
					if(rowElms!=null && !rowElms.isEmpty()){
						for(int i1 =0;i1<rowElms.size();i1++){
							Table table=new Table();
							table.setName(tableElm.attributeValue("TabName"));
							Element rowElm = rowElms.get(i1);
							List<Element> colnodes = rowElm.elements("ColumnData");
							List<Column> columns=new ArrayList<Column>();
							for (int j = 0; j < colnodes.size(); j++) {
								Column column=new Column();
								column.setName(colnodes.get(j).attributeValue("ColName"));
								column.setType(colnodes.get(j).attributeValue("ColType"));
								column.setValue(colnodes.get(j).attributeValue("ColValue"));
								columns.add(column);
							}
							table.setColumns(columns);

							List<Element> keynodes = rowElm.elements("KeyData");
							List<Key> keys = null;
							if(keynodes!=null && !keynodes.isEmpty()){
								keys = new ArrayList<Key>();
							}
							for (int k = 0; k < keynodes.size(); k++) {
								Key key=new Key();
								key.setName(keynodes.get(k).attributeValue("KeyName"));
								key.setValue(keynodes.get(k).attributeValue("KeyValue"));
								key.setType(keynodes.get(k).attributeValue("KeyType"));
								keys.add(key);
							}
							table.setKeys(keys);
							tables.add(table);
						}

					}

				}
			}
			return tables;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}finally{
			if(document!=null){
				document.clearContent();
			}
			if(reader!=null){
				reader=null;
			}
		}
	}
}
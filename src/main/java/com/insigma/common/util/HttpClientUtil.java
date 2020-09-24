package com.insigma.common.util;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;


/**
 * 利用HttpClient进行post请求的工具类
 * @author xxx
 *
 */
public class HttpClientUtil {

	private static String  CHARTSET="UTF-8";
	private static Log log= LogFactory.getLog(HttpClientUtil.class);
	private static final String HTTP = "http";
	private static final String HTTPS = "https";
	private static SSLConnectionSocketFactory sslsf = null;
	private static PoolingHttpClientConnectionManager cm = null;
	private static SSLContextBuilder builder = null;

	static {
		try {
			builder = new SSLContextBuilder();
			// 全部信任 不做身份鉴定
			builder.loadTrustMaterial(null, new TrustStrategy() {
				@Override
				public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
					return true;
				}
			});
			sslsf = new SSLConnectionSocketFactory(builder.build(), new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);
			Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
					.register(HTTP, new PlainConnectionSocketFactory())
					.register(HTTPS, sslsf)
					.build();
			cm = new PoolingHttpClientConnectionManager(registry);
			cm.setMaxTotal(200);//max connection
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static CloseableHttpClient getHttpClient() throws Exception {
		CloseableHttpClient httpClient = HttpClients.custom()
				.setSSLSocketFactory(sslsf)
				.setConnectionManager(cm)
				.setConnectionManagerShared(true)
				.build();
		return httpClient;
	}

	/**
	 * readHttpResponse
	 * @param httpResponse
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public static String readHttpResponse(HttpResponse httpResponse)  throws ParseException, IOException {
		StringBuilder builder = new StringBuilder();
		// 获取响应消息实体
		HttpEntity entity = httpResponse.getEntity();
		// 响应状态
		builder.append("status:" + httpResponse.getStatusLine());
		builder.append("headers:");
		HeaderIterator iterator = httpResponse.headerIterator();
		while (iterator.hasNext()) {
			builder.append("\t" + iterator.next());
		}
		// 判断响应实体是否为空
		if (entity != null) {
			String responseString = EntityUtils.toString(entity);
			builder.append("response length:" + responseString.length());
			builder.append("response content:" + responseString.replace("\r\n", ""));
		}
		return builder.toString();
	}

	/**
	 * doPost
	 * @param url
	 * @param map
	 * @return
	 */
	public static JSONObject doPost(String url,Map<String,String> map){
		return doPost(url,map,CHARTSET);
	}

	/**
	 * doPost
	 * @param url
	 * @return
	 */
	public static JSONObject doPost(String url){
		return doPost(url,null,CHARTSET);
	}

	/**
	 * doPost
	 * @param url
	 * @param map
	 * @param charset
	 * @return
	 */
	public static JSONObject doPost(String url, Map<String,String> map, String charset){
		CloseableHttpClient httpClient = null;
		HttpPost httpPost = null;
		String result = null;
		CloseableHttpResponse response = null;
		try{
			httpClient = getHttpClient();
			httpPost = new HttpPost(url);
			//设置参数
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			if(map!=null){
				Iterator iterator = map.entrySet().iterator();
				while(iterator.hasNext()){
					Entry<String,String> elem = (Entry<String, String>) iterator.next();
					list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));
				}
				if(list.size() > 0){
					UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,charset);
					httpPost.setEntity(entity);
				}
			}
			response = httpClient.execute(httpPost);
			if(response != null){
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					HttpEntity resEntity = response.getEntity();
					if(resEntity != null){
						result = EntityUtils.toString(resEntity,charset);
					}
				}else {
					readHttpResponse(response);
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally {
			try {
				response.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		log.info(result);
		JSONObject jsonobject = JSONObject.parseObject(result);
		return jsonobject;
	}

	/**
	 * doGet
	 * @param url
	 * @param charset
	 * @return
	 */
	public static JSONObject doGet(String url,String charset){
		CloseableHttpClient httpClient = null;
		HttpGet httpGet = null;
		String result = null;
		CloseableHttpResponse response = null;
		try{
			httpClient = getHttpClient();
			httpGet = new HttpGet(url);
			response = httpClient.execute(httpGet);
			if(response != null){
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					HttpEntity resEntity = response.getEntity();
					if(resEntity != null){
						result = EntityUtils.toString(resEntity,charset);
					}
				}else {
					readHttpResponse(response);
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally {
			try {
				response.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		log.info(result);
		JSONObject jsonobject = JSONObject.parseObject(result);
		return jsonobject;
	}

	/**
	 * doPostJson
	 * @param url
	 * @param json
	 * @return
	 */
	public static String doPostJson(String url, String json) {
		// 创建Httpclient对象
		CloseableHttpClient httpClient=null;
		CloseableHttpResponse response = null;
		String result = "";
		try {
			httpClient = getHttpClient();
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			// 创建请求内容
			StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
			httpPost.setEntity(entity);
			// 执行http请求
			response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity resEntity = response.getEntity();
				if(resEntity != null){
					result = EntityUtils.toString(response.getEntity(), "utf-8");
				}
			}else {
				readHttpResponse(response);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return result;
	}


	/**
	 * doGet
	 * @param url
	 * @return
	 */
	public static JSONObject doGet(String url){
		return doGet(url,CHARTSET);
	}

	public static void main(String [] a){
		HttpClientUtil.doGet("https://www.baidu.com", "utf-8");
	}
}


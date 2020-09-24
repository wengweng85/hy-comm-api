package com.insigma.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
  *  ҵ����ˮ���������ɹ�����
 * @author wengsh
 *
 */
public class IDGenertor {
	
	private static IDGenertor idGenertor = null;
	//�����ڼ�
	private static SimpleDateFormat yMd = new SimpleDateFormat("yyyyMMdd");
	
	//�����뼶
	private static SimpleDateFormat yMdHmsS = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	
	private IDGenertor() {
		 
	}
	
	 /**
	     * ȡ��PrimaryGenerater�ĵ���ʵ��
	*
	* @return
	*/
	public static IDGenertor getInstance() {
	  if (idGenertor == null) {
	      synchronized (SerialNumberTool.class) {
	          if (idGenertor == null) {
	        	  idGenertor = new IDGenertor();
	          }
	      }
	  }
	  return idGenertor;
	}

	public synchronized  String run() {
		//return yMd.format(new Date(System.currentTimeMillis()))+SerialNumberTool.getInstance().generaterNextNumber(9)+UUIDGenerator.generate();
		//���ɹ��� ��ǰʱ�䵽���뼶+uuid
		return yMdHmsS.format(new Date(System.currentTimeMillis()))+UUIDGenerator.generate();
	}
	
	/**
	 *  ҵ����ˮ��
	 * @return
	 */
	public static String generate() {
		return IDGenertor.getInstance().run();
	}
	
	/**
	 * uuid
	 * @return
	 */
	public static String uuidgenerate() {
		return UUIDGenerator.generate().toString();
	}
	
    public static void main(String[] args) {
        //  ���̲߳���
    	for (int i = 0; i <1000000 ; i++) {
        	System.out.println(IDGenertor.generate());
        }
    }

}

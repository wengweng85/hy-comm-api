package com.insigma.mvc.repository.main.dao.pub;

import java.util.HashMap;
import java.util.List;

/**
 * ����ҵ���
 * @author admin
 *
 */
public interface PubMapper {
	List<HashMap<String,Object>> commonQuery(HashMap hm);
}

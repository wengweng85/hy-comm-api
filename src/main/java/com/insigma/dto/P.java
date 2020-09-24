package com.insigma.dto;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class P extends AbstractParam
	implements Map<String, Object> {
	
	private static final long serialVersionUID = -6933477447867096169L;
	private Map<String, Object> params = new HashMap();

	
	@Override
	public int size() {
		return this.params.size();
	}

	@Override
	public boolean isEmpty() {
		return this.params.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		 return this.params.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return this.params.containsValue(value);
	}

	@Override
	public Object get(Object key) {
		return this.params.get(key);
	}

	@Override
	public Object put(String key, Object value) {
		return this.params.put(key, value);
	}

	@Override
	public Object remove(Object key) {
		return this.params.remove(key);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		this.params.putAll(m);		
	}

	@Override
	public void clear() {
		this.params.clear();
		
	}

	@Override
	public Set<String> keySet() {
		return this.params.keySet();
	}

	@Override
	public Collection<Object> values() {
		return this.params.values();
	}

	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		return this.params.entrySet();
	}

}

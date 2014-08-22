package com.example.datomictest01.util;

import java.util.List;

/**
 * Common interface of TaskCallback.
 * 
 * @param <E>
 */
public interface TaskCallback<E> {
	
	/**
	 * call back for on success list.
	 * @param list result list.
	 */
	public void onSuccess(List<E> list);
	
	/**
	 * call back for on success list with readmore or not.
	 * @param list result list
	 */
	public void onSuccess(List<E> list, boolean more);
	
	/**
	 * call back for on success single object.
	 * @param object result object.
	 */
	public void onSuccess(E object);
	
	public void onFailure(String a);
}

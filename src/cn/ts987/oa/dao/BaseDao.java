package cn.ts987.oa.dao;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

@SuppressWarnings("unchecked")
public class BaseDao<T> implements IBaseDao<T>{
	
	public BaseDao() {
		ParameterizedType entityClassType = (ParameterizedType) this.getClass().getGenericSuperclass();
		this.clazz = (Class<T>) entityClassType.getActualTypeArguments()[0];
	}
	
	private Class<T> clazz = null;
	
	@Resource
	private SessionFactory sessionFactory = null;
	
	
	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public void save(T entity) {
		getSession().save(entity);
	}
	
	@Override
	public void delete(long id) {
		getSession().createQuery("DELETE FROM " + clazz.getSimpleName() + " WHERE id = (:id)")
			.setParameter("id", id)
			.executeUpdate();
		//getSession().delete(entity);
	}
	  
	@Override
	public void update(T entity) { 
		
		getSession().update(entity);
	}

	@Override
	public T findById(long id) {
		return (T)getSession().get(clazz, id);
	}

	@Override
	public List<T> findByIds(Collection<Long> ids) {
		return getSession().createQuery(
				"FROM " + clazz.getSimpleName() +  " WHERE id IN (:ids)") //
				.setParameterList("ids", ids).list();
	}
	
	@Override
	public List<T> findByIds(Long[] ids) {
		if (ids == null || ids.length == 0) {
			return Collections.EMPTY_LIST;
		} else {
			return getSession().createQuery(//
					"FROM " + clazz.getSimpleName() + " WHERE id IN (:ids)")//
					.setParameterList("ids", ids)//
					.list();
		}
	}  

	@Override
	public List<T> findAll() {
		return getSession().createQuery("FROM " + clazz.getSimpleName()).list();
	}
	
	
}

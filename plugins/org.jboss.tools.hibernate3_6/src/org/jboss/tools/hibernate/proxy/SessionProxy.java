package org.jboss.tools.hibernate.proxy;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jboss.tools.hibernate.spi.ISession;
import org.jboss.tools.hibernate.spi.ISessionFactory;

public class SessionProxy implements ISession {
	
	private Session target;
	private ISessionFactory targetFactory;

	public SessionProxy(ISessionFactory sessionFactory, Session session) {
		target = session;
		targetFactory = sessionFactory;
	}

	@Override
	public String getEntityName(Object o) {
		return target.getEntityName(o);
	}

	@Override
	public ISessionFactory getSessionFactory() {
		return targetFactory;
	}

	@Override
	public Query createQuery(String queryString) {
		return target.createQuery(queryString);
	}

	@Override
	public boolean isOpen() {
		return target.isOpen();
	}

	@Override
	public void close() {
		target.close();
	}

	@Override
	public boolean contains(Object object) {
		return target.contains(object);
	}

}

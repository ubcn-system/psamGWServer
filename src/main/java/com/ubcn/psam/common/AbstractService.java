package com.ubcn.psam.common;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public abstract class AbstractService
extends AbstractObject
implements InitializingBean, DisposableBean, Service
{ 
	
	public final void afterPropertiesSet()
	  throws Exception
	{
	  initService();
	}
	
	public final void destroy()
	  throws Exception
	{
	  destroyService();
	}
	
	protected void initService()
	  throws Exception
	{}
	
	protected void destroyService()
	  throws Exception
	{}
}

package com.ubcn.psam.common;


import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ubcn.psam.common.util.MessageUtils;


public abstract class AbstractObject
{
  protected final Log logger = LogFactory.getLog(getClass());
  
  protected String getClassName()
  {
    return getClass().getName();
  }
  
  public String getMessage(String key)
  {
    return MessageUtils.getMessage(getClassName(), key);
  }
  
  public String getMessage(String key, Locale locale)
  {
    return MessageUtils.getMessage(getClassName(), key, locale);
  }
  
  public String getMessage(String key, String defaultMessage)
  {
    return MessageUtils.getMessage(getClassName(), key, defaultMessage);
  }
  
  public String getMessage(String key, Locale locale, String defaultMessage, Object... arguments)
  {
    return MessageUtils.getMessage(
      getClassName(), key, locale, defaultMessage, arguments);
  }
  
  public String getMessage(String key, String defaultMessage, Locale locale, Object... arguments)
  {
    return MessageUtils.getMessage(
      getClassName(), key, locale, defaultMessage, arguments);
  }
}
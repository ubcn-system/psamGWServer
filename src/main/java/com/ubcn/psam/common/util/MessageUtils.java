package com.ubcn.psam.common.util;

import java.util.Hashtable;
import java.util.Locale;

import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ResourceBundleMessageSource;

public abstract class MessageUtils
{
private static Hashtable<String, ResourceBundleMessageSource> cachedMessageSources = new Hashtable<String, ResourceBundleMessageSource>();

  public static String getMessage(String baseName, String key)
  {
    return getMessage(baseName, key, Locale.getDefault(), null, new Object[0]);
  }

  public static String getMessage(String baseName, String key, Locale locale)
  {
    return getMessage(baseName, key, locale, null, new Object[0]);
  }

  public static String getMessage(String baseName, String key, String defaultMessage)
  {
    return getMessage(baseName, key, Locale.getDefault(), defaultMessage, new Object[0]);
  }

  public static String getMessage(String baseName, String key, String defaultMessage, Locale locale, Object... arguments)
  {
    return getMessage(baseName, key, locale, defaultMessage, arguments);
  }

  public static String getMessage(String baseName, String key, Locale locale, String defaultMessage, Object... arguments)
  {
    if ((baseName == null) || (key == null)) {
      return defaultMessage;
    }
    ResourceBundleMessageSource messageSource = (ResourceBundleMessageSource)cachedMessageSources.get(baseName);
    if (messageSource == null)
    {
      messageSource = new ResourceBundleMessageSource();
      messageSource.setBasename(baseName);

      cachedMessageSources.put(baseName, messageSource);
    }
    if (locale == null) {
      locale = Locale.getDefault();
    }
    try
    {
      return messageSource.getMessage(key, arguments, locale);
    }
    catch (NoSuchMessageException localNoSuchMessageException) {}
    return defaultMessage;
  }
}

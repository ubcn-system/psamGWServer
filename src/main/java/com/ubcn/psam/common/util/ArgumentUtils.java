package com.ubcn.psam.common.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;


public class ArgumentUtils
{

  private static final String MESSAGE_BUNDLE = AssertUtils.class
    .getName().replaceFirst(new StringBuilder()
    .append(AssertUtils.class.getSimpleName()).append("$").toString(), "") + "messages";

  public static final int LENGTH_FLAG_EQUAL = 0;
  public static final int LENGTH_FLAG_GREATER_OR_EQUAL = 1;

  public static enum Flag
  {
    LESS_THAN,  LESS_EQUAL,  EQUAL,  GREATER_EQUAL,  GREATER_THAN;

    private Flag() {}
  }

  public static void validateTrue(boolean value)
  {
    validateTrue(value, "expression", Locale.getDefault());
  }

  public static void validateTrue(boolean value, String valueName)
  {
    validateTrue(value, valueName, Locale.getDefault());
  }

  public static void validateTrue(boolean value, Locale locale)
  {
    validateTrue(value, "expression", locale);
  }

  public static void validateTrue(boolean value, String expression, Locale locale)
  {
    if (!value) {
      throw new IllegalArgumentException(MessageUtils.getMessage(MESSAGE_BUNDLE, "must_be_true", locale, null, new Object[] { expression }));
    }
  }

  public static void validateNotNull(Object argument)
  {
    validateNotNull(argument, "argument", Locale.getDefault());
  }

  public static void validateNotNull(Object argument, String argumentName)
  {
    validateNotNull(argument, argumentName, Locale.getDefault());
  }

  public static void validateNotNull(Object argument, Locale locale)
  {
    validateNotNull(argument, "argument", locale);
  }

  public static void validateNotNull(Object argument, String argumentName, Locale locale)
  {
    if (argument == null) {
      throw new IllegalArgumentException(MessageUtils.getMessage(MESSAGE_BUNDLE, "must_be_not_null", locale, null, new Object[] { argumentName }));
    }
  }

  public static void validateNotEmpty(Object argument)
  {
    validateNotEmpty(argument, "argument", Locale.getDefault());
  }

  public static void validateNotEmpty(Object argument, String argumentName)
  {
    validateNotEmpty(argument, argumentName, Locale.getDefault());
  }

  public static void validateNotEmpty(Object argument, Locale locale)
  {
    validateNotEmpty(argument, "argument", locale);
  }

  public static void validateNotEmpty(Object argument, String argumentName, Locale locale)
  {
    if (ObjectUtils.isEmpty(argument)) {
      throw new IllegalArgumentException(MessageUtils.getMessage(MESSAGE_BUNDLE, "must_be_not_empty", locale, null, new Object[] { argumentName }));
    }
  }

  public static void validateInstanceOf(Object argument, Class<?> type)
  {
    validateInstanceOf(argument, type, "argument", Locale.getDefault());
  }

  public static void validateInstanceOf(Object argument, Class<?> type, String argumentName)
  {
    validateInstanceOf(argument, type, argumentName, Locale.getDefault());
  }

  public static void validateInstanceOf(Object argument, Class<?> type, Locale locale)
  {
    validateInstanceOf(argument, type, "argument", locale);
  }

  public static void validateInstanceOf(Object argument, Class<?> type, String argumentName, Locale locale)
  {
    AssertUtils.assertNotNull(type, "type");
    if (!type.isInstance(argument)) {
      throw new IllegalArgumentException(MessageUtils.getMessage(MESSAGE_BUNDLE, "must_be_instance_of", locale, null, new Object[] {argumentName + "(" + argument

        .getClass() + ")", type

        .toString() }));
    }
  }

  public static void validateEqual(Object argument, Object target)
  {
    validateEqual(argument, target, "argument", "target",

      Locale.getDefault());
  }

  public static void validateEqual(Object argument, Object target, String argumentName, String targetName)
  {
    validateEqual(argument, target, argumentName, targetName, Locale.getDefault());
  }

  public static void validateEqual(Object argument, Object target, Locale locale)
  {
    validateEqual(argument, target, "argument", "target", locale);
  }

  public static void validateEqual(Object argument, Object target, String argumentName, String targetName, Locale locale)
  {
    if (ObjectUtils.isNotEqual(argument, target)) {
      throw new IllegalArgumentException(MessageUtils.getMessage(MESSAGE_BUNDLE, "must_be_equal_to", locale, null, new Object[] { argumentName, targetName }));
    }
  }

  public static void validateLength(Object argument, int length, Flag flag)
  {
    validateLength(argument, length, flag, "argument", Locale.getDefault());
  }

  public static void validateLength(Object argument, int length, Flag flag, String argumentName)
  {
    validateLength(argument, length, flag, argumentName, Locale.getDefault());
  }

  public static void validateLength(Object argument, int length, Flag flag, Locale locale)
  {
    validateLength(argument, length, flag, "argument", locale);
  }

  public static void validateLength(Object argument, int length, Flag flag, String argumentName, Locale locale)
  {
    switch (flag)
    {
    case LESS_THAN:
      if ((argument != null) && ((!(argument instanceof String)) ||

        (((String)argument).length() < length)) &&
        ((!argument.getClass().isArray()) ||
        (Array.getLength(argument) < length)) && ((!(argument instanceof Collection)) ||

        (((Collection<?>)argument).size() < length)))
      {
        if ((argument instanceof Map)) {
          if (((Map<?, ?>)argument).size() < length) {
            break;
          }
        }
      }
      else {
        throw new IllegalArgumentException(MessageUtils.getMessage(MESSAGE_BUNDLE, "must_be_less_than", locale, null, new Object[] { argumentName,

          Integer.valueOf(length) }));
      }
      break;
    case LESS_EQUAL:
      if ((argument != null) && ((!(argument instanceof String)) ||

        (((String)argument).length() <= length)) &&
        ((!argument.getClass().isArray()) ||
        (Array.getLength(argument) <= length)) && ((!(argument instanceof Collection)) ||

        (((Collection<?>)argument).size() <= length)))
      {
        if ((argument instanceof Map)) {
          if (((Map<?, ?>)argument).size() <= length) {
            break;
          }
        }
      }
      else {
        throw new IllegalArgumentException(MessageUtils.getMessage(MESSAGE_BUNDLE, "must_be_less_equal", locale, null, new Object[] { argumentName,

          Integer.valueOf(length) }));
      }
      break;
    case EQUAL:
      if ((argument != null) && ((!(argument instanceof String)) ||

        (((String)argument).length() == length)) &&
        ((!argument.getClass().isArray()) ||
        (Array.getLength(argument) == length)) && ((!(argument instanceof Collection)) ||

        (((Collection<?>)argument).size() == length)))
      {
        if ((argument instanceof Map)) {
          if (((Map<?, ?>)argument).size() == length) {
            break;
          }
        }
      }
      else {
        throw new IllegalArgumentException(MessageUtils.getMessage(MESSAGE_BUNDLE, "must_be_equal_length", locale, null, new Object[] { argumentName,

          Integer.valueOf(length) }));
      }
      break;
    case GREATER_EQUAL:
      if ((argument != null) && ((!(argument instanceof String)) ||

        (((String)argument).length() >= length)) &&
        ((!argument.getClass().isArray()) ||
        (Array.getLength(argument) >= length)) && ((!(argument instanceof Collection)) ||

        (((Collection<?>)argument).size() >= length)))
      {
        if ((argument instanceof Map)) {
          if (((Map<?, ?>)argument).size() >= length) {
            break;
          }
        }
      }
      else {
        throw new IllegalArgumentException(MessageUtils.getMessage(MESSAGE_BUNDLE, "must_be_greater_equal", locale, null, new Object[] { argumentName,

          Integer.valueOf(length) }));
      }
      break;
    case GREATER_THAN:
      if ((argument != null) && ((!(argument instanceof String)) ||

        (((String)argument).length() > length)) &&
        ((!argument.getClass().isArray()) ||
        (Array.getLength(argument) > length)) && ((!(argument instanceof Collection)) ||

        (((Collection<?>)argument).size() > length)))
      {
        if ((argument instanceof Map)) {
          if (((Map<?, ?>)argument).size() > length) {
            break;
          }
        }
      }
      else {
        throw new IllegalArgumentException(MessageUtils.getMessage(MESSAGE_BUNDLE, "must_be_greater_than", locale, null, new Object[] { argumentName,

          Integer.valueOf(length) }));
      }
      break;
    }
  }
}
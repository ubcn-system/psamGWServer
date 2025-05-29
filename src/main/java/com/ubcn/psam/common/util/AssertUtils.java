package com.ubcn.psam.common.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import org.springframework.util.ObjectUtils;

public class AssertUtils {

	private static final String MESSAGE_BUNDLE = AssertUtils.class.getName().replaceFirst(
			new StringBuilder().append(AssertUtils.class.getSimpleName()).append("$").toString(), "") + "messages";

	public static final int LENGTH_FLAG_EQUAL = 0;
	public static final int LENGTH_FLAG_GREATER_OR_EQUAL = 1;

	public static enum Flag {
		LESS_THAN, LESS_EQUAL, EQUAL, GREATER_EQUAL, GREATER_THAN;

		private Flag() {
		}
	}

	public static void assertTrue(boolean value) {
		assertTrue(value, "expression", Locale.getDefault());
	}

	public static void assertTrue(boolean value, String expression) {
		assertTrue(value, expression, Locale.getDefault());
	}

	public static void assertTrue(boolean value, Locale locale) {
		assertTrue(value, "expression", Locale.getDefault());
	}

	public static void assertTrue(boolean value, String expression, Locale locale) {
		if (!value) {
			throw new AssertionError(MessageUtils.getMessage(MESSAGE_BUNDLE, "assertion_failed") + MessageUtils
					.getMessage(MESSAGE_BUNDLE, "must_be_true", locale, null, new Object[] { expression }));
		}
	}

	public static void assertNotNull(Object object) {
		assertNotNull(object, "object", Locale.getDefault());
	}

	public static void assertNotNull(Object object, String objectName) {
		assertNotNull(object, objectName, Locale.getDefault());
	}

	public static void assertNotNull(Object object, Locale locale) {
		assertNotNull(object, "object", locale);
	}

	public static void assertNotNull(Object object, String objectName, Locale locale) {
		if (object == null) {
			throw new AssertionError(MessageUtils.getMessage(MESSAGE_BUNDLE, "assertion_failed") + MessageUtils
					.getMessage(MESSAGE_BUNDLE, "must_be_not_null", locale, null, new Object[] { objectName }));
		}
	}

	public static void assertNotEmpty(Object object) {
		assertNotEmpty(object, "object", Locale.getDefault());
	}

	public static void assertNotEmpty(Object object, String objectName) {
		assertNotEmpty(object, objectName, Locale.getDefault());
	}

	public static void assertNotEmpty(Object object, Locale locale) {
		assertNotEmpty(object, "object", Locale.getDefault());
	}

	public static void assertNotEmpty(Object object, String objectName, Locale locale) {
		if (ObjectUtils.isEmpty(object)) {
			throw new AssertionError(MessageUtils.getMessage(MESSAGE_BUNDLE, "assertion_failed") + MessageUtils
					.getMessage(MESSAGE_BUNDLE, "must_be_not_empty", locale, null, new Object[] { objectName }));
		}
	}

	public static void assertInstanceOf(Object object, Class<?> type) {
		assertInstanceOf(object, type, "object", Locale.getDefault());
	}

	public static void assertInstanceOf(Object object, Class<?> type, String objectName) {
		assertInstanceOf(object, type, objectName, Locale.getDefault());
	}

	public static void assertInstanceOf(Object object, Class<?> type, Locale locale) {
		assertInstanceOf(object, type, "object", locale);
	}

	public static void assertInstanceOf(Object object, Class<?> type, String objectName, Locale locale) {
		assertNotNull(type, "type");
		if (!type.isInstance(object)) {
			throw new AssertionError(MessageUtils.getMessage(MESSAGE_BUNDLE, "assertion_failed")
					+ MessageUtils.getMessage(MESSAGE_BUNDLE, "must_be_instance_of", locale, null,
							new Object[] { objectName + "(" + object .getClass() + ")", type .toString() }));
		}
	}

	public static void assertLength(Object object, int length, Flag flag) {
		assertLength(object, length, flag, "object", Locale.getDefault());
	}

	public static void assertLength(Object object, int length, Flag flag, String objectName) {
		assertLength(object, length, flag, objectName, Locale.getDefault());
	}

	public static void assertLength(Object object, int length, Flag flag, Locale locale) {
		assertLength(object, length, flag, "object", locale);
	}

	public static void assertLength(Object object, int length, Flag flag, String objectName, Locale locale) {
		switch (flag) {
		case LESS_THAN:
			if ((object != null) && ((!(object instanceof String)) || (((String) object).length() < length))
					&& ((!object.getClass().isArray()) || (Array.getLength(object) < length))
					&& ((!(object instanceof Collection)) || (((Collection<?>) object).size() < length))) {
				if ((object instanceof Map)) {
					if (((Map<?, ?>) object).size() < length) {
						break;
					}
				}
			} else {
				throw new AssertionError(MessageUtils.getMessage(MESSAGE_BUNDLE, "assertion_failed") + MessageUtils
						.getMessage(MESSAGE_BUNDLE, "must_be_less_than", locale, null, new Object[] { objectName,

								Integer.valueOf(length) }));
			}
			break;
		case LESS_EQUAL:
			if ((object != null) && ((!(object instanceof String)) ||

					(((String) object).length() <= length))
					&& ((!object.getClass().isArray()) || (Array.getLength(object) <= length))
					&& ((!(object instanceof Collection)) ||

							(((Collection<?>) object).size() <= length))) {
				if ((object instanceof Map)) {
					if (((Map<?, ?>) object).size() <= length) {
						break;
					}
				}
			} else {
				throw new AssertionError(MessageUtils.getMessage(MESSAGE_BUNDLE, "assertion_failed") + MessageUtils
						.getMessage(MESSAGE_BUNDLE, "must_be_less_equal", locale, null, new Object[] { objectName,

								Integer.valueOf(length) }));
			}
			break;
		case EQUAL:
			if ((object != null) && ((!(object instanceof String)) ||

					(((String) object).length() == length))
					&& ((!object.getClass().isArray()) || (Array.getLength(object) == length))
					&& ((!(object instanceof Collection)) ||

							(((Collection<?>) object).size() == length))) {
				if ((object instanceof Map)) {
					if (((Map<?, ?>) object).size() == length) {
						break;
					}
				}
			} else {
				throw new AssertionError(MessageUtils.getMessage(MESSAGE_BUNDLE, "assertion_failed") + MessageUtils
						.getMessage(MESSAGE_BUNDLE, "must_be_equal_length", locale, null, new Object[] { objectName,

								Integer.valueOf(length) }));
			}
			break;
		case GREATER_EQUAL:
			if ((object != null) && ((!(object instanceof String)) ||

					(((String) object).length() >= length))
					&& ((!object.getClass().isArray()) || (Array.getLength(object) >= length))
					&& ((!(object instanceof Collection)) ||

							(((Collection<?>) object).size() >= length))) {
				if ((object instanceof Map)) {
					if (((Map<?, ?>) object).size() >= length) {
						break;
					}
				}
			} else {
				throw new AssertionError(MessageUtils.getMessage(MESSAGE_BUNDLE, "assertion_failed") + MessageUtils
						.getMessage(MESSAGE_BUNDLE, "must_be_greater_equal", locale, null, new Object[] { objectName,

								Integer.valueOf(length) }));
			}
			break;
		case GREATER_THAN:
			if ((object != null) && ((!(object instanceof String)) ||

					(((String) object).length() > length))
					&& ((!object.getClass().isArray()) || (Array.getLength(object) > length))
					&& ((!(object instanceof Collection)) ||

							(((Collection<?>) object).size() > length))) {
				if ((object instanceof Map)) {
					if (((Map<?, ?>) object).size() > length) {
						break;
					}
				}
			} else {
				throw new AssertionError(MessageUtils.getMessage(MESSAGE_BUNDLE, "assertion_failed") + MessageUtils
						.getMessage(MESSAGE_BUNDLE, "must_be_greater_than", locale, null, new Object[] { objectName,

								Integer.valueOf(length) }));
			}
			break;
		}
	}
}
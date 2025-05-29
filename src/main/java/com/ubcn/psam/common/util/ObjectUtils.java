package com.ubcn.psam.common.util;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ObjectUtils {

	private static final String MESSAGE_BUNDLE = ObjectUtils.class.getName().replaceFirst(
			new StringBuilder().append(ObjectUtils.class.getSimpleName()).append("$").toString(), "") + "messages";

	public static boolean isEmpty(Object s) {
		if (s == null) {
			return true;
		}
		if ((s instanceof String) && (((String) s).trim().length() == 0)) {
			return true;
		}
		if (s instanceof Map) {
			return ((Map<?, ?>) s).isEmpty();
		}
		if (s instanceof List) {
			return ((List<?>) s).isEmpty();
		}
		if (s instanceof Object[]) {
			return (((Object[]) s).length == 0);
		}
		return false;
	}

	public static boolean isNotEmpty(Object object) {
		return !isEmpty(object);
	}

	public static boolean isTrue(Object object) {
		if (isEmpty(object)) {
			return false;
		}
		if ((object instanceof Boolean)) {
			return ((Boolean) object).booleanValue();
		}
		if ((object instanceof String)) {
			return Boolean.valueOf((String) object).booleanValue();
		}
		if ((object instanceof Number)) {
			double value = ((Number) object).doubleValue();

			return (!Double.isNaN(value)) && (value != 0.0D);
		}
		if ((object instanceof Character)) {
			char ch = ((Character) object).charValue();

			return (ch != 0) && (ch != 'f') && (ch != 'F');
		}
		if ((object instanceof Void)) {
			throw new IllegalArgumentException(MessageUtils.getMessage(MESSAGE_BUNDLE, "cannot_evaluate_void"));
		}
		throw new AssertionError(MessageUtils.getMessage(MESSAGE_BUNDLE, "cannot_evaluate_unknown",

				Locale.getDefault(), null, new Object[] { object

						.getClass().getName() }));
	}

	public static boolean isFalse(Object object) {
		return !isTrue(object);
	}

	public static boolean isEqual(Object left, Object right) {
		if (left == right) {
			return true;
		}
		if ((left == null) || (right == null)) {
			return false;
		}
		if (((left instanceof Object[])) && ((right instanceof Object[]))) {
			return Arrays.deepEquals((Object[]) left, (Object[]) right);
		}
		if (((left instanceof byte[])) && ((left instanceof byte[]))) {
			return Arrays.equals((byte[]) left, (byte[]) right);
		}
		if (((left instanceof short[])) && ((right instanceof short[]))) {
			return Arrays.equals((short[]) left, (short[]) right);
		}
		if (((left instanceof int[])) && ((right instanceof int[]))) {
			return Arrays.equals((int[]) left, (int[]) right);
		}
		if (((left instanceof long[])) && ((right instanceof long[]))) {
			return Arrays.equals((long[]) left, (long[]) right);
		}
		if (((left instanceof char[])) && ((right instanceof char[]))) {
			return Arrays.equals((char[]) left, (char[]) right);
		}
		if (((left instanceof float[])) && ((right instanceof float[]))) {
			return Arrays.equals((float[]) left, (float[]) right);
		}
		if (((left instanceof double[])) && ((right instanceof double[]))) {
			return Arrays.equals((double[]) left, (double[]) right);
		}
		if (((left instanceof boolean[])) && ((right instanceof boolean[]))) {
			return Arrays.equals((boolean[]) left, (boolean[]) right);
		}
		return left.equals(right);
	}

	public static boolean isNotEqual(Object left, Object right) {
		return !isEqual(left, right);
	}

	public static <T> T nullValue(T source, T replacement) {
		return source == null ? replacement : source;
	}

	public static <T> T emptyValue(T source, T replacement) {
		return isEmpty(source) ? replacement : source;
	}
}
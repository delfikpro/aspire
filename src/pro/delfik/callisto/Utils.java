package pro.delfik.callisto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pro.delfik.callisto.vimeworld.Guild;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.function.Function;

public final class Utils {

	public static JSONObject[] toJavaArray(JSONArray jsonArray) {
		JSONObject[] array = new JSONObject[jsonArray.length()];
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				array[i] = jsonArray.getJSONObject(i);
			} catch (JSONException e) {
				array[i] = null;
			}
		}
		return array;
	}

	public static <From, To> List<To> transform(List<From> from, Function<From, To> converter) {
		List<To> result = new ArrayList<>();
		for (From f : from) result.add(converter.apply(f));
		return result;
	}
	public static <T> String merge(T[] array, Function<T, String> converter, String separator) {
		if (array.length == 0) return "";
		StringBuilder b = new StringBuilder(converter.apply(array[0]));
		for (int i = 1; i < array.length; i++) b.append(separator).append(converter.apply(array[i]));
		return b.toString();
	}
	
	public static int asInt(String s) {
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException | NullPointerException ex) {
			return -1;
		}
	}

	public static List<JSONObject> toJavaList(JSONArray members) {
		List<JSONObject> list = new ArrayList<>();
		for (int pos = 0; pos < members.length(); pos++) {
			try {
				list.add(members.getJSONObject(pos));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public static String to8(String string) {
		try {
			return new String(string.getBytes("windows-1251"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return string;
		}
	}
	
	public static String to1251(String string) {
		try {
			return new String(string.getBytes("UTF-8"), "windows-1251");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return string;
		}
	}
	
	public static <T> Map<String, T> mapFilledWith(Iterable<? extends Object> objects, T t) {
		Map<String, T> map = new HashMap<>();
		for (Object o : objects) map.put(o.toString(), t);
		return map;
	}
	
	public static class ArrayIterable<T> implements Iterable<T> {
		private final T[] array;

		public ArrayIterable(T[] array) {
			this.array = array;
		}

		@Override
		public Iterator<T> iterator() {
			return new ArrayIterator<>(array);
		}
	}
	public static class ArrayIterator<T> implements Iterator<T> {
		@Override
		public boolean hasNext() {
			return position < array.length - 1;
		}

		@Override
		public T next() {
			return array[position++];
		}

		private final T[] array;
		private int position = 0;

		ArrayIterator(T[] array) {
			this.array = array;
		}
	}
}

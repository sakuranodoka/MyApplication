package object;

import java.lang.reflect.Field;

public class Clone {
	public static Object cloneObject(Object obj){
		try {
			Object clone = obj.getClass().newInstance();
			for (Field field : obj.getClass().getDeclaredFields()) {
				field.setAccessible(true);
				field.set(clone, field.get(obj));
			}
			return clone;
		} catch (Exception e) {
			return null;
		}
	}
}

package es.uji.geotec.commonutils.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import es.uji.geotec.commonutils.Log;
import es.uji.geotec.commonutils.message.ErrorMessages;

public class ObjectBuilder<T> {

	private static final String TAG = ObjectBuilder.class.getName();
	
	private Constructor<T> constructor;
	private Object[] args;

	public ObjectBuilder(final Constructor<T> constructor, final Object... args) {
		this.constructor = constructor;
		this.args = args;
	}
	
	public T newInstance() {
		try {
			return constructor.newInstance(args);
		} catch (InstantiationException e) {
			Log.e(TAG, e, ErrorMessages.REFLECTION_INSTANTIATION_ERROR, constructor.getName());
		} catch (IllegalAccessException e) {
			Log.e(TAG, e, ErrorMessages.REFLECTION_ILLEGAL_ACCESS, constructor.getName());
		} catch (IllegalArgumentException e) {
			Log.e(TAG, e, ErrorMessages.REFLECTION_ILLEGAL_ARGUMENT, constructor.getName());
		} catch (InvocationTargetException e) {
			Log.e(TAG, e, ErrorMessages.REFLECTION_INVOCATION_TARGET_ERROR, constructor.getName());
		}
		
		throw new RuntimeException(ErrorMessages.OBJECT_BUILDER_NEW_INSTANCE_ERROR);
	}
}

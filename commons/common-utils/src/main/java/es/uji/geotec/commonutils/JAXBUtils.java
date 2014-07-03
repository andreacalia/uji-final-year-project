package es.uji.geotec.commonutils;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import es.uji.geotec.commonutils.message.ErrorMessages;

/**
 * Provides utilities fot JAXB marshalling
 *
 */
public class JAXBUtils {

	private static final String TAG = JAXBUtils.class.getName();

	/**
	 * Perform the marshal operation using JAXB on the parameter object
	 * 
	 * @param object the object to marshal
	 * @return the String representation of the parameter object
	 */
	public static <T> String marshal(T object) {
		// Create a XML String from TimeTable class
		try {
			final JAXBContext context = JAXBContext.newInstance(object.getClass());
			final Marshaller marshaller = context.createMarshaller();
			final StringWriter sw = new StringWriter();
			marshaller.marshal(object, sw);
			return sw.toString();
		} catch (JAXBException e) {
			Log.e(TAG, e, ErrorMessages.JAXB_MARSHAL_ERROR);
			throw new RuntimeException(e);
		}
	}
}

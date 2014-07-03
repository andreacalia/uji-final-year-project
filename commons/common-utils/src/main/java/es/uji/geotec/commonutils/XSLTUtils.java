package es.uji.geotec.commonutils;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import es.uji.geotec.commonutils.expection.XSLTException;

/**
 * This class provides helpers to apply XSL transformations
 *
 */
public class XSLTUtils {

	private static final String TAG = XSLTUtils.class.getName();

	public static String applyXSLT(final String xml, final String xsltFilePath) throws XSLTException {
		Log.i(TAG, "Applying "+xsltFilePath+" style sheet");
		
		final StreamSource inputReader = new StreamSource(new StringReader(xml));
		final StreamSource stylesource = new StreamSource(new File(xsltFilePath));
		final StringWriter outputTransformed = new StringWriter();

		try {
			
			final Transformer transformer = TransformerFactory.newInstance().newTransformer(stylesource);
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			
			transformer.transform(inputReader, new StreamResult(outputTransformed));
			
		} catch (TransformerConfigurationException | TransformerFactoryConfigurationError e) {
			final XSLTException ex = new XSLTException("Error creating transformer");
			ex.setStackTrace(e.getStackTrace());
			throw ex;
		} catch (TransformerException e) {
			e.printStackTrace();
			final XSLTException ex = new XSLTException("Error applying xslt transformation");
			ex.setStackTrace(e.getStackTrace());
			throw ex;
		}
		
		return outputTransformed.toString();
	}
}

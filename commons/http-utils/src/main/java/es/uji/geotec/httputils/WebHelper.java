package es.uji.geotec.httputils;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

/**
 * Abstraction to make HTTP requests and to clean up the result
 *
 */
public final class WebHelper {

//	private static final String TAG = WebHelper.class.getName();

	/**
	 * Clean the html page of html parameter and return the first DOM node
	 * @param html is a String containing the html page
	 * @return first DOM node
	 */
	public static TagNode cleanHTML(final String html) {
		final HtmlCleaner cleaner = new HtmlCleaner();
		final CleanerProperties props = cleaner.getProperties();
		props.setAllowHtmlInsideAttributes(true);
		props.setAllowMultiWordAttributes(true);
		props.setRecognizeUnicodeChars(true);
		props.setOmitComments(true);
		return cleaner.clean(html);
	}
	
	/**
	 * Commodity method to get a web page and clean it
	 * @param url the valid url of the resource
	 * @return first DOM node
	 */
	public static TagNode getAndCleanWebPage(final String url) {
		return cleanHTML(WebExecutor.syncGetRequest(url));
	}
	
}

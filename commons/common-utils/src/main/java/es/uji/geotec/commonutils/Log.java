package es.uji.geotec.commonutils;

/**
 * Simple log
 *
 */
public final class Log {

	private static final boolean debugMode = true;

	public static boolean debugEnabled = false;
	
	// ERROR
	
	public static void e(final String TAG, final String msg) {
		final String text = "ERROR ["+TAG+"]: "+msg;
		printError(text);
	}
	public static void e(final String TAG, final String msg, final Object... args) {
		final String text = "ERROR ["+TAG+"]: "+msg;
		printError(String.format(text, args));
	}
	public static void e(final String TAG, final Exception e, final String msg) {
		final String text = "ERROR ["+TAG+"]: "+msg;
		printError(text, e);
	}
	public static void e(final String TAG, final Exception e, final String msg, final Object... args) {
		final String text = "ERROR ["+TAG+"]: "+msg;
		printError(String.format(text, args), e);
	}
	
	// INFO
	
	public static void i(final String TAG, final String msg) {
		final String text = "INFO ["+TAG+"]: "+msg;
		printInfo(text);
	}
	
	public static void i(final String TAG, final String msg, final Object... args) {
		final String text = "INFO ["+TAG+"]: "+msg;
		printInfo(String.format(text, args));
	}
	
	// DEBUG
	
	public static void d(final String TAG, final String msg) {
		if( ! debugEnabled ) return;
		final String text = "DEBUG ["+TAG+"]: "+msg;
		printDebug(text);
	}
	
	public static void d(final String TAG, final String msg, final Object... args) {
		if( ! debugEnabled ) return;
		final String text = "DEBUG ["+TAG+"]: "+msg;
		printDebug(String.format(text, args));
	}
	
	// PRIVATE
	
	private static void printInfo(final String msg) {
		System.out.println(msg);
	}
	
	private static void printDebug(final String msg) {
		System.out.println(msg);
	}
	
	private static void printError(final String msg, final Exception e) {
		if( debugMode )
			e.printStackTrace();
		System.err.println(msg);
	}
	
	private static void printError(final String msg) {
		System.err.println(msg);
	}

}

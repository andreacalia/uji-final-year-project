package es.uji.geotec.httputils.exception;

public class RouteServiceException extends Exception {
	private static final long serialVersionUID = -8530568897160864488L;
	public RouteServiceException(String message, Throwable cause) {
		super(message, cause);
	}
	public RouteServiceException(Throwable cause) {
		super(cause);
	}
}
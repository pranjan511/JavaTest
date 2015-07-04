package option;

@SuppressWarnings("serial")
public class GeolocationOptionException extends Exception {
    public GeolocationOptionException() {
    }

    public GeolocationOptionException(String str) {
        super(str);
    }

    public GeolocationOptionException(String str, Throwable cause) {
        super(str, cause);
    }

    public GeolocationOptionException(Throwable cause) {
        super(cause);
    }

    public GeolocationOptionException(String str, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(str, cause, enableSuppression, writableStackTrace);
    }
}

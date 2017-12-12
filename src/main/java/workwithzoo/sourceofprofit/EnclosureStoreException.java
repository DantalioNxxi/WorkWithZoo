package workwithzoo.sourceofprofit;

/**
 * Throws if illegal action of adding or removing from any enclosure.
 * @author DantalioNxxi
 * @see SourceOfProfitException
 * @see workwithzoo.sourceofprofit.Enclosure
 * @version 1.0
 * @since 10.12.2017
 */
public class EnclosureStoreException extends SourceOfProfitException {

    public EnclosureStoreException() {
        super();
    }

    public EnclosureStoreException(String message) {
        super(message);
    }

    public EnclosureStoreException(Throwable cause) {
        super(cause);
    }

    public EnclosureStoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public EnclosureStoreException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

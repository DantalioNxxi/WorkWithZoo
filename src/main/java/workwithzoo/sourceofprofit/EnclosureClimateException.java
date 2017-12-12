package workwithzoo.sourceofprofit;

/**
 * Throws if illegal action of adding or removing from any enclosure.
 * @author DantalioNxxi
 * @see SourceOfProfitException
 * @see workwithzoo.sourceofprofit.Enclosure
 * @version 1.0
 * @since 10.12.2017
 */
public class EnclosureClimateException extends SourceOfProfitException{

    public EnclosureClimateException() {
    }

    public EnclosureClimateException(String message) {
        super(message);
    }

    public EnclosureClimateException(String message, Throwable cause) {
        super(message, cause);
    }

    public EnclosureClimateException(Throwable cause) {
        super(cause);
    }

    public EnclosureClimateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}

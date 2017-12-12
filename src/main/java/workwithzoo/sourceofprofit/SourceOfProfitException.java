package workwithzoo.sourceofprofit;

/**
 * Class of the exceptions for any cashable objects.
 * @author DantalioNxxi
 * @see SourceOfProfit
 * @version 1.0
 * @since 10.12.2017
 */
public class SourceOfProfitException extends Exception{

    public SourceOfProfitException() {
    }

    public SourceOfProfitException(String message) {
        super(message);
    }

    public SourceOfProfitException(String message, Throwable cause) {
        super(message, cause);
    }

    public SourceOfProfitException(Throwable cause) {
        super(cause);
    }

    public SourceOfProfitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
    
}

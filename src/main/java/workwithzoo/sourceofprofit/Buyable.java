
package workwithzoo.sourceofprofit;

/**
 *
 * @author DantalioNxxi
 */
public interface Buyable {
    
    /**
     * Sells the object.
     * @return true, if the sale held success.
     */
    boolean selling();
    
    /**
     * Buy the object.
     * @return true, if the buying held success.
     */
    boolean buy();
    
}

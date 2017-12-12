
package workwithzoo.sourceofprofit;

/**
 * For instance, who can to buys and sells.
 * @author DantalioNxxi
 */
public interface Buyable {
    
    /**
     * Sell object.
     * @return cost of the selling object
     */
    float sell();
    
    /**
     * Buy object.
     * @return instance of the Object.
     */
    Buyable buy();
    
}

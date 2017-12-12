
package workwithzoo.fauna;

/**
 * Abstract class for hierarchy of the Animals. It is Middle Animals.
 * By default his chields like the middle climate.
 * @author DantalioNxxi
 * @see Animal
 * @see Likes
 * @see Climate
 * @see ClimateObserver
 * @since 26.11.2017
 * @version 1.0.3
 */
@Likes(likesClimate = Climate.TypeClimate.MIDDLE_CLIMATE)
/**
 * //By default is requires less food and brings less profit.
 */
public class Middle extends Animal{
    /**
     * If cost, costCare, defaultProfit, aggression did not defined, then they will by default.
     * @param nickname name of animal.
     */
    protected Middle(String nickname){
        super(nickname);
        this.cost = 30_000;
        this.costCare = cost/50;
        this.defaultProfit = cost/25;
        this.aggression = 3;
    }
    
}

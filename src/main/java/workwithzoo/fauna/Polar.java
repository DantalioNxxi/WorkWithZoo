
package workwithzoo.fauna;

/**
 * Abstract class for hierarchy of the Animals. It is Polar Animals.
 * By default his chields like the cold climate.
 * @author DantalioNxxi
 * @see Animal
 * @see Likes
 * @see Climate
 * @see ClimateObserver
 * @since 26.11.2017
 * @version 1.0.3
 */
@Likes(likesClimate = Climate.TypeClimate.POLAR_CLIMATE)
/**
 * //By default is requires more food and brings more profit.
 */
public abstract class Polar extends Animal{
    /**
     * If cost, costCare, defaultProfit, aggression did not defined, then they will by default.
     * @param nickname name of animal.
     */
    protected Polar(String nickname){
        super(nickname);
        this.cost = 50_000;
        this.costCare = cost/50;
        this.defaultProfit = cost/25;
        this.aggression = 5;
    }
}

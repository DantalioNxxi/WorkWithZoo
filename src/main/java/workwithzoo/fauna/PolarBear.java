
package workwithzoo.fauna;

/**
 * Class for definite animal. It is PolarBear.
 * @author DantalioNxxi
 * @see Animal
 * @see Deathable
 * @see Likes
 * @see Climate
 * @see ClimateObserver
 * @see Polar
 * @since 26.11.2017
 * @version 1.0.3
 */

@Likes(likesClimate = Climate.TypeClimate.POLAR_CLIMATE)
public class PolarBear extends Polar {//implemets Climate...
    
    public PolarBear(String nickname) {
        super(nickname);
        this.nickname = nickname;
        this.cost = 50_000;
        this.costCare = cost/60;
        this.defaultProfit = cost/26;
        //sets max possible start aggression
        this.aggression = random.nextInt(6); //Polar is more aggressive. Just assumption
    }
    
}

package workwithzoo.fauna;

/**
 * Class for definite animal. It is Lion.
 * @author DantalioNxxi
 * @see Animal
 * @see Deathable
 * @see Likes
 * @see Climate
 * @see ClimateObserver
 * @see Tropical
 * @since 26.11.2017
 * @version 1.0.3
 */
@Likes(likesClimate = Climate.TypeClimate.TROPICAL_CLIMATE)
public class Lion extends Tropical{
    public Lion(String nickname) {
        super(nickname);
        this.nickname = nickname;
        this.cost = 36_000;
        this.costCare = cost/50; //
        this.defaultProfit = cost/18; //
        
        //sets max possible start aggression
        this.aggression = random.nextInt(7); //Lion is more aggressive. Just assumption
    }
}

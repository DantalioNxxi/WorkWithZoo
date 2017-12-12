package workwithzoo.fauna;


/**
 * Class for definite animal. It is AmurTiger.
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
@Likes(likesClimate = Climate.TypeClimate.MIDDLE_CLIMATE)
public class AmurTiger extends Middle{
    public AmurTiger(String nickname) {
        super(nickname);
        this.nickname = nickname;
        this.cost = 36_000;
        this.costCare = cost/50;
        this.defaultProfit = cost/18;
        
        this.aggression = random.nextInt(6); //Tiger is more aggressive. Just assumption
    }
    
}

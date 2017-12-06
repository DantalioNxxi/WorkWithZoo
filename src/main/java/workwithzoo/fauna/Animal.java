package workwithzoo.fauna;

import java.util.Random;
import workwithzoo.sourceofprofit.Buyable;
import workwithzoo.sourceofprofit.Losseable;
import workwithzoo.sourceofprofit.Profitable;

/**
 *
 * @author DantalioNxxi
 */
public class Animal implements Buyable, Profitable, Losseable{ //Reproducable
    
    private final int MAX_AGGRESSION = 10;
    
    public String nickname;
    public double cost;
    public double costCare;
    public int aggression;
    private double defaultProfit;
    protected StateHealth stateHealth;
    
    /**
     * Возможно придётся реализовывать Comporable или просто использовать ordinal
     */
    protected static enum StateHealth{
        WELL, TIRED, AGGRESSIVE, SICK, STARVE
    }

    public Animal(String nickname, double cost, StateHealth stateHealth) {
        this.nickname = nickname;
        this.cost = cost;
        this.costCare = cost/10; //100$
        this.stateHealth = stateHealth;
        this.defaultProfit = cost/8; //Допустим, если медведь стоит 1000$, то в день с него можно получить 125$
        
        Random random = new Random();
        // От нуля до 5 или лучше использовать MAX_AGRESSION? Порог агрессии по умолчанию
        this.aggression = random.nextInt(5); 
    }
    
    public void setAggression(int aggr){
        if (aggr<=0) {
            aggression = 0;
        } else if (aggr>MAX_AGGRESSION) {
            aggression=MAX_AGGRESSION;
        } else aggression = aggr;
    }

    @Override
    public double sell() {
        //...Удаление из всех наблюдаемых объектов, из пулов
        //Возврат стоимости животного.
        return cost;
    }

    @Override
    public Animal buy() {
        return this;
    }

    @Override
    public double getProfit() {
        double profit = defaultProfit;
        if (aggression>5) profit*=(10 - stateHealth.ordinal())/10.0;
        return profit;
        //Стоимость содержания, агрессия ниже некоторого порога, хорошее состояние здоровья
    }

    @Override
    public double getLoss() {
        return costCare;
        //Стоимость содержания, агрессия выше некоторого порога, ухудшенное состояние здоровья
    }

    @Override
    public String toString() {
        return "Animal{" + "Кличка=" + nickname
                + ", Стоимость="+ cost
                + ", Агрессивность=" + aggression
                + ", Состояние здоровья=" + stateHealth + '}';
    }
    
}










//==============
//public abstract class Animal implements Buyable, Profitable, Losseable{ //Reproducable
//    /**
//     * After pull from the enclosure, the animal do not must to leave the zoo
//     */
////    boolean isInsideZoo;
//    
//    /**
//     * If true, then this male gender.
//     */
////    boolean isMale;
//    
//    public String nickname;
//    public double cost;
//    
//    int aggression;
//    
////    int age;
//    
//    protected TypeFood typeFood;
//    
//    protected StateHealth stateHealth;
//    
//    protected static enum TypeFood{
//        MEAT, HERB, ANY
//    }
//    
//    /**
//     * оставлять ли здесь состояние беременности?
//     */
//    protected static enum StateHealth{
//        WELL, TIRED, PREGNANT, AGGRESSIVE, SICK, STARVE
//    }
//
//    @Override
//    public double sell() {
//        //...Удаление из всех наблюдаемых объектов, из пулов
//        //Возврат стоимости животного.
//        return cost;
//    }
//
//    @Override
//    public Animal buy() {
//        return this;
//    }
//    
//    
//    
//}
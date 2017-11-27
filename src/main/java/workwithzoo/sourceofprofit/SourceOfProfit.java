
package workwithzoo.sourceofprofit;

/**
 * The class of the object, which can brings profit, be bought or sold.
 * @author DantalioNxxi
 */
public abstract class SourceOfProfit implements Profitable, Buyable{
    protected float cost;
    protected boolean isProfitably;
    
    //методы для соблюдения консистивности
}

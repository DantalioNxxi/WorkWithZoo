
package workwithzoo.sourceofprofit;

/**
 * The class of the object, which can brings profit, be bought or sold.
 * @author DantalioNxxi
 */
public abstract class SourceOfProfit implements Profitable, Buyable, Losseable{
    protected double cost;
    
    //методы для соблюдения консистивности

    
    //Либо через getCost(), либо через метод buy???
    
    public double getCost() {
        return cost;
    }
}

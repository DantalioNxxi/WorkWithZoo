
package workwithzoo.sourceofprofit;

import workwithzoo.user.Businessman;

/**
 * The class of the object, which can brings profit, be bought or sold.
 * @author DantalioNxxi
 */
public abstract class SourceOfProfit implements Profitable, Buyable, Losseable{
    protected float cost;
    public Businessman owner;
    
    //методы для соблюдения консистивности

    public void setCost(float cost){
        this.cost = cost;
    }
    //Либо через getCost(), либо через метод buy???
    
    public float getCost() {
        return cost;
    }
}

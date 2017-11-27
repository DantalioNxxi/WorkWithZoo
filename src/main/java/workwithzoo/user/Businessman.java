
package workwithzoo.user;

import java.util.HashMap;
import java.util.Map;
import workwithzoo.sourceofprofit.SourceOfProfit;
/**
 *
 * @author DantalioNxxi
 */
class Businessman extends User{
    /**
     * If money less than zero, then the businessmen will have to became in debt.
     */
    private float money;
    
    //сделать обобщённым
    /**
     * Pool of the sources of income
     */
    private Map<String, SourceOfProfit> agencies = new HashMap<>();    
    
    public void setAgency(String name, SourceOfProfit agency){
        
        if (agencies.containsValue(name)) {//If such object already in pull
        throw new IllegalArgumentException("Объект с именем "+name+" уже есть!");
        }
        agencies.put(name, agency);
    }
    
    public Businessman(float startMoney, int id, String firstname, String lastname){
        super(id, firstname, lastname);
        money = startMoney;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }
}


package workwithzoo.user;

import java.util.HashMap;
import java.util.Map;
import workwithzoo.sourceofprofit.Losseable;
import workwithzoo.sourceofprofit.Profitable;
import workwithzoo.sourceofprofit.SourceOfProfit;
/**
 *
 * @author DantalioNxxi
 */
class Businessman extends User implements Profitable, Losseable{
    /**
     * If money less than zero, then the businessmen will have to became in debt.
     */
    private double money;
    
    //сделать обобщённым
    /**
     * Pool of the sources of income
     */
    private Map<String, SourceOfProfit> agencies = new HashMap<>();    
    
    //Присутствует int! st.nval не обработает
    public Businessman(double startMoney, int id, String firstname, String lastname){
        super(id, firstname, lastname);
        money = startMoney;
    }
    
    //Возможно понадобится какой-нибудь интерфейс Agenciable?
    public void addAgency(String name, SourceOfProfit agency){
        if (agencies.containsKey(name)) {//If such object already in pull
        throw new IllegalArgumentException("Объект с именем "+name+" уже есть!");
        }
        money-=agency.getCost(); // Либо в долги, либо выбрасывать исключение
        agencies.put(name, agency);
    }
    
    public SourceOfProfit getAgency(String name, SourceOfProfit agency){
        
        if (agencies.containsKey(name)) {//If such object already in pull
            throw new IllegalArgumentException("Объект с именем "+name+" уже есть!");
        }
        return agencies.get(name);
    }
    
    public void sellAgency(String name){
        if (!agencies.containsKey(name)) {//If such object not exits at pull
            throw new IllegalArgumentException("Объекта с именем "+name+" нет в наличии!");
        }
        money+=agencies.get(name).getCost();
        agencies.remove(name);
    }
    
    public double getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    //Компоновщик
    @Override
    public double getProfit() {
        double profit = 0;
        for (String name : agencies.keySet()){
            profit+=agencies.get(name).getProfit();
        }
        return profit - this.getLoss();
    }

    @Override
    public double getLoss(){ 
        double loss = 0;
        for (String name : agencies.keySet()){
            loss+=agencies.get(name).getLoss();
        }
        return loss;
    }

    @Override
    public String toString() {
        return "Businessman{" + super.getFirstname() + super.getLastname()
                + "money=" + money
                + ", agencies=" + agencies + '}';
    }
    
    
}

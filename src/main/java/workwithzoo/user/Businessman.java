
package workwithzoo.user;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import workwithzoo.fauna.Climate;
import workwithzoo.sourceofprofit.Losseable;
import workwithzoo.sourceofprofit.Profitable;
import workwithzoo.sourceofprofit.SourceOfProfit;
/**
 * Class of any businessman, who can to bring profitm, losse, and to has agencies.
 * @author DantalioNxxi
 * @since 26.11.2017
 * @version 1.0.3
 * @see Profitable
 * @see Losseable
 * @see SourceOfProfit
 * @see Climate
 * @see User
 */
public class Businessman extends User implements Profitable, Losseable{ //на время пока сделал всех пабликом
    
    /**Settings for the date*/
    public static final Locale LOCALE = new Locale("ru", "RU");
    /**Settings for the date*/
    public static final DateFormat DF = DateFormat.getDateInstance(DateFormat.DEFAULT, LOCALE);
    /**In future singlethon of date must be independent class*/
    public Calendar calendar = Calendar.getInstance();
    /**Current date.*/
    public Date currentDate = new Date();
    
    /**
     * If money less than zero, then the businessmen will have to became in debt.
     * In future this field must be is private.
     */
    public float money;
    
    /**
     * Pool of the sources of income.
     */
    private Map<String, SourceOfProfit> agencies = new HashMap<>();    
    
    /**
     * Constructor for businessman.
     * @param startMoney start asset.
     * @param id uniq number.
     * @param firstname
     * @param lastname 
     */
    public Businessman(float startMoney, int id, String firstname, String lastname){
        super(id, firstname, lastname);
        money = startMoney;
    }
    
    /**
     * Bues a new agency, which can bring profit and loss.
     * @param name name of the buyable agency
     * @param agency variable of the agency
     */
    public void buyAgency(String name, SourceOfProfit agency){
        if (agencies.containsKey(name)) {//If such object already in pull
        throw new IllegalArgumentException("Объект с именем "+name+" уже есть!");
        }
        System.out.println("Деньги до покупки"+money);
        agency.owner = this;
        money-=agency.getCost(); //Businessman can to move in debt state.
        agencies.put(name, agency);
        System.out.println("Приобретено предприятие:\n"
                +agency.toString()+"\nОстаток: "+money);
    }
    
    /**
     * Searchs agency with any name.
     * @param name of the agency.
     * @return agency with name "name".
     */
    public SourceOfProfit getAgency(String name){
        
        if (agencies.containsKey(name)) {//If such object already in pull
            throw new IllegalArgumentException("Объект с именем "+name+" уже есть!");
        }
        return agencies.get(name);
    }
    
    /**
     * When agency with name "name" is selling, then he removing from pull of agencies
     * and owner gets cost of selling agency.
     * @param name 
     */
    public void sellAgency(String name){
        if (!agencies.containsKey(name)) {//If such object not exits at pull
            throw new IllegalArgumentException("Объекта с именем "+name+" нет в наличии!");
        }
        money+=agencies.get(name).sell();
        agencies.remove(name);
    }
    
    /**
     * Will be using in future.
     * @return asset of the Businessman.
     */
    public float getMoney() {
        return money;
    }

    /**
     * Will be using in future.
     * @return asset of the Businessman.
     */
    private void setMoney(float money) {
        this.money = money;
    }

    /**
     * For the collection profit from all agences using Pattern Composite.
     * @return profit from all agencies.
     */
    @Override
    public float getProfit() {
        System.out.println("\nСчитаем прибыль с предприятий:");
        float profit = 0;
//        for (String name : agencies.keySet()){
//            profit+=agencies.get(name).getProfit();
//        }
        for (Map.Entry<String, SourceOfProfit> e : agencies.entrySet()){
            profit+=e.getValue().getProfit();
        }
        return profit; //- this.getLoss();
    }

    /**
     * For the collection loss from all agences using Pattern Composite.
     * @return loss from all agencies.
     */
    @Override
    public float getLoss(){ 
        System.out.println("\nСчитаем убытки с предприятий:");
        float loss = 0;
        for (String name : agencies.keySet()){
            loss+=agencies.get(name).getLoss();
        }
        return loss;
    }
    
    /**
     * During next days is collection all profit and loss from all agencies for every day.
     * Also changing the date, calendar and Climate.
     * @param days will move.
     */
    public void addDays(int days){
        double profit = 0;
        double loss = 0;
        for (int d=0;d<days;d++) {
            System.out.println("Текущая дата "+DF.format(currentDate));
            calendar.setTime(currentDate); //set current date
            int oldMonth = calendar.getTime().getMonth(); //set Current month
            calendar.add(Calendar.DAY_OF_MONTH, 1);// add day to setDate
            currentDate = calendar.getTime(); // get date, who was chainged

            profit=this.getProfit(); //at first to compute the profit
            loss=this.getLoss();//after loss

            if (currentDate.getMonth()!=oldMonth){ //If the month was chainged
                Climate.changeMonth(oldMonth);
            }

            money-=loss;
            money+=profit;
            System.out.println("\nИтого с предприятий:\nПрибыль: "+profit
                    +"; \nУбыток: "+loss
                    +"; \nСчёт на "+DF.format(currentDate)+" "+money+"\n");
        }
    }

    @Override
    public String toString() {
        return "Businessman{" + super.getFirstname() + " "+super.getLastname()
                + ", money=" + money
                + ", agencies: " + agencies.keySet() + '}';
    }
    
    
}

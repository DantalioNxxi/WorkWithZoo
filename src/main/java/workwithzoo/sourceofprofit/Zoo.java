
package workwithzoo.sourceofprofit;

import java.util.HashMap;
import java.util.Map;
import workwithzoo.employee.Employee;
import workwithzoo.fauna.Animal;
import workwithzoo.user.User;
/**
 *
 * Maybe it will implement Storeable?? 
 * @author DantalioNxxi
 */
public class Zoo extends SourceOfProfit implements Buyable, Profitable, Losseable{
    
    public class ThreadOfVisitors {
        private double intensity;
        
        static final int MIN_INTENS = 0;
        static final int MAX_INTENS = 10; // Или 100
        
        //Есть вариант вместить поля количества сотрудников, качества ухода, количество посетителей в день
        
        //Качество ухода можно связать с сотрудниками через посредника таким же образом
    }
    
    public String name;
    public User owner;
    
    private final ThreadOfVisitors threadVisitrors;
    /**
     * If the zoo is contact, then probability of accidents increase,
     * but attendanse increase too.
     */
//    public boolean isContact = false;
//    private float square;
// Вероятность несчастного случая
//    private float pAccident; 
    
    //Quantity of visitors is equal one by default.
    public int qVisits = 1;
    
    //Оставлять ли переменную, если используется компоновщик?
    private double profitForDay;
    
    //Coefficient of the profit for day.
    private float kProfit;
    
    //Стоимость всего зоопарка:
    private double cost = 200;
    
    //Качество ухода за животными:
    private float qCare = 1;
    
    /**
     * сделать обобщённым (или ограниченным с учётом capacity)
     */
    private Map<Double, Enclosure> enclosures = new HashMap<>();    
    
    private Map<Integer, Employee> staff = new HashMap<>();    
    
    //Необходимо ли это поле?
    private Map<String, Animal> animals = new HashMap<>();    
    
    /**
     * By default zoo does not brings profit.
     * @param owner
     * @param name
     */
    public Zoo(User owner, String name){
        
        threadVisitrors = new ThreadOfVisitors();
        
        this.name = name;
        this.owner = owner;
//        this.isContact = isContact;
    }

    
    public void addDays(double days){
        //TO DO...
        // Use the mediator between animals and threadVisitors, dateInstance, 
        System.out.println("В зоопарке "+this.name+" прошло "+days+" дней...");
    }
    
    /**
     * Adds a new aviary in the zoo.
     * @param id
     * @param enclosure 
     */
    public void addEnclosure(double id, Enclosure enclosure){
        
        if (enclosures.containsKey(id)) {//If such object already in pull
            throw new IllegalArgumentException("Вольер с ID="+id+" уже есть!");
        }
        this.cost+=enclosure.cost;
        enclosures.put(id, enclosure);
    }
    
    /**
     * Removes the enclosure with ID=id frow the zoo
     * @param id 
     */
    public void removeEnclosure(double id){
        if (!enclosures.containsKey(id)) {//If such object already in pull
            throw new IllegalArgumentException("Вольера с ID="+id+" нет в зоопарке!");
        }
        this.cost-=enclosures.get(id).cost;
        enclosures.remove(id);
    }
    
    /**
     * Adds a new employee. The income for the mounth will have fallen.
     * @param inn
     * @param employee 
     */
    public void addEmployee(int inn, Employee employee){
        
        if (staff.containsKey(inn)) {//If such object already in pull
        throw new IllegalArgumentException("Сотрудник с ИНН="+inn+" уже работает в зоопарке!");
        }
        staff.put(inn, employee);
    }
    
    /**
     * To dismiss the employee with INN=inn frow the zoo.
     * @param inn 
     */
    public void sackEmployee(int inn){
        //profitForDay will changed...
        if (!staff.containsKey(inn)) {//If such employee not exist at staff
            throw new IllegalArgumentException("Сотрудник с ИНН="+inn+" не работает в зоопарке!");
        }
        staff.remove(inn);
    }
    
    @Override
    public double getProfit(){
//        return profitForDay*kProfit;
        double profit = 0;
        for (Double number : enclosures.keySet()){
            profit+=enclosures.get(number).getProfit();
        }
        return profit;
        //Или отдельно пробегать по животным, если допустить, что животное может не находиться в вольере?
    }

    @Override
    public double getLoss() {
        double loss = 0;
        for (Double number : enclosures.keySet()){
//            loss+=enclosures.get(number).; А БУДЕТ ЛИ ВОЛЬЕР ПРИНОСИТЬ УБЫТКИ? ТОЛЬКО ЕСЛИ БУДЕМ ВЛКЮЧАТЬ В КОМПОНОВЩИК ВСЁ ПОДРЯД
        }
        
        for (Integer inn : staff.keySet()){
            loss+=staff.get(inn).getLoss();
        }
        
        return loss;
    }
    
    
    /**
     * Buy the zoo.
     * @return 
     */
    @Override
    public Buyable buy(){
        return this;
    }
    
    /**
     * Sells the zoo.
     * @return 
     */
    @Override
    public double sell(){
        return cost;
    }
    
    @Override
    public String toString(){
        return "Name "+name+" Owner "+owner; //+"isContact "+isContact
    }
    
}

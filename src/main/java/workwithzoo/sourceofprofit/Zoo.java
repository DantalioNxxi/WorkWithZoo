
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
public class Zoo extends SourceOfProfit {
    public String name;
    public User owner;
    /**
     * If the zoo is contact, then probability of accidents increase,
     * but attendanse increase too.
     */
    public boolean isContact = false;
    
    //Quantity of visitors is equal one by default.
    public int attendanse = 1;
    
    //Лишний расчёт?
//    private float square;
    private float pAccident;
    private float profitForDay;
    
    //Coefficient of the profit for day.
    private float kProfit;
    private float cost = 200;
    
    /**
     * сделать обобщённым (или ограниченным с учётом capacity)
     */
    private Map<Integer, Enclosure> enclosures = new HashMap<>();    
    
    private Map<Integer, Employee> staff = new HashMap<>();    
    
    private Map<String, Animal> animals = new HashMap<>();    
    
    /**
     * By default zoo does not brings profit.
     * @param owner
     * @param name
     * @param isContact 
     */
    public Zoo(User owner, String name, boolean isContact){
        isProfitably = false;
        this.name = name;
        this.owner = owner;
        this.isContact = isContact;
    }
          
    /**
     * Adds a new aviary in the zoo.
     * @param id
     * @param enclosure 
     */
    public void addEnclosure(int id, Enclosure enclosure){
        
        if (enclosures.containsValue(id)) {//If such object already in pull
        throw new IllegalArgumentException("Вольер с таким ID"+id+" уже есть!");
        }
        enclosures.put(id, enclosure);
    }
    
    /**
     * Removes the enclosure with ID=id frow the zoo
     * @param id 
     */
    public void removeEnclosure(int id){
        
    }
    
    /**
     * Adds a new employee. The income for the mounth will have fallen.
     * @param inn
     * @param employee 
     */
    public void addEmployee(int inn, Employee employee){
        
        if (staff.containsValue(inn)) {//If such object already in pull
        throw new IllegalArgumentException("Сотрудник с таким ИНН"+inn+" уже работает в зоопарке!");
        }
        staff.put(inn, employee);
    }
    
    /**
     * To dismiss the employee with INN=inn frow the zoo.
     * @param inn 
     */
    public void sackEmployee(int inn){
        //profitForDay will changed...
    }
    
    @Override
    public float getProfit(){
        return profitForDay*kProfit;
    }
    
    /**
     * Buy the zoo.
     * @return 
     */
    @Override
    public boolean buy(){
        //check the sale
        return true;
    }
    
    /**
     * Sells the zoo.
     * @return 
     */
    @Override
    public boolean selling(){
        //check the sale
        return true;
    }
    
}

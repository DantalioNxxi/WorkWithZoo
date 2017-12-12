
package workwithzoo.employee;

/**
 * If the zoo contains guide, then a profit is growing.
 * @author DantalioNxxi
 * @see Employee
 * @since 26.11.2017
 * @version 1.0.2 
 */
public class Guide extends Employee{
    
    /**
     * By default his salary more first value on 100
     * Salary will be equal 600
     * @param inn INN of the Employee
     * @param experience experience for the Employee
     */
    public Guide(int inn, int experience){
        super(inn, experience, 600);
    }
    
    /**
     * Salary will be equal 600
     * @param inn INN of the Employee
     */
    public Guide(int inn){
        super(inn, 0, 600);
        
    }
    
}

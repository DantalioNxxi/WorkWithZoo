
package workwithzoo.employee;

/**
 * Keepers are care for animals. 
 * @author DantalioNxxi
 * @see Employee
 * @since 26.11.2017
 * @version 1.0.2
 */
public class Keeper extends Employee{

    /**
     * Salary will be equal default = 500
     * @param inn INN of the Employee
     * @param experience experience for the Employee
     */
    public Keeper(int inn, int experience) {
        super(inn, experience);
    }

    /**
     * Salary will be equal default = 500
     * @param inn INN of the Employee
     */
    public Keeper(int inn) {
        super(inn);
    }
    
}

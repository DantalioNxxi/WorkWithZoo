
package workwithzoo.employee;

/**
 * Vet can to cure a sick or starve animal.
 * @author DantalioNxxi
 * @see Employee
 * @since 26.11.2017
 * @version 1.0.2
 */
public class Vet extends Employee{ //Если появился vet - животные оповещаются... и быстрее вылечиваются

    /**
     * By default his salary more first value on 200
     * Salary will be equal 700
     * @param inn INN of the Employee
     * @param experience experience for the Employee
     */
    public Vet(int inn, int experience) {
        super(inn, experience, 700);
    }

    /**
     * By default his salary more first value on 200
     * Salary will be equal 700
     * @param inn INN of the Employee
     */
    public Vet(int inn) {
        super(inn, 0, 700);
    }
    
}

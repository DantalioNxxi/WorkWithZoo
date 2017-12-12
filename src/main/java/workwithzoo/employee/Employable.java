
package workwithzoo.employee;

/**
 * Interface for any, who can to get salary and has INN.
 * @author DantalioNxxi
 */
public interface Employable {
    
    /**
     * Returnes a value of the salary of any employee.
     * @return salary of the employee
     */
    float getSalary();
    
    /**
     * Sets a value of the salary for any employee.
     * @param salary salary for employee by default
     */
    void setSalary(float salary);
    
    /**
     * Returns INN of any employee.
     * @return INN if the empoyee
     */
    int getINN();
}

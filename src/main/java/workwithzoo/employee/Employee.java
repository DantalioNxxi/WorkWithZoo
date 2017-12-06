
package workwithzoo.employee;

import workwithzoo.sourceofprofit.Losseable;

/**
 * The class of the anybody Employee.
 * @author DantalioNxxi
 */
public class Employee implements Employable, Losseable{

    private final double INN; //INN
    public double experience;
    private double salary;

    public Employee(double inn, double experience) {
        this.INN = inn;
        this.experience = experience;
    }
    
    @Override
    public String toString() {
        return "Employee{" + "ИНН=" + INN + ", Стаж=" + experience + ", Зарплата=" + salary + '}';
    }

    /**
     * 
     * @return value of salary
     */
    @Override
    public double getSalary() {
        return salary;
    }

    @Override
    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public double getINN() {
        return INN;
    }

    @Override
    public double getLoss() {
        return getSalary();
    }
    
    
}

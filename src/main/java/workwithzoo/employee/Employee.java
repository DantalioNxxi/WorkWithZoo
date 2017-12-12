
package workwithzoo.employee;

import workwithzoo.sourceofprofit.Losseable;
/**
 * The abstract class of the anybody Employee.
 * Loss computes from value of the salary.
 * @author DantalioNxxi
 * @see Employable
 * @see Losseable
 * @since 26.11.2017
 * @version 1.0.2 
 */
public abstract class Employee implements Employable, Losseable{

    protected final int INN; //INN of the Employee
    public int experience; //Than more experience, the more salary
    protected float salary = 500; //the salary for day by default at the work with Zoo

    /**
     * 
     * @param inn INN of the Employee
     * @param experience experience for the Employee
     * @param salary salary of the Employee
     */
    protected Employee(int inn, int experience, float salary) {
        this.INN = inn;
        this.experience = experience;
        this.salary = (float)(salary+salary*(experience/10.0)); //1.5*salary*(experience/10.0); 
//        System.out.println("В конструкторе зарплата стала равна: "+salary);
    }
    
    /**
     * 
     * @param inn INN of the Employee
     * @param experience experience for the Employee
     */
    protected Employee(int inn, int experience) {
        this.INN = inn;
        this.experience = experience;
        this.salary = (float)(salary+salary*(experience/10.0)); //1.5*salary*(experience/10.0); 
//        System.out.println("В конструкторе зарплата стала равна: "+salary);
    }
    
    /**
     * Salary will be is default value.
     * @param inn INN of new employee
     */
    protected Employee(int inn) {
        this.INN = inn;
    }
    
    @Override
    public String toString() {
        return "Employee{Профессия: "+ getClass().getSimpleName() + ", ИНН=" + INN
                + ", Стаж=" + experience + ", Зарплата=" + salary + '}';
    }

    /**
     * @return value of salary
     */
    @Override
    public float getSalary() {
        return salary;
    }

    /**
     * Lets set a new salary for the emloyee.
     * @param salary is a new sa;ary
     */
    @Override
    public void setSalary(float salary) {
        this.salary = salary;
    }

    /**
     * @return INN of the Employee
     */
    @Override
    public int getINN() {
        return INN;
    }

    /**
     * Loss computes from value of the salary.
     * @return salary of the employee
     */
    @Override
    public float getLoss() {
        return getSalary();
    }
    
    
}

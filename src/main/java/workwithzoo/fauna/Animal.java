package workwithzoo.fauna;

import workwithzoo.sourceofprofit.Buyable;

/**
 *
 * @author DantalioNxxi
 */
public abstract class Animal implements Buyable{ //Reproducable
    /**
     * After pull from the enclosure, the animal do not must to leave the zoo
     */
    boolean isInsideZoo;
    
    /**
     * If true, then this male gender.
     */
//    boolean isMale;
    
    int agression;
    
//    int age;
    
    protected TypeFood typeFood;
    
    protected StateHealth stateHealth;
    
    protected static enum TypeFood{
        MEAT, HERB, ANY
    }
    
    /**
     * оставлять ли здесь состояние беременности?
     */
    protected static enum StateHealth{
        WELL, TIRED, PREGNANT, AGGRESSIVE, SICK, STARVE
    }

    @Override
    public boolean selling() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean buy() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}

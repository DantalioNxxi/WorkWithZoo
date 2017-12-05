
package workwithzoo.sourceofprofit;

//import ncec.worwithzoo.fauna.Animal;

import java.util.ArrayList;
import java.util.Map;
import workwithzoo.fauna.Animal;


//
/**
 * The class of the aviary.
 * @author DantalioNxxi
 */
public class Enclosure {
    
    /**
     * сделать иерархию?.. с установленными ценами и характеристиками
     */
    public static enum TypeEnclosure {
        SMALLGLASS, LARGEGLASS, OPENED, CLOSED
    }    
    
    protected double id;
    //вместимость
    protected int capacity;
    
    /**
     * The type of enclosure.
     */
    protected TypeEnclosure typeEnclosure;
    protected double cost;
//    protected float space;
    
    protected boolean isThermoregulation;
    
    protected Map<Integer, Animal> animals; 
    
    /**
     * A list of the solved animals.
     */
    protected ArrayList<String> solvedAnimals;

    public Enclosure(double id, double cost){ //for tokenizer
        this.id = id;
        this.cost = cost;
    }
    
    @Override
    public String toString() {
        return "Enclosure{" + "id=" + id + ", cost=" + cost + '}';
    }
    
    
}




//public abstract class Enclosure implements Buyable, Storeable{
//    
//    /**
//     * сделать иерархию?.. с установленными ценами и характеристиками
//     */
//    public static enum TypeEnclosure {
//        SMALLGLASS, LARGEGLASS, OPENED, CLOSED
//    }    
//    
//    protected int id;
//    //вместимость
//    protected int capacity;
//    
//    /**
//     * The type of enclosure.
//     */
//    protected TypeEnclosure typeEnclosure;
//    protected float cost;
////    protected float space;
//    
//    protected boolean isThermoregulation;
//    
//    protected Map<Integer, Animal> animals; 
//    
//    /**
//     * A list of the solved animals.
//     */
//    protected ArrayList<String> solvedAnimals;
//    
//}
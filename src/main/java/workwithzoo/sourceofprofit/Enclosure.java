
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
public class Enclosure implements Buyable, Profitable{
//Losseable если обходить животных внутри или учитывать затраты на терморегляцию
    
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
    
    protected Map<String, Animal> animals; 
    
    //Температура в вольере, которая влияет на самочувствие животного.
    // Вероятно понадобится будет счётчик пребывания животного не в своей среде.
    
    //Закрытость вольера. Безопасность для посетителей.
    
    //Наличие животных, экзотичных по ситуации (теплолюбивых в зимнее время) повышает количество посетителей.
    
    //Модификация вольеров?
    
    /**
     * A list of the solved animals.
     */
    protected ArrayList<Animal> solvedAnimals; //Не имеет смысла без иерархии подклассов Enclosure

    public Enclosure(double id, double cost){ //is double for tokenizer
        this.id = id;
        this.cost = cost;
    }

    @Override
    public double sell() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Buyable buy() {
        return this;
    }

    @Override
    public double getProfit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
package workwithzoo.sourceofprofit;

import java.util.Random;

/**
 * Class factory for Enlosures.
 * @author DantalioNxxi
 */
public class EnclosureFactory {
//    private Random random = new Random();
    private int id;
    private int capacity;

    public void setId(int id) {
        this.id = id;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }
    
    public Enclosure getNewEnclosure(){
        return new Enclosure(id, capacity);
    }
    
}

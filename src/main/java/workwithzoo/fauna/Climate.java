package workwithzoo.fauna;

import java.util.LinkedList;
import java.util.List;

/**
 * Class with static parameters of out Climate.
 * @author DantalioNxxi
 * @see ClimateObserver
 * @since 07.12.2017
 */
public class Climate {
    
    /**
     * Class of the Climate does not be create.
     */
    private Climate(){}
    
    /**
     * Thoose, who will respond to changing global climate.
     */
    public static List<ClimateObserver> observers = new LinkedList<>();
    
    /**
     * Value of the global climate.
     */
    private static TypeClimate outClimate = TypeClimate.POLAR_CLIMATE;
    
    /**
     * Exists three simple kind of the global climate.
     */
    public static enum TypeClimate{
        POLAR_CLIMATE, MIDDLE_CLIMATE, TROPICAL_CLIMATE
    }

    public static TypeClimate getOutClimate() {
        return outClimate;
    }
    
    public static void setOutClimate(TypeClimate outClimate) {
        Climate.outClimate = outClimate;
    }
    
    /**
     * Calling by users or singethon instance of th Date (in Future).
     * Use method notifyObservers for alert them about change outClimate.
     * @param oldMonth whose will check, and change outClimate for the next month.
     */
    public static void changeMonth(int oldMonth){
        switch(oldMonth){
            case 0: {
                outClimate = TypeClimate.MIDDLE_CLIMATE;
                System.out.println("Наступила весна...");
                notifyObservers();
                break;}
            case 7: {
                outClimate = TypeClimate.MIDDLE_CLIMATE;
                System.out.println("Наступила осень...");
                notifyObservers();
                break;}
            case 4: {
                outClimate = TypeClimate.TROPICAL_CLIMATE;
                System.out.println("Наступило лето...");
                notifyObservers();
                break;}
            case 10: {
                outClimate = TypeClimate.POLAR_CLIMATE;
                System.out.println("Наступила зима...");
                notifyObservers();
                break;}
            default: break;
        }
    }
    
    public static void addObserver(ClimateObserver ob){
        observers.add(ob);
    }
    
    public static void removeObserver(ClimateObserver ob){
        observers.remove(ob);
    }
    
    /**
     * Notification to observers happening during changing of the outClimate.
     */
    public static void notifyObservers(){
        System.out.println("Климат: \"Оповещаем подписавшихся!\"");
        if (!observers.isEmpty()){
            for(ClimateObserver ob : observers){
                ob.updateClimate(outClimate);
            }
        }
    }
    
}

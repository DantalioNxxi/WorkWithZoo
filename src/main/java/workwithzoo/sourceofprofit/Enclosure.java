
package workwithzoo.sourceofprofit;

import java.util.HashMap;
import java.util.Map;
import workwithzoo.fauna.Animal;
import workwithzoo.fauna.Climate;
import workwithzoo.fauna.ClimateObserver;

/**
 * The class of the aviary.
 * In future must have BuilderFactory for create Enclosures into the zoo.
 * Enclosure updates climateUpdate for all animals, which he contains.
 * If the thermoregulation is disabled, enclosure gives to animals outGlobalClimate
 * @author DantalioNxxi
 * @since 26.11.2017
 * @version 1.0.3
 * @see Buyable
 * @see Losseable
 * @see Climate
 * @see ClimateObserver
 * @see Zoo
 */
public class Enclosure implements Buyable, Losseable, ClimateObserver{
    
    protected int id;
    /**Than more capacity, then more cost and loss at enabled thermoregulation.*/
    protected int capacity;
    
    private Map<String, Animal> intoAnimals = new HashMap<>();
    
    protected float cost;
    
    /**If thermoregulation is enabled.*/
    protected boolean isThermoregulation = false;
    /**If thermoregulation is was set into the enclosure.*/
    protected boolean tregulationIsOn = false;
    /**If zoo is not null, we will can't bue enclosure*/
    public Zoo zoo;
    
    private Climate.TypeClimate innerClimate = Climate.getOutClimate();//by default his climate equals outClimate
    
    /**
     * One place for animal cost 5000.
     * @param id for identification in world and after in zoo.
     * @param capacity among of the places for animals
     */
    public Enclosure(int id, int capacity){ //is double for tokenizer
        this.id = id;
        this.capacity = capacity;
        this.cost = capacity*5_000;
        System.out.println(this);
    }
    
    public int getId() {
        return id;
    }
    
    /**
     * Get climate, who into Enclosure.
     * @return inner climate.
     */
    public Climate.TypeClimate getInnerClimate() {
        return innerClimate;
    }
    
    /**
     * @see Climate
     * Using by Pattern Observer and use Pattern Observer
     * for accept outClimate and transfer to intoAnimals.
     * If thermoretulation is disabled, then he will accepts outCLimate.
     * @param newClimate given climate from global outClimate
     */
    @Override
    public void updateClimate(Climate.TypeClimate newClimate) {
        if (tregulationIsOn==false) innerClimate = Climate.getOutClimate();
            for(Map.Entry<String, Animal> a : intoAnimals.entrySet()){
                a.getValue().updateClimate(newClimate);
            }
    }
    
    /**
     * Sets newClimate by using thermoregulation.
     * If thermoregulation system did not set early,
     * then throw exception.
     * @param newClimate in the type Climate.TypeClimate
     * @throws EnclosureClimateException if enclosure does not support thermoregulation.
     */
    public void setClimate(Climate.TypeClimate newClimate)throws EnclosureClimateException{
        if (isThermoregulation) {
            tregulationIsOn = true;
            innerClimate = newClimate;
            System.out.println("В вольере "+id+" задан климат "+innerClimate.name());
            updateClimate(innerClimate);
                //Оповещает животных как наблюдаемый или через посредника
        } else throw new EnclosureClimateException("Вольер с id="+id+" не поддерживает терморегуляцию!");
    }
    
    /**
     * Sets newClimate by using thermoregulation.
     * If thermoregulation system did not set early,
     * then throw exception.
     * @param newClimate in the String type, who will parse to Enum Constant Climate.TypeClimate
     * @throws EnclosureClimateException if enclosure does not support thermoregulation.
     */
    public void setClimate(String newClimate)throws EnclosureClimateException{
        if (isThermoregulation) {
            tregulationIsOn = true;
            try{
                innerClimate = Climate.TypeClimate.valueOf(newClimate);
                System.out.println("В вольере "+id+" задан климат "+innerClimate.name());
                updateClimate(innerClimate);
            } catch(IllegalArgumentException e){
                //hidin standats exception and throw specific exception responsibale for the Climate.
                throw new EnclosureClimateException("Не существует климат типа: "+newClimate);
            }
            
        } else throw new EnclosureClimateException("Вольер с id="+id+" не поддерживает терморегуляцию!");
    }
    
    /**
     * Disables thermoregulation, if this system was setted early.
     * If enclosure do not supporn this system, print a warning.
     */
    public void offClimate(){
        if (isThermoregulation) {
            tregulationIsOn = false;
            innerClimate = Climate.getOutClimate();
            System.out.println("В вольере "+id+" теперь такая же температура, как и на улице: "+Climate.getOutClimate());
            updateClimate(innerClimate);
            //Оповещает животных как наблюдаемый или через посредника
        } else System.out.println("Вольер с id="+id+" не поддерживает терморегуляцию!");
        //лучше убрать исключение... а просто выводить, так как ничего критического по сути нет
    }
    
    /**
     * Set the thermoregulation system into an enclosure.
     */
    public void setThermoregulation(){
        if (isThermoregulation) {
            System.out.println("Вольер "+id+ " уже поддерживает терморегуляцию.");
        } else {
            isThermoregulation = true;
            System.out.println("В вольере с "+id+ " была установлена терморегуляция.");
            if (zoo!=null){ // if the enclosure is a part of a zoo;
                zoo.owner.money-=cost;
            }
            cost*=1.5;//changed value
            
        }
    }
    
    /**
     * Adds animal into enclosure.
     * @param name nickname of the adding animal
     * @param a adding animal
     * @throws EnclosureStoreException if enclosure if full or adding animal with similary nickename.
     * @throws EnclosureClimateException there are animal, who does not like current inner climate.
     */
    public void addAnimal(String name, Animal a) throws EnclosureStoreException, EnclosureClimateException{
        try{
            if (a.getLikesClimate()!=this.innerClimate) throw new EnclosureClimateException("Климат в вольере "
                +id+" не соответствует предпочтениям животного по кличке "+name);
//            if (!a.isInsideZoo){
//                System.out.println("Сперва приобретите животное.");
//            } else if (intoAnimals.size()==capacity){
//                throw new EnclosureStoreException("В вольер "+ id + " больше нельзя добавить животных!");
//            } else if (intoAnimals.containsKey(name)){
//                throw new EnclosureStoreException("В вольере не могут находиться животные с одинаковыми именами!");
//            } else {
//                    a.enclosure = this;
//                    intoAnimals.put(name, a);
//            }
        } catch (EnclosureClimateException e){
                //just print Warning. but the climate will be set.
                System.out.println(e.getMessage()); //steel just get message, because it need testing wrong climateTypes
        } finally {
            if (!a.isInsideZoo){
                System.out.println("Сперва приобретите животное.");
            } else if (intoAnimals.size()==capacity){
                throw new EnclosureStoreException("В вольер "+ id + " больше нельзя добавить животных!");
            } else if (intoAnimals.containsKey(name)){
                throw new EnclosureStoreException("В вольере не могут находиться животные с одинаковыми именами!");
            } else {
                    a.enclosure = this;
                    intoAnimals.put(name, a);
            }
        }
        
    }
    
    /**
     * Removes animal from the enclosure.
     * @param name nickname of animal,
     * @throws EnclosureStoreException if into Enclosure there is not animal with such nickname.
     */
    public void removeAnimal(String name) throws EnclosureStoreException{
        if (!intoAnimals.containsKey(name)){
            throw new EnclosureStoreException("В вольере "+id+" нет животного с именем "+name);
        }
        intoAnimals.get(name).enclosure = null;
        intoAnimals.remove(name);
        System.out.println(name+" был удалён из вольера "+id);
    }
    
    /**
     * @see Buyable
     * @return cost enclosure
     */
    @Override
    public float sell() {
        zoo = null;
        return cost;
        //continue for computing responsibale the zoo.
    }

    /**
     * @see Buyable
     * @return this enclosure
     */
    @Override
    public Buyable buy() {
        return this; 
    }

    /**
     * If thermoregulation is enabled, then for one place will spend 100.
     * @return loss for day
     */
    @Override
    public float getLoss() {
        float loss=0;
        if (tregulationIsOn) {
            loss+= capacity*100; // changed value (may be new constant)
        }
        //TO DO...
        return loss;
    }

    @Override
    public String toString() {
        return "Enclosure{" + "id=" + id
                + ", Вместимость=" + capacity
                + ", Стоимость=" + cost
                + ", Поддержка терморегуляции=" + isThermoregulation
                + (tregulationIsOn?", Климат: " + innerClimate+ ", Затраты в день="+getLoss():"")
                + ", Животные: "+ intoAnimals.keySet()
                + '}';
    }
} //The end of the class

//    public void offThermoregulation(){
//        if (isThermoregulation==false) {
//            System.out.println("Вольер уже поддерживает терморегуляцию.");
//        } else {
//            isThermoregulation = true;
//            cost*=2;
//        }
//    }
    
//    public void innerMessage(Class classAttack, Class classVictim){
//        Animal victim = checkAnimale(classVictim);
//        switch (classAttack.getSimpleName()){
//                case "Polar":{
//                    System.out.println("Да это Polar");
//                    if (victim!=null){
//                        
//                    }
//                    break;
//                }
//                case "Middle":{
//                    System.out.println("Да это Middle");
//                    break;
//                }
//                case "Tropical":{
//                    System.out.println("Да это Tropical");
//                    break;
//                }
//            }
//    }
    
//    private <T extends Animal> Animal checkAnimale(Class<T> cl){
//        for (Map.Entry<String, Animal> a : intoAnimals.entrySet()){
//            if (a.getValue().getClass()==cl) return a.getValue();
//        }
//        return null;
//    }
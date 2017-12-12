package workwithzoo.sourceofprofit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import workwithzoo.employee.Employee;
import workwithzoo.employee.Guide;
import workwithzoo.employee.Keeper;
import workwithzoo.employee.Vet;
import workwithzoo.fauna.Animal;
import workwithzoo.fauna.Climate;
import workwithzoo.fauna.ClimateObserver;
import workwithzoo.user.Businessman;

/**
 * @author DantalioNxxi
 * @since 26.11.2017
 * @version 1.0.3
 * @see Businessman
 * @see Profitable
 * @see Losseable
 * @see Buyable
 * @see Climate
 * @see ClimateObserver
 */
public class Zoo extends SourceOfProfit implements Buyable, Profitable, Losseable, ClimateObserver {

    public class ZooLifeStyle { // not full, not correct mediator

        /**Value of the bad care*/
        private static final double MIN_DEGREE_OF_BAD_SERVE = 0;
        /**Value of the bad care*/
        private static final double MAX_DEGREE_OF_BAD_SERVE = 10;

        /**
         * Generic class for compute degree of the bad serve for types,
         * which inherited class Employee. For example: Vet.class, Keeper.class, Guied.class etc.
         * Using by Animal.
         * @param <T> must inherited class Employee
         * @param cl variable - class of chields of the Employee class.
         * @return degree of the bad serv, then, for example, if amount of Vets is not
         * enough, return more degree.
         * @see Employee
         * @see Animal
         */
        public <T extends Employee> double degreeOfBadServe(Class<T> cl) {
            double dBadServe = MIN_DEGREE_OF_BAD_SERVE;
            if (containsEmployee(cl)) {
                if (cl == Guide.class) {
                    dBadServe = (double) animals.size() / getExperience(cl);
                    if (dBadServe <= 2) {
                        return MIN_DEGREE_OF_BAD_SERVE;//A count of the vets must be not more, than 2.
                    } else {
                        return dBadServe >= MAX_DEGREE_OF_BAD_SERVE ? MAX_DEGREE_OF_BAD_SERVE : dBadServe;
                    }
                } else if (cl == Keeper.class) {
                    dBadServe = (double) animals.size() / getExperience(cl);
                    if (dBadServe <= 2) {
                        return MIN_DEGREE_OF_BAD_SERVE;//A count of the vets must be not more, than 2.
                    } else {
                        return dBadServe >= MAX_DEGREE_OF_BAD_SERVE ? MAX_DEGREE_OF_BAD_SERVE : dBadServe;
                    }
                } else if (cl == Vet.class) {
                    dBadServe = (double) animals.size() / getExperience(cl);
                    if (dBadServe <= 2) {
                        return MIN_DEGREE_OF_BAD_SERVE;//A count of the vets must be not more, than 2.
                    } else {
                        return dBadServe >= MAX_DEGREE_OF_BAD_SERVE ? MAX_DEGREE_OF_BAD_SERVE : dBadServe;
                    }
                } else {
                    System.out.println("Появился новый тип сотрудника, не обработанный в degreeOfBadServe: " + cl.getSimpleName());
                    dBadServe = (double) animals.size() / getExperience(cl);
                    if (dBadServe <= 2) {
                        return MIN_DEGREE_OF_BAD_SERVE;//A count of the vets must be not more, than 2.
                    } else {
                        return dBadServe >= MAX_DEGREE_OF_BAD_SERVE ? MAX_DEGREE_OF_BAD_SERVE : dBadServe;
                    }
                }
            } else { //If bad serve of the class's employees is maximum
                return MAX_DEGREE_OF_BAD_SERVE;
            }
        }

        /**
         * Returns complex experience of the employees of the define Type cl. 
         * Using by degreeOfBadServe.
         * @param <T> must inherited Employee
         * @param cl class of the Employees, for example, Vet.class
         * @return complex experience of the employees of the define Type cl
         */
        public <T extends Employee> int getExperience(Class<T> cl) {
            int ex = 0;
            if (containsEmployee(cl)) {
                //if such employees was found in te zoo
                for (Map.Entry<Integer, Employee> e : staff.entrySet()) {
                    if (e.getValue().getClass() == cl) {
                        ex += (e.getValue().experience + 1);
                    }
                }
            }
            return ex;
        }

        /**
         * Check containing of employees of accept type int the zoo.
         * @param type of the Employee
         * @return true, if zoo contains at least one such employee.
         */
        public boolean containsEmployee(Class type) {
//            System.out.println("Проверяем наличие сотрудников класса "+type);
            for (Map.Entry<Integer, Employee> e : staff.entrySet()) {
                if (e.getValue().getClass() == type) {
                    return true;
                }
            }
            return false;
        }

        /**
         * @param type must inherited class Employee
         * @return count employees such type in the zoo
         */
        public int countEmployee(Class type) {
//            System.out.println("Считаем количество сотрудников типа: "+type.getSimpleName());
            int count = 0;
            for (Map.Entry<Integer, Employee> e : staff.entrySet()) {
                if (e.getValue().getClass() == type) {
                    count++;
                }
            }
            return count;
        }

        /**
         * Check general state of all animals into the zoo.
         * If was founded dead animals, then at presence Keepers,
         * corpses will be clear. In future it is must affect to profit.
         */
        public void checkStateOfAnimals() {
            System.out.println("ПРОВЕРЯЕМ СОСТОЯНИЕ ЖИВОТНЫХ:");
            List<String> deads = new ArrayList<>(); // dead animals (corpses)

            if (!animals.isEmpty()) {
                int i = 0;
                for (Map.Entry<String, Animal> a : animals.entrySet()) {
                    System.out.println((i+1)+". "+a.getValue().nickname + ":");
                    a.getValue().checkStateOfAnimal();
                    //=======
                    if (a.getValue().isDead()) {
                        deads.add(a.getValue().nickname);
                    }
                    i++;
                }
            } else {
                System.out.println("Животных не найдено.");
            }
            //clear deads animals
            if (containsEmployee(Keeper.class) && !deads.isEmpty()) {
                System.out.println("Трупы следующих животных будут удалены из зоопарка: " + deads);
                for (String d : deads) {
                    removeAnimal(d);
                }
            }
        }
    }

    public String name;
    public Businessman owner;
    public final ZooLifeStyle zooLifeStyle;
    public EnclosureFactory ef = new EnclosureFactory();
    private Random random = new Random();

    private Map<Integer, Enclosure> enclosures = new HashMap<>();
    private Map<Integer, Employee> staff = new HashMap<>();
    private Map<String, Animal> animals = new HashMap<>();

    /**
     * By default zoo does not brings profit.
     * His cost = 10_000 (ala registered capital).
     * @param owner businessman, who opened the zoo
     * @param name of the zoo
     * @see Climate
     * @see Animal
     * @see Employee
     * @see Enclosure
     */
    public Zoo(Businessman owner, String name) {

        /**It is necessary for compute state of animals. In future need mediator
         or another Pattern.*/
        zooLifeStyle = new ZooLifeStyle();

        Climate.addObserver(this);

        this.name = name;
        this.owner = owner;
        cost = 10_000;
        System.out.println("Создан Зоопарк: " + this);
    }

    /**
     * Adds a new enclosure in the zoo.
     * If zoo already contains such enclosure or
     * ID does not equal to enlosure.ID then show Warning.
     * @param id and his ID
     * @param enclosure adding enclosure
     */
    public void addEnclosure(int id, Enclosure enclosure) {
        if (id!=enclosure.id){
            System.out.println("Id вольера должен совпадать с его настоящим Id");
        } else if (enclosures.containsKey(id)) {//If such object already in pull
//            throw new IllegalArgumentException("Вольер с ID=" + id + " уже есть!");
            System.out.println("Вольер с ID=" + id + " уже есть!");
        } else {
            if (owner != null) {
                owner.money -= enclosure.cost;
                this.cost += enclosure.cost;
                enclosures.put(id, enclosure);
                System.out.println("В Зоопарк " + name + " добавлен вольер " + id + ". Остаток: " + owner.getMoney());
                Climate.addObserver(enclosure);
            } else System.out.println("Сначала купите этот зоопарк");
        }
    }
    
    /**
     * There is can to build a new Enclosure without buying him.
     * @param capacity amount of places in the new enclosure
     */
    public void createNewEnlosure(int capacity){
        if (ef.getCapacity()!=capacity) ef.setCapacity(capacity);
        while(true){
            int newId = random.nextInt(101)+1; //100 enclosures is already many
            if (enclosures.containsKey(newId)) continue;
            ef.setId(newId);
            break;
        }
        Enclosure e = ef.getNewEnclosure();
        this.cost += e.cost;
        enclosures.put(e.id, e);
        System.out.println("В Зоопарк " + name + " добавлен вольер c ID = " + e.id + " вместимостью: " + capacity);
        Climate.addObserver(e);
        //из коста не должно вычитаться
    }

    /**
     * Removes the enclosure with ID=id frow the zoo.
     * If zoo does not constains enclosure with such ID
     * then throw exception.
     * @param id of the Enclosure
     */
    public void removeEnclosure(int id) {
        if (!enclosures.containsKey(id)) {//If such object already in pull
            throw new IllegalArgumentException("Вольера с ID=" + id + " нет в зоопарке!");
        }
        this.cost -= enclosures.get(id).cost;//clear field zoo and return cost
        if (owner != null) {
            System.out.println("Вольер " + id + " был продан, " + "получено: " + enclosures.get(id).cost);
            owner.money += enclosures.get(id).sell();
        } else {
            System.out.println("Вольер с ID=" + id + " удалён из Зоопарка: " + name);
        }

        Climate.removeObserver(enclosures.get(id));
        enclosures.remove(id);
    }

    /**
     * Buys the animal and sub his cost from asset of the owner.
     * If animal already in zoo will be throw Exception.
     * @param a adding animal
     */
    public void addAnimal(Animal a) throws IllegalArgumentException{

        if (animals.containsValue(a)) {//If such object already in pull
            throw new IllegalArgumentException(a.getClass().getName() + " по кличке " + a.nickname + " уже в зоопарке!");
        }
        this.cost += a.cost;

        a.zoo = this;
        animals.put(a.nickname, a.buy());//меняется флаг "в зоопарке"

        if (owner != null) {
            owner.money -= a.cost;
            System.out.println("Приобретено " + a.getClass().getSimpleName()
                    + " по кличке " + a.nickname
                    + ". Остаток: " + owner.getMoney());
        } else {
            System.out.println("В Зоопарк " + name + " добавлен " + a.getClass().getSimpleName() + " по кличке " + a.nickname + ".");
        }

        //Here may to locate adding to enclosure. But animal may be in zoo and not enclosure concurrently.
    }

    /**
     * Removes animal from zoo.
     * If he located in enclosure, then 
     * firsly will be removed from him.
     * @param name of removing animal.
     */
    public void sellAnimal(String name) {
        try {
            Animal sellAnimal = animals.get(name);
            if (sellAnimal.enclosure != null) {
                enclosures.get(sellAnimal.enclosure.id).removeAnimal(name);//throw
            }
            System.out.println("Животное по кличке " + animals.get(name).nickname
                    + " продано, получено: " + animals.get(name).cost);
            owner.money += sellAnimal.sell();
            animals.remove(name);
        } catch (EnclosureStoreException e) {
            System.out.println(e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("Животное по кличке " + name + " не найдено!");
        }
    }

    /**
     * If animal did not bought,
     * or enclosure with such ID not founded then show Warning!
     * @param name of animal
     * @param id of the enclosure, where will be push.
     */
    public void addAnimalIntoEnclosure(String name, int id) {
        try {
            if (!animals.containsKey(name)) {
                System.out.println("Животное по кличке " + name + " не куплено!");
            } else if (!enclosures.containsKey(id)) {
                System.out.println("Вольер с Id=" + id + " не найден!");
            } else {
                enclosures.get(id).addAnimal(name, animals.get(name));//throw
                animals.get(name).enclosure = enclosures.get(id);
                System.out.println(animals.get(name).nickname + " был помещён в вольер "+id);
            }
        } catch (EnclosureStoreException | EnclosureClimateException e) {
            System.out.println("Не удалось добавить животное в вольер!\n" + e.getMessage()); //e.getCause().);
        }
    }
    

    /**
     * If ecloosure with such ID there is not in the zoo,
     * show Warning!
     * @param name of animal
     * @param id of enclosure
     */
    public void removeAnimalFromEnclosure(String name, int id) {
        try {
            enclosures.get(id).removeAnimal(name);
        } catch (EnclosureStoreException e) {
            System.out.println(e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("Нет такого вольера!"); //e.getCause().);
        }
    }

    /**
     * If animal into enclosure, then firstly he will be removed
     * from enclosure. And cost of the zoo will decrease.
     * @param name of animal
     */
    public void removeAnimal(String name) {
        if (animals.containsKey(name) && animals.get(name).enclosure != null) {
            removeAnimalFromEnclosure(name, animals.get(name).enclosure.getId());
        }
        cost -= animals.get(name).cost;
        animals.get(name).zoo = null;
        //сделать флаг или применить шаблон, чтобы позже нельзя было добавлять умерших животных
        System.out.println(animals.get(name).getClass().getSimpleName() + " по кличке " + name + " был удалён из зоопарка.");
        animals.remove(name);
    }

    /**
     * If there are animals, which does not there in enclosure,
     * they will be updating by zoo.
     * At the create Zoo, he automate sign to observer
     * to climate.
     * @param newClimate outClimate
     * @see Climate
     * @see ClimateObserver
     */
    @Override
    public void updateClimate(Climate.TypeClimate newClimate) {
        for (Map.Entry<String, Animal> a : animals.entrySet()) {
            if (a.getValue().enclosure == null) { //Если животное не в вольере
                a.getValue().updateClimate(Climate.getOutClimate());
            }
        }
    }

    /**
     * Adds a new employee. The income for the mounth will have fallen.
     * If such employee already working in the zoo,
     * will br throw exception.
     * @param inn
     * @param employee
     */
    public void addEmployee(int inn, Employee employee) {

        if (staff.containsKey(inn)) {//If such object already in pull
            throw new IllegalArgumentException("Сотрудник с ИНН=" + inn + " уже работает в зоопарке!");
        }
        System.out.println("В зоопарке " + name + " нанят новый сотрудник " + employee.getClass().getSimpleName() + ", ИНН: " + inn);
        staff.put(inn, employee);
    }

    /**
     * To dismiss the employee with INN=inn frow the zoo.
     * If such employee will not found, then wil throw exception.
     * @param inn
     */
    public void sackEmployee(int inn) {
        //profitForDay will changed...
        if (!staff.containsKey(inn)) {//If such employee not exist at staff
            //ЛУЧШЕ СИСТЕМ ОУТЫ
            throw new IllegalArgumentException("Сотрудник с ИНН=" + inn + " не работает в зоопарке!");
        }
        staff.remove(inn);
        System.out.println("Сотрудник с ИНН=" + inn + " был уволен.");
    }

    /**
     * Computes profit from zoo for day.
     * Uses Pattern Composite for collection profit.
     * Just before collection will be checking of the state of animals.
     * @return general profit form zoo and his parts(animals).
     * @see Profitable
     */
    @Override
    public float getProfit() {
//        return profitForDay*kProfit;
        float profit = 0;
        System.out.println("------" + "Зоопарк " + name + "------");

        zooLifeStyle.checkStateOfAnimals();

        for (Map.Entry<String, Animal> a : animals.entrySet()) {
            profit += a.getValue().getProfit();
        }

        System.out.println("Прибыль с животных: " + profit);
        System.out.println("За день доход с зоопарка составил " + profit);
        return profit;
    }

    /**
     * Computes loss from zoo for day.
     * Uses Pattern Composite for collection loss.
     * @return loss from all Losseale parts of the zoo
     * (employee's salaryes, care for animals, and supporting thermoregulation enclosures)
     */
    @Override
    public float getLoss() {
        float loss = 0;
        float lossEnclosure = 0;
        float lossAnimals = 0;
        float lossStaff = 0;
        System.out.println("------" + "Зоопарк " + name + "------");
        for (Integer number : enclosures.keySet()) {
            lossEnclosure += enclosures.get(number).getLoss();
            loss += enclosures.get(number).getLoss(); //А БУДЕТ ЛИ ВОЛЬЕР ПРИНОСИТЬ УБЫТКИ? ТОЛЬКО ЕСЛИ БУДЕМ ВЛКЮЧАТЬ В КОМПОНОВЩИК ВСЁ ПОДРЯД
        }

        for (String nickname : animals.keySet()) {
            lossAnimals += animals.get(nickname).getLoss();
            loss += animals.get(nickname).getLoss();
        }

        for (Integer inn : staff.keySet()) {
            lossStaff += staff.get(inn).getLoss();
            loss += staff.get(inn).getLoss();
        }

        System.out.println("Убыток с вольеров: "
                + lossEnclosure
                + "\nУбыток с животных: "
                + lossAnimals
                + "\nУбыток с сотрудников: "
                + lossStaff);
        System.out.println("За день убыток с зоопарка составил " + loss);
//        addDays(1); 
        return loss;
    }

    /**
     * Buy the zoo.
     *
     * @return
     */
    @Override
    public Buyable buy() {
        return this;
    }

    /**
     * Sells the zoo.
     * Before selling all animals and enclosures remove from observers of the Climate.
     * @return cost which including costs of all animal and enclosures.
     */
    @Override
    public float sell() {
        Climate.removeObserver(this);
        for (Map.Entry<Integer, Enclosure> e : enclosures.entrySet()) {
            Climate.removeObserver(e.getValue());
        }
        for (Map.Entry<String, Animal> a : animals.entrySet()) {
            Climate.removeObserver(a.getValue());
        }
        //а может и не надо отписывать? но тогда надо создавать экземпляр климата, а не статик
        owner = null;

        return cost;
    }

    /**
     * Print info about:
     * Employess, Enclosures, Animals. Also the amount of each kind of the Employee.
     */
    public void showAllInfo() {
        System.out.println("++++++++++++ Зоопарк " + name + " ++++++++++++");
        showEmployees();
        showEnclosures();
        showAnimals();
        System.out.println("Количество сотрудников типа: " + Guide.class.getSimpleName()
                + ": " + zooLifeStyle.countEmployee(Guide.class));
        System.out.println("Количество сотрудников типа: " + Vet.class.getSimpleName()
                + ": " + zooLifeStyle.countEmployee(Vet.class));
        System.out.println("Количество сотрудников типа: " + Keeper.class.getSimpleName()
                + ": " + zooLifeStyle.countEmployee(Keeper.class));
        System.out.println("++++++++++++++++++++++++++++++++++++++++++");
        //checking
    }

    public void showAnimals() {
        for (Map.Entry<String, Animal> a : animals.entrySet()) {
            System.out.println(a.getValue());
        }
    }

    public void showEmployees() {
        for (Map.Entry<Integer, Employee> e : staff.entrySet()) {
            System.out.println(e.getValue());
        }
    }

    public void showEnclosures() {
        for (Map.Entry<Integer, Enclosure> e : enclosures.entrySet()) {
            System.out.println(e.getValue());
        }
    }

    @Override
    public String toString() {
        return "{Зоопарк: " + name
                + "; Стоимость: " + cost
                + "; Владелец: " + (owner != null ? owner.getFirstname() + " " + owner.getLastname() : "Отсутствует")
                + "; Количество вольеров: " + enclosures.size()
                + "; Количество животных: " + animals.size()
                + "; Количество сотрудников: " + staff.size()+"}"; //+"isContact "+isContact
    }

}

//    public Zoo(String name){
//        zooLifeStyle = new ZooLifeStyle();
//        this.name = name;
//        cost = 10_000;
//        System.out.println("Создан Зоопарк: "+this);
//    }
//    public void addDays(double days){ //потом будет скорее всего private
//        //TO DO...
//        // Use the mediator between animals and threadVisitors, dateInstance, 
//        System.out.println("В зоопарке "+this.name+" прошло "+days+" дней...");
//    }
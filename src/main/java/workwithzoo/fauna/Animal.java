package workwithzoo.fauna;

import java.util.EnumSet;
import java.util.Random;
import workwithzoo.employee.Guide;
import workwithzoo.employee.Keeper;
import workwithzoo.employee.Vet;
import workwithzoo.sourceofprofit.Buyable;
import workwithzoo.sourceofprofit.Enclosure;
import workwithzoo.sourceofprofit.Losseable;
import workwithzoo.sourceofprofit.Profitable;
import workwithzoo.sourceofprofit.Zoo;

/**
 * Abstract class for any Animal.
 * Than more cost, and more exoitical climate likes, that more cost care and profit for day.
 * @author DantalioNxxi
 * @see Deathable
 * @see Profitable
 * @see Losseable
 * @see Buyable
 * @see Likes
 * @see Climate
 * @see ClimateObserver
 * @see Polar
 * @since 26.11.2017
 * @version 1.0.3
 */
public abstract class Animal implements Buyable, Profitable, Losseable, Deathable, ClimateObserver{ //Reproducable
    
    protected static final int MAX_AGGRESSION = 10;
    /**Using by setting starts aggression*/
    protected static Random random = new Random();
    
    public boolean isInsideZoo = false;
    
    public String nickname;
    /**If zoo is null, then animal did not bought.*/
    public Zoo zoo;
    /**If enlosure equal null, then animal does not brings profit.*/
    public Enclosure enclosure;
    
    public float cost;
    public float costCare;
    protected float defaultProfit;
    
    /**Than more aggression, then accidents are probability (it is perspective functional).*/
    protected int aggression;
    /**Than more wrong parameters (tired,fever,hungry,agony), that less profit from animal.*/
    private int tired=0;
    private int fever=0;
    private int hungry=0;
    private int agony=0;
    /**His a feeling by default is WELL only*/
    protected EnumSet<StateHealth> feeling = EnumSet.of(StateHealth.WELL); 
    
    /**
     * Enum of possible states of health of the Animal.
     * Include WELL, AGGRESSIVE, TIRED, HUNGRY, SICK, STARVE, DEAD.
     */
    protected static enum StateHealth{
        WELL(0, "полностью здоров", 0), AGGRESSIVE(2, "агрессивный", 4), TIRED(3, "уставший", 10), 
        HUNGRY(4,"голодный", 8), SICK(7, "болеет", 8), STARVE(9, "при смерти!", 4), DEAD(10, "мёртв!", 0);
        private final int priority;
        private final String message;
        /**max possible days in any state*/
        private final int maxCountOfDays; //max among of days at any state
        /**
         * @param priority for the searching of the states.
         * @param message of the state, when animal at him.
         * @param countOfDays max days in any state except WELL and DEAD states.
         */
        private StateHealth(int priority, String message, int countOfDays){
            this.priority = priority;
            this.message = message;
            this.maxCountOfDays = countOfDays;
        }
        /**Returns constant with priority.
         * @param priority pririty necessary for search any state in big quantity of states.
         * @return state with such priority.
         */
        public static StateHealth getPriorityOfValue(int priority){
            for (StateHealth s : StateHealth.values())
            {
                if (s.priority==priority) return s;
            }
            throw new IllegalArgumentException("Не существует константы с приоритетом "+priority);
        }
        
        public int getPriority(){return priority;}
        public String getMessage(){return message;}
    }
    
    /**
     * Constructor by default.
     * Any animal can have nickname.
     * @param nickname name of animal
     */
    protected Animal(String nickname){
        this.nickname = nickname;
    }
    
    /**
     * Get likesClimate from annotation Likes.
     * @return type of climate which animal likes 
     * @see Likes
     */
    public Climate.TypeClimate getLikesClimate(){
        Class<?> cl = getClass();
        Likes annotation = cl.getAnnotation(Likes.class);
        return annotation.likesClimate();
    }

    /**
     * Than more summ, then less profit animal is bringing.
     * Max value is equal 10.
     * @return summ of bad_states's points.
     */
    public int getSumFeeling(){
        int sumFeeling = 0;
        for(StateHealth s : feeling){
            sumFeeling+=s.getPriority();
        }
        if (sumFeeling>10) return 10;
        return sumFeeling;
    }
    
    /**
     * If the animal was died, then his states of feeling will contained only DEAD state.
     * @return true, if animal is die.
     */
    public boolean isDead(){
        return feeling.contains(StateHealth.DEAD);
    }
    
    /**
     * Show message of the StateHealth, which was increased.
     * @param ch increased or appear State
     */
    private void showIncreaseState(StateHealth ch){
        System.out.println(getClass().getSimpleName() + " по кличке "
                                + nickname
                                + " "+ch.message);
    }
    
    /**
     * Show message of the StateHealth, which was disappeared.
     * Usually it is a wrong State.
     * @param ch disappeared StateHealth.
     */
    private void showDecreaseState(StateHealth ch){
        System.out.println(getClass().getSimpleName() + " по кличке "
                                + nickname
                                + " больше не "+ch.message);
    }
    
    /**
     * Increases StateHealth of the animal.
     * Some states can to move to next bad states.
     * For examples State SICK can to move to State STARVE.
     * @param ch statehealth, who have to will increased.
     * @param count adding points for this state
     */
    private void increaseState(StateHealth ch, int count){
            switch(ch){
                case AGGRESSIVE:{
                    if (aggression==0){
                        aggression+=count;
                        showIncreaseState(ch);
                    } else if (aggression < MAX_AGGRESSION) {
                        aggression += count;
                        showIncreaseState(ch);
                    } else {
                        feeling.add(ch);
                        showIncreaseState(ch);
                    }
                    break;
                }
                case TIRED:{
                    if (tired==0){
                        tired+=count;
                        feeling.add(ch);
                        showIncreaseState(ch);
                    } else if (tired < ch.maxCountOfDays) {
                        tired += count;
                        showIncreaseState(ch);
                    } else showIncreaseState(ch);
                    break;
                }
                case HUNGRY:{
                    if (hungry==0){
                        hungry+=count;
                        feeling.remove(StateHealth.WELL);
                        feeling.add(ch);
                        showIncreaseState(ch);
                    } else if (hungry < ch.maxCountOfDays) {
                        hungry += count;
                        showIncreaseState(ch);
                    } else increaseState(StateHealth.STARVE, count);
                    break;
                }
                case SICK:
                    if (fever==0){
                        fever+=count;
                        feeling.remove(StateHealth.WELL);
                        feeling.add(ch);
                        showIncreaseState(ch);
                    } else if (fever < ch.maxCountOfDays) {
                        fever += count;
                        showIncreaseState(ch);
                    } else increaseState(StateHealth.STARVE, count);
                    break;
                case STARVE:{
                     if (feeling.add(StateHealth.STARVE)) {
                        agony += count;
                        showIncreaseState(StateHealth.STARVE);
                    } else if (agony < StateHealth.STARVE.maxCountOfDays) {
                        agony += count;
                        showIncreaseState(StateHealth.STARVE);
                    } else {
                        die();
                    }
                    break;
                }
                default:break;
            }
    }
        
    /**
     * Decreases or to disappear StateHealth of the animal.
     * Some states can to disappeared.
     * For examples State SICK can to disappear.
     * @param ch statehealth, who have to will increased.
     * @param count adding points for this state
     */
    private void decreaseState(StateHealth ch, int count){
        if (feeling.contains(ch)){
            switch(ch){
                case AGGRESSIVE:{
                    if (aggression<=count){
                        aggression=0;
//                        feeling.remove(ch);
                    } else if (aggression==MAX_AGGRESSION){
                            aggression-=count;
                            showDecreaseState(ch);
//                        System.out.println(getClass().getSimpleName() + " по кличке "+ nickname+ " становится спокойнее");
                    } else {
                        aggression-=count;
                        System.out.println(getClass().getSimpleName() + " по кличке "+ nickname+ " становится спокойнее");
                    }
                    break;
                }
                case TIRED:{
                    if (tired<=count){
                        tired=0;
                        feeling.remove(ch);
                        showDecreaseState(ch);
                    } else{
                        tired-=count;
                        System.out.println(getClass().getSimpleName() + " по кличке "+ nickname+ " восполняет энергию");
                    }
                    break;
                }
                case HUNGRY:{
                    if (agony>0) decreaseState(StateHealth.STARVE, count);
                    if (hungry<=count){
                        hungry=0;
                        feeling.remove(ch);
                        showDecreaseState(ch);
                        if(!feeling.contains(StateHealth.SICK)) {
                            feeling.add(StateHealth.WELL);
                            System.out.println(getClass().getSimpleName() + " по кличке "+ nickname+ " выздоровел!");
                        }
                    } else{
                        hungry-=count;
                        System.out.println(getClass().getSimpleName() + " по кличке "+ nickname+ " поправляется");
                    }
                    break;
                }
                case SICK:{
                    if (agony>0) decreaseState(StateHealth.STARVE, count);
                    if (fever<=count){
                        fever=0;
                        feeling.remove(ch);
                        showDecreaseState(ch);
                        if(!feeling.contains(StateHealth.HUNGRY)) {
                            feeling.add(StateHealth.WELL);
                            System.out.println(getClass().getSimpleName() + " по кличке "+ nickname+ " выздоровел!");
                        }
                    } else{
                        fever-=count;
                        System.out.println(getClass().getSimpleName() + " по кличке "+ nickname+ " идёт на поправку");
                    }
                    break;
                }
                case STARVE:{
                    if (agony<=count){
                        agony=0;
                        feeling.remove(ch);
                        showDecreaseState(ch);
                    } else {
                        agony-=count;
                        System.out.println(getClass().getSimpleName() + " по кличке "+ nickname+ " стало немного лучше");
                    }
                    break;
                }
                default:break;
            }
        }
    }
    
    /**
     * Possible template method for checking states of any liveable entity.
     */
    public void checkStateOfAnimal(){
        if (!isDead()) chechCare(); //firstly checking eating/hungry
        if (!isDead()) checkClimate();//climate
        if (!isDead()) checkTherapy();//cure
        if (!isDead()) checkGuide();//tired and aggression
//            checkDislike();//only aggression
//        System.out.println("fever = "+fever);
//        System.out.println("agony = "+agony);
//        System.out.println("hungry = "+hungry);
//        System.out.println("aggression = "+aggression);
    }
    
    /**
     * If in zoo not enough are Guides, then animal will be tired,
     * because intensivity of the people will be grow. 
     */
    public void checkGuide(){
        if (isInsideZoo && enclosure!=null){
            double q = zoo.zooLifeStyle.degreeOfBadServe(Guide.class);
            if (q==0) {
                System.out.println("Гидов достаточно");
                if (feeling.contains(StateHealth.TIRED)) decreaseState(StateHealth.TIRED, 2);
//                else decreaseState(StateHealth.AGGRESSIVE, 1);
            } else if(q<=5){
                System.out.println("Гидов больше среднего");
                if (feeling.contains(StateHealth.TIRED)) decreaseState(StateHealth.TIRED, 1);
//                else decreaseState(StateHealth.AGGRESSIVE, 1);
            } else if (q==10){
                System.out.println("Нет гидов. Животное не успевает отдыхать.");
                increaseState(StateHealth.TIRED, 2);
            } else if (q>5){
                System.out.println("Гидов меньше среднего");
//                if (feeling.contains(StateHealth.TIRED)) decreaseState(StateHealth.TIRED, 1);
            } 
        } else {System.out.println("Животное не устаёт.");}
    }
    
    /**
     * If in zoo not enough Vets, then nobody can to cure animals.
     */
    public void checkTherapy(){
        if (isInsideZoo){
            double qTher = zoo.zooLifeStyle.degreeOfBadServe(Vet.class);
            if (qTher==0) {
                System.out.println("Ветеринаров достаточно");
                if (feeling.contains(StateHealth.STARVE)) decreaseState(StateHealth.STARVE, 2);
                else decreaseState(StateHealth.SICK, 2);
            } else if(qTher<=5){
                System.out.println("Ветеринаров больше среднего");
                if (feeling.contains(StateHealth.STARVE)) decreaseState(StateHealth.STARVE, 1);
                else decreaseState(StateHealth.SICK, 1);
            } else if (qTher==10){
                System.out.println("Нет ветеринаров. Некому лечить.");
            } else if (qTher>5){
                System.out.println("Ветеринаров меньше среднего");
                if (feeling.contains(StateHealth.STARVE)) decreaseState(StateHealth.STARVE, 1);
            }
        } else {System.out.println("Животное некому лечить.");}
    }

    /**
     * If in zoo not enough are keepers, then animals will be HUNGRY.
     * Nobody can to care for animals.
     */
    public void chechCare(){
        if (isInsideZoo){
            double qNCare = zoo.zooLifeStyle.degreeOfBadServe(Keeper.class);
            if (qNCare==0) {
                System.out.println("Киперов достаточно");
                decreaseState(StateHealth.HUNGRY, 2);
            } else if(qNCare<=5){
                System.out.println("Киперов меньше среднего");
                decreaseState(StateHealth.HUNGRY, 1);
            } else if (qNCare==10){
                System.out.println("Животное некому кормить. Наймите кипера.");
                increaseState(StateHealth.HUNGRY, 2);
            } else if (qNCare>5){
                System.out.println("Киперов больше среднего");
                increaseState(StateHealth.HUNGRY, 1);
            }
        } else {System.out.println("Животное "+nickname+"некому кормить. Поместите его в зоопарк.");}
    }
    
    /**
     * Than animal does not likes outClimate more, then he more feels bad.
     * For example: Animal likes POLAR climate, outClimate equal TROPICAL climate,
     * then animal's state health SICK will be increase on 2 point.
     * And if outClimate is equal MIDDLE climate, then SICK increase on 1 poing.
     * However if in zoo were are anough vets and keepers, then they can to decrease 1 point bad state.
     */
    public void checkClimate(){
        Climate.TypeClimate out = (enclosure!=null?enclosure.getInnerClimate():Climate.getOutClimate());
            switch(getLikesClimate()){
                case POLAR_CLIMATE:{//animal likes cold weather
                    switch(out){
                        case MIDDLE_CLIMATE:{
                            System.out.println(getClass().getSimpleName()+" по кличке "+nickname+" жарко.");
                            //if bouth server do not equal zero:
                            if (!(zoo.zooLifeStyle.degreeOfBadServe(Vet.class)==0 &&
                                    zoo.zooLifeStyle.degreeOfBadServe(Keeper.class)==0)){
                                //If in zoo were are anough vets and keepers, then they can to safe actually statehealth.
                                increaseState(StateHealth.SICK, 1);
                            }
                            break;
                        }
                        case TROPICAL_CLIMATE:{
                            System.out.println(getClass().getSimpleName()+" по кличке "+nickname+" очень жарко.");
                            if ((zoo.zooLifeStyle.degreeOfBadServe(Vet.class)==0 &&
                                    zoo.zooLifeStyle.degreeOfBadServe(Keeper.class)==0)){
                                increaseState(StateHealth.SICK, 1);
                            } else increaseState(StateHealth.SICK, 2);
                            break;
                        }
                        default:{
                            if (feeling.contains(StateHealth.STARVE)) decreaseState(StateHealth.STARVE, 1);
                            else decreaseState(StateHealth.SICK, 1);
                            break;
                        }
                    }break;}
                    
                case MIDDLE_CLIMATE:{//animal likes middle climate
                    switch(out){
                        case POLAR_CLIMATE:{
                            System.out.println(getClass().getSimpleName()+" по кличке "+nickname+" холодно.");
                            //If in zoo were are anough vets and keepers, then they can to safe actually statehealth.
                            if (!(zoo.zooLifeStyle.degreeOfBadServe(Vet.class)==0 &&
                                    zoo.zooLifeStyle.degreeOfBadServe(Keeper.class)==0)){
                                increaseState(StateHealth.SICK, 1);
                            }
                            break;
                        }
                        case TROPICAL_CLIMATE:{
                            System.out.println(getClass().getSimpleName()+" по кличке "+nickname+" жарко.");
                            if (!(zoo.zooLifeStyle.degreeOfBadServe(Vet.class)==0 &&
                                    zoo.zooLifeStyle.degreeOfBadServe(Keeper.class)==0)){
                                //If in zoo were are anough vets and keepers, then they can to safe actually statehealth.
                                increaseState(StateHealth.SICK, 1);
                            }
                            break;
                        }
                        default:{
                            if (feeling.contains(StateHealth.STARVE)) decreaseState(StateHealth.STARVE, 1);
                            else decreaseState(StateHealth.SICK, 2);
                            break;
                        }
                    }break;}
                    
                case TROPICAL_CLIMATE:{//animal likes warm weather
                    switch(out){
                        case MIDDLE_CLIMATE:{
                            System.out.println(getClass().getSimpleName()+" по кличке "+nickname+" холодно.");
                            if (!(zoo.zooLifeStyle.degreeOfBadServe(Vet.class)==0 &&
                                    zoo.zooLifeStyle.degreeOfBadServe(Keeper.class)==0)){
                                //If in zoo were are anough vets and keepers, then they can to safe actually statehealth.
                                increaseState(StateHealth.SICK, 1);
                            }
                            break;
                        }
                        case POLAR_CLIMATE:{
                            System.out.println(getClass().getSimpleName()+" по кличке "+nickname+" очень холодно.");
                            if ((zoo.zooLifeStyle.degreeOfBadServe(Vet.class)==0 &&
                                    zoo.zooLifeStyle.degreeOfBadServe(Keeper.class)==0)){
                                increaseState(StateHealth.SICK, 1);
                            } else increaseState(StateHealth.SICK, 2);
                            break;
                        }
                        default:{
                            if (feeling.contains(StateHealth.STARVE)) decreaseState(StateHealth.STARVE, 1);
                            else decreaseState(StateHealth.SICK, 1);
                            break;
                        }
                    }break;}
                
                default: break;
            }
    }
    
    /**
     * If animal not into enclosure, when he gets outClimate from zoo.
     * Else from enclosure.
     * @param newClimate newOut climate.
     */
    @Override
    public void updateClimate(Climate.TypeClimate newClimate) {
        if (newClimate!=getLikesClimate()) checkClimate();
    }
    
    /**
     * When the animal is dieing, his statehealth will have containg only DEAD state.
     */
    @Override
    public void die() {
        System.out.println(getClass().getSimpleName() + " по кличке "+ nickname+" умер.");
        cost=costCare=defaultProfit=aggression=tired=fever=hungry=agony=0; //steel it is made for the new buy
        feeling = EnumSet.of(StateHealth.DEAD);
    }
    
    /**
     * If the animal feeld bad, then profit will be less.
     * @return profit for day from animal.
     * @see Profitable
     */
    @Override
    public float getProfit() {
        float profit = defaultProfit;
        if (enclosure!=null){
            profit*=(10 - getSumFeeling())/10.0; //is compiting profit
        } else {
            profit = 0;
            System.out.println(getClass().getSimpleName()+" по кличке "+nickname+" ещё не в вольере!");
        }
        System.out.println("profit с "+nickname+": "+profit);
        return profit;
    }
    
    /**
     * If in zoo not exits Keepers, then animal does not can eat and soon can to die.
     * Though, if the animal does not eat, loss = 0.
     * @see Losseable
     * @return 
     */
    @Override
    public float getLoss() {
        float care = (float)(costCare*((10 - zoo.zooLifeStyle.degreeOfBadServe(Keeper.class))/10.0));//If nobody is fooding
//        System.out.println("За этот день "+nickname+" обошёлся в "+care);
        return care;
    }
    
    /**
     * @see Buyable
     * @return 
     */
    @Override
    public float sell() {
        //...Удаление из всех наблюдаемых объектов, из пулов
        //Возврат стоимости животного.
        zoo = null;
        isInsideZoo = false;
        return cost;
    }

    /**
     * @see Buyable
     * @return 
     */
    @Override
    public Animal buy() {
        isInsideZoo = true;
        return this;
    }

    @Override
    public String toString() {
        return "Animal{" + "Кличка: " + nickname
                + ", Вольер: " +(enclosure!=null?enclosure.getId():"Не помещён")
                + ", Стоимость="+ cost
                + ", Стоимость содержания="+ costCare
                + ", Доход по умолчанию="+ defaultProfit
                + ", Агрессивность=" + aggression
                + ", Состояние здоровья: " + feeling + '}';
    }
    
    @Deprecated
    private void setAggression(int aggr){
        if (aggr<=0) {
            aggression = 0;
        } else if (aggr>MAX_AGGRESSION) {
            aggression=MAX_AGGRESSION;
            //TO DO..
        } else aggression = aggr;
    }
    
    @Deprecated
    private boolean checkFeeling(int priority){
        for (StateHealth s : feeling){
            if (s.getPriority()==priority){
                return true;
            }
        } return false;
    }
    
}

//    public void checkDislike(){
//        System.out.println("ПРОВЕРКА AGGRESSION");
//        if (isInsideZoo && enclosure!=null){
//            Class mySuperClass = getClass().getSuperclass();
//            System.out.println("У "+nickname+" супер-класс: "+mySuperClass.getSimpleName());
//            if (feeling.contains(StateHealth.AGGRESSIVE)) //и не middle
//                enclosure.innerMessage(mySuperClass,mySuperClass==Polar.class?Tropical.class:Polar.class);
//        }
//    }

//    private void increaseAggression(){
//        if (aggression==MAX_AGGRESSION) {
//            aggression=MAX_AGGRESSION;
    //to do
//        } else aggression++;
//    }
//    private void decreaseAggression(){
//        if (aggression == 0) {
    //to do
//        } else aggression--;
//    }
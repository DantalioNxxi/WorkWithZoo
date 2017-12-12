
package mytests;

import org.junit.Test;
import workwithzoo.sourceofprofit.*;
import workwithzoo.user.*;
import workwithzoo.fauna.*;
import workwithzoo.employee.*;

/**
 * Simple Test For Work With Zoo
 * @author DantalioNxxi
 */
public class Test1 {
    
    public Test1() {
    }
    
     @Test
     public void hello() throws Exception{
         System.out.println("Test - Hello!");
         Businessman b1 = new Businessman(200_000, 1, "Alexander", "Strokov");
         System.out.println(b1.toString());
         Enclosure e1 = new Enclosure(101, 5);
         e1.setThermoregulation();
         Zoo z1 = new Zoo(b1, "BigZoo");
         b1.buyAgency("BigZoo", z1);
         z1.addEnclosure(101, e1);
         
         PolarBear bear1 = new PolarBear("Снежок");
         z1.addAnimal(bear1);
         b1.addDays(1);
         z1.addAnimalIntoEnclosure("Снежок", 101);
         
         e1.setClimate(Climate.TypeClimate.MIDDLE_CLIMATE);
         b1.addDays(1);
         
         Lion l1 = new Lion("Лео");
         z1.addAnimal(l1);
         z1.addAnimalIntoEnclosure("Лео", 101);
         b1.addDays(2);
         e1.setClimate(Climate.TypeClimate.TROPICAL_CLIMATE);
         b1.addDays(1);
         
         
         System.out.println(z1);
         System.out.println(bear1);
         System.out.println(l1);
         
         Guide g1 = new Guide(11, 3); 
         z1.addEmployee(11, g1);
         System.out.println(g1);
         System.out.println(z1);
         
         Vet v1 = new Vet(22, 1);
         z1.addEmployee(22, v1);
         z1.showAllInfo();
         
         Keeper k1 = new Keeper(33, 2);
         z1.addEmployee(33, k1);
         b1.addDays(2);
         z1.showAllInfo();
         
         b1.addDays(3);
         z1.showAllInfo();
         
         Enclosure e2 = new Enclosure(102, 2);
         z1.addEnclosure(102, e2);
         z1.removeAnimalFromEnclosure("Снежок", 101);
         z1.addAnimalIntoEnclosure("Снежок", 102);
         b1.addDays(4);
         z1.showAnimals();
         
         Vet v2 = new Vet(222, 0);
         z1.sackEmployee(22);
         z1.addEmployee(222, v2);
         b1.addDays(2);
         
         Lion l2 = new Lion("Лёва");
         z1.addAnimal(l2);
         z1.addAnimalIntoEnclosure("Лёва", 101);
         b1.addDays(2);
         z1.showEnclosures();
         
         b1.addDays(100);
         
         AmurTiger t1 = new AmurTiger("Тимоха");
         System.out.println(b1);
         z1.addAnimal(t1);
         z1.addAnimalIntoEnclosure("Тимоха", 102);
         
         b1.addDays(3);
         z1.addEmployee(22, v1);
         b1.addDays(1);
         e1.setClimate("MIDDLE_CLIMATE");
         z1.showAnimals();
         
         e1.offClimate();
         z1.showEnclosures();
         b1.addDays(4); 
         z1.showAnimals();
         z1.createNewEnlosure(10);
         z1.showEnclosures();
         
         
         
         //============
//         b1.addDays(1);
//         System.out.println(b1.toString());
//         System.out.println(z1.toString());
//         b1.addDays(18);
//         b1.addDays(30);
//         b1.addDays(30);
//         b1.addDays(92);
//         
//         b1.addDays(2);
//         z1.addAnimal(bear1);
//         b1.addDays(1);
//         z1.addAnimalIntoEnclosure("Снежок", 101);
//         b1.addDays(1);
//         e1.setClimate(Climate.TypeClimate.POLAR_CLIMATE);
//         b1.addDays(1);
//         b1.addDays(1);
//         Climate.notifyObservers();
         
     }
}

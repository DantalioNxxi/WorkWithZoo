
//Токен для TT_WORD = -3
//Токен для TT_NUMBER = -2
//Токен для EOL = 10
//Токен для EOF = -1
//44 ,
//40 (
//41 )
//61 =
//10 - перевод строки
//35 #
import org.junit.Test;
//import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.StreamTokenizer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
//import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
//import workwithzoo.sourceofprofit.Enclosure;

/**
 * @version 1.0.1
 * @author VYZH
 * @author DantalioNxxi
 * @since 28.11.2017
 */
public class TestScenario {

    @Test
    public void run() throws Exception {
        new TestScenario().scenario();
    }

    private void scenario() throws IOException, ClassNotFoundException,
            NullPointerException, NoSuchMethodException {

        //Map of the our classes
        Map<String, String> classes = new HashMap<>();
        classes.put("Zoo", "workwithzoo.sourceofprofit.Zoo");
        classes.put("User", "workwithzoo.user.User");
        classes.put("Enclosure", "workwithzoo.sourceofprofit.Enclosure"); // add Enclosure

        //Map of the our variable-Objects
        Map<String, Object> variables = new HashMap<>();

        try {
            InputStream in = getClass().getResourceAsStream("ui_commands.txt"); //change way
            StreamTokenizer st = new StreamTokenizer(in);
            st.commentChar('#');
            st.wordChars('$', '$');

            String previousWord = null; //Previous token

            //For to fill the methods
            int qParForMethod = 0; //количество параметров для подсчёта параметров метода
            ArrayList paramForMethod = new ArrayList(); //сами параметры убрали в них <Object>
            boolean fillMethod = false; //флаг заполнения параметров
            StringBuilder tempOwn = new StringBuilder(); //Переменная-ключ, для которой будет вызван метод
            Method tempMethod = this.getClass().getMethod("run"); //метод, для которого собираем параметры (изначально заглушка)
            Object tempObject = new Object(); //объект, для которого вызываем

            //For to fill the constructors
            int qParForCtor = 0; //количество параметров для подсчёта параметров конструктора
            ArrayList paramForCtor = new ArrayList<>(); //сами параметры
            boolean fillCtor = false; //флаг заполнения параметров
            Constructor tempCtor = this.getClass().getConstructor(); //конструктор, для которого собираем параметры (изначально заглушка)
            StringBuilder tempVar = new StringBuilder(); //Переменная-ключ будущего объекта
            StringBuilder tempClass = new StringBuilder(); //Класс будущего объекта

            while (StreamTokenizer.TT_EOF != st.nextToken()) {
                switch (st.ttype) {

                    case StreamTokenizer.TT_WORD: //If this token is the word

                        if (previousWord != null) {
                            switch (previousWord) {

                                case "let": //If previous word was "let"
                                    variables.put(st.sval, null); //add variable-key for future a new Object
                                    tempVar.append(st.sval);
//                                    fillCtor = true;
                                    break;

                                case "new": //If previous word was "new"
                                    String classname = classes.get(st.sval); //search the class for such word
                                    Class<?> cl = Class.forName(classname);

                                    if (fillMethod) { //If this "new Parameter"
                                        System.out.println("Предыдущее new, но мы заполняем метод.");
                                        paramForMethod.add(st.sval); // НА БУДУЩЕЕ ДЛЯ ОБРАБОТКИ ПАРАМЕТРА NEW
                                        qParForMethod--;
                                        System.out.println("param.add(st.sval)");
                                    } else { //TO DO...Execute constructor: (but not inner Ctor and not Ctor without parameters)
                                        tempClass.append(classname);
                                        //Search this contructor. By to take first constructor this parameters
                                        Constructor[] ctors = cl.getConstructors();
                                        for (Constructor ctor : ctors) {
                                            Class[] parameters = ctor.getParameterTypes(); // take the first constr. still unique
                                            Constructor с1 = cl.getDeclaredConstructor(parameters); // get the constructor

                                            if (с1.getParameterCount() != 0) { //We will to fill params
                                                qParForCtor = parameters.length;
                                                tempCtor = ctor;
                                                fillCtor = true;
                                            }
                                        }
                                    }
                                    break;

                                default:
                                    if (st.sval.charAt(0) == '$' && !st.sval.contains(".")) {// If we had meet the variable into constructor or method
                                        if (fillCtor) {
                                            //Находим объект из коллекции по названию переменной
                                            //и кладём в коллекцию параметров конструктора
                                            paramForCtor.add(variables.get(st.sval.substring(1))); //В будущем здесь будет генерироваться-ошибка написания сценария.
                                            qParForCtor--;
                                        }
                                        if (fillMethod) {
                                            //Находим объект из коллекции по названию переменной
                                            //и кладём в коллекцию параметров метода
                                            paramForMethod.add(variables.get(st.sval.substring(1)));
                                            qParForMethod--;
                                        }

                                    } else { // If this token a simple string:
                                        if (fillCtor) { // If we fill the Constructor
                                            paramForCtor.add(st.sval);
                                            qParForCtor--;
                                        }

                                        if (fillMethod) { // If we fill the Method
                                            paramForMethod.add(st.sval);
                                            qParForMethod--;
                                        }
                                    }
                                    break;
                            }
                        }
                        if (st.sval.charAt(0) == '$' && st.sval.contains(".")) { //If we had meet the method
                            int dot = st.sval.indexOf("."); // Indeks of a dot after the name of variable Object
                            String nameOfMethod = new StringBuilder(st.sval).substring(dot).substring(1); //get SimpleName of the Method
                            String methodOwn = st.sval.substring(1, dot); //variable of Object for this method
                            Class<?> clOwner = Class.forName(variables.get(methodOwn).getClass().getName()); //get the class if this variable Object
                            //Search this method in class clOwner:
                            Method[] meths = clOwner.getMethods();
                            for (Method method : meths) {
                                if (method.getName().equals(nameOfMethod)) { // Нашли нужный метод
                                    Class[] parameters = method.getParameterTypes();

                                    if (method.getParameterCount() != 0) { //We will to fill params
                                        qParForMethod = parameters.length;
//                                                    tempObject = variables.get(methodOwn);
                                    }
                                    tempOwn.append(methodOwn);
                                    tempObject = variables.get(methodOwn); //перенёс из if
                                    tempMethod = method;
                                    fillMethod = true; // Их надо будет разбить на ситуации, когда есть параметры, и когда их нет
                                }
                            }
                        }
                        previousWord = st.sval;
                        break;

                    case StreamTokenizer.TT_NUMBER: //If this token is the number
                        if (previousWord != null && st.nval != 44) { //44 is ","
                            if (fillCtor) { // If we fill the Constructor
                                paramForCtor.add(st.nval); //временно к инт
                                qParForCtor--;
                            }

                            if (fillMethod) { // If we fill the Method
                                paramForMethod.add(st.nval);
                                qParForMethod--;
                            }
                        }
                        previousWord = Double.toString(st.nval); //so at next iteration the previous word will not be is equals the null!
                        break;

                    case StreamTokenizer.TT_EOL: //If this token is the end of the line
                        previousWord = null;
                        break;

                    default: //If this token is anothe ttype
                        previousWord = null;
                        break;
                }

                if (fillCtor) {
                    if (StreamTokenizer.TT_EOL == st.nextToken() || qParForCtor == 0) { //We have revieved all parameters
//                        System.out.println(paramForCtor);
                        variables.putIfAbsent(tempVar.toString(), tempCtor.newInstance(paramForCtor.toArray()));

                        System.out.println("# created"
                                + " $" + tempVar.toString()
                                + " is a new "
                                + tempCtor.getDeclaringClass().getSimpleName()
                                + " = "
                                + variables.get(tempVar.toString()).toString());

                        // clear flags and temporary strings
                        fillCtor = false;
                        tempVar.delete(0, tempVar.length());
                        tempClass.delete(0, tempClass.length());// may be yet to change tempClass?
                        paramForCtor.clear();
                    }
                }

                if (fillMethod) {
                    if (StreamTokenizer.TT_EOL == st.nextToken() || qParForMethod == 0) { //We have revieved all parameters
//                        System.out.println(paramForMethod);
                        tempMethod.invoke(tempObject, paramForMethod.toArray());
                        System.out.println("# completed "
                                + tempMethod.getName()
                                + " ("
                                + paramForMethod
                                + ") "
                                + "for $"
                                + tempOwn
                                + " = "
                                + tempObject.toString());
                        //Clear temporary elements:
//                        tempMethod=null;
//                        tempObject=null;
                        tempOwn.delete(0, tempOwn.length());
                        fillMethod = false;
                        paramForMethod.clear();
                    }
                }
            } //The end of while
            
            in.close();
            System.out.println("\nВыведем наши переменные:");
            for (String key : variables.keySet()) {
                System.out.println("key = " + key + ", Object = " + variables.get(key));
            }
            
        } catch (Exception e) {
            System.out.println("Сработало исключение! " + e.toString() + " " + e.getMessage());
            return;
        }
    } // The end of method.

} // The end of class.

//=========
//LAST VERSION:
//public class TestScenario{
//    
//    @Test
//    public void run() throws Exception {
//        new TestScenario().scenario();
//    }
//
//    private void scenario() throws IOException, ClassNotFoundException,
//            NullPointerException, NoSuchMethodException {
//
//        //Map of the our classes
//        Map<String, String> classes = new HashMap<>();
//        classes.put("Zoo", "workwithzoo.sourceofprofit.Zoo");
//        classes.put("User", "workwithzoo.user.User");
//        classes.put("Enclosure", "workwithzoo.sourceofprofit.Enclosure"); // add Enclosure
//        
//        //Map of the our variable-Objects
//        Map<String, Object> variables = new HashMap<>();
//        
//        try{
//            InputStream in = getClass().getResourceAsStream("ui_commands.txt"); //change way
//            StreamTokenizer st = new StreamTokenizer(in);
//            st.commentChar('#');
//            st.wordChars('$', '$');
//            
//            String previousWord = null; //Previous token
//            
//            //For to fill the methods
//            int qParForMethod = 0; //количество параметров для подсчёта параметров метода
//            ArrayList paramForMethod = new ArrayList(); //сами параметры убрали в них <Object>
//            boolean fillMethod = false; //флаг заполнения параметров
//            Method tempMethod = this.getClass().getMethod("run"); //метод, для которого собираем параметры (изначально заглушка)
//            Object tempObject = new Object(); //объект, для которого вызываем
//            
//            //For to fill the constructors
//            int qParForCtor = 0; //количество параметров для подсчёта параметров конструктора
//            ArrayList paramForCtor = new ArrayList<>(); //сами параметры
//            boolean fillCtor = false; //флаг заполнения параметров
//            Constructor tempCtor = this.getClass().getConstructor(); //конструктор, для которого собираем параметры (изначально заглушка)
//            StringBuilder tempVar = new StringBuilder(); //Переменная-ключ будущего объекта
//            StringBuilder tempClass = new StringBuilder(); //Класс будущего объекта
//            
//            while (StreamTokenizer.TT_EOF != st.nextToken()) {
//                switch (st.ttype) {
//                    
//                    case StreamTokenizer.TT_WORD:
//                        System.out.println("\nЭТО СЛОВО TT_WORD = "+st.sval);
//                        
//                        if (previousWord != null) {
//                            switch (previousWord) {
//                                
//                                case "let" : //If previous word was "let"
//                                    System.out.println("Добавили переменную-ключ "+st.sval+" в variables");
//                                    variables.put(st.sval, null);
//                                    tempVar.append(st.sval);
////                                    fillCtor = true;
//                                    break;
//                                    
//                                case "new" : //If previous word was "new"
//                                    String classname = classes.get(st.sval); //search the class for such word
//                                    Class<?> cl = Class.forName(classname);
//                                    
//                                    if (fillMethod) {
//                                        System.out.println("Предыдущее new, но мы заполняем метод.");
//                                        paramForMethod.add(st.sval); // НА БУДУЩЕЕ ДЛЯ ОБРАБОТКИ ПАРАМЕТРА NEW
//                                        qParForMethod--;
//                                        System.out.println("param.add(st.sval)");
//                                    } else { //TO DO...Execute constructor: (but not inner Ctor and not Ctor without parameters)
//                                        tempClass.append(classname);
//                                        //Search this contructor. By to take first constructor this parameters
//                                        Constructor[] ctors = cl.getConstructors();
//                                        for (Constructor ctor : ctors){
//                                                Class[] parameters = ctor.getParameterTypes(); // take the first constr. still unique
//                                                Constructor с1 = cl.getDeclaredConstructor(parameters); // get the constructor
//                                                
//                                                if (с1.getParameterCount()!=0){
//                                                    System.out.println("try fill param:");
//                                                    qParForCtor = parameters.length;
//                                                    System.out.println("qParamForCtor= "+qParForCtor);
//                                                    tempCtor = ctor;
//                                                    fillCtor = true;
//                                                }
//                                        }
//                                        System.out.println("tempCtor = "+tempCtor.getName());
//                                    }
//                                    break;
//
//                                default :
//                                    if (st.sval.charAt(0)=='$' && !st.sval.contains(".")) {// If we had meet the variable into constructor or method
//                                        if (fillCtor) {
//                                            //Находим объект из коллекции по названию переменной
//                                            //и кладём в коллекцию параметров конструктора
//                                            System.out.println("Будем добавлять эту переменную в конструктор");
//                                            paramForCtor.add(variables.get(st.sval.substring(1))); //В будущем здесь будет исключение-ошибка написания сценария.
//                                            qParForCtor--;
//                                        }
//                                        if (fillMethod) {
//                                            //Находим объект из коллекции по названию переменной
//                                            //и кладём в коллекцию параметров метода
//                                            System.out.println("Будем добавлять эту переменную в метод");
//                                            paramForMethod.add(variables.get(st.sval.substring(1)));
//                                            qParForMethod--;
//                                        }
//                                        
//                                    } else { // Если это просто строка:
//                                        System.out.println("Это просто строка st.sval = "+st.sval);
//                                        if (fillCtor) {
//                                            System.out.println("Будем добавлять эту строку в конструктор");
//                                            paramForCtor.add(st.sval);
//                                            qParForCtor--;
//                                        }
//
//                                        if (fillMethod) {
//                                            System.out.println("Будем добавлять эту строку в метод");
//                                            paramForMethod.add(st.sval);
//                                            qParForMethod--;
//                                        }
//                                    }
//                                    break;
//                            }
//                        }
//                                    if (st.sval.charAt(0)=='$' && st.sval.contains(".")) { //If we had meet the method
//                                        int dot = st.sval.indexOf("."); // Indeks of a dot after the name of variable Object
//                                        String nameOfMethod =new StringBuilder(st.sval).substring(dot).substring(1); //get SimpleName of the Method
//                                        String methodOwn = st.sval.substring(1, dot); //variable of Object for this method
//                                        Class<?> clOwner = Class.forName(variables.get(methodOwn).getClass().getName()); //get the class if this variable Object
//                                        //Search this method in class clOwner:
//                                        Method[] meths = clOwner.getMethods();
//                                        for (Method method : meths){
//                                            if (method.getName().equals(nameOfMethod)){ // Нашли нужный метод
//                                                Class[] parameters = method.getParameterTypes();
//                                                
//                                                if (method.getParameterCount()!=0){
//                                                    System.out.println("try fill param:");
//                                                    qParForMethod = parameters.length;
//                                                    System.out.println("qParam= "+qParForMethod);
////                                                    tempObject = variables.get(methodOwn);
//                                                }
//                                                tempObject = variables.get(methodOwn); //перенёс из if
//                                                tempMethod = method;
//                                                fillMethod = true;
//                                            }
//                                        }
//                                    } else {
//                                    }
//                        System.out.println("В TT_WORD: previous = "+previousWord);
//                        previousWord = st.sval;
//                        break;
//                        
//                    case StreamTokenizer.TT_NUMBER:
//                        System.out.println("\nЭТО ЧИСЛО TT_NUMBER = "+st.nval);
//                        if (previousWord != null && st.nval!=44) { //44 is ","
//                                    if (fillCtor) {
//                                        System.out.println("Будем добавлять эту цифру в конструктор");
//                                        paramForCtor.add(st.nval); //временно к инт
//                                        qParForCtor--;
//                                    }
//                                    
//                                    if (fillMethod) {
//                                        System.out.println("Будем добавлять эту цифру в метод");
//                                        paramForMethod.add(st.nval);
//                                        qParForMethod--;
//                                    }
//                        }
//                        previousWord = Double.toString(st.nval); //so previous word will not be is equals the null!
//                        break;
//                        
//                    case StreamTokenizer.TT_EOL:
//                        System.out.println("\nЭТО КОНЕЦ СТРОКИ: TT.EOL");
//                        System.out.println("У нас тут st.ttype = "+st.ttype);
//                        previousWord = null;
//                        break;
//                        
//                    default:
//                        System.out.println("\nЭТО ЧТ0-ТО ДРУГОЕ: ttype = "+st.ttype);
//                        System.out.println("У нас тут st.ttype = "+st.ttype);
//                        previousWord = null;
//                        break;
//                }
//
//                     if (fillCtor){
//                    if (StreamTokenizer.TT_EOL==st.nextToken()||qParForCtor==0){
//                        System.out.println("\nДля конструктора получили все параметры:");
//                        System.out.println(paramForCtor);
////                        System.out.println("Вызов конструкора tempCtor = "
////                                +tempCtor.getName()
////                                +" Для tempVar = "+tempVar.toString()
////                                +" Далее # completed...");
//
//                        //System.out.println("# created " + cl.getSimpleName());//class.forname(tempClass).getSimple
//                        
//                        variables.putIfAbsent(tempVar.toString(), tempCtor.newInstance(paramForCtor.toArray()));
//                        System.out.println("Добавили инстанс успешно!");
//                        System.out.print(" newInstance = "+variables.get(tempVar.toString()).toString());
//                        // clear flags and temporary strings
//                        fillCtor = false;
//                        tempVar.delete(0, tempVar.length());
//                        tempClass.delete(0, tempClass.length());// may be yet to change tempClass?
//                        paramForCtor.clear();
//                    }
//                }
//
//                if (fillMethod){
//                    if (StreamTokenizer.TT_EOL==st.nextToken()||qParForMethod==0){
//                        System.out.println("\nДля метода получили все параметры:");
//                        System.out.println(paramForMethod);
////                        System.out.println("Вызов метода tempM = "
////                                +tempMethod.getName()
////                                +" Для tempO = "+tempObject.toString()
////                                +" Далее # completed...");
//                        tempMethod.invoke(tempObject, paramForMethod.toArray());
//                        System.out.println("Вызвали метод успешно!");
//                        //Clear temporary elements:
////                        tempMethod=null;
////                        tempObject=null;
//                        fillMethod = false;
//                        paramForMethod.clear();
//                    }
//                }
//            } //The end of while
//            in.close();
//            
//            System.out.println("\nВыведем наши переменные:");
//            for(String key : variables.keySet()){
//                System.out.println("key = "+key+", Object = "+variables.get(key));
//            }
//        }
//        catch (Exception e){
//            System.out.println("Сработало исключение! "+e.toString()+" "+e.getMessage());
//            return;
//        }
//    }
//    
//} // The end of class.
//=========
//public class TestScenario{
//    @Test
//    public void run() throws Exception {
//        new TestScenario().scenario();
//    }
//
//    private void scenario() throws IOException, ClassNotFoundException,
//            NullPointerException, NoSuchMethodException {
//
//        Map<String, String> classes = new HashMap<>();
//        classes.put("Zoo", "workwithzoo.sourceofprofit.Zoo");
//        classes.put("User", "workwithzoo.user.User");
//        classes.put("Enclosure", "workwithzoo.sourceofprofit.Enclosure"); // add Enclosure
//        
//        Map<String, Object> variables = new HashMap<>();
//        
//        try{
//            InputStream in = getClass().getResourceAsStream("ui_commands.txt"); //change way
//            StreamTokenizer st = new StreamTokenizer(in);
//            st.commentChar('#');
//            st.wordChars('$', '$');
//            
//            String previousWord = null;
//            
//            int qParForMethod = 0; //количество параметров для подсчёта параметров метода
//            ArrayList paramForMethod = new ArrayList(); //сами параметры убрали в них <Object>
//            boolean fillMethod = false; //флаг заполнения параметров
//            Method tempMethod = this.getClass().getMethod("run"); //метод, для которого собираем
//            Object tempObject = new Object(); //объект, для которого вызываем
//            
//            int qParForCtor = 0; //количество параметров для подсчёта параметров конструктора
//            ArrayList paramForCtor = new ArrayList<>(); //сами параметры
//            boolean fillCtor = false; //флаг заполнения параметров
//            Constructor tempCtor = this.getClass().getConstructor(); //конструктор, для которого собираем
//            StringBuilder tempVar = new StringBuilder(); //Переменная-ключ будущего объекта
//            StringBuilder tempClass = new StringBuilder(); //Класс будущего объекта
//            
//            System.out.println("Токен для TT_WORD = "+StreamTokenizer.TT_WORD);
//            System.out.println("Токен для TT_NUMBER = "+StreamTokenizer.TT_NUMBER);
//            System.out.println("Токен для EOL = "+StreamTokenizer.TT_EOL);
//            System.out.println("Токен для EOF = "+StreamTokenizer.TT_EOF+"\n");
//            
//            while (StreamTokenizer.TT_EOF != st.nextToken()) {
//                switch (st.ttype) {
//                    
//                    case StreamTokenizer.TT_WORD:
//                        System.out.println("\nЭТО СЛОВО TT_WORD = "+st.sval);
//                        
//                        if (previousWord != null) {
//                            switch (previousWord) {
//                                
//                                case "let" :
//                                    System.out.println("Предыдущее было let.");
//                                    System.out.println("У нас тут st.sval = "+st.sval);
//                                    System.out.println("Добавили переменную-ключ "+st.sval+" в variables");
//                                    variables.put(st.sval, null);
//                                    tempVar.append(st.sval);
////                                    fillCtor = true;
//                                    break;
//                                    
//                                case "new" :
//                                    System.out.println("Пошёл case new :");
////                                    System.out.println("Ведь previous = "+previousWord);
//                                    System.out.println("У нас тут st.sval = "+st.sval);
////                                    System.out.println("Ищем classname в classes для "+st.sval);
//                                    String classname = classes.get(st.sval);
////                                    System.out.println("Нашёл classname = "+classname);
//                                    Class<?> cl = Class.forName(classname);
//                                    System.out.println("ПОСЛЕ new classname= "+classname);
//                                    
//                                    
////                                    System.out.println("# created " + cl.getSimpleName());//class.forname(tempClass).getSimple
//                                    
//                                    if (fillMethod) {
//                                        System.out.println("Предыдущее new и мы заполняем метод.");
//                                        paramForMethod.add(st.sval); // НА БУДУЩЕЕ ДЛЯ ОБРАБОТКИ ПАРАМЕТРА ЧЕРЕЗ NEW
//                                        qParForMethod--;
//                                        System.out.println("param.add(st.sval)");
//                                    } else { //TO DO...Execute constructor: (but not inner Ctor)
//                                        tempClass.append(classname);
//                                        System.out.println("Предыдущее new и мы получаем конструктор.");
////                                        fillCtor =true;
////                                        System.out.println("И тут cl = "+cl);
////                                        tempCtor = cl.getConstructor();
//                                        Constructor[] ctors = cl.getConstructors();
//                                        for (Constructor ctor : ctors){
////                                            System.out.println("Есть конструктор "+ctor.getName());
////                                                System.out.println("Берём первый попавшийся:");//Пока единственный
//                                                Class[] parameters = ctor.getParameterTypes();
//                                                Constructor с1 = cl.getDeclaredConstructor(parameters);
//                                                System.out.println("ПОЛУЧИЛИ конструктор: "+с1);
////                                                System.out.println("HERE Completed "
////                                                +с1.getName()+" "
////                                                +Arrays.asList(parameters));
//                                                
//                                                if (с1.getParameterCount()!=0){
//                                                    System.out.println("try fill param:");
//                                                    qParForCtor = parameters.length;
//                                                    System.out.println("qParamForCtor= "+qParForCtor);
//                                                    tempCtor = ctor;
//                                                    fillCtor = true;
//                                                }
//                                        }
//                                        System.out.println("tempCtor = "+tempCtor.getName());
//                                    }
//                                    break;
//
//                                default :
//                                    if (st.sval.charAt(0)=='$' && !st.sval.contains(".")) {// If we had meet the variable
//                                        System.out.println("Встретили переменную с $. Обработаем.");
//                                        System.out.println("У нас тут st.sval = "+st.sval);
//                                        if (fillCtor) {
//                                            //Находим объект из коллекции по названию переменной
//                                            //и кладём в коллекцию параметров конструктора
//                                            System.out.println("Будем добавлять эту переменную в конструктор");
//                                            paramForCtor.add(variables.get(st.sval.substring(1))); //В будущем здесь будет исключение-ошибка написания сценария.
//                                            qParForCtor--;
//                                            System.out.println("paramForCtor.add("+st.sval.substring(1)+")");
//                                        }
//                                        if (fillMethod) {
//                                            //Находим объект из коллекции по названию переменной
//                                            //и кладём в коллекцию параметров метода
////                                            System.out.println("ВОТ ТУТ ТО В МЕТОД ОНА И ПОЙДЁТ!");
//                                            System.out.println("Будем добавлять эту переменную в метод");
//                                            paramForMethod.add(variables.get(st.sval.substring(1)));
//                                            qParForMethod--;
//                                            System.out.println("paramForMethod.add("+st.sval.substring(1)+")");
//                                        }
//                                        
//                                    } else { // Если это просто строка:
//                                        System.out.println("Это просто строка st.sval = "+st.sval);
//                                        if (fillCtor) {
//                                            System.out.println("Будем добавлять эту строку в конструктор");
//                                            paramForCtor.add(st.sval);
//                                            qParForCtor--;
//                                            System.out.println("paramForCtor.add("+st.sval+")");
//                                        }
//
//                                        if (fillMethod) {
//                                            System.out.println("Будем добавлять эту строку в метод");
//                                            paramForMethod.add(st.sval);
//                                            qParForMethod--;
//                                            System.out.println("paramForMethod.add("+st.sval+")");
//                                        }
//                                    }
//                                    break;
//                                    
////                                    System.out.println("Отловил "+st.sval+" после "+previousWord);
////                                    if (st.sval.charAt(0)=='$' && st.sval.contains(".")) { //If we had meet the method
////                                        System.out.println("ЗДЕСЬ БУДЕТ ОБРАБОТКА МЕТОДОВ ПОСЛЕ $");
////                                        System.out.println("st.sval = "+st.sval);
////                                        int dot = st.sval.indexOf(".");
////                                        String nameOfMethod =new StringBuilder(st.sval).substring(dot).substring(1);
//////                                        System.out.println("nameOfMethod = "+nameOfMethod);
////                                        
////                                        String methodOwn = st.sval.substring(1, dot);
//////                                        System.out.println("ПОЛУЧИЛИ ПЕРЕМЕННУЮ МЕТОДА: "+methodOwn);
//////                                        System.out.println("ПОЛУЧИЛИ КЛАСС ОБЪЕКТА ПЕРЕМЕННОЙ "+variables.get(methodOwn).getClass().getName());
//////                                        String methodOwner = methods.get(st.sval);
////                                        Class<?> clOwner = Class.forName(variables.get(methodOwn).getClass().getName());
//////                                        System.out.println("ПОЛУЧИЛИ класс: "+clOwner);
////                                        
////                                        //Ищем этот метод:
////                                        Method[] meths = clOwner.getMethods();
////                                        for (Method method : meths){
//////                                            System.out.println("Есть метод: "+method.getName());
////                                            if (method.getName().equals(nameOfMethod)){
//////                                                System.out.println("Нашли нужный метод");
////                                                Class[] parameters = method.getParameterTypes();
////                                                Method m1 = clOwner.getMethod(nameOfMethod, parameters);
//////                                                System.out.println("ПОЛУЧИЛИ метод: "+m1);
//////                                                System.out.println("HERE Completed "
//////                                                +m1.getName()+" "
//////                                                +Arrays.asList(parameters));
////                                                
////                                                if (method.getParameterCount()!=0){
////                                                    System.out.println("try fill param:");
////                                                    qParForMethod = parameters.length;
////                                                    System.out.println("qParam= "+qParForMethod);
//////                                                    tempMethod = method;
//////                                                    fillMethod = true;
////                                                    
////                                                    tempObject = variables.get(methodOwn);
//////                                                    System.out.println("tempO = "+tempObject);
////                                                }
////                                                tempMethod = method;
////                                                fillMethod = true;
////                                            }
////                                        }//break;
////                                        System.out.println("Обработка метода не должна восприниматься, как слово!"
////                                                + "Поэтому следующие else пропускаем!..");
////                                    } else 
//                                        
//                                        
//                            }
//                        }
//                        //Из default:
////                          System.out.println("Отловил "+st.sval+" после "+previousWord);
//                        System.out.println("Previous был равен null!");
//                          System.out.println("Поэтому будет провека: является ли это методом?");
//                                    if (st.sval.charAt(0)=='$' && st.sval.contains(".")) { //If we had meet the method
//                                        System.out.println("Да. Это метод! ЗДЕСЬ БУДЕТ ОБРАБОТКА МЕТОДОВ ПОСЛЕ $");
//                                        System.out.println("st.sval = "+st.sval);
//                                        int dot = st.sval.indexOf(".");
//                                        String nameOfMethod =new StringBuilder(st.sval).substring(dot).substring(1);
//                                        System.out.println("nameOfMethod = "+nameOfMethod);
//                                        
//                                        String methodOwn = st.sval.substring(1, dot);
//                                        System.out.println("ПОЛУЧИЛИ ПЕРЕМЕННУЮ МЕТОДА: "+methodOwn);
//                                        System.out.println("ПОЛУЧИЛИ КЛАСС ОБЪЕКТА ПЕРЕМЕННОЙ "+variables.get(methodOwn).getClass().getName());
////                                        String methodOwner = methods.get(st.sval);
//                                        Class<?> clOwner = Class.forName(variables.get(methodOwn).getClass().getName());
//                                        System.out.println("ПОЛУЧИЛИ класс: "+clOwner);
//                                        
//                                        //Ищем этот метод:
//                                        Method[] meths = clOwner.getMethods();
//                                        for (Method method : meths){
////                                            System.out.println("Есть метод: "+method.getName());
//                                            if (method.getName().equals(nameOfMethod)){
//                                                System.out.println("Нашли нужный метод");
//                                                Class[] parameters = method.getParameterTypes();
//                                                Method m1 = clOwner.getMethod(nameOfMethod, parameters);
//                                                System.out.println("ПОЛУЧИЛИ метод: "+m1);
////                                                System.out.println("HERE Completed "
////                                                +m1.getName()+" "
////                                                +Arrays.asList(parameters));
//                                                
//                                                if (method.getParameterCount()!=0){
//                                                    System.out.println("try fill param:");
//                                                    qParForMethod = parameters.length;
//                                                    System.out.println("qParam= "+qParForMethod);
////                                                    tempMethod = method;
////                                                    fillMethod = true;
//                                                    
////                                                    tempObject = variables.get(methodOwn);
////                                                    System.out.println("tempO = "+tempObject);
//                                                }
//                                                tempObject = variables.get(methodOwn); //перенёс из if
//                                                tempMethod = method;
//                                                fillMethod = true;
//                                            }
//                                        }
//                                    } else {
//                                        System.out.println("Нет, не является!");
//                                    }
////                        System.out.println("st.sval = " + st.sval + ", " + st.ttype);
//                        System.out.println("В TT_WORD: previous = "+previousWord);
//                        previousWord = st.sval;
//                        System.out.println("previous = st.sval = "+previousWord);
//                        break;
//                        
//                    case StreamTokenizer.TT_NUMBER:
//                        System.out.println("\nЭТО ЧИСЛО TT_NUMBER = "+st.nval);
//                        if (previousWord != null && st.nval!=44) { //44 is Space
//                            System.out.println("Перед st.nval, previous = "+previousWord);
////                            switch (previousWord) {
////                                case "$zoo.addEnclosure" :
//                                    if (fillCtor) {
//                                        System.out.println("Будем добавлять эту цифру в конструктор");
//                                        paramForCtor.add(st.nval);//временно на инт
//                                        qParForCtor--;
//                                        System.out.println("paramForCtor.add(st.nval)="+st.nval);
//                                    }
//                                    
//                                    if (fillMethod) {
//                                        System.out.println("Будем добавлять эту цифру в метод");
//                                        paramForMethod.add(st.nval);
//                                        qParForMethod--;
//                                        System.out.println("paramForMethod.add(st.nval)="+st.nval);
//                                    }
////                                    break;
////                                case "$zoo.addDays" :
//                        }
////                        previousWord = st.sval; //Сейчас будет нул
////                        System.out.println("previous = st.sval = "+previousWord);
//                        
//                        previousWord = Double.toString(st.nval); //чтобы был не нулл
//                        System.out.println("И после nval="+st.nval+" previous = "+previousWord);
//                        break;
//                        
//                    case StreamTokenizer.TT_EOL:
//                        System.out.println("\nЭТО КОНЕЦ СТРОКИ: TT.EOL");
//                        System.out.println("У нас тут st.ttype = "+st.ttype);
//                        System.out.println("У нас тут st.sval = "+st.sval);
//                        System.out.println("У нас тут st.nval = "+st.nval);
//                        
//                        
//                       
//                        
//                        
//                        
//                        
//                        previousWord = null;
//                        break;
//                        
//                    default:
//                        System.out.println("\nЭТО ЧТ0-ТО ДРУГОЕ: ttype = "+st.ttype);
//                        System.out.println("У нас тут st.ttype = "+st.ttype);
//                        System.out.println("У нас тут st.sval = "+st.sval);
//                        System.out.println("У нас тут st.nval = "+st.nval);
//                        previousWord = null;
//                        break;
//                }
////                previousWord = st.sval;
//
//                     if (fillCtor){
//                    if (StreamTokenizer.TT_EOL==st.nextToken()||qParForCtor==0){
//                        System.out.println("\nДля конструктора получили все параметры:");
//                        System.out.println(paramForCtor);
////                        System.out.println("Вызов конструкора tempCtor = "
////                                +tempCtor.getName()
////                                +" Для tempVar = "+tempVar.toString()
////                                +" Далее # completed...");
//                        
//                        System.out.println("Кол-во параметров = "+tempCtor.getParameterCount());
//                        variables.putIfAbsent(tempVar.toString(), tempCtor.newInstance(paramForCtor.toArray()));
//                        System.out.println("Добавили инстанс успешно!");
//                        System.out.print(" newInstance = "+variables.get(tempVar.toString()).toString());
//
//                        System.out.println("\nУ нас тут st.ttype = "+st.ttype);
//                        System.out.println("У нас тут st.sval = "+st.sval);
//                        System.out.println("У нас тут st.nval = "+st.nval);
//                        
//                        fillCtor = false;
//                        tempVar.delete(0, tempVar.length());
//                        tempClass.delete(0, tempClass.length());// may be yet to change tempClass?
//                        paramForCtor.clear();
////                        System.out.println("Пробуем исправить:");
////                        switch (st.ttype){
////                            case StreamTokenizer.TT_EOL: previousWord=null;
////                            case StreamTokenizer.TT_NUMBER: previousWord=Double.toString(st.nval);
////                            case StreamTokenizer.TT_WORD: previousWord=st.sval;
////                        }
//                        //Попробуем исправить
////                        previousWord = st.sval;
//                    }
//                }
//
//                if (fillMethod){
//                    if (StreamTokenizer.TT_EOL==st.nextToken()||qParForMethod==0){
//                        System.out.println("\nДля метода получили все параметры:");
//                        System.out.println(paramForMethod);
////                        System.out.println("Вызов метода tempM = "
////                                +tempMethod.getName()
////                                +" Для tempO = "+tempObject.toString()
////                                +" Далее # completed...");
//                        //TO DO Case Parameter...
//                        tempMethod.invoke(tempObject, paramForMethod.toArray());
//                        System.out.println("Вызвали метод успешно!");
//                        System.out.println("У нас тут st.ttype = "+st.ttype);
//                        System.out.println("У нас тут st.sval = "+st.sval);
//                        System.out.println("У нас тут st.nval = "+st.nval);
////                        
//                        
//                        //Clear temporary elements:
////                        tempMethod=null;
////                        tempObject=null;
////                        System.out.println("Пытаемся очистить параметры:");
//                        fillMethod = false;
//                        paramForMethod.clear();
////                        System.out.println("Очищены успешно");
////                        System.out.println("Сейчас st.sval = "+st.sval);
////                        System.out.println("Следующий токен: = "+st.nextToken()); /UBRAL 
////                        System.out.println(st.nextToken());
////                        System.out.println("Пробуем исправить:");
////                        switch (st.ttype){
////                            case StreamTokenizer.TT_EOL: {previousWord=null;break;}
//////                            case StreamTokenizer.TT_NUMBER: previousWord=Double.toString(st.nval);
////                            case StreamTokenizer.TT_WORD: {previousWord=st.sval;break;}
////                        }
//                    }
////                  else{qParam--;}
////                        System.out.println("Пробуем исправить:");
////                        switch (st.ttype){
////                            case StreamTokenizer.TT_EOL: previousWord=null;
////                            case StreamTokenizer.TT_NUMBER: previousWord=Double.toString(st.nval);
////                            case StreamTokenizer.TT_WORD: previousWord=st.sval;
////                        }
//                    //Попробуем исправить
////                    previousWord = st.sval;
//                }
//
////                if (fillCtor){
////                    if (StreamTokenizer.TT_EOL==st.nextToken()||qParForCtor==0){
////                        System.out.println("\nДля конструктора получили все параметры:");
////                        System.out.println(paramForCtor);
//////                        System.out.println("Вызов конструкора tempCtor = "
//////                                +tempCtor.getName()
//////                                +" Для tempVar = "+tempVar.toString()
//////                                +" Далее # completed...");
////                        
////                        System.out.println("Кол-во параметров = "+tempCtor.getParameterCount());
////                        variables.putIfAbsent(tempVar.toString(), tempCtor.newInstance(paramForCtor.toArray()));
////                        System.out.println("Добавили инстанс успешно!");
////                        System.out.print(" newInstance = "+variables.get(tempVar.toString()).toString());
////
////                        System.out.println("\nУ нас тут st.ttype = "+st.ttype);
////                        System.out.println("У нас тут st.sval = "+st.sval);
////                        System.out.println("У нас тут st.nval = "+st.nval);
////                        
////                        fillCtor = false;
////                        tempVar.delete(0, tempVar.length());
////                        tempClass.delete(0, tempClass.length());// may be yet to change tempClass?
////                        paramForCtor.clear();
//////                        System.out.println("Пробуем исправить:");
//////                        switch (st.ttype){
//////                            case StreamTokenizer.TT_EOL: previousWord=null;
//////                            case StreamTokenizer.TT_NUMBER: previousWord=Double.toString(st.nval);
//////                            case StreamTokenizer.TT_WORD: previousWord=st.sval;
//////                        }
////                        //Попробуем исправить
//////                        previousWord = st.sval;
////                    }
////                }
////
////                if (fillMethod){
////                    if (StreamTokenizer.TT_EOL==st.nextToken()||qParForMethod==0){
////                        System.out.println("\nДля метода получили все параметры:");
////                        System.out.println(paramForMethod);
//////                        System.out.println("Вызов метода tempM = "
//////                                +tempMethod.getName()
//////                                +" Для tempO = "+tempObject.toString()
//////                                +" Далее # completed...");
////                        //TO DO Case Parameter...
////                        tempMethod.invoke(tempObject, paramForMethod.toArray());
////                        System.out.println("Вызвали метод успешно!");
////                        System.out.println("У нас тут st.ttype = "+st.ttype);
////                        System.out.println("У нас тут st.sval = "+st.sval);
////                        System.out.println("У нас тут st.nval = "+st.nval);
//////                        
////                        
////                        //Clear temporary elements:
//////                        tempMethod=null;
//////                        tempObject=null;
//////                        System.out.println("Пытаемся очистить параметры:");
////                        fillMethod = false;
////                        paramForMethod.clear();
//////                        System.out.println("Очищены успешно");
//////                        System.out.println("Сейчас st.sval = "+st.sval);
////                        System.out.println("Следующий токен: = "+st.nextToken());
//////                        System.out.println(st.nextToken());
//////                        System.out.println("Пробуем исправить:");
//////                        switch (st.ttype){
//////                            case StreamTokenizer.TT_EOL: {previousWord=null;break;}
////////                            case StreamTokenizer.TT_NUMBER: previousWord=Double.toString(st.nval);
//////                            case StreamTokenizer.TT_WORD: {previousWord=st.sval;break;}
//////                        }
////                    }
//////                  else{qParam--;}
//////                        System.out.println("Пробуем исправить:");
//////                        switch (st.ttype){
//////                            case StreamTokenizer.TT_EOL: previousWord=null;
//////                            case StreamTokenizer.TT_NUMBER: previousWord=Double.toString(st.nval);
//////                            case StreamTokenizer.TT_WORD: previousWord=st.sval;
//////                        }
////                    //Попробуем исправить
//////                    previousWord = st.sval;
////                }
//            } //The end of while
//            in.close();
//            
//            System.out.println("\nВыведем наши переменные:");
//            for(String key : variables.keySet()){
//                System.out.println("key = "+key+", Object = "+variables.get(key));
//            }
//        }
//        catch (Exception e){
//            System.out.println("Сработало исключение! "+e.toString()+" "+e.getMessage());
//            return;
//        }
//    }
//    
//} // The end of class.
//==========
//
//public class TestScenario{
//    public static void main(String[] args) throws Exception {
//        new TestScenario().run(args);
//    }
//
//    @Test
//    private void run(String[] args) throws IOException, ClassNotFoundException {
//
//        Map<String, String> classes = new HashMap<>();
//        classes.put("Zoo", "workwithzoo.sourceofprofit.Zoo");
//        classes.put("User", "workwithzoo.user.User");
////        classes.put("User", "workwithzoo.sourceofprofit.Zoo");
//
//        InputStream in = getClass().getResourceAsStream("ui_commands.txt"); //change way
//        StreamTokenizer st = new StreamTokenizer(in);
//        st.commentChar('#');
//
//        String previousWord = null;
//        while (StreamTokenizer.TT_EOF != st.nextToken()) {
//            switch (st.ttype) {
//                case StreamTokenizer.TT_WORD:
//                    if (previousWord != null) {
//                        switch (previousWord) {
//                            case "let" :
//                                break;
//                            case "new" :
//                                String classname = classes.get(st.sval);
//                                Class<?> cl = Class.forName(classname);
//                                System.out.println("# created " + cl.getSimpleName());
//                                break;
//                        }
//                    }
//                    System.out.println("st.sval = " + st.sval + ", " + st.ttype);
//                    previousWord = st.sval;
//                    break;
//            }
//        }
//        in.close();
//    }
//}

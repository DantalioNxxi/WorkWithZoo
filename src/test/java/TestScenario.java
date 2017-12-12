
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
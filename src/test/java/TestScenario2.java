//Токен для TT_WORD = -3
//Токен для TT_NUMBER = -2
//Токен для EOL = 10
//Токен для EOF = -1
//44 ,
//40 (
//41 )
//61 =
//10 - перевод строки
//34 "  - кавычки по умолчанию воспринимает, как обрамляющие st.sval,
//        поэтому 2-я кавычка игнорируется а из токена получаем WORD st.sval без кавычек
//35 #
//59 ;
//95 _  -  подчёркивания запрещены

import org.junit.Test;
import java.io.IOException;
import java.io.InputStream;
import java.io.StreamTokenizer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author VYZH
 * @author DantalioNxxi
 * @version 1.0.2
 * @since 28.11.2017
 */
public class TestScenario2 {

    @Test
    public void run() throws Exception {
        new TestScenario2().scenario();
    }

    // 200 lines
    private void scenario() throws IOException, ClassNotFoundException,
            NullPointerException, NoSuchMethodException {

        //Map of the our classes
        Map<String, String> classes = new HashMap<>();
        //        classes.put("User", "workwithzoo.user.User");
        classes.put("Businessman", "workwithzoo.user.Businessman");
        classes.put("Zoo", "workwithzoo.sourceofprofit.Zoo");
        classes.put("Enclosure", "workwithzoo.sourceofprofit.Enclosure");
        classes.put("Climate.TypeClimate", "workwithzoo.fauna.Climate$TypeClimate");

        classes.put("Employee", "workwithzoo.employee.Employee");
        classes.put("Guide", "workwithzoo.employee.Guide");
        classes.put("Vet", "workwithzoo.employee.Vet");
        classes.put("Keeper", "workwithzoo.employee.Keeper");

        classes.put("AmurTiger", "workwithzoo.fauna.AmurTiger");
        classes.put("Lion", "workwithzoo.fauna.Lion");
        classes.put("PolarBear", "workwithzoo.fauna.PolarBear");

        //Map of the our variable-Objects
        Map<String, Object> variables = new HashMap<>();

        try {
            InputStream in = getClass().getResourceAsStream("ui_commands2.txt"); //change way
            StreamTokenizer st = new StreamTokenizer(in);
            st.commentChar('#');
            st.wordChars('$', '$');
            st.wordChars('_', '_');
            st.ordinaryChar('.');

            Stack stack = new Stack();
            while (StreamTokenizer.TT_EOF != st.nextToken()) {
//                System.out.println("\ntoken = " + st.ttype + ", " + st.sval + ", " + st.nval + ";\nBefore_stack = " + stack + ";\nBefore_vars = " + variables);
                switch (st.ttype) {
                    case '"':
                        stackAdd(stack, variables, st.sval);
                        break;

                    case StreamTokenizer.TT_WORD:
                        if (st.sval.startsWith("$")) {//it is a viariable
                            stackAdd(stack, variables, variables.get(st.sval));
                        } else {//it is a simple word
                            stackAdd(stack, variables, st.sval);
                        }
                        break;

                    case StreamTokenizer.TT_NUMBER://it is a number
                        stack.push(st.nval);
                        break;

                    case ')'://it is a final of method or constructor
                        List args = new ArrayList();//pop his arguments
                        while (!Character.valueOf('(').equals(stack.peek())) {
                            args.add(stack.pop());
                        }
                        stack.pop();//pop the openly brace

                        String name = String.valueOf(stack.pop());//pop and safe a name of the constructor or the method
                        String keyword = String.valueOf(stack.pop());//pop and safe keyword

                        if (args.contains('.')) {//if array contains dots. steel it working only with fields, constants
                            Stack tstack = new Stack();//template stack
                            tstack.addAll(args);

                            Stack newstack = new Stack();//future computed args

                            List targs = new ArrayList();
                            StringBuilder complexClass = new StringBuilder();
                            StringBuilder complexField = new StringBuilder();

                            while (!tstack.isEmpty()) {
                                targs.add(tstack.pop());//save token in the template array
                                if (!tstack.isEmpty() && tstack.peek().equals('.')) {//if it is part of name class
                                    complexClass.append(targs.get(targs.size() - 1)).append('.');//добавляем к классу
                                    targs.add(tstack.pop());
                                } else if (!targs.isEmpty() // if it is field
                                        && targs.get(targs.size() - 2).equals('.')) {
                                    complexField.append(targs.remove(targs.size() - 1));
                                    targs.remove(targs.size() - 1);//erase the field name
                                    complexClass.deleteCharAt(complexClass.length() - 1);//erase a last dot
                                    try {
                                        String anyclass = classes.get(complexClass.toString());
                                        Class<?> anycl = Class.forName(anyclass);
                                        newstack.add(getField(anycl, complexField.toString()));//get an arg with type anycl
                                        complexClass.delete(0, complexClass.length());//clear class and field
                                        complexField.delete(0, complexField.length());
                                    } catch (NullPointerException e) {
                                        System.out.println("Класс " + complexClass + " не был найден в пуле классов!");
                                    }

                                } else {
                                    newstack.add(targs.remove(targs.size() - 1));//add this arg in future stack
                                }
                            }

                            args.clear();//clear old args
                            while (!newstack.isEmpty()) {//updating of the arrays args
                                args.add(newstack.pop());
                            }
                        }

                        Collections.reverse(args);//reverse args for to adding to the parameters
                        //Checking of the keywords:
                        if ("new".equals(keyword)) {//if it is a constructor
                            try {
                                String classname = classes.get(name);
                                Class<?> cl = Class.forName(classname);

                                for (Constructor<?> c : cl.getDeclaredConstructors()) {
                                    Object[] realArgs = getParameters(c.getParameterTypes(), args);
                                    if (realArgs != null) {//if fit parameters was founded, create new variable 
                                        stackAdd(stack, variables, c.newInstance(realArgs));//and add hir into stack
                                        //break of cycle is necessary???
                                    }//in future here will comuputing for empty constructors
                                }
                            } catch (NullPointerException e) {
                                System.out.println("ОШИБКА! КЛАСС НЕ НАЙДЕН! " + e.getCause().getMessage());
                            }

                        } else if (".".equals(keyword)) {//if it is a method

                            Object variable = stack.pop();//get variable for invoking
                            Class<?> cl = variable.getClass();//get her class

                            int countOfMethods = 0;//if method did not searched, then print warning
                            for (Method m : cl.getDeclaredMethods()) {
                                if (name.equals(m.getName())) {//if founded a fit method
                                    Object[] realArgs = getParameters(m.getParameterTypes(), args);
                                    if (realArgs != null) {//if parameters are similarity
                                        try {
                                            m.invoke(variable, realArgs);
                                            break;
                                        } catch (IllegalArgumentException e) {
                                            System.out.println("Исключение при передаче - m.invoke(realArgs): "
                                                    + e.getCause().getMessage());
                                        } catch (InvocationTargetException i) {
                                            System.out.println("Исключ при передаче - target invocation: "
                                                    + i.getCause().getMessage());
                                        }

                                    } else if (realArgs == null && m.getParameterTypes().length == 0) {
                                        m.invoke(variable);//if count of parameters is null
                                        break;
                                    }
                                } else {
                                    countOfMethods++;
                                    if (countOfMethods == cl.getDeclaredMethods().length) {
                                        System.out.println("Метод " + name + " так и не был найден!");
                                    }
                                }
                            }//end of for
                        }//end of the else if "."
                        break;
                    case ','://commas are pass
                        break;
                    case ';'://it is too
                        break;
                    default://if it is unknown token
                        stack.push(Character.valueOf((char) st.ttype));
                        break;
                }
//                System.out.println("After_stack = " + stack + ",;\nAfter_vars = " + variables);
            } //The end of while

            in.close();
            System.out.println("\nВыведем наши переменные:");
            for (Map.Entry<String, Object> e : variables.entrySet()) {
                String type = e.getValue() != null ? String.valueOf(e.getValue().getClass()) : "null";
                System.out.println("key = " + e.getKey() + ", " + type + " = " + e.getValue());
            }

        } catch (Exception e) {
            System.out.println("Сработало исключение! " + e.toString() + " - " + e.getCause().getMessage());
            return;
        }
    } // The end of method.

    /**
     * Perspective method, whose return the field with name "anyfield" and a
     * type "anyclass"
     *
     * @param anyclass
     * @param anyfield
     * @return
     */
    private Object getField(Class anyclass, String anyfield) {
        if (anyclass.isEnum()) {//if anyclass is Enum, then searching fit constant
            for (int i = 0; i < anyclass.getEnumConstants().length; i++) {
                if (anyclass.getEnumConstants()[i].toString().equals(anyfield)) {
                    return anyclass.getEnumConstants()[i];
                }
            }
            throw new IllegalArgumentException("Константа не найдена!");
        } else {
            System.out.println("Здесь должна быть обработка поля");
            throw new IllegalArgumentException("Обработка поля отсутствует!");
        }
    }

    /**
     * Check the adding to stack. If this a new variable, then to create her.
     *
     * @param stack
     * @param variables
     * @param value
     * @return
     */
    private Object stackAdd(Stack stack, Map<String, Object> variables, Object value) {
        if (!"new".equals(value) && !stack.isEmpty() && Character.valueOf('=').equals(stack.peek())) {
            stack.pop();//erase an equal sign
            String name = (String) stack.pop();//get a name of the future variable
            if (!"let".equals(stack.pop())) {//erase keyword "let"
                throw new IllegalStateException("Тут должно быть let, а его нет!");
            }
            variables.put("$" + name, value);
        }
        return stack.push(value);//add value in stack
    }

    /**
     * Try get realArgs at these parameterTypes from any args
     *
     * @param parameterTypes
     * @param args
     * @return
     * @see TestScenario2#getParameter(java.lang.Class, java.lang.Object)
     */
    private Object[] getParameters(Class<?>[] parameterTypes, List args) {

        if (args.size() != parameterTypes.length) {
            return null;
        }
        List result = new ArrayList();
        for (int i = 0; i < parameterTypes.length; i++) {
            Object realArg = null;
            try {
                realArg = getParameter(parameterTypes[i], args.get(i));
            } catch (UnsupportedOperationException e) {
                System.out.println("Обнаружено несовпадение параметров или отсутствие поддержки типов");
                return null;
            }
            result.add(realArg);
        }
        return result.toArray();
    }

    /**
     * Using by method getParameters for to cast any arg
     *
     * @param parameterType
     * @param arg
     * @return
     * @see TestScenario2#getParameters(java.lang.Class<?>[], java.util.List)
     */
    private Object getParameter(Class<?> parameterType, Object arg) {
        if (parameterType.isInstance(arg)) {//if types is equals
            return arg;
        } else if (arg instanceof Double) {
            if (Double.TYPE.equals(parameterType)) {
                return arg;
            } else if (Integer.TYPE.equals(parameterType)
                    || Integer.class.equals(parameterType)) {
                return ((Double) arg).intValue();
            } else if (Float.TYPE.equals(parameterType)
                    || Float.class.equals(parameterType)) {
                return ((Double) arg).floatValue();
            }
        }         
        //template block is needless
        else if (arg instanceof Enum){
                    if (Enum.class.equals(parameterType)){
                        return parameterType.cast((Enum)arg);
                    }
                }
        
        else if (CharSequence.class.isAssignableFrom(parameterType) && CharSequence.class.isInstance(arg)) {
            return parameterType.cast(arg);//if it is sequence of simbols
        }
        System.out.println("not supported type = " + parameterType);
        throw new UnsupportedOperationException("not supported type = " + parameterType);
        //for working methods with different parameterTypes of similary count of parameters
        //advisably do not trowing exception
    }

} // The end of class.

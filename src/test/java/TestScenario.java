import java.io.IOException;
import java.io.InputStream;
import java.io.StreamTokenizer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author VYZH
 * @since 28.11.2017
 */
public class TestScenario {
    public static void main(String[] args) throws Exception {
        new TestScenario().run(args);
    }

    private void run(String[] args) throws IOException, ClassNotFoundException {

        Map<String, String> classes = new HashMap<>();
        classes.put("Zoo", "workwithzoo.sourceofprofit.Zoo");
        classes.put("User", "workwithzoo.user.User");
//        classes.put("User", "workwithzoo.sourceofprofit.Zoo");

        InputStream in = getClass().getResourceAsStream("ui_commands.txt");
        StreamTokenizer st = new StreamTokenizer(in);
        st.commentChar('#');

        String previosWord = null;
        while (StreamTokenizer.TT_EOF != st.nextToken()) {
            switch (st.ttype) {
                case StreamTokenizer.TT_WORD:
                    if (previosWord != null) {
                        switch (previosWord) {
                            case "let" :
                                break;
                            case "new" :
                                String classname = classes.get(st.sval);
                                Class<?> cl = Class.forName(classname);
                                System.out.println("# created " + cl.getSimpleName());
                                break;
                        }
                    }
                    System.out.println("st.sval = " + st.sval + ", " + st.ttype);
                    previosWord = st.sval;
                    break;
            }
        }
        in.close();
    }
}

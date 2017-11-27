
package workwithzoo.sourceofprofit;

/**
 * The object can to keep anything
 * @author DantalioNxxi
 */
public interface Storeable <T>{
    void put(T t);
    T pull(T t);
}

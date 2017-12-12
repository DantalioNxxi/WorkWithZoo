package workwithzoo.fauna;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This is a preferred climate for any animal.
 * If the annotation does not declare, then animal use the annotation his parent,
 * which has annotation by default - Tropical, Polar, Middle.
 * @see Animal
 * @author DantalioNxxi
 * @since 08.12.2017
 * @version 1.0.2
 */
@Documented
@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Likes {
    Climate.TypeClimate likesClimate();
}

package workwithzoo.fauna;

/**
 * Interface for istance, who can to die.
 * Usually is using by agents of the fauna or flora.
 * @author DantalioNxxi
 * @since 07.12.2017
 */
public interface Deathable {
    /**
     * @see Animal
     * Each agent to die in its own.
     */
    public void die();
}

package workwithzoo.fauna;

/**
 *
 * @author DantalioNxxi
 * @see Climate
 * @see workwithzoo.sourceofprofit.Enclosure
 * @since 07.12.2017
 */
public interface ClimateObserver {
    /**
     * Using by observers, which sign under notification about changing outClimate.
     * @param newClimate new climate, who is changing during changing months.
     * But new Climate can to change by other closly and storeable objects,
     * for example Enclosure.
     */
    public void updateClimate(Climate.TypeClimate newClimate);
}

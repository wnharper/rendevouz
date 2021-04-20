package Model;

/**
 * <h2>Country</h2>
 * The country class provides the structure and attributes in order to
 * create a country.
 *
 * @author  Warren Harper
 * @version 1.0
 * @since   2021-04-04
 */

public class Country {
    private int countryId;
    private String countryName;


    public Country(int countryId, String countryName) {
        this.countryId = countryId;
        this.countryName = countryName;
    }

    /**
     * Method returns country ID
     * @return country ID
     */
    public int getCountryId() {
        return countryId;
    }

    /**
     * Method returns country name
     * @return country name
     */
    public String getCountryName() {
        return countryName;
    }
}

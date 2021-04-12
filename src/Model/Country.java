package Model;

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

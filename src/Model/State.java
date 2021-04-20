package Model;

/**
 * <h2>State</h2>
 * The state class provides the structure and attributes in order to
 * create a country state.
 *
 * @author  Warren Harper
 * @version 1.0
 * @since   2021-04-04
 */

public class State {

    final int stateId;
    final String stateName;
    final int countryId;


    public State(int stateId, String stateName, int countryId) {
        this.stateId = stateId;
        this.stateName = stateName;
        this.countryId = countryId;
    }
}

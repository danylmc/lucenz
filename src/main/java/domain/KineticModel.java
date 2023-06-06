package domain;

/**
 * This enum represents all the kinetic models available
 * to the 200 and 300 level version of LUCENZ.
 * This replaces the variable UP in the original code.
 * The models are ordered differently so models that are
 * use in LUCENZ2 and LUCENZ3 are first and models only
 * used in LUCENZ3 are last.
 * */

public enum KineticModel {
	/**
	 * 200 and 300 level model: 
	 * Uninhibited One Substrate
	 */
	UninhibitedOneSub(1),
	/**
	 * 200 and 300 level model:
	 * Competitive inhibition
	 */
	InhibitedCompetitive(4),
	/**
	 * 200 and 300 level model:
	 * Non-competitive inhibition
	 */
	InhibitedNonCompetitive(5),
	/**
	 * 200 and 300 level model:
	 * Uncompetitive inhibition
	 */
	InhibitedUnCompetitive(6),
	/**
	 * 300 level model:
	 * Uninhibited Two Substrate - Ordered Bi-Bi
	 */
	UninhibitedTwoSubOrderedBiBi(2),
	/**
	 * 300 level model:
	 * Uninhibited Two Substrate - Ping-Pong
	 */
	UninhibitedTwoSubPingPong(3);
	
	/**
	 * Stores the value this model type has in the original program
	 */
	private int value;
	
	/**
	 * Creates a KineticModel type with the given value being what the value
	 * of the model is in the original program
	 * @param value - the int this model type has in the original LUCENZ
	 */
	KineticModel(int value) {
		this.value = value;
	}
	
	/**
	 * Gets the value of this model type
	 * @return the value the model is in the original LUCENZ
	 */
	public int getValue() {
		return this.value;
	}
	
	@Override
	public String toString() {
		switch(this) {
		case UninhibitedOneSub:
			return "Uninhibited - One Substrate";
		case UninhibitedTwoSubOrderedBiBi:
			return "Uninhibited - Sequential";
		case UninhibitedTwoSubPingPong:
			return "Uninhibited - Ping-Pong";
		case InhibitedCompetitive:
			return "Competitive Inhibition";
		case InhibitedNonCompetitive:
			return "Non-Competitive Inhibition";
		case InhibitedUnCompetitive:
			return "Un-Competitive Inhibition";
		default:
			return "";
		}
	}
	
}

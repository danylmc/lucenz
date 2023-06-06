package domain;

/**
 * This enum represents all the graph types available
 * to the 200 and 300 level version of LUCENZ.
 * The graph types are ordered so the types used
 * in LUCENZ2 and LUCENZ3 are first and graph types only
 * used in LUCENZ3 are last.
 */
public enum GraphType {
	/**
	 * 200 and 300 level graph type:
	 * Velocity vs [S]
	 */
	VelocityVS,
	/**
	 * 200 and 300 level graph type:
	 * Lineweaver-Burke
	 */
	LineweaverBurke,
	/**
	 * 200 and 300 level graph type:
	 * Hanes
	 */
	Hanes,
	/**
	 * 200 and 300 level graph type:
	 * Eadie-Hofstee
	 */
	EadieHofstee,
	/**
	 * 200 and 300 level graph type:
	 * Dixon
	 */
	Dixon,
	/**
	 * 300 level graph type:
	 * Hunter-Downs
	 */
	HunterDowns
}

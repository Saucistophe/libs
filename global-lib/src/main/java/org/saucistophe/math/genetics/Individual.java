package org.saucistophe.math.genetics;

/**
 An invidual that can evolve in a genetic simulation.
 */
public abstract class Individual
{
	/**
	The fitness of this individual in the selection process.
	Represents the victories vs defeats during the fights.
	*/
	public int fitness = 0;

	/**
	 Makes a child, that will be forged from the average of the parents.

	 @param otherIndividual The other parent.
	 @return The child of both parents.
	 */
	public abstract Individual makeChild(Individual otherIndividual);

	/**
	 Inits this child with random values.
	 */
	public abstract void randomize();

	/**
	 Fight the other individual.
	 A fight is not a mutual fight, and the inverse fight must be fought.

	 @param otherIndividual The other parent.
	 */
	public abstract void fight(Individual otherIndividual);
}

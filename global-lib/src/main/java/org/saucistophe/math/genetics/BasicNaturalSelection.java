package org.saucistophe.math.genetics;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.saucistophe.patterns.Pair;

/**
 A basic genetic algorithm implementation.
 */
public class BasicNaturalSelection
{
	/**
	 The mixing factor of the algorithm.
	 The minimum value is 2, for which 6 individuals will fight, 2 will win, 2 children will be made, and 2 random others will be born.
	 Increasing this factor drastically increases the number of children created at each iteration, and thus reduces the focus on good individuals.
	 */
	public static int MIXING_FACTOR = 4;

	/**
	 The list of individuals fighting for fitness.
	 */
	public List<Individual> individuals;

	/**
	 @param parents A list of potential parents.
	 @return A list of all the possibles couples.
	 */
	public List<Pair<Individual, Individual>> getCouples(List<Individual> parents)
	{
		List<Pair<Individual, Individual>> couples = new ArrayList<>();
		for (int i = 0; i < parents.size(); i++)
		{
			for (int j = i + 1; j < parents.size(); j++)
			{
				couples.add(new Pair<>(parents.get(i), parents.get(j)));
			}
		}

		assert (couples.size() == parents.size() * (parents.size() - 1) / 2);

		return couples;
	}

	/**
	 Plays an iteration of the algorithm: Make all possible couples fight each other,
	 and let the best generation have children.
	 The individuals will be reset before processing, and will be sorted by descending fitness afterwards.
	 */
	public void iterate()
	{
		assert (individuals.size() == MIXING_FACTOR * (MIXING_FACTOR + 2));

		// Reset all fitnesses.
		individuals.parallelStream().forEach(i -> i.fitness = 0);

		Logger.getLogger(BasicNaturalSelection.class.getName()).log(Level.INFO, "New iteration. {0} Fights!", individuals.size() * (individuals.size() - 1) / 2);
		// For each possible couple:
		getCouples(individuals).parallelStream()
				.forEach(couple ->
						{
							Logger.getLogger(BasicNaturalSelection.class.getName()).log(Level.INFO, "Fight begins! {0}", couple);
							// Make the couple fight.
							couple.left.fight(couple.right);
							couple.right.fight(couple.left);
							Logger.getLogger(BasicNaturalSelection.class.getName()).log(Level.INFO, "Fight over!   {0}", couple);
						});

		Logger.getLogger(BasicNaturalSelection.class.getName()).log(Level.INFO, "Fights over for this iteration.");

		// Order the individuals by descending fitness.
		individuals.sort((a, b) ->
				{
					return Integer.compare(b.fitness, a.fitness);
				});

		// Get the list of top individual, that will act as parents for the next generation.
		List<Individual> parents = individuals.subList(0, MIXING_FACTOR);

		Logger.getLogger(BasicNaturalSelection.class.getName()).log(Level.INFO, "The winner are {0}", parents);

		// Get their children.
		List<Individual> children = getCouples(parents)
				.stream()
				.map(couple -> couple.left.makeChild(couple.right))
				.collect(Collectors.toList());

		// Make some room for the children.
		individuals = individuals.subList(0, individuals.size() - children.size());

		// Randomize the leftovers, and insert the children.
		for (int i = MIXING_FACTOR; i < individuals.size(); i++)
		{
			individuals.get(i).randomize();
		}
		individuals.addAll(children);
	}

	/**
	 Inits the individuals with random values.
	 */
	public void init()
	{
		individuals.parallelStream().forEach(Individual::randomize);
	}
}

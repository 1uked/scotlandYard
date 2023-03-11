package uk.ac.bris.cs.scotlandyard.model;

import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;

import uk.ac.bris.cs.scotlandyard.model.Board.GameState;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYard.Factory;

import java.util.HashSet;
import java.util.Set;

/**
 * cw-model
 * Stage 1: Complete this class
 */
public final class MyGameStateFactory implements Factory<GameState> {

	@Nonnull @Override public GameState build(
			GameSetup setup,
			Player mrX,
			ImmutableList<Player> detectives) {
		// TODO
		/* Test 1*/
		if(mrX == null)
			throw new NullPointerException();
		/* Test 4 */
		else if(mrX.isDetective()== true)
			throw new IllegalArgumentException();

		/* Test 7 - Create Set for colors and locations and throw if a repeat is added*/
		Set<String> colorSet = new HashSet<String>();
		Set<Integer> startLocationSet = new HashSet<Integer>();
		/* Test 7 END*/

		/* Test 2 + 3*/
		for (int i = 0; i < detectives.size(); i++) {
			/* Test 7 Implementation */
			if (colorSet.add(detectives.get(i).piece().webColour()) == false)
				throw new IllegalArgumentException();
			/* Test 8*/
			else if (startLocationSet.add(detectives.get(i).location()) == false)
				throw new IllegalArgumentException();
			/* Test 8 END */
			/* Test 7 END*/
			if (detectives.get(i) == null)
				throw new NullPointerException();
				/* Test 5*/
			else if (detectives.get(i).isDetective() == false)
				throw new IllegalArgumentException();
			/* Test 6 passes prior to this with no implementation*/
			/* Test 9 + 10 */
			if (detectives.get(i).hasAtLeast(ScotlandYard.Ticket.DOUBLE,1) || detectives.get(i).hasAtLeast(ScotlandYard.Ticket.SECRET,1))
				throw new IllegalArgumentException();
			/* Test 9 + 10 End*/
		}


		throw new RuntimeException("Implement me!");

	}

}

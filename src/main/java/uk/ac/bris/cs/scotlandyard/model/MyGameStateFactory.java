package uk.ac.bris.cs.scotlandyard.model;

import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableSet;
import uk.ac.bris.cs.scotlandyard.model.Board.GameState;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYard.Factory;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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
		if (mrX == null)
			throw new NullPointerException();
		/* Test 4 */
		else if (mrX.isDetective())
			throw new IllegalArgumentException();

		/* Test 7 - Create Set for colors and locations and throw if a repeat is added*/
		Set<String> colorSet = new HashSet<String>();
		Set<Integer> startLocationSet = new HashSet<Integer>();
		/* Test 7 END*/

		/* Test 2 + 3*/
		for (int i = 0; i < detectives.size(); i++) {
			/* Test 7 Implementation */
			if (!colorSet.add(detectives.get(i).piece().webColour()))
				throw new IllegalArgumentException();
			/* Test 8*/
			else if (!startLocationSet.add(detectives.get(i).location()))
				throw new IllegalArgumentException();
			/* Test 8 END */
			/* Test 7 END*/
			if (detectives.get(i) == null)
				throw new NullPointerException();
				/* Test 5*/
			else if (!detectives.get(i).isDetective())
				throw new IllegalArgumentException();
			/* Test 6 passes prior to this with no implementation*/
			/* Test 9 + 10 */
			if (detectives.get(i).hasAtLeast(ScotlandYard.Ticket.DOUBLE,1) || detectives.get(i).hasAtLeast(ScotlandYard.Ticket.SECRET,1))
				throw new IllegalArgumentException();
			/* Test 9 + 10 End*/
		}
		return new MyGameState(setup,
				ImmutableSet.of(Piece.MrX.MRX),
				ImmutableSet.of(), // What goes here? They don't say
				mrX,
				detectives);
	}
	private final class MyGameState implements GameState {
		private GameSetup setup;
		private ImmutableSet<Piece> remaining;
		private ImmutableList<LogEntry> log;
		private Player mrX;
		private List<Player> detectives;
		private ImmutableSet<Move> moves;
		private ImmutableSet<Piece> winner;

		private MyGameState(final GameSetup setup,
							final ImmutableSet<Piece> remaining,
							final ImmutableList<LogEntry> log,
							final Player mrX,
							final List<Player> detectives) {
			this.setup = setup;
			this.remaining = remaining;
			this.log = log;
			this.mrX = mrX;
			this.detectives = detectives;

			if (setup == null) {
				throw new NullPointerException("Setup is null");
			} else if (remaining == null) {
				throw new NullPointerException("Remaining is null");
			} else if (log == null) {
				throw new NullPointerException("Log is null");
			} else if (mrX == null) {
				throw new NullPointerException("Mr X is null");
			} else if (detectives == null) {
				throw new NullPointerException("Detectives is null");
			} else if (setup.moves.isEmpty()) {
				throw new IllegalArgumentException("Moves is empty!");
			}
		}

		@Nonnull
		@Override
		public GameSetup getSetup() {
			return setup;
		}

		@Nonnull
		@Override
		public ImmutableSet<Piece> getPlayers() {
			return null;
		}

		@Nonnull
		@Override
		public Optional<Integer> getDetectiveLocation(Piece.Detective detective) {
			return Optional.empty();
		}

		@Nonnull
		@Override
		public Optional<TicketBoard> getPlayerTickets(Piece piece) {
			return Optional.empty();
		}

		@Nonnull
		@Override
		public ImmutableList<LogEntry> getMrXTravelLog() {
			return getMrXTravelLog();
		}

		@Nonnull
		@Override
		public ImmutableSet<Piece> getWinner() {
			return null;
		}

		@Nonnull
		@Override
		public ImmutableSet<Move> getAvailableMoves() {
			return null;
		}

		@Nonnull
		@Override
		public GameState advance(Move move) {
			return null;
		}
	}
}

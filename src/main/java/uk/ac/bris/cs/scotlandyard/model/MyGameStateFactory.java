package uk.ac.bris.cs.scotlandyard.model;

import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableSet;
import uk.ac.bris.cs.scotlandyard.model.Board.GameState;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYard.Factory;

import java.util.*;

/**
 * cw-model
 * Stage 1: Complete this class
 */
public final class MyGameStateFactory implements Factory<GameState> {


	@Nonnull @Override public GameState build(GameSetup setup, Player mrX, ImmutableList<Player> detectives) {
		return new MyGameState(setup, ImmutableSet.of(Piece.MrX.MRX), ImmutableList.of(), mrX, detectives);
	}
	private final class MyGameState implements GameState {
		/* Variables */
		private GameSetup setup;
		private ImmutableSet<Piece> remaining;
		private ImmutableList<LogEntry> log;
		private Player mrX;
		private List<Player> detectives;
		private ImmutableSet<Move> moves;
		private ImmutableSet<Piece> winner;
		private ImmutableSet<Piece> allPlayers;
		/* Variables END */

		/* Constructor */
		private MyGameState(
				final GameSetup setup,
				final ImmutableSet<Piece> remaining,
				final ImmutableList<LogEntry> log,
				final Player mrX,
				final List<Player> detectives)
		{

			/* Tests and Checks*/
			if (mrX == null) {
				throw new NullPointerException("Mr X is null");
			} else if (mrX.isDetective()) {
				throw  new IllegalArgumentException("Mr X is a detective");
			} else if (setup.moves.isEmpty()) {
				throw new IllegalArgumentException("Moves is empty!");
			} else if (setup.graph.nodes().isEmpty()) {
				throw new IllegalArgumentException("Graph is empty");
			}

			Set<String> colorSet = new HashSet<String>();/* Test 7 */
			Set<Integer> startLocationSet = new HashSet<Integer>();/* Create Set for  detective colors and locations*/

			/* Detective Properties*/
			for(Player detective: detectives){
				if (detective == null) {
					throw new NullPointerException("Detective(s) is null");
				} else if (!detective.isDetective()) {
					throw new IllegalArgumentException("Multiple MrX");
				} else if (!colorSet.add(detective.piece().webColour())) {
					throw new IllegalArgumentException("Duplicate Detective Color Piece");
				} else if (!startLocationSet.add(detective.location())) {
					throw new IllegalArgumentException("Detective Location Overlap");
				} else if (detective.hasAtLeast(ScotlandYard.Ticket.DOUBLE,1) || detective.hasAtLeast(ScotlandYard.Ticket.SECRET,1))
					throw new IllegalArgumentException("Unsupported Detective Tickets");
			}
			/* Detective Properties END */
			/* Tests END*/

			/* Assign Variables*/
			this.setup = setup;
			this.remaining = remaining;
			this.log = log;
			this.mrX = mrX;
			this.detectives = detectives;
			this.moves = getAvailableMoves();
			this.winner = ImmutableSet.<Piece>builder().build();
			/* All Players*/
			Set<Piece> allPlayer = new HashSet<Piece>();
			allPlayer.add(mrX.piece());
			for(Player d:detectives){allPlayer.add(d.piece());}
			this.allPlayers = ImmutableSet.copyOf(allPlayer);
			/* All Players END*/
			/* Variables END*/

		}
		/* Constructor END*/

		/* Methods Privacy Amendment Needed */
		@Nonnull
		@Override
		public GameSetup getSetup() {
			return setup;
		}

		@Nonnull
		@Override
		public ImmutableSet<Piece> getPlayers() {
			return allPlayers;
		}

		@Nonnull
		@Override
		public Optional<Integer> getDetectiveLocation(Piece.Detective detective) {
			for (Player d : detectives) {
				if (detective.equals(d.piece())) {
					return Optional.of(d.location());
				}
			}
			return Optional.empty();
		}

		@Nonnull
		@Override
		public Optional<TicketBoard> getPlayerTickets(Piece piece) {
			List<Player> allP = new ArrayList<Player>();
			allP.addAll(detectives);
			allP.add(mrX);

			for (Player p : (allP)) {
				if (piece.equals(p.piece())) {
					return Optional.of(p.tickets()).map(tickets -> ticket -> tickets.getOrDefault(ticket, 0));
					// Need help to figure it out
				}
			}
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
			return winner;
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
	/* Methods END */


}

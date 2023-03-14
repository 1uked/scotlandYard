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
		private ImmutableSet<Player> remaining;
		private ImmutableList<LogEntry> log;
		private Player mrX;
		private List<Player> detectives;
		private ImmutableSet<Move> moves;
		private ImmutableSet<Piece> winner;
		private ImmutableSet<Player> allPlayers;
		private ImmutableSet<Piece> allPieces;
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

			/* All Players + Pieces*/
			Set<Piece> allPiece = new HashSet<Piece>();
			allPiece.add(mrX.piece());
			for(Player d:detectives){allPiece.add(d.piece());}
			this.allPieces= ImmutableSet.copyOf(allPiece);

			Set<Player> allPlayer = new HashSet<Player>();
			allPlayer.add(mrX);
			allPlayer.addAll(detectives);
			this.allPlayers = ImmutableSet.copyOf(allPlayer);
			/* All Players END*/
			this.setup = setup;
			this.remaining = allPlayers;
			this.log = log;
			this.mrX = mrX;
			this.detectives = detectives;
			this.moves = getAvailableMoves();
			this.winner = ImmutableSet.<Piece>builder().build();
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
			return allPieces;
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
			return log;
		}

		@Nonnull
		@Override
		public ImmutableSet<Piece> getWinner() {
			return winner;
		}

		@Nonnull
		@Override
		public ImmutableSet<Move> getAvailableMoves() {

			Set<Move> singleMoves = new HashSet<Move>();
			Set<Move> doubleMoves = new HashSet<Move>();
			for(Player p: ImmutableSet.of(mrX)){
			    singleMoves.addAll(makeSingleMoves(setup,detectives,p,p.location()));
				if((p.hasAtLeast(ScotlandYard.Ticket.DOUBLE,1)) && !(setup.moves.equals(ImmutableList.of(true)))){
				doubleMoves.addAll(makeDoubleMoves(setup,detectives,p,p.location())); }
				//if (p == mrX) { doubleMoves.addAll(makeDoubleMoves(setup,detectives,p,p.location()));}
			}

			Set<Move> allMoves = new HashSet<Move>();
			allMoves.addAll(singleMoves);
			allMoves.addAll(doubleMoves);
			return ImmutableSet.copyOf(allMoves);
		}

		@Nonnull
		@Override
		public GameState advance(Move move) {
			return this;
			//throw new IllegalArgumentException("Move not found in Board.getAvailableMoves() ");
		}
	}
	/* Methods END */

	/* Helper Functions */
	/* getAvailableMoves() - Single + Double */
	private static Set<Move.SingleMove> makeSingleMoves(GameSetup setup, List<Player> detectives, Player player, int source){

		/* Create an empty collection of some sort, say, HashSet, to store all the SingleMove we generate */
		Set<Move.SingleMove> singleMoves = new HashSet<Move.SingleMove>();
		Set<Integer> invalidLocations = new HashSet<Integer>();
		for(Player p : detectives) { invalidLocations.add(p.location());}

		for(int destination : setup.graph.adjacentNodes(source)) {
			if (invalidLocations.contains(destination)) {continue;}
			for(ScotlandYard.Transport t : setup.graph.edgeValueOrDefault(source, destination, ImmutableSet.of()) ) {

				if (player.has(t.requiredTicket())) {
					Move.SingleMove singleM = new Move.SingleMove(player.piece(), source,t.requiredTicket(),destination);
					singleMoves.add(singleM);
				}

				if(player.has(ScotlandYard.Ticket.SECRET)) {
					Move.SingleMove singleM = new Move.SingleMove(player.piece(), source, ScotlandYard.Ticket.SECRET,destination);
					singleMoves.add(singleM);
				}
			}
		}
		return singleMoves;
	}
	private static Set<Move.DoubleMove> makeDoubleMoves(GameSetup setup, List<Player> detectives, Player player, int source){

		/* Create an empty collection of some sort, say, HashSet, to store all the SingleMove we generate */
		Set<Move.DoubleMove> doubleMoves = new HashSet<Move.DoubleMove>();
		Set<Integer> invalidLocations = new HashSet<Integer>();
		for(Player p : detectives) { invalidLocations.add(p.location());}

		for(int destination : setup.graph.adjacentNodes(source)) {
			if (invalidLocations.contains(destination)) {continue;}
			for(ScotlandYard.Transport t : setup.graph.edgeValueOrDefault(source, destination, ImmutableSet.of()) ) {

				if (player.has(t.requiredTicket())) {
					// Change to call single move with 1 less ticket and store as a set and new source
					Set<Move.SingleMove> secondMove = makeSingleMoves(setup,detectives,player.use(t.requiredTicket()),destination);
					for (Move.SingleMove sMove:secondMove) {
						doubleMoves.add(new Move.DoubleMove(player.piece(), source,t.requiredTicket(),destination,sMove.ticket,sMove.destination));
					}
				}

				if(player.has(ScotlandYard.Ticket.SECRET)) {
					Set<Move.SingleMove> secondMove = makeSingleMoves(setup,detectives,player.use(ScotlandYard.Ticket.SECRET),destination);
					for (Move.SingleMove sMove:secondMove) {
						doubleMoves.add(new Move.DoubleMove(player.piece(), source,ScotlandYard.Ticket.SECRET,destination,sMove.ticket,sMove.destination));
					}
				}
			}
		}
		return doubleMoves;
	}
	/* getAvailableMoves() END*/
	private static Player pieceToPlayer(Piece p, ImmutableSet<Player> allPlayers) {
		for (Player pb : allPlayers) {
			if (pb.piece() == p) {return pb;}
		}
		return null;
	}
	/* Helper Functions END*/
}

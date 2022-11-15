package stc.soccer;

import java.util.List;
import java.util.Scanner;

public class GameEvaluation {

    private final SoccerGame game;

    public GameEvaluation(SoccerGame game) {
        this.game = game;

    }

    public void play(Scanner in) {
        boolean turn = true;
        boolean end = false;
        System.out.println("Your Code Soccer game has been initialized.");

        while (!end) {
            if (turn) {
                System.out.println("Players " + game.getPlayer1() + " turn:");
            } else {
                System.out.println("Players " + game.getPlayer2() + " turn:");
            }

            System.out.println("Current location of ball is " + game.getBallPosition().toString() + ".");

            FieldPoint destination = new FieldPoint(in.nextInt(), in.nextInt());

            if (isEndOfGame(destination)) {
                end = true;
                System.out.println("End of game.");
                break;
            }

            System.out.println("Game continues");

            while (!isMoveValid(destination)) {
                System.out.println("Specified move is not valid. Try again.");
                destination = new FieldPoint(in.nextInt(), in.nextInt());

                if (isEndOfGame(destination)) {
                    end = true;
                    System.out.println("End of game.");
                    break;
                }
            }

            System.out.println("Move is valid.");

            if (!isMoveBounce(destination)) {
                System.out.println("Move doesn't bounce.");
                turn = !turn;
            }

            game.addMove(destination);

        }

        System.out.println("End lol");
    }

    private boolean isMoveValid(FieldPoint dest) {
        return isPointInsideBounds(dest) && isMoveMade(dest) &&
                isPointCloseEnough(dest) && !isMoveInHistory(dest) &&
                !(isPointOnBounds(game.getBallPosition()) && isPointOnBounds(dest));
    }

    private boolean isPointInsideBounds(FieldPoint point) {
        return ((point.row() >= 0) && (point.row() < game.getRows())) &&
                ((point.column() >= 0) && (point.column() < game.getColumns()));
    }

    private boolean isPointCloseEnough(FieldPoint point) {
        final FieldPoint position = game.getBallPosition();

        return ((position.row() - point.row()) > -2 && (position.row() - point.row()) < 2) &&
                ((position.column() - point.column()) > -2 && (position.column() - point.column()) < 2);
    }

    private boolean isMoveMade(FieldPoint point) {
        return (game.getBallPosition().row() != point.row() || game.getBallPosition().column() != point.column());
    }

    private boolean isEndOfGame(FieldPoint point) {
        return isPointCloseEnough(point) && (point.column() == (game.getColumns() / 2) &&
                (point.row() == -1 || point.row() == game.getRows()));
    }

    private boolean isMoveInHistory(FieldPoint dest) {

        if (game.getMoveHistory().containsKey(dest)) {
            final List<FieldPoint> pointHist = game.getMoveHistory().get(dest);

            if (!pointHist.isEmpty()) {
                for (FieldPoint pt: pointHist) {
                    if (pt.equals(game.getBallPosition())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean isMoveBounce(FieldPoint dest) {
        return game.getMoveHistory().containsKey(dest) || isPointOnBounds(dest);
    }

    private boolean isPointOnBounds(FieldPoint point) {
        return ((point.row() == 0 || point.row() == game.getRows() - 1) ||
                (point.column() == 0 || point.column() == game.getColumns() - 1)) &&
                (point.column() != game.getColumns() / 2);
    }
}

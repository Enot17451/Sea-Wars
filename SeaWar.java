import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;


enum Ships {
    AircraftCarrier("Aircraft Carrier", 5),
    Battleship("Battleship", 4),
    Submarine("Submarine", 3),
    Cruiser("Cruiser", 2),
    Destroyer("Destroyer", 1);

    int len;
    String name;

    Ships(String name, int len) {
        this.name = name;
        this.len = len;
    }

    String fullName() {
        return this.name + " ( " + this.len + " cells)";
    }
}

class Coordinate {
    Point f, s;

    Coordinate(Point f, Point s) {
        this.f = f;
        this.s = s;
    }

    @Override
    public String toString() {
        return this.f.let + ":" + this.f.num + " " + this.s.let + ":" + this.s.num;
    }
}

class Point {
    char let;
    int num;

    Point(char y, int x) {
        this.let = y;
        this.num = x;
    }
}

class Help {

    static int max(int a, int b) {
        if (a > b) {
            return a;
        } else return b;
    }

    static int min(int a, int b) {
        if (a < b) {
            return a;
        } else return b;
    }

    static char intToLetter(int num) {
        return (char) (num + 64);
    }

    static int LetterToCoordinate(char ch) {
        return Character.getNumericValue(ch) - 9;
    }

    static void show(Board board) {
        HashMap<Integer, Character> symbol = new HashMap<>();
        symbol.put(0, '~');//fog
        symbol.put(1, '~');//busy
        symbol.put(3, 'O');//ship
        symbol.put(5, 'X');//hit
        symbol.put(8, 'M');//miss
        System.out.println("  1 2 3 4 5 6 7 8 9 10");
        for (int i = 1; i < board.boardArray.length - 1; i++) {
            System.out.print(Help.intToLetter(i));
            for (int j = 1; j < board.boardArray[i].length - 1; j++) {
                System.out.print(" " + symbol.get(board.boardArray[i][j]) + "");
            }
            System.out.println();
        }
    }

    static boolean wrongLength(Ships ship, Coordinate coordinate) {
        int shipLen = ship.len;
        int wordGap = Math.abs(coordinate.f.let - coordinate.s.let) + 1;
        int numGap = Math.abs(coordinate.f.num - coordinate.s.num) + 1;
        if (shipLen != wordGap && shipLen != numGap) {
            return true;
        }
        return false;
    }

    static boolean wrongLocation(Coordinate coordinate) {
        char w1 = coordinate.f.let;
        char w2 = coordinate.s.let;
        int n1 = coordinate.f.num;
        int n2 = coordinate.s.num;
        return !((w1 == w2 && n1 != n2) || (w1 != w2 && n1 == n2));
    }

    static boolean touchAnotherShip(Board board, Coordinate coordinate, Ships ship) {
        int w1 = LetterToCoordinate(coordinate.f.let);
        int w2 = LetterToCoordinate(coordinate.s.let);
        int n1 = coordinate.f.num;
        int n2 = coordinate.s.num;
        for (int i = min(w1, w2); i <= max(w1, w2); i++) {
            for (int j = min(n1, n2); j <= max(n1, n2); j++) {
                if (board.boardArray[i][j]<2){
                    return true;
                }
            }
        }
        return false;
    }

    static Coordinate stringToCoordinate(String coord) {
        int space = coord.indexOf(" ");
        char[] fArray = Arrays.copyOfRange(coord.toCharArray(), 0, space);
        char[] sArray = Arrays.copyOfRange(coord.toCharArray(), space + 1, coord.length());
        char fWord = fArray[0];
        char sWord = sArray[0];
        int fNum = fArray.length > 2 ? 10 : Character.getNumericValue(fArray[1]);
        int sNum = sArray.length > 2 ? 10 : Character.getNumericValue(sArray[1]);
        Point fPoint = new Point(fWord, fNum);
        Point sPoint = new Point(sWord, sNum);
        return new Coordinate(fPoint, sPoint);
    }

    static boolean checkInputCoordinate(String coord, Ships ship, Board board) {
        Coordinate coordinate = stringToCoordinate(coord);
        if (wrongLength(ship, coordinate)) {
            System.out.println("Error! Wrong length of the Submarine! Try again:\n");
            return false;
        }
        if (wrongLocation(coordinate)) {
            System.out.println("Error! Wrong ship location! Try again:\n");
            return false;
        }
        if (touchAnotherShip(board, coordinate, ship)) {
            System.out.println("Error! You placed it too close to another one. Try again:\n");
            return false;
        }
        return true;
    }

}

class Board {
    int[][] boardArray = new int[12][12];

    Board() {
        this.createBoard();
    }

    void createBoard() {
        for (int i = 1; i < boardArray.length - 1; i++) {
            for (int j = 1; j < boardArray[i].length - 1; j++) {
                boardArray[i][j] = 0;
            }
        }
    }
}

public class SeaWar {


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Board board = new Board();
        for (Ships ship : Ships.values()) {
            boolean isPass = false;
            Help.show(board);
            System.out.println("\nEnter the coordinates of the " + ship.fullName() + "\n");
            while (!isPass) {
                String coord = scanner.nextLine();
                System.out.println();
                isPass = Help.checkInputCoordinate(coord, ship, board);
            }

        }

    }
}

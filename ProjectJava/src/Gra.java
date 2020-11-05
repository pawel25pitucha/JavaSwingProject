import java.util.*;


public class Gra extends Plansza {
    public class Action {
        public int x, y;
        public char newValue, oldValue;

        public Action(int x, int y, char newValue, char oldValue) {
            this.x = x;
            this.y = y;
            this.newValue = newValue;
            this.oldValue = oldValue;
        }

    }

    private final int SIZE = 6;
    private char board[][] = new char[SIZE][SIZE];
    public char finalBoard[][] = new char[SIZE][SIZE];

    Stack<Action> gameActions = new Stack<Action>();
    Stack<Action> forwardActions = new Stack<>();

    public Gra() {
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                board[x][y] = ' ';
                finalBoard[x][y] = ' ';
            }
        }
//Plansza na stale :( walczylem z generatorem..

       finalBoard[0][0]='C';finalBoard[0][1]=' ';finalBoard[0][2]='A';finalBoard[0][3]=' '; finalBoard[0][4]='D'; finalBoard[0][5]='B';
        finalBoard[1][0]=' ';finalBoard[1][1]='B';finalBoard[1][2]='C';finalBoard[1][3]=' '; finalBoard[1][4]='A'; finalBoard[1][5]='D';
        finalBoard[2][0]='B';finalBoard[2][1]='A';finalBoard[2][2]=' ';finalBoard[2][3]='D'; finalBoard[2][4]='C'; finalBoard[2][5]=' ';
        finalBoard[3][0]='D';finalBoard[3][1]=' ';finalBoard[3][2]='B';finalBoard[3][3]='C'; finalBoard[3][4]=' '; finalBoard[3][5]='A';
        finalBoard[4][0]='A';finalBoard[4][1]='D';finalBoard[4][2]=' ';finalBoard[4][3]='B'; finalBoard[4][4]=' '; finalBoard[4][5]='C';
        finalBoard[5][0]=' ';finalBoard[5][1]='C';finalBoard[5][2]='D';finalBoard[5][3]='A'; finalBoard[5][4]='B'; finalBoard[5][5]=' ';



    }

    public Stack<Action> getGameActions() {
        return gameActions;
    }

    public void gameMove(char val, int x, int y) {
        gameActions.add(new Action(x, y, val, board[x][y]));
        board[x][y] = val;
    }

//Ruch do przodu w histori
    public Action forward() {
        Action action = forwardActions.pop();
        board[action.x][action.y] = action.newValue;
        gameActions.push(action);
        return action;
    }
//Ruch do tylu
    public Action revert() {
        Action firstAction = gameActions.peek();
        forwardActions.push(firstAction);
        Action action = gameActions.pop();
        board[action.x][action.y] = action.oldValue;
        return action;
    }
//resetowanie hisotrii ruchow
    public void resetGameMoves() {
        boolean continueLoop = true;
        while (continueLoop) {
            try {
                Action removedItem = gameActions.pop();
                System.out.println(removedItem);
            } catch (EmptyStackException ex) {
                continueLoop = false;
            }
        }
    }

    public int getSize() {
        return SIZE;
    }

    public char[][] getBoard() {
        return board;
    }

    public void resetBoard() {
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                board[x][y] = ' ';
            }
        }
    }

    public void setBoard(char chr , int i , int j) {
        board[i][j]=chr;
    }

    public boolean checkBoard(char board[][]) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] != finalBoard[i][j]) return false;
            }
        }
        return true;



/*
        fillSymbols();
        Map<Integer,ArrayList<Character>> kolumna = new TreeMap<>();
        Map<Integer,ArrayList<Character>> rzad= new TreeMap<>();
        ArrayList<Character> a1;
        int acc=0;
        int acc2=0;
        int pozycja=0;
        int pozycja2=0;
        int licznik=0;

        ArrayList<Character> a2;
        for(int i =0;i<6;i++){
                kolumna.put(i,symbols);
                rzad.put(i,symbols);
        }

        for(int i =0;i<6;i++){
            for(int j =0 ; j<6 ; j++){
                char chr = board[i][j];

                for(int z=0;z<rzad.get(i).size();z++) {
                  if(rzad.get(i).get(z) == chr) {
                        pozycja=z;
                        acc++;
                    }else{
                      if(licznik==5) return false;
                      licznik++;
                  }
                }
                licznik=0;

                for(int z=0;z<kolumna.get(j).size();z++) {
                    if (kolumna.get(j).get(z) == chr) {
                        pozycja2=z;
                        acc2++;
                    }else{
                        if(licznik==5) return false;
                        licznik++;
                    }
                }


                if(acc>0 && acc2>0){
                    rzad.get(i).remove(pozycja);
                    rzad.get(j).remove(pozycja2);
                }
            }
        }

        return true;*/
    }






//Ustawianie wskazowek na podstawie skanowania docelowej planszy ktora mial generowac .

    public String normalSetHints(String pozycjaPlanszy,int x , int y  , int pozycja) {
        int acc = 0;
        char chr=' ';

                if (pozycjaPlanszy == "row") {
                    if (pozycja == 1) {
                        for (int i = 0; i < SIZE; i++) {
                            if (finalBoard[x][i] != ' ') {
                                chr = finalBoard[x][i];
                                break;
                            }
                        }
                    } else {
                        for (int i = 0; i < SIZE; i++) {
                            if (finalBoard[x][i] != ' ') {
                                acc++;
                                if (acc == 2) {
                                    chr = finalBoard[x][i];

                                    break;
                                }
                            }
                        }

                    }

                } else if (pozycjaPlanszy == "col") {
                    if (pozycja == 1) {
                        for (int i = 0; i < SIZE; i++) {
                            if (finalBoard[i][y] != ' ') {
                                chr = finalBoard[i][y];
                                break;
                            }
                        }
                    } else {

                        for (int i = 0; i < SIZE; i++) {
                            if (finalBoard[i][y] != ' ') {
                                acc++;
                                if (acc == 2) {
                                    chr = finalBoard[i][y];

                                    break;
                                }
                            }
                        }
                    }
                }


       String finalString = String.valueOf(chr);
        return finalString;
    }
    public String reverseSetHints(String pozycjaPlanszy,int x , int y  , int pozycja) {
        char chr=' ';
        int acc=0;
        if (pozycjaPlanszy == "row") {
            if (pozycja == 1) {
                for (int i = SIZE - 1; i >= 0; i--) {
                    if (finalBoard[x][i] != ' ') {
                        chr = finalBoard[x][i];
                        break;
                    }
                }
            } else {
                for (int i = SIZE - 1; i >= 0; i--) {
                    if (finalBoard[x][i] != ' ') {
                        acc++;
                        if (acc == 2) {
                            chr = finalBoard[x][i];
                            break;
                        }
                    }
                }
            }

        } else if (pozycjaPlanszy == "col") {
            if (pozycja == 1) {
                for (int i = SIZE - 1; i >= 0; i--) {
                    if (finalBoard[i][y] != ' ') {
                        chr = finalBoard[i][y];
                        break;
                    }
                }
            } else {

                for (int i = SIZE - 1; i >= 0; i--) {
                    if (finalBoard[i][y] != ' ') {
                        acc++;
                        if (acc == 2) {
                            chr = finalBoard[i][y];
                            break;
                        }
                    }
                }

            }

        }
        String finalString = String.valueOf(chr);
        return finalString;
    }





    private ArrayList<Character> symbols = new ArrayList<>();
    private Map<Integer, ArrayList<Character>> mozliweKolumna = new TreeMap<>();
    private Map<Integer, ArrayList<Character>> mozliweRzad = new TreeMap<>();
    ArrayList<Character> localSymbols =new ArrayList<>();
    private char emptySymbol = 'x';
    public void fillSymbols(){
        symbols.add('A');
        symbols.add('B');
        symbols.add('C');
        symbols.add('D');
        symbols.add(' ');
        symbols.add(' ');
    }

///Proba napisania generatora ///
        public void Generator(int x, int y) {
            fillSymbols();
            for (int i = 0; i < SIZE; i++) {
                mozliweKolumna.put(i, symbols);
                mozliweRzad.put(i, symbols);
            }
            localSymbols=symbols;
            Collections.shuffle(localSymbols);  //miesza symbole
            randomFill(x, y);


            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    System.out.println(finalBoard[i][j]);
                }
            }
            }
            int nextX=0; int nextY=0;

        public boolean randomFill ( int startX, int startY){
            if (startX >= SIZE || startY >= SIZE) return true;
            for (int i=0 ; i<localSymbols.size(); i++) {
                    if (mozliweKolumna.get(startY).size() > 0 && mozliweRzad.get(startX).size() > 0) {
                    insertSymbol(startX, startY, localSymbols.get(i)); //probujemy zaladowac symbol
                    nextX = nextPositionX(startX, startY);
                    nextY = nextPositionY(startX, startY);
                    if (randomFill(nextX, nextY)) return true;
                    else {
                        //nie mozna zapelnic reszty tablicy
                        revertSymbol(startX, startY);
                    }
            }
        }
        return false;
    }

    public void insertSymbol(int x , int y , char symbol){
            finalBoard[x][y]=symbol;

            if(mozliweRzad.get(x).size()>0){
                for(int i=0 ; i< mozliweRzad.get(x).size() ; i++){
                    if(mozliweRzad.get(x).get(i)==symbol){
                        mozliweRzad.get(x).remove(i);
                        break;
                    }
                }
            }
            if(mozliweKolumna.get(y).size()>0){
                for(int i =0 ; i<mozliweKolumna.get(y).size(); i++){
                    if(mozliweKolumna.get(y).get(i)==symbol){
                        mozliweKolumna.get(y).remove(i);
                       break;
                    }
                }
            }

        }

        public void revertSymbol(int x , int y){
            char symbol=finalBoard[x][y];
           //finalBoard[x][y]= emptySymbol;

            mozliweRzad.get(x).add(symbol);
            mozliweKolumna.get(y).add(symbol);


        }
        public Integer nextPositionY(int x , int y){
            y++;
            if(y>=SIZE){
                y=0;
            }
            return y;
        }
        public Integer nextPositionX(int x , int y){
            y++;
            if(y>=SIZE){
                x++;
            }
            return x;
        }


}




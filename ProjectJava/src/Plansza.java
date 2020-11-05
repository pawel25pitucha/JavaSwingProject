/**
 *
 * @author Paweł Pitucha
 *
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.EmptyStackException;
import java.util.Scanner;
import java.util.Stack;


public class Plansza
{
    public static void main(String[] args)
    {
        Gra gra = new Gra();
        new Frame(gra);

    }

}


class Frame extends JFrame {
    public Frame(Gra gra) {
        startWindow(gra);
    }

    //--------------PIERWSZE OKIENKO----------------\\
    public void startWindow(Gra gra) {

        //POBIERANIE ROZDZIELCZOSCI
        Toolkit zestaw = Toolkit.getDefaultToolkit();
        Dimension rozmiarEkranu = zestaw.getScreenSize();
        int wysEkranu = rozmiarEkranu.height;
        int szerEkranu = rozmiarEkranu.width;
        szerEkranu = (szerEkranu / 8) * 3;
        setLocation(szerEkranu, wysEkranu / 4);

        JPanel settingsPanel = new JPanel();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        settingsPanel.setLayout(null);
        settingsPanel.setBackground(Color.pink);

        JButton start = new JButton("LET'S START!!!");
        start.setBackground(Color.cyan);
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Window win = SwingUtilities.getWindowAncestor(settingsPanel);
                win.dispose(); //zamyka okno ustawien
                settingsPanel.setVisible(false);
                ramka(gra);

            }
        });
        ImageIcon image = new ImageIcon("front.png");
        Image image2 = image.getImage();
        Image newimage = image2.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
        image = new ImageIcon(newimage);

        JLabel picLabel = new JLabel(image);
        picLabel.setBounds(100, 50, 200, 200);
        start.setBounds(100, 300, 200, 50);
        settingsPanel.add(picLabel);
        settingsPanel.add(start);
        add(settingsPanel);
        setSize(400, 400);
        setVisible(true);
    }
    //--------------GLOWNE OKIENKO----------------\\

    public void ramka(Gra gra) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        setSize(1000, 1000);
        int szer = Toolkit.getDefaultToolkit().getScreenSize().width;
        int wys = Toolkit.getDefaultToolkit().getScreenSize().height;
        int szerRamki = getSize().width;
        int wysRamki = getSize().height;
        setLocation((szer-szerRamki)/2,(wys-wysRamki)/2);


        setTitle("A to D Game");
        setLayout(null);
        Container content = getContentPane();
        content.setBackground(Color.LIGHT_GRAY);
        MainGamePanel panel = new MainGamePanel(gra);
        panel.setBounds(95, 100, 600, 600);

        BackgroundPanel backgroundPanel = new BackgroundPanel(gra,panel);
        backgroundPanel.setBounds(0, 0, 1000, 1000);


        backgroundPanel.add(panel);

        ButtonsPanel buttonsPanel = new ButtonsPanel(gra, panel);
        buttonsPanel.setBounds(775, 100, 200, 600);

        Paint paint = new Paint();
        paint.setBounds(0, 0, 1000, 1000);
        paint.setBackground(Color.LIGHT_GRAY);

        backgroundPanel.add(buttonsPanel);
        backgroundPanel.add(paint);

        content.add(backgroundPanel);

    }


//--------------INTERFEJS GRAFICZNY GRY----------------\\

    class MainGamePanel extends JPanel {

        private BoardTextField board[][];

        public void changeColorBoard(Color color) {
            for (int x = 0; x < 6; x++) {
                for (int y = 0; y < 6; y++) {
                    board[x][y].setBackground(color);
                }
            }
        }

        public void clearBoard() {
            for (int x = 0; x < 6; x++) {
                for (int y = 0; y < 6; y++) {
                    board[x][y].setText("");
                }
            }
        }

        public void setBoardText(int x, int y, String text) {
            board[x][y].setText(text);
        }

        public Color getBoardColor(){
          return board[0][0].getBackground();
        }


        public MainGamePanel(Gra gra) {
            board = new BoardTextField[gra.getSize()][gra.getSize()];
            setLayout(new GridLayout(6, 6));
            for (int i = 0; i < gra.getSize(); i++) {
                for (int j = 0; j < gra.getSize(); j++) {
                    board[i][j] = new BoardTextField(gra, i, j);
                    board[i][j].setHorizontalAlignment(JTextField.CENTER);
                    board[i][j].setBackground(new Color(255, 218, 185));
                    board[i][j].setFont(new Font("SansSerif", Font.BOLD, 25));
                    add(board[i][j]);
                }
            }
        }
    }


//--------------PANEL PRZYCISKOW----------------\\

    class ButtonsPanel extends JPanel {
        private char board[][];

        public ButtonsPanel(Gra gra, MainGamePanel panel) {
            setLayout(new GridLayout(4, 2));

            JButton checkButton = new JButton("CHECK");
            checkButton.setBounds(0, 0, 50, 30);
            checkButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    board = gra.getBoard();
                    if (gra.checkBoard(board)) {
                        Color defaultColor = panel.getBoardColor();
                        panel.changeColorBoard(Color.GREEN);
                        JOptionPane.showMessageDialog(panel, "Awesome! Good job brother :)");
                        panel.changeColorBoard(defaultColor);
                    } else {
                        Color defaultColor = panel.getBoardColor();
                        panel.changeColorBoard(Color.red);
                        JOptionPane.showMessageDialog(panel, "Try again :(");
                        panel.changeColorBoard(defaultColor);
                    }
                }
            });
            add(checkButton);
// RESTART BUTTON
            JButton restartButton = new JButton("RESTART");
            restartButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    panel.clearBoard();
                    gra.resetBoard();
                }
            });
            add(restartButton);

            JPanel j = new JPanel();
            j.setBackground(Color.LIGHT_GRAY);
            add(j);

            JPanel j2 = new JPanel();
            j2.setBackground(Color.LIGHT_GRAY);
            add(j2);

//REVERT BUTTON
            JButton revertButton = new JButton();
            revertButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        Gra.Action action = gra.revert();
                        panel.setBoardText(action.x, action.y, String.valueOf(action.oldValue));
                    } catch (EmptyStackException exception) {
                            System.out.println("Nie ma juz co cofać :(");
                    }
                }
            });
            Image img = new ImageIcon("revert2.png").getImage();
            Image newimg = img.getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH);
            Icon icon = new ImageIcon(newimg);
            revertButton.setIcon(icon);
            ;
            add(revertButton);

            JButton forwardButton = new JButton();
            forwardButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try{
                    Gra.Action action = gra.forward();
                    panel.setBoardText(action.x, action.y, String.valueOf(action.newValue));
                    }catch(EmptyStackException ex){
                        System.out.println("Nie mozesz isc juz do przodu");
                    }
                }
            });
            Image img2 = new ImageIcon("forward.png").getImage();
            Image newimg2 = img2.getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH);
            Icon icon2 = new ImageIcon(newimg2);
            forwardButton.setIcon(icon2);
            ;
            add(forwardButton);
///SAVE BUTTON
            JButton saveButton = new JButton("SAVE");
            Stack<Gra.Action> ruchy = gra.getGameActions();
            char board[][] = gra.getBoard();
            saveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fc = new JFileChooser();
                    if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                        File plik = fc.getSelectedFile();
                        try {
                            PrintWriter pw = new PrintWriter(plik);
                            //Scanner skaner =  new Scanner(plik);
                            for (int i = 0; i < 6; i++) {
                                for (int j = 0; j < 6; j++) {
                                    //board[i][j] = skaner.next().charAt(0);

                                    int flaga = 1;
                                    for (Gra.Action ruch : ruchy) {
                                        if (ruch.x == i && ruch.y == j) flaga = 0;
                                    }
                                    if (flaga == 1) {
                                        pw.println("false");
                                    } else
                                        pw.println(true);
                                    pw.println(board[i][j]);
                                }
                            }

                            pw.close();
                        } catch (FileNotFoundException ex) {
                            System.out.println("Nieznaleziono pliku");
                        }
                    }

                }
            });

            add(saveButton);
//LOAD BUTTON
            JButton loadButton = new JButton("LOAD");
            loadButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fc = new JFileChooser();
                    if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                        File plik = fc.getSelectedFile();
                        try {
                            Scanner skaner = new Scanner(plik);
                            boolean czybylo;
                            char[][] newBoard = new char[6][6];
                            gra.resetGameMoves();
                            panel.clearBoard();
                            gra.resetBoard();

                            for (int i = 0; i < 6; i++) {
                                for (int j = 0; j < 6; j++) {
                                    czybylo = skaner.nextBoolean();
                                    if (czybylo == true) {
                                        board[i][j] = skaner.next().charAt(0);
                                        char chr = board[i][j];
                                        gra.gameMove(chr, i, j);
                                        gra.setBoard(chr,i,j);
                                        String string = String.valueOf(chr);
                                        panel.setBoardText(i, j, string);

                                    }
                                }
                            }
                            skaner.close();
                        } catch (FileNotFoundException ex) {
                            System.out.println("Nieznaleziono pliku");
                        }
                        System.out.println(gra.gameActions);
                    }

                }

            });
            add(loadButton);


        }
    }


//--------------KEY LISTENERS----------------\\

    class BoardTextField extends JTextField implements KeyListener {
        private char value;
        private final Gra gra;
        private final int x, y;

        @Override
        public void keyTyped(KeyEvent e) {
            setText("");
            char chr = Character.toUpperCase(e.getKeyChar());
            if (chr >= 'A' && chr <= 'D') {
                value = chr;
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                value = ' ';
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            setText(String.valueOf(value));
            gra.gameMove(value, x, y);
        }

        public BoardTextField(Gra gra, int x, int y) {
            this.gra = gra;
            this.x = x;
            this.y = y;

            addKeyListener(this);
        }

    }

    //--------------PAINT PANEL----------------\\
    class Paint extends JPanel {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke((new BasicStroke()).getLineWidth() + 6));
            g2.setColor(new Color(153, 0, 0));
            //Otoczka Planszy
            g2.drawLine(95, 100, 695, 100);
            g2.drawLine(695, 100, 695, 700);
            g2.drawLine(695, 700, 95, 700);
            g2.drawLine(95, 700, 95, 100);
        }
    }


    //--------------BACKGROUND PANEL WITH HINTS----------------\\
    class BackgroundPanel extends JPanel {
        private Gra gra;
        private MainGamePanel panel;

        public BackgroundPanel(Gra gra,MainGamePanel panel) {
            this.gra = gra;
            this.panel=panel;

            setSize(200, 30);
            setLayout(null);
            JButton rules = new JButton("ZASADY");
            rules.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ImageIcon pic = new ImageIcon("instrukcja.png");
                    Image img = pic.getImage();
                    Image newimg = img.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
                    pic = new ImageIcon(newimg);


                    JOptionPane.showMessageDialog(rules, "W każdym rzędzie i w każdej kolumnie diagramu na rysunku powinny znaleźć się cztery litery - A, B, C, D - oraz dwa puste pola. \n" +
                            " Przy brzegu diagramu umieszczone są \"podpowiedzi\". Każda z nich określa i wskazuje pierwszą (z gwiazdką) lub drugą (bez gwiazdki)\n" +
                            " literę w danym rzędzie lub kolumnie \n" +
                            "Korzystając z tych podpowiedzi, należy wypełnić diagram 24 literami.\n" +
                            "\n", "Instrukcja", JOptionPane.INFORMATION_MESSAGE, pic);
                }
            });

            rules.setBackground(Color.pink);
            rules.setBounds(295, 0, 200, 30);
            add(rules);

            //CHOOSE COLOR OF BOARD///

            String[] wybierz={"Domyślny","Pink", "Blue" , "Cyan"};

            JComboBox box =new JComboBox(wybierz);
            box.setSelectedIndex(0);
            box.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JComboBox cb = (JComboBox)e.getSource();
                    String st = (String)cb.getSelectedItem();
                    if(st=="Pink") panel.changeColorBoard(Color.PINK);
                    else if(st=="Blue") panel.changeColorBoard(Color.blue);
                    else if(st=="Cyan") panel.changeColorBoard(Color.CYAN);
                    else if(st=="Domyślny") panel.changeColorBoard(new Color(255, 218, 185));

                }
            });
            box.setBounds(875,20,100,50);
            add(box);


            JLabel colorChose = new JLabel("Choose board color:");
            colorChose.setBounds(750,20,150,50);
            add(colorChose);




            ///////////////HINTS////////////////

//Hints left site
            JTextField hint1 = new JTextField();
            hint1.setFont(new Font("SansSerif", Font.BOLD, 15));
            hint1.setBounds(20, 125, 50, 50);
            hint1.setHorizontalAlignment(JTextField.CENTER);
            hint1.setBackground(new Color(255, 218, 185));
            hint1.setText(gra.normalSetHints("row", 0, 0, 2));
            add(hint1);

            JTextField hint2 = new JTextField();
            hint2.setFont(new Font("SansSerif", Font.BOLD, 15));
            hint2.setBounds(20, 225, 50, 50);
            hint2.setBackground(new Color(255, 218, 185));
            hint2.setHorizontalAlignment(JTextField.CENTER);
            hint2.setText(gra.normalSetHints("row", 1, 0, 1) + "*");
            add(hint2);

            JTextField hint3 = new JTextField();
            hint3.setFont(new Font("SansSerif", Font.BOLD, 15));
            hint3.setBounds(20, 325, 50, 50);
            hint3.setHorizontalAlignment(JTextField.CENTER);
            hint3.setBackground(new Color(255, 218, 185));
            hint3.setText(gra.normalSetHints("row", 2, 0, 1) + "*");
            add(hint3);

            JTextField hint4 = new JTextField();
            hint4.setFont(new Font("SansSerif", Font.BOLD, 15));
            hint4.setHorizontalAlignment(JTextField.CENTER);
            hint4.setBounds(20, 425, 50, 50);
            hint4.setBackground(new Color(255, 218, 185));
            hint4.setText(gra.normalSetHints("row", 3, 0, 2));
            add(hint4);

            JTextField hint5 = new JTextField();
            hint5.setFont(new Font("SansSerif", Font.BOLD, 15));
            hint5.setHorizontalAlignment(JTextField.CENTER);
            hint5.setBounds(20, 525, 50, 50);
            hint5.setBackground(new Color(255, 218, 185));
            hint5.setText(gra.normalSetHints("row", 4, 0, 2));
            add(hint5);


            //Hints right site
            JTextField hint6 = new JTextField();
            hint6.setFont(new Font("SansSerif", Font.BOLD, 15));
            hint6.setHorizontalAlignment(JTextField.CENTER);
            hint6.setBounds(710, 225, 50, 50);
            hint6.setBackground(new Color(255, 218, 185));
            hint6.setText(gra.reverseSetHints("row", 1, 0, 2));
            add(hint6);

            JTextField hint7 = new JTextField();
            hint7.setFont(new Font("SansSerif", Font.BOLD, 15));
            hint7.setHorizontalAlignment(JTextField.CENTER);
            hint7.setBounds(710, 425, 50, 50);
            hint7.setBackground(new Color(255, 218, 185));
            hint7.setText(gra.reverseSetHints("row", 3, 0, 2));
            add(hint7);

            JTextField hint8 = new JTextField();
            hint8.setFont(new Font("SansSerif", Font.BOLD, 15));
            hint8.setHorizontalAlignment(JTextField.CENTER);
            hint8.setBounds(710, 625, 50, 50);
            hint8.setBackground(new Color(255, 218, 185));
            hint8.setText(gra.reverseSetHints("row", 5, 0, 1) + "*");
            add(hint8);

            //hints top
            JTextField hint9 = new JTextField();
            hint9.setFont(new Font("SansSerif", Font.BOLD, 15));
            hint9.setHorizontalAlignment(JTextField.CENTER);
            hint9.setBounds(220, 35, 50, 50);
            hint9.setBackground(new Color(255, 218, 185));
            hint9.setText(gra.normalSetHints("col", 0, 1, 2));
            add(hint9);

            JTextField hint10 = new JTextField();
            hint10.setFont(new Font("SansSerif", Font.BOLD, 15));
            hint10.setHorizontalAlignment(JTextField.CENTER);
            hint10.setBounds(420, 35, 50, 50);
            hint10.setBackground(new Color(255, 218, 185));
            hint10.setText(gra.normalSetHints("col", 0, 3, 1) + "*");
            add(hint10);

            JTextField hint11 = new JTextField();
            hint11.setFont(new Font("SansSerif", Font.BOLD, 15));
            hint11.setHorizontalAlignment(JTextField.CENTER);
            hint11.setBounds(520, 35, 50, 50);
            hint11.setBackground(new Color(255, 218, 185));
            hint11.setText(gra.normalSetHints("col", 0, 4, 1) + "*");
            add(hint11);

            //hints bottom

            JTextField hint12 = new JTextField();
            hint12.setFont(new Font("SansSerif", Font.BOLD, 15));
            hint12.setHorizontalAlignment(JTextField.CENTER);
            hint12.setBounds(120, 720, 50, 50);
            hint12.setBackground(new Color(255, 218, 185));
            hint12.setText(gra.reverseSetHints("col", 0, 0, 2));
            add(hint12);

            JTextField hint13 = new JTextField();
            hint13.setFont(new Font("SansSerif", Font.BOLD, 15));
            hint13.setHorizontalAlignment(JTextField.CENTER);
            hint13.setBounds(220, 720, 50, 50);
            hint13.setBackground(new Color(255, 218, 185));
            hint13.setText(gra.reverseSetHints("col", 0, 1, 1) + "*");
            add(hint13);

            JTextField hint14 = new JTextField();
            hint14.setFont(new Font("SansSerif", Font.BOLD, 15));
            hint14.setHorizontalAlignment(JTextField.CENTER);
            hint14.setBounds(420, 720, 50, 50);
            hint14.setBackground(new Color(255, 218, 185));
            hint14.setText(gra.reverseSetHints("col", 0, 3, 2));
            add(hint14);

            JTextField hint15 = new JTextField();
            hint15.setFont(new Font("SansSerif", Font.BOLD, 15));
            hint15.setHorizontalAlignment(JTextField.CENTER);
            hint15.setBounds(620, 720, 50, 50);
            hint15.setBackground(new Color(255, 218, 185));
            hint15.setText(gra.reverseSetHints("col", 0, 5, 1) + "*");
            add(hint15);

        }
    }
}



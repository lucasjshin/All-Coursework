import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.DecimalFormat;

public class SudokuPlayer implements Runnable, ActionListener {

    // final values must be assigned in vals[][]
    int[][] vals = new int[9][9];
    Board board = null;

    /// --- AC-3 Constraint Satisfication --- ///

    // Useful but not required Data-Structures;
    ArrayList<Integer>[] globalDomains = new ArrayList[81];
    ArrayList<Integer>[] neighbors = new ArrayList[81];
    Queue<Arc> globalQueue = new LinkedList<Arc>();
    int counter = 0;

    /*
     * This method sets up the data structures and the initial global constraints
     * (by calling allDiff()) and makes the initial call to backtrack().
     * You should not change this method header.
     */
    private final void AC3Init() {
        // Do NOT remove these lines (required for the GUI)
        board.Clear();
        recursions = 0;

        /**
         * YOUR CODE HERE:
         * Create Data structures ( or populate the ones defined above ).
         * These will be the data structures necessary for AC-3.
         **/
        for (int i = 0; i < globalDomains.length; i++) {
            int val = vals[(int) Math.floor(i / 9)][i % 9];
            // System.out.println(val);
            globalDomains[i] = new ArrayList<Integer>();
            // System.out.println("val"+val);
            if (val == 0) {
                for (int j = 1; j <= 9; j++) {
                    if (globalDomains[i] == null) {
                        globalDomains[i] = new ArrayList<Integer>();
                    }
                    globalDomains[i].add(j);
                    // System.out.println(globalDomains[i].toString());
                }
            } else {
                if (globalDomains[i] == null) {
                    globalDomains[i] = new ArrayList<Integer>();
                }
                globalDomains[i].add(val);
            }
        }

        for (int i = 0; i < 9; i++) {
            int[] tester = { (i * 9), (i * 9) + 1, (i * 9) + 2, (i * 9) + 3, (i * 9) + 4, (i * 9) + 5, (i * 9) + 6,
                    (i * 9) + 7, (i * 9) + 8 };
            allDiff(tester);
        }
        for (int i = 0; i < 9; i++) {
            int[] tester = { i, 9 + i, 18 + i, 27 + i, 36 + i, 45 + i, 54 + i, 63 + i, 72 + i };
            allDiff(new int[] { i, 9 + i, 18 + i, 27 + i, 36 + i, 45 + i, 54 + i, 63 + i, 72 + i });
        }
        for (int i = 0; i <= 60; i += 3) {
            int[] temp = new int[9];
            int tempIndex = 0;
            for (int j = i; j < i + 3; j++) {
                for (int k = 0; k < 3; k++) {
                    // System.out.println(tempIndex);
                    temp[tempIndex] = j + (9 * k);
                    tempIndex++;
                }
            }
            if (i % 9 == 6) {
                i += 18;
            }
            allDiff(temp);
        }
        // System.out.println(globalDomains[6].toString());

        // Initial call to backtrack() on cell 0 (top left)
        boolean success = backtrack(0);
        if (success) {
            int domIndex = 0;
            for (int i = 0; i < vals.length; i++) {
                for (int j = 0; j < vals[i].length; j++) {
                    // System.out.println(globalDomains[domIndex].toString());
                    vals[i][j] = globalDomains[i*9+j].get(0);
                    domIndex++;
                }
            }
        }

        // Prints evaluation of run
        Finished(success);

    }

    /*
     * This method defines constraints between a set of variables.
     * Refer to the book for more details. You may change this method header.
     */
    private final void allDiff(int[] all) {
        // YOUR CODE HERE
        for (int j : all) {
            // System.out.println(j);
        }

        for (int i = 0; i < all.length; i++) {
            for (int j = 0; j < all.length; j++) {
                if (i != j) {
                    if (neighbors[all[i]] == null) {

                        neighbors[all[i]] = new ArrayList<Integer>();

                    }

                    neighbors[all[i]].add(all[j]);
                    Arc addQueue = new Arc(all[i], all[j]);
                    if (!globalQueue.contains(addQueue)) {

                        globalQueue.add(addQueue);

                    }

                }

            }

        }
        // System.out.println(neighbors[1].toString());

    }

    /*
     * This is the backtracking algorithm. If you change this method header, you
     * will have
     * to update the calls to this method.
     */
    private final boolean backtrack(int cell) {

        // Do NOT remove
        recursions += 1;

        boolean backCheck = true;
        // YOUR CODE HERE

        if (cell > 80) {

            return true;
        }

        if (vals[(int) Math.floor(cell / 9)][cell % 9] != 0) {

            backtrack(cell + 1);

        }

        if (!AC3()) {

            return false;

        } else {

            for (int i : globalDomains[cell]) {
                // System.out.println(i);
                ArrayList<Integer> v = new ArrayList<Integer>();
                v.add(i);
                globalDomains[cell] = v;

                if (backtrack(cell + 1)) {

                    // vals[(int)Math.floor(cell/9)][cell%9] = i;
                    // System.out.println("running"+vals[(int)Math.floor(cell/9)][cell%9]);
                    return true;

                }

            }

        }

        return false;

    }

    /*
     * This is the actual AC3 Algorithm. You may change this method header.
     */
    private final boolean AC3() {
        // System.out.println(globalQueue.toString());
        while (!globalQueue.isEmpty()) {
            // System.out.println(globalQueue.peek());
            Arc current = globalQueue.poll();
            
            boolean fix = Revise(current);

            if (fix) {
                 //System.out.println(globalDomains[current.Xi].toString()+"b");
                 //System.out.println(globalDomains[current.Xi].size());
                if (globalDomains[current.Xi].size() == 0) {
                    // System.out.println("run"+current.Xi+" "+current.Xj);
                    System.out.println("runs");
                    return false;
                }
                for (int k : neighbors[current.Xi]) {
                    if (k != current.Xj) {
                        // System.out.println(new Arc(k,current.Xi).toString());
                        globalQueue.add(new Arc(k, current.Xi));
                    }
                }
            }
        }

        return true;
    }

    /*
     * This is the Revise() procedure. You may change this method header.
     */
    private final boolean Revise(Arc t) {
        // System.out.println("Fdafdsfafdsafdsafdsafdsaf");
        // System.out.println(globalDomains[4].toString());
        // System.out.println(t.Xi+"i"+t.Xj+"j");
        boolean revised = false;

        ArrayList<Integer> toRemove = new ArrayList<Integer>();

        for (Integer i : globalDomains[t.Xi]) {
            boolean mismatch = false;
            for (Integer j : globalDomains[t.Xj]) {
                // System.out.println(j.compareTo(i));
                if (j.compareTo(i) != 0) {
                    mismatch = true;
                }

            }
            if (!mismatch) {
                // System.out.println(i);
                toRemove.add(i);
            }

        }
        for (Integer i : toRemove) {

            globalDomains[t.Xi].remove(i);
            System.out.println("xj: " + globalDomains[t.Xj].toString());
            System.out.println(globalDomains[t.Xi].toString());
            if (counter > 100) {
                System.out.println("xj: " + globalDomains[t.Xj].toString());
                System.out.println(globalDomains[t.Xi].toString());
                counter++;

            }
            revised = true;

        }

        // System.out.println(globalDomains[t.Xi].size());

        // System.out.println(revised);

        // YOUR CODE HERE
        // System.out.println(revised);
        return revised;
    }

    /*
     * This is where you will write your custom solver.
     * You should not change this method header.
     */
    private final void customSolver() {

        // set 'success' to true if a successful board
        // is found and false otherwise.
        boolean success = true;
        board.Clear();

        System.out.println("Running custom algorithm");

        // -- Your Code Here --

        Finished(success);

    }

    /// ---------- HELPER FUNCTIONS --------- ///
    /// ---- DO NOT EDIT REST OF FILE --- ///
    /// ---------- HELPER FUNCTIONS --------- ///
    /// ---- DO NOT EDIT REST OF FILE --- ///
    public final boolean valid(int x, int y, int val) {

        if (vals[x][y] == val)
            return true;
        if (rowContains(x, val))
            return false;
        if (colContains(y, val))
            return false;
        if (blockContains(x, y, val))
            return false;
        return true;
    }

    public final boolean blockContains(int x, int y, int val) {
        int block_x = x / 3;
        int block_y = y / 3;
        for (int r = (block_x) * 3; r < (block_x + 1) * 3; r++) {
            for (int c = (block_y) * 3; c < (block_y + 1) * 3; c++) {
                if (vals[r][c] == val)
                    return true;
            }
        }
        return false;
    }

    public final boolean colContains(int c, int val) {
        for (int r = 0; r < 9; r++) {
            if (vals[r][c] == val)
                return true;
        }
        return false;
    }

    public final boolean rowContains(int r, int val) {
        for (int c = 0; c < 9; c++) {
            if (vals[r][c] == val)
                return true;
        }
        return false;
    }

    private void CheckSolution() {
        // If played by hand, need to grab vals
        board.updateVals(vals);

        for (int v = 1; v <= 9; v++) {
            // Every row is valid
            for (int r = 0; r < 9; r++) {
                if (!rowContains(r, v)) {
                    board.showMessage("Value " + v + " missing from row: " + (r + 1));// + " val: " + v);
                    return;
                }
            }
            // Every column is valid
            for (int c = 0; c < 9; c++) {
                if (!colContains(c, v)) {
                    board.showMessage("Value " + v + " missing from column: " + (c + 1));// + " val: " + v);
                    return;
                }
            }
            // Every block is valid
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 3; c++) {
                    if (!blockContains(r, c, v)) {
                        return;
                    }
                }
            }
        }
        board.showMessage("Success!");
    }

    /// ---- GUI + APP Code --- ////
    /// ---- DO NOT EDIT --- ////
    enum algorithm {
        AC3, Custom
    }

    class Arc implements Comparable<Object> {
        int Xi, Xj;

        public Arc(int cell_i, int cell_j) {
            if (cell_i == cell_j) {
                try {
                    throw new Exception(cell_i + "=" + cell_j);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
            Xi = cell_i;
            Xj = cell_j;
        }

        public int compareTo(Object o) {
            return this.toString().compareTo(o.toString());
        }

        public String toString() {
            return "(" + Xi + "," + Xj + ")";
        }
    }

    enum difficulty {
        easy, medium, hard, random
    }

    public void actionPerformed(ActionEvent e) {
        String label = ((JButton) e.getSource()).getText();
        if (label.equals("AC-3"))
            AC3Init();
        else if (label.equals("Clear"))
            board.Clear();
        else if (label.equals("Check"))
            CheckSolution();
        // added
        else if (label.equals("Custom"))
            customSolver();
    }

    public void run() {
        board = new Board(gui, this);

        long start = 0, end = 0;

        while (!initialize())
            ;
        if (gui)
            board.initVals(vals);
        else {
            board.writeVals();
            System.out.println("Algorithm: " + alg);
            switch (alg) {
                default:
                case AC3:
                    start = System.currentTimeMillis();
                    AC3Init();
                    end = System.currentTimeMillis();
                    break;
                case Custom: // added
                    start = System.currentTimeMillis();
                    customSolver();
                    end = System.currentTimeMillis();
                    break;
            }

            CheckSolution();

            if (!gui)
                System.out.println("time to run: " + (end - start));
        }
    }

    public final boolean initialize() {
        switch (level) {
            case easy:
                vals[0] = new int[] { 0, 0, 0, 1, 3, 0, 0, 0, 0 };
                vals[1] = new int[] { 7, 0, 0, 0, 4, 2, 0, 8, 3 };
                vals[2] = new int[] { 8, 0, 0, 0, 0, 0, 0, 4, 0 };
                vals[3] = new int[] { 0, 6, 0, 0, 8, 4, 0, 3, 9 };
                vals[4] = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
                vals[5] = new int[] { 9, 8, 0, 3, 6, 0, 0, 5, 0 };
                vals[6] = new int[] { 0, 1, 0, 0, 0, 0, 0, 0, 4 };
                vals[7] = new int[] { 3, 4, 0, 5, 2, 0, 0, 0, 8 };
                vals[8] = new int[] { 0, 0, 0, 0, 7, 3, 0, 0, 0 };
                break;
            case medium:
                vals[0] = new int[] { 0, 4, 0, 0, 9, 8, 0, 0, 5 };
                vals[1] = new int[] { 0, 0, 0, 4, 0, 0, 6, 0, 8 };
                vals[2] = new int[] { 0, 5, 0, 0, 0, 0, 0, 0, 0 };
                vals[3] = new int[] { 7, 0, 1, 0, 0, 9, 0, 2, 0 };
                vals[4] = new int[] { 0, 0, 0, 0, 8, 0, 0, 0, 0 };
                vals[5] = new int[] { 0, 9, 0, 6, 0, 0, 3, 0, 1 };
                vals[6] = new int[] { 0, 0, 0, 0, 0, 0, 0, 7, 0 };
                vals[7] = new int[] { 6, 0, 2, 0, 0, 7, 0, 0, 0 };
                vals[8] = new int[] { 3, 0, 0, 8, 4, 0, 0, 6, 0 };
                break;
            case hard:
                vals[0] = new int[] { 1, 2, 0, 4, 0, 0, 3, 0, 0 };
                vals[1] = new int[] { 3, 0, 0, 0, 1, 0, 0, 5, 0 };
                vals[2] = new int[] { 0, 0, 6, 0, 0, 0, 1, 0, 0 };
                vals[3] = new int[] { 7, 0, 0, 0, 9, 0, 0, 0, 0 };
                vals[4] = new int[] { 0, 4, 0, 6, 0, 3, 0, 0, 0 };
                vals[5] = new int[] { 0, 0, 3, 0, 0, 2, 0, 0, 0 };
                vals[6] = new int[] { 5, 0, 0, 0, 8, 0, 7, 0, 0 };
                vals[7] = new int[] { 0, 0, 7, 0, 0, 0, 0, 0, 5 };
                vals[8] = new int[] { 0, 0, 0, 0, 0, 0, 0, 9, 8 };
                break;
            case random:
            default:
                ArrayList<Integer> preset = new ArrayList<Integer>();
                while (preset.size() < numCells) {
                    int r = rand.nextInt(81);
                    if (!preset.contains(r)) {
                        preset.add(r);
                        int x = r / 9;
                        int y = r % 9;
                        if (!assignRandomValue(x, y))
                            return false;
                    }
                }
                break;
        }
        return true;
    }

    public final boolean assignRandomValue(int x, int y) {
        ArrayList<Integer> pval = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));

        while (!pval.isEmpty()) {
            int ind = rand.nextInt(pval.size());
            int i = pval.get(ind);
            if (valid(x, y, i)) {
                vals[x][y] = i;
                return true;
            } else
                pval.remove(ind);
        }
        System.err.println("No valid moves exist.  Recreating board.");
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                vals[r][c] = 0;
            }
        }
        return false;
    }

    private void Finished(boolean success) {

        if (success) {
            board.writeVals();
            // board.showMessage("Solved in " + myformat.format(ops) + " ops \t(" +
            // myformat.format(recursions) + " recursive ops)");
            board.showMessage("Solved in " + myformat.format(recursions) + " recursive ops");

        } else {
            // board.showMessage("No valid configuration found in " + myformat.format(ops) +
            // " ops \t(" + myformat.format(recursions) + " recursive ops)");
            board.showMessage("No valid configuration found");
        }
        recursions = 0;

    }

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);

        System.out.println("Gui? y or n ");
        char g = scan.nextLine().charAt(0);

        if (g == 'n')
            gui = false;
        else
            gui = true;

        if (gui) {
            System.out.println("difficulty? \teasy (e), medium (m), hard (h), random (r)");

            char c = '*';

            while (c != 'e' && c != 'm' && c != 'n' && c != 'h' && c != 'r') {
                c = scan.nextLine().charAt(0);
                if (c == 'e')
                    level = difficulty.valueOf("easy");
                else if (c == 'm')
                    level = difficulty.valueOf("medium");
                else if (c == 'h')
                    level = difficulty.valueOf("hard");
                else if (c == 'r')
                    level = difficulty.valueOf("random");
                else {
                    System.out.println("difficulty? \teasy (e), medium (m), hard (h), random(r)");
                }
            }

            SudokuPlayer app = new SudokuPlayer();
            app.run();

        } else { // no gui

            boolean again = true;

            int numiters = 0;
            long starttime, endtime, totaltime = 0;

            while (again) {

                numiters++;
                System.out.println("difficulty? \teasy (e), medium (m), hard (h), random (r)");

                char c = '*';

                while (c != 'e' && c != 'm' && c != 'n' && c != 'h' && c != 'r') {
                    c = scan.nextLine().charAt(0);
                    if (c == 'e')
                        level = difficulty.valueOf("easy");
                    else if (c == 'm')
                        level = difficulty.valueOf("medium");
                    else if (c == 'h')
                        level = difficulty.valueOf("hard");
                    else if (c == 'r')
                        level = difficulty.valueOf("random");
                    else {
                        System.out.println("difficulty? \teasy (e), medium (m), hard (h), random(r)");
                    }

                }

                System.out.println("Algorithm? AC3 (1) or Custom (2)");
                if (scan.nextInt() == 1)
                    alg = algorithm.valueOf("AC3");
                else
                    alg = algorithm.valueOf("Custom");

                SudokuPlayer app = new SudokuPlayer();

                starttime = System.currentTimeMillis();

                app.run();

                endtime = System.currentTimeMillis();

                totaltime += (endtime - starttime);

                System.out.println("quit(0), run again(1)");
                if (scan.nextInt() == 1)
                    again = true;
                else
                    again = false;

                scan.nextLine();

            }

            System.out.println("average time over " + numiters + " iterations: " + (totaltime / numiters));
        }

        scan.close();
    }

    class Board {
        GUI G = null;
        boolean gui = true;

        public Board(boolean X, SudokuPlayer s) {
            gui = X;
            if (gui)
                G = new GUI(s);
        }

        public void initVals(int[][] vals) {
            G.initVals(vals);
        }

        public void writeVals() {
            if (gui)
                G.writeVals();
            else {
                for (int r = 0; r < 9; r++) {
                    if (r % 3 == 0)
                        System.out.println(" ----------------------------");
                    for (int c = 0; c < 9; c++) {
                        if (c % 3 == 0)
                            System.out.print(" | ");
                        if (vals[r][c] != 0) {
                            System.out.print(vals[r][c] + " ");
                        } else {
                            System.out.print("_ ");
                        }
                    }
                    System.out.println(" | ");
                }
                System.out.println(" ----------------------------");
            }
        }

        public void Clear() {
            if (gui)
                G.clear();
        }

        public void showMessage(String msg) {
            if (gui)
                G.showMessage(msg);
            System.out.println(msg);
        }

        public void updateVals(int[][] vals) {
            if (gui)
                G.updateVals(vals);
        }

    }

    class GUI {
        // ---- Graphics ---- //
        int size = 40;
        JFrame mainFrame = null;
        JTextField[][] cells;
        JPanel[][] blocks;

        public void initVals(int[][] vals) {
            // Mark in gray as fixed
            for (int r = 0; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                    if (vals[r][c] != 0) {
                        cells[r][c].setText(vals[r][c] + "");
                        cells[r][c].setEditable(false);
                        cells[r][c].setBackground(Color.lightGray);
                    }
                }
            }
        }

        public void showMessage(String msg) {
            JOptionPane.showMessageDialog(null,
                    msg, "Message", JOptionPane.INFORMATION_MESSAGE);
        }

        public void updateVals(int[][] vals) {

            // System.out.println("calling update");
            for (int r = 0; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                    try {
                        vals[r][c] = Integer.parseInt(cells[r][c].getText());
                    } catch (java.lang.NumberFormatException e) {
                        System.out.println("Invalid Board: row col: " + (r + 1) + " " + (c + 1));
                        showMessage("Invalid Board: row col: " + (r + 1) + " " + (c + 1));
                        return;
                    }
                }
            }
        }

        public void clear() {
            for (int r = 0; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                    if (cells[r][c].isEditable()) {
                        cells[r][c].setText("");
                        vals[r][c] = 0;
                    } else {
                        cells[r][c].setText("" + vals[r][c]);
                    }
                }
            }
        }

        public void writeVals() {
            for (int r = 0; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                    cells[r][c].setText(vals[r][c] + "");
                }
            }
        }

        public GUI(SudokuPlayer s) {

            mainFrame = new javax.swing.JFrame();
            mainFrame.setLayout(new BorderLayout());
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JPanel gamePanel = new javax.swing.JPanel();
            gamePanel.setBackground(Color.black);
            mainFrame.add(gamePanel, BorderLayout.NORTH);
            gamePanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            gamePanel.setLayout(new GridLayout(3, 3, 3, 3));

            blocks = new JPanel[3][3];
            for (int i = 0; i < 3; i++) {
                for (int j = 2; j >= 0; j--) {
                    blocks[i][j] = new JPanel();
                    blocks[i][j].setLayout(new GridLayout(3, 3));
                    gamePanel.add(blocks[i][j]);
                }
            }

            cells = new JTextField[9][9];
            for (int cell = 0; cell < 81; cell++) {
                int i = cell / 9;
                int j = cell % 9;
                cells[i][j] = new JTextField();
                cells[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                cells[i][j].setHorizontalAlignment(JTextField.CENTER);
                cells[i][j].setSize(new java.awt.Dimension(size, size));
                cells[i][j].setPreferredSize(new java.awt.Dimension(size, size));
                cells[i][j].setMinimumSize(new java.awt.Dimension(size, size));
                blocks[i / 3][j / 3].add(cells[i][j]);
            }

            JPanel buttonPanel = new JPanel(new FlowLayout());
            mainFrame.add(buttonPanel, BorderLayout.SOUTH);
            // JButton DFS_Button = new JButton("DFS");
            // DFS_Button.addActionListener(s);
            JButton AC3_Button = new JButton("AC-3");
            AC3_Button.addActionListener(s);
            JButton Clear_Button = new JButton("Clear");
            Clear_Button.addActionListener(s);
            JButton Check_Button = new JButton("Check");
            Check_Button.addActionListener(s);
            // buttonPanel.add(DFS_Button);
            JButton Custom_Button = new JButton("Custom");
            Custom_Button.addActionListener(s);
            // added
            buttonPanel.add(AC3_Button);
            buttonPanel.add(Custom_Button);
            buttonPanel.add(Clear_Button);
            buttonPanel.add(Check_Button);

            mainFrame.pack();
            mainFrame.setVisible(true);

        }
    }

    Random rand = new Random();

    // ----- Helper ---- //
    static algorithm alg = algorithm.AC3;
    static difficulty level = difficulty.easy;
    static boolean gui = true;
    static int numCells = 15;
    static DecimalFormat myformat = new DecimalFormat("###,###");

    // For printing
    static int recursions;
}

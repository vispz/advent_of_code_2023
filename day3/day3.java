package day3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

class Day3 {

    public static void main(String[] args) {
        var lines = readFileToCharArr("data/day3-input.txt");
        part1(lines);
        part2(lines);
    }

    private static void part1(ArrayList<ArrayList<Character>> lines) {
        var total = 0;
        System.out.println();
        for (int rowIx = 0; rowIx < lines.size(); rowIx++) {
            ArrayList<Character> ln = lines.get(rowIx);
            for (int startIx = 0; startIx < ln.size(); startIx++) {
                // If previous character was a digit it's already included
                if ((startIx > 0) && (Character.isDigit(ln.get(startIx - 1))))
                    continue;
                if (!Character.isDigit(ln.get(startIx)))
                    continue;
                int endIx = findNumEndIx(ln, startIx);
                if (isPartNum(lines, rowIx, startIx, endIx)) {
                    var partNum = charListToInt(ln, startIx, endIx);
                    total += partNum;
                    // System.out.printf("Part num %d", partNum);
                    // System.out.println();
                }
            }
        }
        System.out.println();
        System.out.printf("Part 1 Total: %d", total);
        System.out.println();
    }

    private static void part2(ArrayList<ArrayList<Character>> lines) {
        var gearPos2Nums = new HashMap<Integer, HashSet<Integer>>();
        System.out.println();
        int m = lines.size();
        for (int rowIx = 0; rowIx < lines.size(); rowIx++) {
            ArrayList<Character> ln = lines.get(rowIx);
            for (int startIx = 0; startIx < ln.size(); startIx++) {
                // If previous character was a digit it's already included
                if ((startIx > 0) && (Character.isDigit(ln.get(startIx - 1))))
                    continue;
                if (!Character.isDigit(ln.get(startIx)))
                    continue;
                int endIx = findNumEndIx(ln, startIx);
                var gearPos = adjGearIx(lines, rowIx, startIx, endIx);
                if (gearPos != null) {
                    var partNum = charListToInt(ln, startIx, endIx);
                    HashSet<Integer> gset;
                    int gearPosLin = pos2LinIx(gearPos, m);
                    if (gearPos2Nums.containsKey(gearPosLin))
                        gset = gearPos2Nums.get(gearPosLin);
                    else
                        gset = new HashSet<Integer>();
                    gearPos2Nums.put(gearPosLin, gset);
                    gset.add(partNum);
                    System.out.printf("Part num %d", partNum);
                    System.out.println();
                }
            }
        }
        Long total = 0L;
        for (var p : gearPos2Nums.values()) {
            if (p.size() != 2)
                continue;
            List<Integer> partNums = new ArrayList<>(p);
            total += partNums.get(0) * partNums.get(1);
        }
        System.out.println();
        System.out.println(gearPos2Nums);
        System.out.printf("Part 1 Total: %d", total);
        System.out.println();
    }

    private static Integer pos2LinIx(Tuple<Integer, Integer> t, int n) {
        return (t.x + 1) * n + t.y;
    }

    private static boolean isPartNum(ArrayList<ArrayList<Character>> lines, int rowIx, int startIx, int endIx) {
        int m = lines.size(), n = lines.get(rowIx).size();
        var validRows = new ArrayList<ArrayList<Character>>();
        validRows.add(lines.get(rowIx));
        if (rowIx > 0)
            validRows.add(lines.get(rowIx - 1));
        if (rowIx < m - 1)
            validRows.add(lines.get(rowIx + 1));
        for (var row : validRows) {
            int rowStIx = startIx > 0 ? startIx - 1 : startIx;
            int rowEndIx = endIx < (n - 1) ? endIx + 1 : endIx;
            for (int ix = rowStIx; ix <= rowEndIx; ix++)
                if (isSpecialChar(row.get(ix)))
                    return true;
        }
        return false;
    }

    private static Tuple<Integer, Integer> adjGearIx(ArrayList<ArrayList<Character>> lines, int rowIx, int startIx,
            int endIx) {
        int m = lines.size(), n = lines.get(rowIx).size();
        var validIxAndRows = new ArrayList<Tuple<Integer, ArrayList<Character>>>();
        validIxAndRows.add(new Tuple<>(rowIx, lines.get(rowIx)));
        if (rowIx > 0)
            validIxAndRows.add(new Tuple<>(rowIx - 1, lines.get(rowIx - 1)));
        if (rowIx < m - 1)
            validIxAndRows.add(new Tuple<>(rowIx + 1, lines.get(rowIx + 1)));
        for (var rix : validIxAndRows) {
            int rowStIx = startIx > 0 ? startIx - 1 : startIx;
            int rowEndIx = endIx < (n - 1) ? endIx + 1 : endIx;
            int adjRowIx = rix.x;
            List<Character> adjRow = rix.y;
            for (int ix = rowStIx; ix <= rowEndIx; ix++)
                if (adjRow.get(ix) == '*')
                    return new Tuple<>(adjRowIx, ix);
        }
        return null;
    }

    private static int charListToInt(ArrayList<Character> ln, int start_ix, int end_ix) {
        int num = 0;
        for (int i = start_ix; i <= end_ix; i++) {
            num = num * 10 + Character.getNumericValue(ln.get(i));
        }
        return num;
    }

    private static boolean isSpecialChar(Character c) {
        return ((c != '.') && (!Character.isDigit(c)));
    }

    public static int findNumEndIx(ArrayList<Character> ln, int start_ix) {
        for (int i = start_ix; i < ln.size(); i++)
            if (!Character.isDigit(ln.get(i)))
                return i - 1;
        return ln.size() - 1;
    }

    public static ArrayList<ArrayList<Character>> readFileToCharArr(String fl_name) {
        var lines = new ArrayList<ArrayList<Character>>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fl_name));
            String line;
            while ((line = br.readLine()) != null) {
                ArrayList<Character> line_chars = new ArrayList<Character>();
                for (char c : line.toCharArray()) {
                    line_chars.add(c);
                }
                lines.add(line_chars);
            }
            br.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return lines;
    }
}

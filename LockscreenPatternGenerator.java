// Created by Yash Hooda

/*    Hello Everyone 👋

I made this code to mainly generate a
 random Lockscreen Pattern sequence. 
 And it draws that pattern on console too.

Actually sometimes the drawing doesn't look
 good with random sequences so I've also added
 some of my favourite pattern sequences. 
Hope you'll use one of them
 as your lockscreen password 😁.

The program doesn't require any input just 
 hit the Run button multiple times to see 
 different outcomes and some pre-defined ones.

Your feedbacks are welcome :)

@author Yash Hooda, 2022/05/14
*/

import java.util.*;
import java.util.stream.*;

public class LockscreenPatternGenerator {

    public static void main(String[] args) {

        printTitleBox("Lockscreen pattern");

        printTitleBox("Randomly generated");
        DrawPattern.draw(getRandomSeq());

        printTitleBox("Default sequences");

        DrawPattern.draw(getExamples());

        System.out
                .println(new String(Base64.getDecoder().decode("CgoKCgogICAgICAgICAgICBDcmVhdGVkIGJ5OiBNaW5obwoKIA")));
    }

    private static int[][] getRandomSeq() {
        int totalPattern = 7;
        int[][] randSeq = new int[totalPattern][];

        for (int i = 0; i < randSeq.length; i++) {

            int patternLength = 4 + new Random().nextInt(6);

            randSeq[i] = Generator.getRandomSequence(patternLength);

        }

        return randSeq;
    }

    private static int[][] getExamples() {
        return new int[][] {
                { 7, 5, 3, 6, 4, 1, 9 },
                { 5, 6, 7, 2, 5, 3 },
                { 2, 5, 7, 3, 6, 8, 4, 1, 9 },
                { 5, 4, 6, 2, 8, 9, 7, 1 },
                { 9, 5, 2, 8, 7, 3, 6, 4 },
                { 2, 3, 5, 9, 8, 6, 4, 1, 7 },
                { 7, 5, 8, 2, 4, 6, 9, 1, 3 },
        };
    }

    private static void printTitleBox(String plainText) {

        String titleCase = Arrays.stream(plainText.split(" "))
                .map(t -> Character.toUpperCase(t.charAt(0))
                        + t.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));

        int len = titleCase.length();

        System.out.println(
                new StringBuffer()
                        .append("一".repeat(len - 3)).append("\n#  ")
                        .append(titleCase).append("  #\n")
                        .append("一".repeat(len - 3)).append("\n\n"));

    }
}

class DrawPattern implements ICnvDetails {

    public static void draw(int[][] seqs) {

        int i = 1;
        for (int[] seq : seqs) {

            Canvas ctx = new Canvas(cnvWidth, cnvHeight);

            System.out.printf("Seq #%d -> %s%n",
                    i++, Arrays.toString(seq));

            drawNumpad(ctx);
            drawUnlockPattern(seq, ctx);

            ctx.drawCanvas();
            System.out.println();
        }

    }

    private static void drawNumpad(Canvas ctx) {
        int ASCII_ONE = 49;

        for (int i = colStart; i <= colEnd; i += colStep) {
            for (int j = rowStart; j <= rowEnd; j += rowStep) {

                ctx.ellipse(i, j, xRadius, yRadius, arcAngle, 0);
                ctx.putChar(i, j, (char) ASCII_ONE++);
            }
        }

    }

    private static Map<Integer, List<Integer>> numCoords() {

        Map<Integer, List<Integer>> coordinates = new HashMap<>();

        int NUMS = 1;

        for (int i = colStart; i <= colEnd; i += colStep) {
            for (int j = rowStart; j <= rowEnd; j += rowStep) {
                coordinates.put(NUMS++, List.of(j, i));
            }
        }

        return coordinates;
    }

    private static void drawUnlockPattern(int[] arr, Canvas ctx) {

        IntStream.range(0, arr.length - 1)
                .forEachOrdered(i -> {

                    List<List<Integer>> lists = new LinkedList<>();

                    lists.add(numCoords().get(arr[i]));
                    lists.add(numCoords().get(arr[i + 1]));

                    ctx.line(lists.get(0).get(0),
                            lists.get(0).get(1),
                            lists.get(1).get(0),
                            lists.get(1).get(1));

                });
    }
}

interface ICnvDetails {
    int cnvWidth = 30;
    int cnvHeight = 20;

    int rowStart = 4;
    int rowStep = 10;
    int rowEnd = 24;

    int colStart = 3;
    int colStep = 6;
    int colEnd = 15;

    int xRadius = 2;
    int yRadius = 3;
    int arcAngle = 360;
}

class Generator {

    private static final String[] KEY_PAD = { "1", "2", "3", "4", "5", "6", "7", "8", "9" };

    public static int[] getRandomSequence(int patternLength) {

        List<List<String>> paths = new ArrayList<>();
        String start = KEY_PAD[new Random().nextInt(KEY_PAD.length)];

        while (patternLength > 1) {

            List<List<String>> oldPaths = paths;
            paths = new ArrayList<>();

            if (oldPaths.isEmpty()) {

                for (String nextStep : getNextSteps(start, new ArrayList<>())) {

                    List<String> l = new LinkedList<>();

                    l.add(start);
                    l.add(nextStep);
                    paths.add(l);

                }

            } else {

                for (List<String> oldPath : oldPaths) {
                    for (String nextStep : getNextSteps(oldPath.get(
                            oldPath.size() - 1), oldPath)) {

                        List<String> newPath = new LinkedList<>(oldPath);

                        newPath.add(nextStep);
                        paths.add(newPath);

                    }
                }
            }

            patternLength--;
        }

        return paths.get(new Random()
                .nextInt(paths.size()))
                .stream()
                .mapToInt(Integer::parseInt)
                .toArray();
    }

    private static List<String> getNextSteps(String currentStep, List<String> paths) {

        List<String> result = new ArrayList<>();

        for (String end : KEY_PAD) {

            if (!currentStep.equals(end) &&
                    !paths.contains(end)) {

                if ((!currentStep.equals("1") ||
                        !end.equals("3") ||
                        paths.contains("2")) &&

                        (!currentStep.equals("1") ||
                                !end.equals("7") ||
                                paths.contains("4"))
                        &&

                        (!currentStep.equals("1") ||
                                !end.equals("9") ||
                                paths.contains("5"))
                        &&

                        (!currentStep.equals("2") ||
                                !end.equals("8") ||
                                paths.contains("5"))
                        &&

                        (!currentStep.equals("3") ||
                                !end.equals("1") ||
                                paths.contains("2"))
                        &&

                        (!currentStep.equals("3") ||
                                !end.equals("7") ||
                                paths.contains("5"))
                        &&

                        (!currentStep.equals("3") ||
                                !end.equals("9") ||
                                paths.contains("6"))
                        &&

                        (!currentStep.equals("4") ||
                                !end.equals("6") ||
                                paths.contains("5"))
                        &&

                        (!currentStep.equals("6") ||
                                !end.equals("4") ||
                                paths.contains("5"))
                        &&

                        (!currentStep.equals("7") ||
                                !end.equals("1") ||
                                paths.contains("4"))
                        &&

                        (!currentStep.equals("7") ||
                                !end.equals("3") ||
                                paths.contains("5"))
                        &&

                        (!currentStep.equals("7") ||
                                !end.equals("9") ||
                                paths.contains("8"))
                        &&

                        (!currentStep.equals("8") ||
                                !end.equals("2") ||
                                paths.contains("5"))
                        &&

                        (!currentStep.equals("9") ||
                                !end.equals("7") ||
                                paths.contains("8"))
                        &&

                        (!currentStep.equals("9") ||
                                !end.equals("1") ||
                                paths.contains("5"))
                        &&

                        (!currentStep.equals("9") ||
                                !end.equals("3") ||
                                paths.contains("6"))) {

                    result.add(end);
                }
            }
        }

        return result;
    }
}

class Canvas {

    private final int width;
    private final int height;
    private final char[][] cnv;
    private final char marker = '.';
    private final char lineMarker = '$';

    public Canvas(int width, int height) {
        if (width < 0 || height < 0)
            throw new IllegalArgumentException();

        this.width = width;
        this.height = height;
        cnv = new char[height][width];

        IntStream.range(0, height)
                .forEach(i -> Arrays.fill(cnv[i], ' '));
    }

    public void putChar(int x, int y, char c) {
        cnv[x][y] = c;
    }

    private void drawLine(int x1, int y1, int x2, int y2) {

        int dX = x2 - x1;
        int dY = y2 - y1;
        double distance = Math.hypot(dX, dY);

        double xShiftPerStep = dX / distance;
        double yShiftPerStep = dY / distance;

        IntStream.iterate(0, i -> i <= distance, i -> i + 1)
                .forEach(i -> {

                    int x = x1 + (int) (i * xShiftPerStep);
                    int y = y1 + (int) (i * yShiftPerStep);
                    cnv[y][x] = lineMarker;

                });
    }

    public void line(int x1, int y1, int x2, int y2) {

        if ((x1 < 0 || x1 >= width) ||
                (x2 < 0 || x2 >= width) ||
                (y1 < 0 || y1 >= height) ||
                (y2 < 0 || y2 >= height))
            throw new IllegalArgumentException("can't draw");

        drawLine(x1, y1, x2, y2);
    }

    private void drawEllipse(int x, int y, int rx, int ry, int arcAngle, int rotatingAngle) {

        double sinb = Math.sin(Math.toRadians(rotatingAngle));
        double cosb = Math.cos(Math.toRadians(rotatingAngle));

        DoubleStream.iterate(0, i -> i < arcAngle, i -> i + 0.1)
                .forEach(i -> {

                    double sina = Math.sin(Math.toRadians(i));

                    double cosa = Math.cos(Math.toRadians(i));

                    int cx = (int) Math.round(x + rx * cosa * cosb - ry * sina * sinb);
                    int cy = (int) Math.round(y + rx * cosa * sinb + ry * sina * cosb);

                    safeDraw(cx, cy);

                });
    }

    private void safeDraw(int x, int y) {
        if ((x < 0 || x >= width * 2) ||
                (y < 0 || y >= height * 2))
            throw new IllegalArgumentException();

        cnv[x][y] = marker;
    }

    public void ellipse(int x, int y, int rx, int ry, int arcAngle, int rotationAngle) {

        drawEllipse(x, y, rx, ry, arcAngle, rotationAngle);
    }

    public void drawCanvas() {

        var res = new StringBuilder();

        char horizontalBorder = '-';
        char verticalBorder = '|';

        String border = String.valueOf(horizontalBorder)
                .repeat(Math.max(0, width + 2));

        res.append(border).append('\n');

        for (int i = 0; i < height; i++) {
            res.append(verticalBorder);

            for (int j = 0; j < width; j++) {
                res.append(cnv[i][j]);
            }

            res.append(verticalBorder).append('\n');
        }
        res.append(border);

        System.out.println(res);
    }
}

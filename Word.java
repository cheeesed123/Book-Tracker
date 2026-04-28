import java.util.Scanner;
//utility methods
public class Word {
    public static final Scanner word = new Scanner(System.in);
    // takes a char array, returns whether the target is in it.
    //returns true if a user-input char meets a condition, false otherwise.
    public static boolean nextCharToBoolean(String prompt, char condition1, char condition2) {
        System.out.print(prompt);
        // loop for valid input with try catch.
        while (true) {
            String b = word.nextLine().toLowerCase();
            if (b.isBlank()) {
                System.out.println("Answer cannot be blank. Please try again.");
                continue;
            }
            char c = b.charAt(0);
            //get input
            if (c == ' ') {
                System.out.println("Answer cannot be blank. Please try again.");
            }
            // if its the first value, true, else if second, false
            // if neither, retry. Blanks not permitted.
            if (c == condition1)
                return true;
            else if (c == condition2)
                return false;
            else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    public static void FatalError() {
        System.out.println("An error occurred while reloading the file, try to recover it by hand.");
        System.out.println("The program will exit now, press enter to continue.");
        Word.word.nextLine();
        System.exit(1);
    }
    public static boolean isDigit(String a) {
        try {
            Integer.valueOf(a);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
    public static String nextLine(String prompt) {
        System.out.print(prompt);
        return word.nextLine();
    }
    public static String nextline() {
        return nextLine("");
    }
    public static int nextInt(int moreT) {
        while (true) {
            String a = word.nextLine();
            if (a.isEmpty())
                return -1;
            else if (!isDigit(a)) {
                System.out.println("Invalid choice. Please try again.");
            } else {
                if (Integer.parseInt(a) > moreT)
                    return Integer.parseInt(a);
            }
                
        }
    }
    public static int nextInt(String prompt, String error, int moreT, int lessT) {
        while (true) {
            System.out.print(prompt);
            String a = word.nextLine();
            
            if (a.isEmpty())
                return -1;
            if (!isDigit(a)) {
                System.out.println(error);
            } else {
                int val = Integer.parseInt(a);
                //System.out.println(a + (val > moreT) + (val < lessT) + lessT);
                if (val > moreT && val < lessT)
                    return val;
                else System.out.println(error);
            }
                
        }
    }
    public static boolean contains(String[] options, String choice) {
        for (String a : options) {
            if (a.equals(choice)) {
                return true;
           }
        }
        return false;
    }
}

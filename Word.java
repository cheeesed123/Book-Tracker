import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
/**Some basic utility methods. This is home to some {@code final} variables.
 * <ul> 
 *  {@link #CSVVARCOUNT}
 *  {@link #CSVHEADER}
 *  {@link #word}
 * </ul>*/
public class Word {
    
    /**The CSV header for {@code books.csv}. It is defined because it often has to be specified to be exempted by the {@code Files.write()}.
     * @see #word
     * @see #CSVVARCOUNT
     */
    final public static String CSVHEADER = "ID,Title,Author,Series,Pages,PagesDone,dateCreated,finishedDate,Note";
    /**A count of the amount of items in {@link #CSVHEADER}. */
    final public static int CSVVARCOUNT = CSVHEADER.split(",").length;
    /**{@code word} is a {@link Scanner} object, it takes user-inputs through {@link System#in}.
     * @see #CSVVARCOUNT
     * @see #CSVHEADER
    */
    final public static Scanner word = new Scanner(System.in);
    final public static Path path = Paths.get("lib", "books.csv");
    /**
    @param prompt question to ask user
    @param condition1 what user-input must match for {@code true}
    @param condition2 what user-input must match for(@code false)
    @return true for when user-input matches {@code condition1}, false for when user-input matches {@code condition2}
    @see #nextInt(int)
    @see #nextInt(String, String, int, int)
    @see #nextline()
    @see #nextLine(String)
    else it just goes in a loop until the user gives  a valid input
    */
    public static boolean nextCharToBoolean(String prompt, char condition1, char condition2) {
        System.out.print(prompt);
        //loop for valid input with try catch.
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
            //if its the first value, true, else if second, false
            //if neither, retry. Blanks not permitted.
            if (c == condition1)
                return true;
            else if (c == condition2)
                return false;
            else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    /**Uses a try-catch to test if a digit can become an Integer.
     * @param a the digit to test
     * @return {@code false} if it isnt a digit, true otherwise.
     */
    public static boolean isDigit(String a) {
        try {
            Integer.valueOf(a);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
    /**
     * Asks the user a question, returns the answer
     * @param prompt the question to ask the user
     * @return String - the user-input
     * @see #nextCharToBoolean(String, char, char)
     * @see #nextInt(int)
     * @see #nextInt(String, String, int, int)
     * @see #nextline()
     */
    public static String nextLine(String prompt) {
        System.out.print(prompt);
        return word.nextLine();
    }
    public static String nextline() {
        return nextLine("");
    }
    /**Gets a user-input.
     * If its not a digit(which is tested with {@link #isDigit(String)}), it makes the user try again.
     * If its less than than the parameter, it also makes the user try again.
     * @param moreT the lowest allowed
     * @return -1 for no input, else the user-input parsed as an int for a valid input
     * @see #nextCharToBoolean(String, char, char)
     * @see #nextInt(String, String, int, int)
     * @see #nextline()
     * @see #nextLine(String)
     */
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
    /**A more complex {@link #nextInt(int)}.
     * @param prompt The question to ask the user
     * @param error The error message
     * @param moreT The lower limit
     * @param lessT The upper limit
     * @return -1 for blank, else the user-input
      */
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
    /**Makes a String of the same character repeated a certain amount of times.
     * @param c the character
     * @param times the amount of times
     * @return String, its length is equal to c.length() * times.
     */
    public static String repeatChar(String c, int times) {
        String sentence = "";
        for (int i = 0; i < times; i++) sentence += c;
        return sentence;
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

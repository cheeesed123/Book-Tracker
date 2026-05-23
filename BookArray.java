/**A class used for the {@link Runner#ListB()} program.
 * Its got some basic functions, its main purpose is for the ability to wrap text by word.
 * The {@code BookArray} class has a reference to a Book object when its created, details in the constructor doc.
 * @see #BookArray(Book, int)
 * @see Runner#PrintB()
 * @see Runner#ListB()
 * @see Book*/
public class BookArray {
    final private Book me;
    final private String line, num;
    /** The constructor, it has:
     * @param me a reference to a Book object representing the same Book/CSV line as this object.
     * @param i the index of this object in the array mentioned in {@link Runner#PrintB}
     * @see #toString(boolean, int)
     * 
    */
    public BookArray(Book me, int i) {
        this.me = me;
        this.line = (i + 1) + ". " + me.getTitle() + " by " + me.getAuthor();
        this.num = (i + 1) + ".";
    }
    //getters
    /**Short for:
     * Get Length Book Name
     * @return the length of the name itself, which is in the form:
     * <pre> title by author </pre>.
     * @see #GLBI()
     * @see #GLBW(int)
     */
    public int GLBN() {
        return line.length() - GLBI();
    }
    /**Short for:
     * Get Length Book I (index)
     * @return the length of the number in the form: "1."
     * @see #GLBN()
     * @see #GLBW(int)
     */
    public int GLBI() {
        return num.length();
    }
    /**Short for:
     * Get Length By Word
     * @param median the upper threshold of the amount of words.
     * @return the index of a {@link String} as to where the number will split the String into the most words allowed by the parameters.
     * An example would be:
     * <pre>
     * name = "This is a very long text file";
     * int b = GLBW(15); // b = 14 so 
     * String a1 = name.substring(0,GLBW(15));
     * String a2 = "This is a very";
     * boolean result a1.equals(a2); //returns true.
     * </pre>
     * @see #GLBN()
     * @see #GLBI()
     */
    private int GLBW(int median) {
        String[] lineA = line.split(" ");
        int guess = 0, i = 0;
        while (guess < median && i < lineA.length - 1) {
            guess += lineA[i].length() + 1;
            i++;
        }
        return guess;
    }
    /** Makes a String of {@code goal - current} whitespaces.
     * @param goal the total length of the string being manipulated
     * @param current the current length
     * @return a set of whitespaces in the form {@link String} that makes goal == current.
     * @see #toString()
    */
    private String whitespace(int goal, int current) {
        String sentence = "";
        while (current < goal) {
            sentence += " ";
            current++;
        }
        return sentence;
    }
    /**The big deal for this class, arguably the entire reason the class exists.
     * @param withPercent a boolean for if the percent should be added, which is in fact, quite pointless since this method is only ever called with {@code true}.
     * @param median At what point to start wrapping text.
     * @return a singular String to be printed that will represent one book object in the list.
     * @see #GLBW(int)
     * @see #GLBI()
     * @see #whitespace(int, int)
     * @see Runner#PrintB()
     */
    public String toString(boolean withPercent, int median) {
        String part1, part2 = "";
        int splitPoint;
        boolean twoPart;
        //first, we find if we need to split the line, and process that.
        twoPart = line.length() > median;
        if (twoPart) {
            //find closest word
            splitPoint = GLBW(median);
            part1 = line.substring(0,splitPoint);
            part2 = whitespace(GLBI() + 1, 0) + line.substring(splitPoint);
        } else {
            part1 = line;
        }
        part1 += whitespace(median + 2, part1.length());
        if (withPercent)
            part1 += me.getPercent() + "%";
        if (twoPart)
            part1 += "\n";
        return part1 + part2;
    }
}

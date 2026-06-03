import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.List;
/** The Object class, each representing one line in {@code books.csv}. These represent "books" */
public class Book {
    private String title;
    private String author;
    private String series;
    final private String dateCreated;
    private String dateFinished;
    private String note;
    private int pages;
    private int pagesDone;
    final private long ID;
    /**Constructor used when creating a NEW book (will append to CSV)
     * This is used when a user wants a new book created.
     * Also writes to the CSV using {@link #toCSV()}.
     * This is very similar to the other constructor, but since this one is under the condition its a brand new book, some variables are just set to their defaults.
     * @param ID a unique value assigned to the book. The {@link Runner#findNextID()} method ensures there's no gaps for neatness
     * @param title the title of the book.
     * @param author the author of the book.
     * @param series the series of the book, if no value is given it defaults to "None" as specified in {@link Runner#BookNew()}
     * @param pages the amount of pages of the book.
     * @param note A note. This one defaults to "None" as well,
     * @see #Book(long, String, String, String, int, int, String, String, String)
    */
    public Book(long ID, String title, String author, String series, int pages, String note) {
        LocalDate now = LocalDate.now();
        String month = String.valueOf(now.getMonthValue());
        String day = String.valueOf(now.getDayOfMonth());
        String year = String.valueOf(now.getYear());

        this.ID = ID;
        this.title = title;
        this.author = author;
        this.series = series;
        this.pages = pages;
        this.pagesDone = 0;

        this.dateCreated = String.join("/", month, day, year);
        this.dateFinished = "None";
        this.note = note;

        toCSV();
    }
    /**Constructor used when loading from CSV (DO NOT write back to file)
     * This Constructor is used when initially loading data from the CSV.
     * Its used to define every variable of the {@link Book} objects.
     * @param ID a unique value assigned to the book. The {@link Runner#findNextID()} method ensures there's no gaps for neatness
     * @param title the title of the book.
     * @param author the author of the book.
     * @param series the series of the book, if no value is given it defaults to "None" as specified in {@link Runner#BookNew()}
     * @param pages the amount of pages of the book.
     * @param pagesDone the amount of pages done in the book.
     * @param dateCreated the date the book was created. Is defined only when using the "for user" constructor. It is defined using {@link LocalDate#now()}.
     * @param dateFinished the date the book was finished, is default (and usually) set to "None". Is set to a real date by {@link #setPagesDone(int)}
     * @param note A note. This one defaults to "None" as well.
     * @see #Book(long, String, String, String, int, String)
     * @see Runner#BookNew()
     * @see #setPagesDone(int)
    */
    public Book(long ID, String title, String author, String series, int pages, int pagesDone, String dateCreated, String dateFinished, String note) {
        this.ID = ID;
        this.title = title;
        this.author = author;
        this.series = series;
        this.pages = pages;
        this.pagesDone = pagesDone;
        this.dateCreated = dateCreated;
        this.dateFinished = dateFinished;
        this.note = note;
    }
    /**appends to CSV
     * This method appends a new {@link Book} object to the end of the file using {@code Files.write()}.
     * @see #writeCSV()
    */
    private void toCSV() {
        try {
            String IDS = String.valueOf(ID);
            String pagesS = String.valueOf(pages);
            String pagesDoneS = String.valueOf(pagesDone);
            String line = String.join(",", IDS, title, author, series, pagesS, pagesDoneS, dateCreated, dateFinished, note) + "\n";
            Files.write(Word.path, line.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
        }
    }

    /**
     * Update pagesDone in-memory AND persist the change by replacing the matching CSV line.
     * Matching is done by title, author and pages (trimmed comparison).
     * 
     * While {@link #toCSV()} just appends to the end, this changes a line in the middle using the ID system.
     * @see #toCSV()
     */
    private void writeCSV(){
        String IDS, pagesS, pagesDoneS;
        try {
            IDS = String.valueOf(ID);
            pagesS = String.valueOf(pages);
            pagesDoneS = String.valueOf(pagesDone);
            List<String> lines = Files.readAllLines(Word.path, StandardCharsets.UTF_8);
            boolean updated = false;
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.trim().equalsIgnoreCase(Word.CSVHEADER)) continue; //header
                String[] parts = line.split(",", Word.CSVVARCOUNT);
                if (parts.length < 6) continue;
                String IDPart = parts[0].trim();
                if (String.valueOf(ID).equals(IDPart)) {
                    //replace the line (preserve spacing style)
                    lines.set(i, String.join(",", IDS, title, author, series, pagesS, pagesDoneS, dateCreated, dateFinished, note) + "\n");
                    updated = true;
                    break;
                }
            }
            if (updated) {
                Files.write(Word.path, lines, StandardCharsets.UTF_8);
            } else {
                //If the record wasn't found, append it to avoid losing the update
                String line = String.join(",", IDS, title, author, series, pagesS, pagesDoneS, dateCreated, dateFinished, note) + "\n";
                Files.write(Word.path, line.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while updating the file: " + e.getMessage());
        }
    }
    /** Deletes a line/book object. */
    public void delete() {
        try {
            List<String> lines = Files.readAllLines(Word.path, StandardCharsets.UTF_8);
            //Remove lines that match this book's ID (skip header)
            lines.removeIf(line -> {
                if (line.trim().equalsIgnoreCase(Word.CSVHEADER)) {
                    return false; //Don't remove header
                }
                return String.valueOf(ID).equals(line.substring(0,line.indexOf(",")));
            });
            //Write the modified content back to the file
            Files.write(Word.path, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("An error occurred while deleting from the file: " + e.getMessage());
        }
    }
    //setters
    /** A setter for {@code title}. Do I really need to explain? Its a setter. It sets the value to a new value.
     * @param title the new title
     * @see #setAuthor(String)
     * @see #setSeries(String)
     * @see #setPages(int)
     * @see #setPagesDone(int)
     * @see #setNote(String)
     */
    public void setTitle(String title) {
        this.title = title;
        writeCSV();
    }
    /** A setter for {@code author}. Do I really need to explain? Its a setter. It sets the value to a new value.
     * @param author the new author 
     * @see #setTitle(String)
     * @see #setSeries(String)
     * @see #setPages(int)
     * @see #setPagesDone(int)
     * @see #setNote(String)
     */
    public void setAuthor(String author) {
        this.author = author;
        writeCSV();
    }
    /** A setter for {@code series}. Do I really need to explain? Its a setter. It sets the value to a new value.
     * @param series the new series
     * @see #setTitle(String)
     * @see #setAuthor(String)
     * @see #setPages(int)
     * @see #setPagesDone(int)
     * @see #setNote(String)
     */
    public void setSeries(String series) {
        this.series = series;
        writeCSV();
    }
    /** A setter for {@code pages}. Do I really need to explain? Its a setter. It sets the value to a new value.
     * @param pages the new pages amount
     * @see #setTitle(String)
     * @see #setAuthor(String)
     * @see #setSeries(String)
     * @see #setPagesDone(int)
     * @see #setNote(String)
     */
    public void setPages(int pages) {
        this.pages = pages;
        writeCSV();
    }
    /** A setter for {@code pagesDone}. Do I really need to explain? Its a setter. It sets the value to a new value.
     * This one also modifies {@code dateFinished}, details in {@link #Book(long, String, String, String, int, int, String, String, String)}
     * @param pagesDone the new amount of pagesDone
     * @see #setTitle(String)
     * @see #setAuthor(String)
     * @see #setSeries(String)
     * @see #setPages(int)
     * @see #setNote(String)
     */
    public void setPagesDone(int pagesDone) {
        this.pagesDone = pagesDone;
        if (pagesDone == pages) {
            //set dateFinished date
            LocalDate now = LocalDate.now();
            String month = String.valueOf(now.getMonthValue());
            String day = String.valueOf(now.getDayOfMonth());
            String year = String.valueOf(now.getYear());
            dateFinished = String.join("/", month, day, year);
        }
        writeCSV();
    }
    /** A setter for {@code note}. Do I really need to explain? Its a setter. It sets the value to a new value.
     * @param note the new note
     * @see #setTitle(String)
     * @see #setAuthor(String)
     * @see #setSeries(String)
     * @see #setPages(int)
     * @see #setPagesDone(int)
     */
    public void setNote(String note) {
        this.note = note;
        writeCSV();
    }
//getters
    /**A getter.
     * @return String
     * @see #getAuthor()
     * @see #getSeries()
     * @see #getPages()
     * @see #getPagesDone()
     * @see #getDateCreated()
     * @see #getDateFinished()
     * @see #getNote()
     * @see #getID()
     * @see #getPercent()
     */
    public String getTitle() {
        return title;
    }
    /**A getter.
     * @return String
     * @see #getTitle()
     * @see #getSeries()
     * @see #getPages()
     * @see #getPagesDone()
     * @see #getDateCreated()
     * @see #getDateFinished()
     * @see #getNote()
     * @see #getID()
     * @see #getPercent()
     */
    public String getAuthor() {
        return author;
    }
    /**A getter.
     * @return String
     * @see #getTitle()
     * @see #getAuthor()
     * @see #getPages()
     * @see #getPagesDone()
     * @see #getDateCreated()
     * @see #getDateFinished()
     * @see #getNote()
     * @see #getID()
     * @see #getPercent()
     */
    public String getSeries() {
        return series;
    }
    /**A getter.
     * @return int
     * @see #getTitle()
     * @see #getAuthor()
     * @see #getSeries()
     * @see #getPagesDone()
     * @see #getDateCreated()
     * @see #getDateFinished()
     * @see #getNote()
     * @see #getID()
     * @see #getPercent()
     */
    public int getPages() {
        return pages;
    }
    /**A getter.
     * @return int
        @see #getTitle()
     * @see #getAuthor()
     * @see #getSeries()
     * @see #getPages()
     * @see #getDateCreated()
     * @see #getDateFinished()
     * @see #getNote()
     * @see #getID()
     * @see #getPercent()
     */
    public int getPagesDone() {
        return pagesDone;
    }
    /**A getter.
     * @return String
        @see #getTitle()
     * @see #getAuthor()
     * @see #getSeries()
     * @see #getPages()
     * @see #getPagesDone()
     * @see #getDateFinished()
     * @see #getNote()
     * @see #getID()
     * @see #getPercent()
     */
    public String getDateCreated() {
        return dateCreated;
    }
    /**A getter.
     * @return String
     * @see #getTitle()
     * @see #getAuthor()
     * @see #getSeries()
     * @see #getPages()
     * @see #getPagesDone()
     * @see #getDateCreated()
     * @see #getNote()
     * @see #getID()
     * @see #getPercent()
     */
    public String getDateFinished() {
        return dateFinished;
    }
    /**A getter.
     * @return String
     * @see #getTitle()
     * @see #getAuthor()
     * @see #getSeries()
     * @see #getPages()
     * @see #getPagesDone()
     * @see #getDateCreated()
     * @see #getDateFinished()
     * @see #getID()
     * @see #getPercent()
     */
    public String getNote() {
        return note;
    }
    /**A getter.
     * @return long
     * @see #getTitle()
     * @see #getAuthor()
     * @see #getSeries()
     * @see #getPages()
     * @see #getPagesDone()
     * @see #getDateCreated()
     * @see #getDateFinished()
     * @see #getNote()
     * @see #getPercent()
     */
    public long getID() {
        return ID;
    }
    /**A getter.
     * @return double
     * @see #getTitle()
     * @see #getAuthor()
     * @see #getSeries()
     * @see #getPages()
     * @see #getPagesDone()
     * @see #getDateCreated()
     * @see #getDateFinished()
     * @see #getNote()
     * @see #getID()
     */
    public int getPercent() {
        if (pages == 0) return 0;
        return (int) Math.round((double) pagesDone / pages * 10000) / 100;
    }
    /** A boolean for when the book is finished.
     * @return boolean- true if the % complete defined by {@link #getPercent()} equals 100. False otherwise.
     */
    public boolean isFinished() {
        return getPercent() == 100;
    }
    /** For <pre> {@code 
     *  Book a = new Book(0, "title", "author", "series", 20, "note");
     *  System.out.println(a.toString());
     * }</pre>
     * Output:
     * <pre> {@code
     * title by author in the series series with 20 pages, created 1/1/1111, and finished 1/2/1111 is 40% done, with these notes: "note".
     * } </pre>
     * This method is not ever used.
     * @return {@code String} - the main sentence
     * @see #viewForm()
     * 
    */
    @Override
    public String toString() {
        String sentence = "";
        sentence += title;
        sentence += " by " + author;
        sentence += " in the " + series;
        sentence += " series with " + pages + " pages,";
        sentence += " created " + dateCreated;
        if (!dateFinished.equals("None")) sentence += ", and finished " + dateFinished;
        sentence += " is " + getPercent() + "% done";
        if (!note.isBlank()) sentence += ", with these note: \"" + note + "\"";
        sentence += ".";
        return sentence;
    }
    /**
     * The main method for viewing books. Is used in {@link Runner#BookList()}
     * An example output, where all variables equal their default values:
     * <pre> {@code
     * Title: ""
     * Author: ""
     * Series: ""
     * Pages: 0
     * Pages done: 0
     * Percent done 0%
     * _______________
     * Date created: 0/0/0000
     * Date finished: 0/0/0000
     * Note: ""
     * } </pre>
     * @return String
     * @see Runner#BookList()
     */
    public String viewForm() {
        //make underline for separator
        String sentence = "", dateCreatedL = "Date created: " + dateCreated;
        //make text box
        sentence +=   "Title: " + title;
        sentence += "\nAuthor: " + author;
        sentence += "\nSeries: " + series;
        sentence += "\nPages: " + pages;
        sentence += "\nPages done: " + pagesDone;
        sentence += "\nPercent done: " + getPercent() + "%";
        sentence += "\n" + Word.repeatChar("_", dateCreatedL.length());
        sentence += "\n" + dateCreatedL;
        sentence += "\nDate finished: " + ((dateFinished.equals("None") ? "Not yet finished." : dateFinished));
        sentence += "\nNote: " + note;
        if (isFinished()) sentence += "\nBook finished! Good job!";
        return sentence;
    }
}

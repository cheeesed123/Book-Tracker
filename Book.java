import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Book {
    private String title;
    private String author;
    private String series;
    private int pages;
    private int pagesDone;
    private long ID;
    private static final Path path = Runner.path;
    // Constructor used when creating a NEW book (will append to CSV)
    public Book(long ID, String title, String author, String series, int pages) {
        this(ID, title, author, series, pages, 0);
        toCSV(ID, title, author, series, pages, pagesDone);
    }
    public Book(long ID, String title, String author, int pages) {
        this(ID, title, author, "None", pages, 0);
        toCSV(ID, title, author, "None", pages, pagesDone);
    }
    // Constructor used when loading from CSV (DO NOT write back to file)
    public Book(long ID, String title, String author, String series, int pages, int pagesDone) {
        this.ID = ID;
        this.title = title;
        this.author = author;
        this.series = series;
        this.pages = pages;
        this.pagesDone = pagesDone;
    }
    public Book(long ID, String title, String author, int pages, int pagesDone) {
        this.ID = ID;
        this.title = title;
        this.author = author;
        this.series = "None";
        this.pages = pages;
        this.pagesDone = pagesDone;
    }
    private void toCSV(long ID, String title, String author, String series, int pages, int pagesDone) {
        try (FileWriter writer = new FileWriter(path.toString(), true)) {
            writer.write(ID + "," + title + "," + author + "," + series + "," + pages + "," + pagesDone + "\n");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
        }
    }

    /**
     * Update pagesDone in-memory AND persist the change by replacing the matching CSV line.
     * Matching is done by title, author and pages (trimmed comparison).
     */
//setters
    private void writeCSV(){
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            boolean updated = false;
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.trim().equalsIgnoreCase("ID,Title,Author,Series,Pages,PagesDone")) continue; // header
                String[] parts = line.split(",", 6);
                if (parts.length < 6) continue;
                String IDPart = parts[0].trim();
                //String titlePart = parts[1].trim();
                //String authorPart = parts[2].trim();
                //String seriesPart = parts[3].trim();
                //String pagesPart = parts[4].trim();
                if (String.valueOf(ID).equals(IDPart)) {
                    // replace the line (preserve spacing style)
                    lines.set(i,ID + "," + title + "," + author + "," + series + "," + pages + "," + pagesDone);
                    updated = true;
                    break;
                }
            }
            if (updated) {
                Files.write(path, lines, StandardCharsets.UTF_8);
            } else {
                // If the record wasn't found, append it to avoid losing the update
                try (FileWriter writer = new FileWriter(path.toString(), true)) {
                    writer.write(ID + "," + title + "," + author + "," + series + "," + pages + "," + pagesDone + "\n");
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while updating the file: " + e.getMessage());
        }
    }
    public void setPagesDone(int pagesDone) {
        this.pagesDone = pagesDone;
        writeCSV();
    }
    public void setTitle(String title) {
        this.title = title;
        writeCSV();
    }
    public void setAuthor(String author) {
        this.author = author;
        writeCSV();
    }
    public void setSeries(String series) {
        this.series = series;
        writeCSV();
    }
    public void setPages(int pages) {
        this.pages = pages;
        writeCSV();
    }
//getters
    public double getPercent() {
        if (pages == 0) return 0.0;
        return (double) Math.round((double) pagesDone / pages * 10000) / 100;
    }
    public int getPages() {
        return pages;
    }
    public int getPagesDone() {
        return pagesDone;
    }
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }
    public String getSeries() {
        return series;
    }
    public long getID() {
        return ID;
    }
    @Override
    public String toString() {
        return title + " by " + author + " in the " + series + " series with " + pages + " pages is " + getPercent() + "% done.";
    }

    public String viewForm() {
        String sentence = "Title: " + title + "\nAuthor: " + author + "\nSeries: " + series + "\nPages: " + pages + "\nPages done: " + pagesDone + "\nPercent done: " + getPercent() + "%";
        if (getPercent() == 100) sentence += "\nBook finished! Book will be removed next time.";
        return sentence;
    }
}

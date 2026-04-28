import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Runner {
    //path to csv
    public static final Path path = Paths.get("lib", "books.csv");
    //array list
    public static ArrayList<Book> books = new ArrayList<>();
    //ID, its value varies a lot
    private static long ID = 0;
    //main
    public static void main(String[] args){
        System.out.println("Welcome to book tracker.");
        try {
            loadBooks();
        } catch (FileNotFoundException e) {
            System.err.println("Error has to do with filename/lack of file: " + e.getMessage());
            boolean createFile = Word.nextCharToBoolean("Would you like to create a new file? [Y/N]\n", 'y', 'n');
            if (createFile) 
                makeNewFile();
            else {
                System.out.println("Okay, goodbye.");
                System.exit(0);
            }
        } catch (NumberFormatException e) {
            System.out.println("Error with ID or pages, check CSV.");
        }
        choice();
    }
    //makes a new books.csv file
    private static void makeNewFile() {
        boolean success = true;
        try {
            Files.createFile(Path.of("lib", "books.csv"));
            Path path2 = Paths.get("lib", "books.csv");
            try (FileWriter writer = new FileWriter(path2.toString())) {
                writer.write("ID,Title,Author,Series,Pages,PagesDone\n");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while creating the file.");
            success = false;
        } finally {
            System.out.println("File " + (success ? "created, " : "failed, ") + "restarting...");
            Restart();
        }
    }
    //attempts to take the data in the csv and convert it to an ArrayList.
    private static void loadBooks() throws FileNotFoundException, NumberFormatException {
        try (Scanner fileScanner = new Scanner(new File(path.toString()))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (line.trim().equalsIgnoreCase("ID,Title,Author,Series,Pages,PagesDone"))
                    continue;
                String[] parts = line.split(",", 6);
                if (parts.length < 3)
                    continue;
                ID = Integer.parseInt(parts[0].trim());
                String title = parts[1].trim();
                String author = parts[2].trim();
                String series = parts[3].trim();
                int pages = Integer.parseInt(parts[4].trim());
                int pagesDone = parts.length >= 6 ? Integer.parseInt(parts[5].trim()) : 0;
                if (pages == pagesDone)
                    continue; //remove complete from database, leave in CSV for storage.
                books.add(new Book(ID, title, author, series, pages, pagesDone));
            }
            if (books.isEmpty()) {
                System.out.println("Your books are either empty, or all complete. Time to add more!");
            }
        }
    }
    //attempts to take the data in the ArrayList and convert it to the csv.
    private static void loadCSV() {
        try (FileWriter writer = new FileWriter(path.toString())) {
            //reset csv
            writer.flush();
            writer.write("ID,Title,Author,Series,Pages,PagesDone\n");
            for (int i = 0; i < books.size(); i++) {
                long bookID = books.get(i).getID();
                String title = books.get(i).getTitle();
                String author = books.get(i).getAuthor();
                String series = books.get(i).getSeries();
                int pages = books.get(i).getPages();
                int pagesDone = books.get(i).getPagesDone();
                writer.write(bookID + "," + title + "," + author + "," + series + "," + pages + "," + pagesDone + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while resetting the file.");
        }
    }
    //main menu, shouldnt be possible to escape.
    private static void choice() {
        //since its the main menu, we can just put it in a loop.
        while (true) {
            String menu = """
                    What would you like to do?

                    [N]ew book
                    [L]ist books
                    [E]dit book
                    [O]ther Edit
                    [S]tats on time
                    [P]rint a series
                    [R]eset zone

                    [C]ycle (restarts program)
                    [Q]uit
                    """;
            System.out.println(menu);
            char choice = ' ';
            String b;
            /* We get an input, if its more than one charaacter, 
            and is a possible code, we make choice = b. Else, it errors and restarts.
            */
            while (true) {
                b = Word.nextLine("Enter choice: ").toLowerCase();
                if (b.length() == 1 && "nleorspcq".contains(b)) {
                    choice = b.charAt(0);
                    break;
                }
                System.out.println("Invalid choice. Please try again.");
            }
            //based on the value of choice, we are able to then decide which program we should run.


            /*
            
            problems involving all search functionality, such as method search, book search, etc.
            Problems are caused by Scanner inputs, might be connected to new Word class.
            
            
            
            */
            switch (choice) {
                case 'n' -> BookNew();
                case 'l' -> BookList();
                case 'e' -> BookEdit();
                case 'o' -> BookMore();
                case 's' -> Time();
                case 'p' -> BookSeries();
                case 'r' -> ResetsAndReloads();
                case 'c' -> Restart();
                case 'q' -> Quit();
                default  -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    //quits
    private static void Quit() {
        System.out.println("Goodbye!");
        System.exit(0);
    }
    //restarts via BAT/BASH file
    private static void Restart() {
        boolean restart = Word.nextCharToBoolean("Are you sure? [Y/N]\n", 'y', 'n');
        if (restart) {
            System.out.println("Okay.");
        }
        else {
            System.out.println("Going back to menu...");
            return;
        }
        System.out.println("Restarting...");
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
        ProcessBuilder pb;
        if (isWindows) {
            String shell = System.getenv("SHELL");
            if (shell != null && shell.contains("bash")) {
                pb = new ProcessBuilder("bash", "runMe.sh", "1");
            } else {
                pb = new ProcessBuilder("cmd.exe", "/c", "call", "runMe.bat", "1"); 
            }
        } else {
            pb = new ProcessBuilder("sh", "runMe.sh", "1");
        }
        pb.inheritIO();
        try {
            pb.start().waitFor();
            System.exit(0);
        } catch (IOException | InterruptedException e) {
            System.err.println("An error occurred while restarting the program." + e.getMessage());
        }
    }
    //a mini menu for reset functionalities.
    private static void ResetsAndReloads() {
        String menu = """
                Resets:
                [R]Reset both (array and csv)
                [L]Reload both
                [C]Reload csv only
                [A]Reload array only
                """;
        System.out.println(menu);
        //get user input
        String b;
        while (true) {
            b = Word.nextLine("Enter choice: ").toLowerCase();
            if ("rlca".contains(b.substring(0,1)))
                break;
            else if (b.isEmpty())
                return;
            else
                System.out.println("Invalid choice. Please try again.");
        }
        char bC = b.charAt(0);
        /*
        r is for reset
        l is for loading both
        c is for csv reset
        a is for array reset -- most useful
        */
        switch (bC) {
            case 'r' -> {
                boolean reset = Word.nextCharToBoolean("Are you sure? This is permanent. [Y/N]\n", 'y','n');
                if (reset) {
                    System.out.println("If you say so.\nResetting books...");
                } else {
                    System.out.println("Thought so, back to menu...");
                    return;
                }
                books.clear();
                try (FileWriter writer = new FileWriter(path.toString())) {
                    writer.write("ID,Title,Author,Series,Pages,PagesDone\n");
                } catch (IOException e) {
                    System.out.println("An error occurred while resetting the file.");
                }
                System.out.println("Both reset.");
                return;
            }
            case 'l' -> {
                    //reset CSV
                loadCSV();
                //reset arrayList
                books.clear();
                try {
                    loadBooks();
                } catch (FileNotFoundException e) {
                    Word.FatalError();
                }
                System.out.println("Both reloaded.");
            }
            case 'c' -> {
                loadCSV();
                System.out.println("CSV reset.");
            }
            case 'a' -> {
                books.clear();
                try {
                    loadBooks();
                } catch (FileNotFoundException e) {
                    Word.FatalError();
                }
                System.out.println("Array reset.");
            }
        }
        System.out.println("Hit enter to continue...");
        Word.nextline();
    }
    //time calcs and such, this is the main time method.
    private static void Time() {
        int a = ListB();
        if (a == -1)
            System.out.println("Hit enter to escape or type a number to view a book.");
        int book;
        //user chooses a number from a list, its tested if its a real number. If it is, make it book.
        //if book also matches parameters, the code exits the loop.
        while (true) {
            if (a == -1) {
                book = Word.nextInt("","Enter a number. Please try again.", -1, books.size() + 1) - 1;
            } else
                book = a;

            if (book < 0)
                return;
            break;
        }
        //define dueDate --defined in timeCalcs now
        
        
        boolean countWeekend = Word.nextCharToBoolean("Count Weekends? [Y/N] ", 'y', 'n');
        double result = timeCalcs(book, countWeekend);
        
        System.out.println("You have to read " + Math.round(result) / 100.0 + " pages per day to finish on time.");
        System.out.println("Hit enter to continue...");
        Word.nextline();
    }
    //validates the due date and prepares it for use
    private static LocalDate validateDate() {
        int month, day, year;
        LocalDate dueDate;
        OUTER:
        while (true) {
            System.out.print("Due date (m/d/y): ");
            String due = Word.nextline().trim();
                String[] parts = due.split("/", 3);
                if (parts.length != 3) {
                    System.out.println("Invalid date format. Please try again.");
                    continue;
                }
            //safety check, takes date, splits it into parts, checks they can be numbers, then does math.
            for (String part : parts) {
                if (!Word.isDigit(part)) {
                    System.out.println("Invalid date format. Please try again.");
                    continue OUTER;
                }
            }

            month = Integer.parseInt(parts[0]);
            day = Integer.parseInt(parts[1]);
            year = Integer.parseInt(parts[2]);

            //normalize two-digit years (e.g. 26 -> 2026)
            if (year < 100) 
                year += (LocalDate.now().getYear()/100)*100; 
            
            try {
                dueDate = LocalDate.of(year, month, day); //validates month/day (incl. leap years)
            } catch (java.time.DateTimeException ex) {
                System.out.println("Invalid date format. Please try again.");
                continue;
            }

            if (dueDate.isBefore(LocalDate.now())) {
                System.out.println("Date is in the past. Please enter today or a future date.");
                continue;
            }
                break;
                // these safety checks have now ensured this is a valid date.
        }
        return dueDate;
    }
    //does the calcs for Time()
    private static double timeCalcs(int book, boolean countWeekend) {
        LocalDate now = LocalDate.now(); //                                             current time
        LocalDate dueDate = validateDate(); //                                          time in the future
        int pagesLeft = books.get(book).getPages() - books.get(book).getPagesDone(); // pages left
        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(now, dueDate); // days between the two dates
        long weeks = java.time.temporal.ChronoUnit.WEEKS.between(now, dueDate); //      weeks between the two dates
        double value = 0;
        if (countWeekend) //                                                            account for weekend
            daysBetween -= (weeks * 2);
        
        if (daysBetween <= 0) //                                                        lowk not sure I think its to make sure the value is positive maybe?
            daysBetween = 1;
        
        if (pagesLeft <= 0) { //                                                        if the book is finished print this.
            System.out.println("No pages left — you're already finished or up to date.");

        } else { 
            if (daysBetween <= 0) { //due today
                value = pagesLeft;
            } else {
                value = (double) pagesLeft / daysBetween * 100;
            }
        }
        return value;
    }
    //capitalizes most words in a title.
    private static String capitalize(String str) {
        //protection against blanks
        if (str.isBlank())
            return str;
        String[] words = str.split(" ");
        for (int i = 0; i < words.length; i++) {
            if (i != 0 && Word.contains(new String[]{"and", "or", "the", "a", "an", "in", "on", "with"},words[i].toLowerCase())) {
                words[i] = words[i].toLowerCase();
                continue;
            }
            words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1);
        }
        return String.join(" ", words);
    }
    //returns the index
    private static int find(String[] words, char option) {
        if (words.length > 0) {
            for (int i = 0; i < words.length; i++) {
                if (words[i].charAt(0) == option)
                    return i;
            }
        }
        return -1;
    }

    //makes new book objects
    private static void BookNew() {
        //header
        System.out.println("New book:");
        //Arrays and variables
        String[] requests = {"Title", "Author", "Series (optional)", "Pages"};
        int Rlength = requests.length;
        String[] responses = new String[Rlength];
        boolean emptySeries = false;
        //loop
        //asks for an input from requests, checks its valid, continues if it is.
        for (int i = 0; i < Rlength; i++) {
            System.out.print(requests[i] + ": ");
            responses[i] = Word.nextline();
            if (i == 2 && responses[i].isBlank()) {
                emptySeries = true;
                continue;
            } else if (i >= 3) {
                if (responses[i].isBlank()) {
                    System.out.println("Pages cannot be blank. Please try again.");
                    i--;
                    continue;
                } else if (!Word.isDigit(responses[3])) {
                    System.out.println("Pages must be a number. Please try again.");
                    i--;
                    continue;
                }
            } else if (responses[i].isBlank() && i != 2) {
                System.out.println("Invalid input. Please try again.");
                i--;
                continue;
            }
            responses[i] = capitalize(responses[i]);
        }
        System.out.print("Hit enter to continue...");
        Word.nextline();
        ID = findNextID();
        //variables for constructor/readability
        String title = responses[0];
        String author = responses[1];
        String series = responses[2];
        int pages = Integer.parseInt(responses[3]);
        if (emptySeries)
            books.add(new Book(ID, title, author, pages));
        else
            books.add(new Book(ID, title, author, series, pages));
    }
    //lists books with the assistance of listB
    private static void BookList() {
        int a = ListB();
        if (a == -1)
            System.out.println("Hit enter to escape or type a number to view a book.");
        int book;
        while (true) {
            if (a == -1) {
                book = Word.nextInt("","Enter a number. Please try again.",-1, books.size() + 1) - 1;
            } else
                book = a;
            if (book < 0)
                return;

            System.out.println("_________________________________");
            System.out.println(books.get(book).viewForm());
            System.out.println("_________________________________");
            System.out.println("Hit enter to escape or type a number to view a book.");
             a = -1;
        }
    }
    //finds the nextID that hasnt been used, by minimum.
    private static long findNextID() {
        // If no books, start with ID 1
        if (books.isEmpty())
            return 1;
        //Ooooo a data stream
        long[] ids = books.stream().mapToLong(Book::getID).toArray();
        Arrays.sort(ids);
        
        // Check for gaps in the sequence
        for (int i = 0; i < ids.length - 1; i++) {
            if (ids[i + 1] - ids[i] > 1) {
                return ids[i] + 1;
            }
        }
        // No gaps found, return the next ID after the highest
        return ids[ids.length - 1] + 1;
    }
    //only displays 21 books at one time, consistent increments.
    //Will pause the display at 21 so you can choose a book you see at the current moment.
    //if it is paused, and you type a number on screen, it returns that number.
    private static int ListB() {
        System.out.println("Books:");
        if (books.isEmpty()) {
            System.out.println("No books to display, try adding some!");
            return -1;
        }
        int counter = 0;
        int times = 0;
        for (int i = 0; i < books.size(); i++) {
            System.out.println((i + 1) + ". " + books.get(i).getTitle() + " by " + books.get(i).getAuthor());
            int r = 23;
            if (times == 0)
                r = 21;
            if (counter == r) {
                counter = 0;
                times++;
                System.out.print("Hit enter to continue...");
                String a = Word.nextline();
                int parsedA;
                if (Word.isDigit(a)) {
                    parsedA = Integer.parseInt(a);
                    if (parsedA > 0 && parsedA < books.size() + 1)
                        return parsedA - 1;
                }
            }
            counter++;
        }
        return -1;
    }
    //A version of ListB that is more customizable, used for a list of texts.
    private static int ListB(ArrayList<String> books, String term) {
        System.out.println(term +":");
        int counter = 0;
        int times = 0;
        for (int i = 0; i < books.size(); i++) {
            System.out.println((i + 1) + ". " + books.get(i));
            int r = 23;
            if (times == 0)
                r = 21;
            if (counter == r) {
                System.out.print("Hit enter to continue...");
                counter = 0;
                times++;
                String a = Word.nextline();
                int parsedA;
                if (Word.isDigit(a)) {
                    parsedA = Integer.parseInt(a);
                    if (parsedA > 0 && parsedA < books.size() + 1)
                        return parsedA - 1;
                }
            }
            counter++;
        }
        return -1;
    }
    //used to display series by books
    private static void ListA(ArrayList<Book> t, String term) {
        System.out.println(term +":");
        int counter = 0;
        int times = 0;
        int maxDigits = String.valueOf(t.size()).length() + 1;
        for (int i = 0; i < t.size(); i++) {
            int itemNumber = i + 1;
            int itemDigits = String.valueOf(itemNumber).length();
            String padding = " ".repeat(maxDigits - itemDigits);
            System.out.println(itemNumber + "." + padding + t.get(i).getTitle());
            int r = 23;
            if (times == 0)
                r = 21;
            if (counter == r) {
                System.out.print("Hit enter to continue...");
                counter = 0;
                times++;
                Word.nextline();
            }
            counter++;
        }
    }
    //Used to edit the number of pages complete in a book
    private static void BookEdit() {
        int a = ListB();
        if (a == -1)
            System.out.println("Type a number to edit a book or hit enter to escape.");
        int book;
        while (true) {
            if (a == -1) {
                book = Word.nextInt("", "Invalid choice. Please try again.", -1, books.size() + 1 + 1) - 1;
                
                if (book < 0)
                    return;
            } else
                book = a;
            int pagesDone = Word.nextInt("Pages done: ", "Invalid choice. Please try again.", -1, books.get(book).getPages() + 1);
            if (pagesDone < 0)
                return;
            books.get(book).setPagesDone(pagesDone);
            return;
        }
    }
    //Used to edit the other data fields
    private static void BookMore() {
        int a = ListB();
        String menu = """
                Options:
                [T]itle
                [A]uthor
                [S]eries
                [P]ages
                Use other edit for pages done
                [Q]uit
                """;
        String[] wordTypes = {"title","author","series","pages"};

        // if its -1, then it still needs a real book value.
        //find book
        if (a == -1)
            System.out.println("Type a number to edit a book or hit enter to escape.");
        int book;
        while (true) {
            if (a == -1) {
                book = Word.nextInt("","Invalid choice. Please try again.", -1, books.size() + 1) - 1;
                
                if (book < 0)
                    return;
            } else
                book = a;
            System.out.println(menu);
            break;
        }
        char option = ' ';
        //define which characteristic
        String prompt;
        while (true) {
            String temp = Word.nextline();
            //blank protection
            if (!temp.isBlank())
                option = temp.toLowerCase().charAt(0);
            else {
                System.out.println("Invalid choice. Please try again.");
                continue;
            }
            //incorrect option protection
            if (!"taspq".contains(String.valueOf(option))) {
                System.out.println("Invalid choice. Please try again.");
                continue;
            }
            break;
        }
        if ("tas".contains(String.valueOf(option)))
            prompt = "New " + wordTypes[find(wordTypes, option)] + ": ";
        else if (option == 'p')
            prompt = "New pages value: ";
        else {
            System.out.println("Returning to menu.");
            return;
        }
        //get value
        String a1;
        while (true) {
            System.out.print(prompt);
            a1 = Word.nextline();
            if (a1.isBlank() || a1.equals("-1"))
                System.out.println("Answer cannot be blank. Please try again.");
            else if (option == 'p' && !Word.isDigit(a1))
                System.out.println("Pages must be a number. Please try again.");
            else {
                a1 = capitalize(a1);
                break;
            }
        }
        if ("tas".contains(String.valueOf(option)))
            System.out.println("New " + wordTypes[find(wordTypes, option)] + " is " + a1);
        else if (option == 'p')
            System.out.println("New pages value is " + a1);
        switch(option) {
            case 't' -> books.get(book).setTitle(a1);
            case 'a' -> books.get(book).setAuthor(a1);
            case 's' -> books.get(book).setSeries(a1);
            case 'p' -> books.get(book).setPages((int)Integer.valueOf(a1));
        }
    }
    private static void BookSeries() {
        //define series
        ArrayList<String> series = new ArrayList<>();
        ArrayList<Book> inSeries = new ArrayList<>();
        for (Book book : books) {
            String seriesT = book.getSeries();
            if (!seriesT.equals("None")) {
                if (!Word.contains(series.toArray(String[]::new), seriesT)) {
                    series.add(seriesT);
                }
                inSeries.add(book);
            }
        }

        //print series
        int a = ListB(series, "Series");
        if (a == -1)
            System.out.println("Type a number to view a series or hit enter to escape.");
        int seriesChoice;
        while (true) {
            if (a == -1) {
                seriesChoice = Word.nextInt("","Invalid choice. Please try again.", -1, series.size() + 1) - 1;
                
                if (seriesChoice < 0)
                    return;
            } else
                seriesChoice = a;
            break;
        }
        //print books in series
        String seriesC = series.get(seriesChoice);
        //create a sublist
        ArrayList<Book> sublist = new ArrayList<>();
        for (Book book : inSeries) {
            if (book.getSeries().equals(seriesC))
                sublist.add(book);
        }
        ListA(sublist, "Books in " + seriesC);
        System.out.println("Hit enter to continue...");
        Word.nextline();
    }
}
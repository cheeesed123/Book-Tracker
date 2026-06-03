from pathlib import Path
newBooks = Path("NewFiles") / "books.csv"
try:
    with open(newBooks, "w") as r:
        r.write("ID,Title,Author,Series,Pages,PagesDone,dateCreated,finishedDate,Note\n")
except:
    print("Something went wrong!")
else:
    print("books.csv updated!")
exit
@echo off
echo Compiling files, this might take some time.
set ISCC="C:\Users\ollie\AppData\Local\Programs\Inno Setup 6\ISCC.exe" 
set c=%% complete  
set p="..\Book-Tracker\"
:: making files, these make the jar and .exe
javac src\*.java -d bin >nul
jar cfe Book-Tracker.jar Runner -C bin . >nul
echo 10%c% //.,)~~~~~(,.\\
:: deleting things that shouldnt exist
:: if NewFiles exists, delete it.
if exist "NewFiles\" (
    rd /s /q NewFiles
)
if exist "runtime\" (
    rd /s /q runtime
)
md NewFiles >nul
echo 20%c% ^|^|` * ^| ^| * `^|^|
:: checking Book-Tracker
if exist %p% (
    if exist "%p%Documentation\" (
        :: if Book-Tracker exists, and documentation exists, delete it.
        cd ..\Book-Tracker
        rd /s /q Documentation
        cd "..\Book Tracker"
    )
    if exist "%p%runtime\" (
        :: if runtime is in Book-Tracker, delete it
        cd ..\Book-Tracker
        rd /s /q runtime
        cd "..\Book Tracker"
    )
    if exist "%p%BTSetup.exe" (
        :: if the exe is in Book-Tracker, delete it.
        cd ..\Book-Tracker
        del BTSetup.exe >nul
        cd "..\Book Tracker"
    )
    if exist "%p%src\Book-Tracker.jar" (
        :: if the .jar exists, delete it
        cd ..\Book-Tracker
        del src\Book-Tracker.jar >nul
        cd "..\Book Tracker"
    )
    if exist "%p%BookTracker.bat" (
        :: if BookTracker command exist, delete it
        cd ..\Book-Tracker
        del BookTracker.bat
        cd "..\Book Tracker"
    )
    if exist "%p%NewFiles\" (
        :: if an old NewFiles folder exists, delete it
        cd ..\Book-Tracker
        rd /s /q NewFiles
        cd "..\Book Tracker"
    )
) else (
    echo "Book-Tracker doesn't seem to exist yet."
    exit 1
)
echo 30%c% ^|^| #  ,_.  # ^|^|
::making runtime
jlink --no-header-files --no-man-pages --compress=zip-8 --strip-debug --add-modules java.base --output runtime
:: grouping new files
copy runMe.* NewFiles >nul
copy thanks!.txt NewFiles >nul
copy BookTracker.bat NewFiles >nul
move Book-Tracker.jar NewFiles >nul
echo 40%c% !!    \_/    !!
move runtime NewFiles >nul
md NewFiles\Documentation >nul
rd /s /q Documentation >nul
echo 50%c% /_\  ~~~~~~ /_\
md Documentation >nul
javadoc -private -Xdoclint:none -d NewFiles\Documentation src\*.java >nul 2>nul
xcopy NewFiles\Documentation Documentation /e /i >nul
echo 60%c% ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
rd /s /q bin
md bin
::new books.csv
type nul > NewFiles\books.csv
python "Make books.py" >nul
echo 70%c% Book Tracker V6
::exit /b
move NewFiles ..\Book-Tracker >nul
:: we have now created our new files, and we can begin moving them after deleting the old ones.
cd ..\Book-Tracker
echo 80%c% by Sam Smith :o

rd /s /q lib
md lib
:: Move new files into place
cd NewFiles
move Book-Tracker.jar ..\src\ >nul
move thanks!.txt ..\src\ >nul
echo 90%C% Made with love,
move runMe.bat .. >nul
move runMe.sh .. >nul
move BookTracker.bat .. >nul
move books.csv ..\lib\ >nul
move Documentation .. >nul
move runtime .. >nul
cd ..
rd NewFiles >nul
cd ..\"Book Tracker"
:: make runtime folder and .exe
jlink --no-header-files --no-man-pages --compress=zip-8 --strip-debug --add-modules java.base --output runtime
%ISCC% "download script.iss" >nul
echo 100%c%use it please!!
echo Compile finished! Check Book-Tracker.
exit /b 0
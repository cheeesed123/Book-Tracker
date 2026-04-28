@echo off
:: --- WINDOWS BATCH SECTION ---
if "%1"=="1" ( 
    echo Restart successful!
    echo:
)
java -jar src\Book-Tracker.jar
rd /s /q bin
::pause

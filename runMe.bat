@echo off
if "%~1"=="1" ( 
    echo Restart successful!
    echo.
)
"%~dp0runtime\bin\java" -jar src\Book-Tracker.jar

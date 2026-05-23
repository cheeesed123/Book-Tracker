@echo off
:: --- WINDOWS BATCH SECTION ---
if "%1"=="1" ( 
    echo Restart successful!
    echo:
)
java -cp bin Runner
::pause

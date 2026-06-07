@echo off
setlocal enabledelayedexpansion
if "%~1"=="" (
    goto Help
) else if "%~1"=="start" (
    call runMe.bat
) else if "%~1"=="reset" (
    echo This will delete your current data permanently. Continue?
    set /p "a=[Y/N]: "
    echo !a!
    if /I "!a!"=="y" (
        if exist "lib\books.csv" (del lib\books.csv)
        echo The CSV has been deleted, the program will replace it upon startup.
    ) else (
        echo Okay, nothing will been deleted.
    )
) else (
    goto Help
)
goto :EOF
:Help
    echo Help menu:
    echo     Syntax:
    echo         BookTracker [command]
    echo     Commands:
    echo         start - start the Book Tracker program
    echo         reset - delete the current CSV
    echo         help  - show this menu, is default command.
    exit /b 0

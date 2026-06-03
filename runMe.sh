if [[ ${1:-0} -eq 1 ]]; then
if [[ -d "bin" ]]; then
	echo -e "Restart successful!\n"
fi
fi
"$(dirname "$0")/runtime/bin/java.exe" -jar src/Book-Tracker.jar
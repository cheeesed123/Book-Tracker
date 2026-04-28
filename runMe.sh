if [[ ${1:-0} -eq 1 ]]; then
	echo -e "Restart successful!\n"
fi
java -jar src/Book-Tracker.jar

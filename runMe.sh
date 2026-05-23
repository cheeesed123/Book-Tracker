if [[ ${1:-0} -eq 1 ]]; then
if [[ -d "bin" ]]; then
	echo -e "Restart successful!\n"
fi
fi
java -cp bin Runner
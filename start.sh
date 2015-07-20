#!/bin/bash
#echo -e "Compilation ..."
#javac *.java

#echo -e "Starting the Server ..."
#java ServeurCentrale 8000

#sleep 2

#./start2.sh &> /dev/null


case "$1" in
    "-server")
	
	echo -e "Compilation ..."
	javac *.java
	echo -e "Starting the Server"
    java ServeurCentrale 8000
    ;;

    "-client")
		case "$2" in 
			"1")
				java Client localhost 8000 8500 "Bruno Mars - Treasure"
				;;
			"2")
				java Client localhost 8000 8501 "John Lennon - Imagine" 
				;;
			"3")
				java Client localhost 8000 8502 "Beatles - All you need is love" 
				;;
			"4")
				java Client localhost 8000 8503 "The Queen - The show must go on"
				;;
			"5")
				java Client localhost 8000 8504 "Lucio Battisti - Il mio Canto Libero"
				;;
			"6")
				java Client localhost 8000 8505 "Tarzan - SoundTrack"
				;;
		esac
	;;
	*)
		echo -e "Usage: "
		echo -e "./start.sh -server"
		echo -e "./start.sh -client N"
	;;
esac
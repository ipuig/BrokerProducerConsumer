compile:
	javac -cp bin -d bin src/*.java

producer:
	java -cp bin Main producer

consumer:
	java -cp bin Main consumer

broker:
	java -cp bin Main broker

ip:
	java -cp bin Main ip

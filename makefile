compile:
	sh cleanup.sh; javac -cp bin -d bin src/*.java

text:
	java -cp bin Main producer
audio:
	java -cp bin Main producer audio
video:
	java -cp bin Main producer video

consumer:
	java -cp bin Main consumer

broker:
	java -cp bin Main broker

ip:
	java -cp bin Main

compile:
	javac -cp bin -d bin src/*.java

producer:
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

clean:
	rm consumer_data/audio/* && consumer_data/video/* && consumer_data/text/*

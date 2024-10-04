JAR_FILE := target/eclang-0.1.0.jar

install:
	sudo apt update
	sudo apt install -y gcc
	sudo apt install -y build-essential
	sudo apt install -y libapr1 libapr1-dev

build:
	mvn clean package

compile:
	java -jar $(JAR_FILE) -c $(FILE)
	gcc out.c -o out -I/usr/include/apr-1.0 -L/usr/lib -lapr-1

compile-run:
	java -jar $(JAR_FILE) -c $(FILE)
	gcc out.c -o out -I/usr/include/apr-1.0 -L/usr/lib -lapr-1
	./out
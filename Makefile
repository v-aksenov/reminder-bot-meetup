clean:
	./gradlew clean

build: clean
	./gradlew bootJar

up: build
	docker-compose build
	docker-compose up

run: build
	./gradlew bootRun

test: clean
	./gradlew test

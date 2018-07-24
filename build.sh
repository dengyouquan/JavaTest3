cd Exam1;
mvn clean;
mvn assembly:assembly;
cp target/app-jar-with-dependencies.jar ../docker/java/app.jar
cd ../docker/mysql;
docker build -t mysql .;
cd ../java;
docker build -t java .;
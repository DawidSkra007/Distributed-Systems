In order to pass urls through the command line use this command (while running those services in seperate terminals): (B)
mvn compile exec:java -pl broker -Dexec.args="http://0.0.0.0:9001/quotations http://0.0.0.0:9002/quotations"

and run this command to run client while havng broker and services running: (C)
mvn clean exec:java -pl client

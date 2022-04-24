#/bin/bash

cd $(dirname "$0")
javac Life.java && jar cfe Life.jar Life Life.class Life.java && java Life $*
read

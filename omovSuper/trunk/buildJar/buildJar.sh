#!/bin/bash

cd ..
cd ../omovCore
mvn clean install
cd ../omovQtjApi
mvn clean install
cd ../omovQtjImpl
mvn clean install
cd ../omovCore
mvn -Dmaven.test.skip=true assembly:assembly

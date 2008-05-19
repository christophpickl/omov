#!/bin/bash

######################
# 
# OurMovies - Yet another movie manager
# Copyright (C) 2008 Christoph Pickl (christoph_pickl@users.sourceforge.net)
# http://omov.sourceforge.net
# 
################################################################

cd ..

cd ../omovSuper
# all submodules will be cleaned + compiled + tested + installed2repo
mvn clean install

cd ../omovCore
#mvn -Dmaven.test.skip=true assembly:assembly
mvn assembly:assembly


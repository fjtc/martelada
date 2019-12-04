#!/bin/sh
################################################################################
# martelada - a very simple Java resource file editor 
# Copyright (C) 2019 Fabio Jun Takada Chino
# 
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#  
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
# 
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <https://www.gnu.org/licenses/>.
################################################################################

# Detemine my home
MY_HOME=$(dirname $0)
oldDir=$(pwd)
cd "$MY_HOME"
MY_HOME=$(pwd)
cd $oldDir

# Find my jar
JAR_NAME=$(find $MY_HOME -maxdepth 1 -regex '.+martelada-.+\.jar$' | sort | tail -n1)

# Jar
if [ -z "$JAVA_HOME" ]; then
    JAVA_CMD="java"
else
    JAVA_CMD="$JAVA_HOME/bin/java"
fi

# Show properties
echo "Home: $MY_HOME"
echo "jar file: $JAR_NAME"

"$JAVA_CMD" -jar "$JAR_NAME"


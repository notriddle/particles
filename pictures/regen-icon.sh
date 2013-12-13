#!/bin/sh
#
# This file is a part of Particles live wallpaper
# Copyright 2013 Michael Howell <michael@notriddle.com>
#
# Particles is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# Particles is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with Particles. If not, see <http://www.gnu.org/licenses/>.

inkscape -e ../src/main/res/drawable-mdpi/ic_launcher.png -w 48 -h 48 -z ic_launcher.svg
inkscape -e ../src/main/res/drawable-hdpi/ic_launcher.png -w 72 -h 72 -z ic_launcher.svg
inkscape -e ../src/main/res/drawable-xhdpi/ic_launcher.png -w 96 -h 96 -z ic_launcher.svg
inkscape -e ../src/main/res/drawable-xxhdpi/ic_launcher.png -w 144 -h 144 -z ic_launcher.svg

inkscape -e ../src/main/res/drawable-mdpi/thumbnail.png -w 96 -h 96 -z thumbnail.svg
inkscape -e ../src/main/res/drawable-hdpi/thumbnail.png -w 144 -h 144 -z thumbnail.svg
inkscape -e ../src/main/res/drawable-xhdpi/thumbnail.png -w 192 -h 192 -z thumbnail.svg
inkscape -e ../src/main/res/drawable-xxhdpi/thumbnail.png -w 288 -h 288 -z thumbnail.svg


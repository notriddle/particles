/*
 * This file is a part of Particles live wallpaper.
 * Copyright 2013 Michael Howell <michael@notriddle.com>
 *
 * Particles is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Particles is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Particles. If not, see <http://www.gnu.org/licenses/>.
 */

package com.notriddle.balls;
import android.preference.PreferenceActivity;
import android.os.Bundle;

public class Settings extends PreferenceActivity {
    @Override public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        addPreferencesFromResource(R.xml.settings);
    }
};


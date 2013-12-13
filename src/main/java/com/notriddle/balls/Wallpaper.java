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
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.view.Choreographer;
import android.view.SurfaceHolder;

public class Wallpaper extends WallpaperService {
    SharedPreferences mPrefs;
    private static final int FPS = 64;

    private static class Ball {
        public double r;
        public double x;
        public double y;
        public double vX;
        public double vY;
        public double m;
        public Ball(double w, double h, int size, double velocity, int kurt) {
            r = normal(kurt) * size;
            x = Math.random() * (w-r)+r;
            y = Math.random() * (h-r)+r;
            vX = (normal(kurt) - 0.5)*velocity/FPS*2;
            vY = (normal(kurt) - 0.5)*velocity/FPS*2;
            // mass equals area times density. All circles have density 1/pi.
            m = r*r;
        }
    };

    // returns the new v1.
    private static double collide(double m1, double m2, double v1, double v2) {
        return ((m1 - m2)/(m1 + m2))*v1 + ((2.0 * m2)/(m1 + m2))*v2;
    }
    private static double normal(int level) {
        double retVal = 0;
        for (int i = 0; i != level; ++i) {
            retVal += Math.random();
        }
        return retVal/level;
    }

    private class MoveBallsThread extends Thread {
        Engine engine;
        public MoveBallsThread(Engine e) {
            engine = e;
        }
        public void run() {
            try {
            while (!interrupted()) {
                Ball[] balls = engine.balls;
                int width = engine.width;
                int height = engine.height;
                if (balls != null) {
                    for (Ball b : balls) {
                        b.x += b.vX;
                        b.y += b.vY;
                        for (Ball c : balls) {
                            double dist = Math.hypot((b.x+b.vX) - (c.x+c.vX), (b.y+b.vY) - (c.y+c.vY));
                            double radii = b.r + c.r;
                            if (dist <= radii) {
                                double bUX = b.vX;
                                double bUY = b.vY;
                                double cUX = c.vX;
                                double cUY = c.vY;
                                b.vX = collide(b.m, c.m, bUX, cUX);
                                c.vX = collide(c.m, b.m, cUX, bUX);
                                b.vY = collide(b.m, c.m, bUY, cUY);
                                c.vY = collide(c.m, b.m, cUY, bUY);
                            }
                        }
                        if (b.x - b.r <= 0 && b.vX < 0) {
                            b.x = b.r;
                            b.vX = -b.vX;
                        } else if (b.x + b.r >= width && b.vX > 0) {
                            b.x = width-b.r;
                            b.vX = -b.vX;
                        }
                        if (b.y - b.r <= 0 && b.vY < 0) {
                            b.y = b.r;
                            b.vY = -b.vY;
                        } else if (b.y + b.r >= height && b.vY > 0) {
                            b.y = height-b.r;
                            b.vY = -b.vY;
                        }
                    }
                }
                engine.drawFrame();

                sleep(1000/FPS);
            }
            } catch (InterruptedException e) {
                // Just finish.
            }
        }
    };

    private class Engine extends WallpaperService.Engine
                         implements SharedPreferences.OnSharedPreferenceChangeListener {
        public Ball[] balls;
        public MoveBallsThread move;
        public int width;
        public int height;
        public Paint paint;
        public Engine() {
            setOffsetNotificationsEnabled(false);
        }
        @Override public void onCreate(SurfaceHolder surface) {
            super.onCreate(surface);
            mPrefs.registerOnSharedPreferenceChangeListener(this);
        }
        @Override public void onDestroy() {
            mPrefs.unregisterOnSharedPreferenceChangeListener(this);
            if (move != null) {
                move.interrupt();
                move = null;
            }
        }
        @Override public void onDesiredSizeChanged(int w, int h) {
            width = w;
            height = h;
            generateBalls();
        }
        @Override public void onVisibilityChanged(boolean visible) {
            if (visible) {
                width = getDesiredMinimumWidth();
                height = getDesiredMinimumHeight();
                move = new MoveBallsThread(this);
                move.start();
            } else {
                move.interrupt();
                move = null;
            }
        }
        private void drawFrame() {
            SurfaceHolder surface = getSurfaceHolder();
            Canvas canvas = null;
            try {
                canvas = surface.lockCanvas();
                if (canvas != null) drawOnCanvas(canvas);
            } finally {
                if (canvas != null) surface.unlockCanvasAndPost(canvas);
            }
        }
        private void drawOnCanvas(Canvas canvas) {
            if (balls == null) generateBalls();
            if (paint == null) generatePaint();
            int bg = Color.parseColor(mPrefs.getString("com.notriddle.balls.background_pref", "#222222"));
            canvas.drawColor(bg);
            for (Ball b : balls) {
                canvas.drawCircle((float)b.x, (float)b.y, (float)b.r, paint);
            }
        }
        private void generatePaint() {
            paint = new Paint();
            int color = Color.parseColor(mPrefs.getString("com.notriddle.balls.color_pref", "#33b5e5"));
            paint.setColor(color);
            paint.setStyle(Paint.Style.FILL);
        }
        private void generateBalls() {
            int w = getDesiredMinimumWidth();
            int h = getDesiredMinimumHeight();
            int amount = Integer.parseInt(mPrefs.getString("com.notriddle.balls.number_pref", "7"));
            int velocity = Integer.parseInt(mPrefs.getString("com.notriddle.balls.velocity_pref", "300"));
            int size = Integer.parseInt(mPrefs.getString("com.notriddle.balls.size_pref", "100"));
            int kurt = Integer.parseInt(mPrefs.getString("com.notriddle.balls.kurt_pref", "3"));
            Ball[] myBalls = new Ball[amount];
            for (int i = 0; i != amount; ++i) {
                myBalls[i] = new Ball(w, h, size, velocity, kurt);
            }
            balls = myBalls;
        }
        @Override public void onSharedPreferenceChanged(SharedPreferences prefs,
                                                        String key) {
            generateBalls();
            generatePaint();
        }
    };

    public void onCreate() {
        super.onCreate();
        mPrefs = PreferenceManager
                 .getDefaultSharedPreferences(this);
    }

    public WallpaperService.Engine onCreateEngine() {
        return new Wallpaper.Engine();
    }
};


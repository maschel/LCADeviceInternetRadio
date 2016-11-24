/*
 *  LCADeviceInternetRadio
 *
 *  MIT License
 *
 *  Copyright (c) 2016
 *
 *  Geoffrey Mastenbroek, geoffrey.mastenbroek@student.hu.nl
 *  Feiko Wielsma, feiko.wielsma@student.hu.nl
 *  Robbin van den Berg, robbin.vandenberg@student.hu.nl
 *  Arnoud den Haring, arnoud.denharing@student.hu.nl
 *
 *  Permission is hereby granted, free of charge, to any person
 *  obtaining a copy of this software and associated documentation
 *  files (the "Software"), to deal in the Software without
 *  restriction, including without limitation the rights to use,
 *  copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the
 *  Software is furnished to do so, subject to the following
 *  conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 *  OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 *  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 *  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 *  OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.maschel.lcadevice.internetradio.DeviceAgent;

import com.maschel.lca.device.Component;
import com.maschel.lca.device.sensor.Sensor;
import com.maschel.lcadevice.internetradio.RadioPlayer.MainMenu;
import com.maschel.lcadevice.internetradio.RadioPlayer.MusicController;
import com.sun.javaws.Main;

/**
 * Created by feiko on 11/24/2016.
 */
public class InternetRadioDevice extends com.maschel.lca.device.Device implements MainMenu.OnVolumeChangedListener{

    private final static String DEVICE_ID = "InternetRadio";
    private final static long ANALYTICS_SYNC_MIN = 10000; // 10 seconds
    private final static long SENSOR_UPDATE_INTERVAL = 0;

    private MainMenu mainMenu;
    private MusicController musicController;

    private int currentVolume = 0;

    private InternetRadioDevice internetRadio = this;

    public InternetRadioDevice()
    {
        super(DEVICE_ID, ANALYTICS_SYNC_MIN, SENSOR_UPDATE_INTERVAL);
        //this.mainMenu = mainMenu.staticMain;
        //this.musicController = mainMenu.myMusicController;

    }

    @Override
    public void setup() {

        System.err.println("DEVICE AGENT SETUP");
        if(MainMenu.staticMain == null)
        {
            System.out.println("Ik ben null");
        }
        Component internetRadioComponent = new Component("internetradio");
        addComponent(internetRadioComponent);

        internetRadioComponent.add(new Sensor("stationName") {
            @Override
            public String readSensor() {
                String result;
                try {
                    result = MainMenu.staticMain.myMusicController.getCurrentRadiostation().getName();
                }
                catch(NullPointerException ex){
                    result = "not playing";
                }
                return result;
            }
        });

        internetRadioComponent.add(new Sensor("stationGenre") {
            @Override
            public String readSensor() {
                String result;
                try {
                    result = MainMenu.staticMain.myMusicController.getCurrentRadiostation().getGenre();
                }
                catch(NullPointerException ex){
                    result = "not playing";
                }
                return result;
            }
        });

        internetRadioComponent.add(new Sensor("stationCountry") {
            @Override
            public String readSensor() {
                String result;
                try {
                    result = MainMenu.staticMain.myMusicController.getCurrentRadiostation().getCountry();
                }
                catch(NullPointerException ex){
                    result = "not playing";
                }
                return result;
            }
        });

        internetRadioComponent.add(new Sensor("testSensor") {
            @Override
            public String readSensor() {
                return "test";
            }
        });

        internetRadioComponent.add(new Sensor("playState") {
            @Override
            public String readSensor() {
                switch(MainMenu.staticMain.myMusicController.getPlayState())
                {
                    case PLAYING: return "playing";
                    case STOPPED: return "stopped";
                    case PAUSED: return "paused";
                }
                return "none";
            }
        });

        internetRadioComponent.add(new Sensor("volume") {
            @Override
            public Integer readSensor() {
                MainMenu.staticMain.removeOnVolumeChangedListener(internetRadio);
                MainMenu.staticMain.setOnVolumeChangedListener(internetRadio);
                return currentVolume;
            }
        });
    }

    @Override
    public void connect() {

    }

    @Override
    public void update() {

    }

    @Override
    public Boolean deviceReadyForAnalyticDataSync() {
        return null;
    }

    @Override
    public void disconnect() {

    }

    @Override
    public void onVolumeChanged(int volume) {
        currentVolume = volume;
    }
}

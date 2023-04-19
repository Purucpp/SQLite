package com.yesandroid.sqlite.base.audio;

import android.media.AudioTrack;

import java.util.Arrays;

public class MyAudioTrack16Bit {
    public AudioTrack mAudioTrack = null;
    public boolean firstStart = false;
    public int packageWriteCount = 0;
    public int packageReadCount = 0;
    public int packageWriteIndex = 0;
    public int packageReadIndex = 0;
    public int packageCountCallback = 0;
    public final int MAX_BUF_PER200_SZIE = 40;
    public final int MAX_BUF_PER320_SZIE = 25;
    public short[] bufSaveForRealTime = new short[8000];
    public short[] bufForRealBudian = new short[320];
    public short lastData = 0;
    public boolean dataNessary = false;
    public int byteAvaliableToRead = 0;

    public MyAudioTrack16Bit() {
    }

    public void prepareAudioTrack() {
        int freq = 4000;
        int minBufferSize = AudioTrack.getMinBufferSize(freq, 4, 2);
        int primePlaySize = minBufferSize * 1;
        this.mAudioTrack = new AudioTrack(3, freq, 4, 2, primePlaySize, 1);
        this.mAudioTrack.setPositionNotificationPeriod(320);
        this.mAudioTrack.setPlaybackPositionUpdateListener(new AudioTrack.OnPlaybackPositionUpdateListener() {
            public void onMarkerReached(AudioTrack audioTrack) {
            }

            public void onPeriodicNotification(AudioTrack audioTrack) {
                if (!MyAudioTrack16Bit.this.firstStart) {
                    MyAudioTrack16Bit.this.byteAvaliableToRead = MyAudioTrack16Bit.this.packageWriteCount * 200 - MyAudioTrack16Bit.this.packageReadCount * 320;
                    if (!MyAudioTrack16Bit.this.dataNessary) {
                        if (MyAudioTrack16Bit.this.byteAvaliableToRead >= 1280) {
                            MyAudioTrack16Bit.this.dataNessary = true;
                        } else {
                            MyAudioTrack16Bit.this.byteAvaliableToRead = 0;
                        }
                    } else if (MyAudioTrack16Bit.this.byteAvaliableToRead < 320) {
                        MyAudioTrack16Bit.this.byteAvaliableToRead = 0;
                        MyAudioTrack16Bit.this.dataNessary = false;
                    }

                    if (MyAudioTrack16Bit.this.byteAvaliableToRead > 2880) {
                        MyAudioTrack16Bit.this.packageWriteCount = 0;
                        MyAudioTrack16Bit.this.packageReadCount = 0;
                        MyAudioTrack16Bit.this.packageWriteIndex = 0;
                        MyAudioTrack16Bit.this.packageReadIndex = 0;
                        MyAudioTrack16Bit.this.packageCountCallback = 0;
                        MyAudioTrack16Bit.this.dataNessary = false;
                        Arrays.fill(MyAudioTrack16Bit.this.bufForRealBudian, (short)0);
                        MyAudioTrack16Bit.this.mAudioTrack.write(MyAudioTrack16Bit.this.bufForRealBudian, 0, 320);
                    } else if (MyAudioTrack16Bit.this.byteAvaliableToRead >= 320) {
                        MyAudioTrack16Bit.this.mAudioTrack.write(MyAudioTrack16Bit.this.bufSaveForRealTime, MyAudioTrack16Bit.this.packageReadIndex * 320, 320);
                        ++MyAudioTrack16Bit.this.packageReadCount;
                        ++MyAudioTrack16Bit.this.packageReadIndex;
                        if (MyAudioTrack16Bit.this.packageReadIndex == 25) {
                            MyAudioTrack16Bit.this.lastData = MyAudioTrack16Bit.this.bufSaveForRealTime[MyAudioTrack16Bit.this.packageReadIndex * 320 - 1];
                            MyAudioTrack16Bit.this.packageReadIndex = 0;
                        } else {
                            MyAudioTrack16Bit.this.lastData = MyAudioTrack16Bit.this.bufSaveForRealTime[MyAudioTrack16Bit.this.packageReadIndex * 320 - 1];
                        }
                    } else {
                        Arrays.fill(MyAudioTrack16Bit.this.bufForRealBudian, MyAudioTrack16Bit.this.lastData);
                        if (MyAudioTrack16Bit.this.mAudioTrack != null && MyAudioTrack16Bit.this.bufForRealBudian != null) {
                            MyAudioTrack16Bit.this.mAudioTrack.write(MyAudioTrack16Bit.this.bufForRealBudian, 0, 320);
                        }
                    }
                } else {
                    Arrays.fill(MyAudioTrack16Bit.this.bufForRealBudian, (short)0);
                    MyAudioTrack16Bit.this.mAudioTrack.write(MyAudioTrack16Bit.this.bufForRealBudian, 0, 320);
                }

                ++MyAudioTrack16Bit.this.packageCountCallback;
            }
        });
        this.packageWriteCount = 0;
        this.packageReadCount = 0;
        this.packageWriteIndex = 0;
        this.packageReadIndex = 0;
        this.packageCountCallback = 0;
        this.firstStart = true;
        this.mAudioTrack.play();
        Arrays.fill(this.bufForRealBudian, (short)0);
        this.mAudioTrack.write(this.bufForRealBudian, 0, 320);
        this.mAudioTrack.write(this.bufForRealBudian, 0, 320);
        this.mAudioTrack.write(this.bufForRealBudian, 0, 320);
    }

    public void writeAudioTrack(short[] buf, int start, int len) {
        System.arraycopy(buf, start, this.bufSaveForRealTime, this.packageWriteIndex * 200, len);
        ++this.packageWriteCount;
        ++this.packageWriteIndex;
        if (this.packageWriteIndex == 40) {
            this.packageWriteIndex = 0;
        }

        this.firstStart = false;
    }

    public void releaseAudioTrack() {
        if (this.mAudioTrack != null) {
            this.mAudioTrack.release();
        }

    }
}

package com.yesandroid.sqlite.base.audio;

import android.media.AudioTrack;

public class MyAudioTrack8Bit {
    public AudioTrack mAudioTrack = null;

    public MyAudioTrack8Bit() {
    }

    public void prepareAudioTrack() {
        int freq = 4000;
        int minBufferSize = AudioTrack.getMinBufferSize(freq, 4, 3);
        int primePlaySize = minBufferSize * 3;
        this.mAudioTrack = new AudioTrack(3, freq, 4, 3, primePlaySize, 1);
        this.mAudioTrack.play();
    }

    public void writeAudioTrack(byte[] buf, int start, int len) {
        this.mAudioTrack.write(buf, start, len);
    }

    public void releaseAudioTrack() {
        if (this.mAudioTrack != null) {
            this.mAudioTrack.release();
            this.mAudioTrack = null;
        }

    }
}

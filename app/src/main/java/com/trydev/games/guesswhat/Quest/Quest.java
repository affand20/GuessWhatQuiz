package com.trydev.games.guesswhat.Quest;

/**
 * Created by user on 6/11/2018.
 */

public class Quest {
    private String trueanswer;
    private int url;

    public Quest(int url, String trueanswer) {
        this.url = url;
        this.trueanswer = trueanswer;
    }

    public int getUrl() {
        return url;
    }

    public void setUrl(int url) {
        this.url = url;
    }

    public String getTrueanswer() {
        return trueanswer;
    }

    public void setTrueanswer(String trueanswer) {
        this.trueanswer = trueanswer;
    }
}

package com.feelthebeats.ftb2omgui;

/**
 * JSNexen
 * 10/9/12
 * 9:06 PM
 */
public class Note {
    private final int time;
    private final int column;
    private final double beatLength;

    public Note(int time, int column, double beatLength) {
        this.time = time;
        this.column = column;
        this.beatLength = beatLength;
    }

    public int getTime() {
        return time;
    }

    public int getColumn() {
        return column;
    }

    public double getBeatLength() {
        return beatLength;
    }
}
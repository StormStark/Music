package com.example.music;

import java.util.ArrayList;

public class PlayingQueue {
    private static ArrayList<SongInfo> queue;
    private static int currentPosition;

    public PlayingQueue(ArrayList<SongInfo> queue, int currentPosition) {
        this.queue = queue;
        this.currentPosition = currentPosition;
    }

    public PlayingQueue(ArrayList<SongInfo> queue) {
        this.queue = queue;
    }

    public static ArrayList<SongInfo> getQueue() {
        return queue;
    }

    public static void setQueue(ArrayList<SongInfo> queue1) {
        queue = queue1;
    }

    public static int getCurrentPosition() {
        return currentPosition;
    }

    public static void setCurrentPosition(int currentPosition1) {
        currentPosition = currentPosition1;
    }

    public static void addSong(SongInfo songInfo){
        queue.add(songInfo);
    }

    public static void addSong(SongInfo songInfo, int position){
        queue.add(position, songInfo);
    }

    public static void removeSong(SongInfo songInfo){
        queue.remove(songInfo);
    }

    public static void removeSong(int position){
        queue.remove(position);
    }
}

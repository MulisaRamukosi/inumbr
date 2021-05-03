package com.puzzle.industries.inumbr.dataModels;

import android.os.Parcel;
import android.os.Parcelable;

public class LuckyNumberGame implements Parcelable {

    private final String name;
    private final int maxNums;
    private final int maxBalls;
    private final int image;
    private final String script;

    public LuckyNumberGame(String name, int maxNums, int maxBalls, int image, String script) {
        this.name = name;
        this.maxNums = maxNums;
        this.maxBalls = maxBalls;
        this.image = image;
        this.script = script;
    }

    protected LuckyNumberGame(Parcel in) {
        name = in.readString();
        maxNums = in.readInt();
        maxBalls = in.readInt();
        image = in.readInt();
        script = in.readString();
    }

    public static final Creator<LuckyNumberGame> CREATOR = new Creator<LuckyNumberGame>() {
        @Override
        public LuckyNumberGame createFromParcel(Parcel in) {
            return new LuckyNumberGame(in);
        }

        @Override
        public LuckyNumberGame[] newArray(int size) {
            return new LuckyNumberGame[size];
        }
    };

    public String getName() {
        return name;
    }

    public int getMaxNums() {
        return maxNums;
    }

    public int getImage() {
        return image;
    }

    public int getMaxBalls() {
        return maxBalls;
    }

    public String getScript() {
        return script;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(maxNums);
        dest.writeInt(maxBalls);
        dest.writeInt(image);
        dest.writeString(script);
    }
}

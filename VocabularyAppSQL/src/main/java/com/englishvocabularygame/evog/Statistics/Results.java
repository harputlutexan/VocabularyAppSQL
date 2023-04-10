package com.englishvocabularygame.evog.Statistics;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created on 20.12.2016.
 */

public class Results implements Parcelable, Serializable {

    public String itemTestIdString;
    public String itemTestType;
    public String itemTestDate;
    public int itemNumberOfCorrectAnswers;
    public int itemNumberOfFalseAnswers;
    public int itemNumberOfEmptyAnswers;
    public String itemSuccessRate;

    public Results(String testIdString, String testType, String testDate, int numberOfCorrectAnswers,
                   int numberOfFalseAnswers, int numberOfEmptyAnswers, String successRate) {
        this.itemTestIdString = testIdString;
        this.itemTestDate = testDate;
        this.itemNumberOfCorrectAnswers = numberOfCorrectAnswers;
        this.itemNumberOfFalseAnswers = numberOfFalseAnswers;
        this.itemNumberOfEmptyAnswers = numberOfEmptyAnswers;
        this.itemTestType = testType;
        this.itemSuccessRate = successRate;
    }

    protected Results(Parcel in) {
        itemTestIdString = in.readString();
        itemTestType = in.readString();
        itemNumberOfCorrectAnswers = in.readInt();
        itemNumberOfFalseAnswers = in.readInt();
        itemNumberOfEmptyAnswers = in.readInt();
        itemTestDate = in.readString();
        itemSuccessRate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(itemTestIdString);
        dest.writeString(itemTestType);
        dest.writeInt(itemNumberOfCorrectAnswers);
        dest.writeInt(itemNumberOfFalseAnswers);
        dest.writeInt(itemNumberOfEmptyAnswers);
        dest.writeString(itemTestDate);
        dest.writeString(itemSuccessRate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Results> CREATOR = new Creator<Results>() {
        @Override
        public Results createFromParcel(Parcel in) {
            return new Results(in);
        }

        @Override
        public Results[] newArray(int size) {
            return new Results[size];
        }
    };

    //    public class MyCreator implements Parcelable.Creator<Results> {
//        public Results createFromParcel (Parcel source){
//            return new Results(source);
//        }
    public Results[] newArray(int size) {
        return new Results[size];
    }


    public String getItemTestDate() {
        return itemTestDate;
    }

    public String getItemTestType() {
        return itemTestType;
    }

    public String getItemTestIdString() {
        return itemTestIdString;
    }

    public int getItemNumberOfEmptyAnswers() {
        return itemNumberOfEmptyAnswers;
    }

    public int getItemNumberOfFalseAnswers() {
        return itemNumberOfFalseAnswers;
    }

    public int getItemNumberOfCorrectAnswers() {
        return itemNumberOfCorrectAnswers;
    }

    public String getItemSuccessRate(){
        return itemSuccessRate;
    }


}

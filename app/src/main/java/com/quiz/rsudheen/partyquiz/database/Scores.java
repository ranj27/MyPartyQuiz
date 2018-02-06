package com.quiz.rsudheen.partyquiz.database;

import android.support.annotation.NonNull;

import java.util.Comparator;

/**
 * Created by rsudheen on 1/8/2018.
 */

public class Scores implements Comparator<Scores> {
    int scoreId;
    String complexity;
    int score;

    public Scores(int scoreId, String complexity, int score) {
        this.scoreId = scoreId;
        this.complexity = complexity;
        this.score = score;
    }

    public Scores() {

    }

    public int getScoreId() {
        return scoreId;
    }

    public void setScoreId(int scoreId) {
        this.scoreId = scoreId;
    }

    public String getComplexity() {
        return complexity;
    }

    public void setComplexity(String complexity) {
        this.complexity = complexity;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Comparator<Scores> MyComparator = new Comparator<Scores>() {
        @Override
        public int compare(Scores o1, Scores o2) {
           return o1.getScore() - o2.getScore();
        }
    };

    @Override
    public int compare(Scores o1, Scores o2) {
        return o1.getScore() - o2.getScore();
    }
}


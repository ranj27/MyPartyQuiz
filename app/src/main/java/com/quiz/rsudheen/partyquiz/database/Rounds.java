package com.quiz.rsudheen.partyquiz.database;

/**
 * Created by rsudheen on 1/8/2018.
 */

public class Rounds {
    int roundId;
    int quizId;
    int roundNumber;

    public Rounds(int roundId, int quizId, int roundNumber) {
        this.roundId = roundId;
        this.quizId = quizId;
        this.roundNumber = roundNumber;
    }

    public int getRoundId() {
        return roundId;
    }

    public void setRoundId(int roundId) {
        this.roundId = roundId;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }
}

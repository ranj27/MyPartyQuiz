package com.quiz.rsudheen.partyquiz.database;

/**
 * Created by rsudheen on 1/4/2018.
 */

public class Quiz {
    String quizRounds;
    int quizId;
    String quizName;
    String createDt;

    public Quiz(){

    }

    public Quiz(String quizName, String quizRounds){
        this.quizName = quizName;
        this.quizRounds  = quizRounds;
    }

    public Quiz(int quizId, String quizName, String quizRounds) {
        this.quizId = quizId;
        this.quizName = quizName;
        this.quizRounds = quizRounds;
    }

    public int getQuizId() {
        return quizId;
    }

    public String getCreateDt() {
        return createDt;
    }

    public void setCreateDt(String createDt) {
        this.createDt = createDt;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public String getQuizName() {
        return quizName;
    }

    public String getQuizRounds(){
        return quizRounds;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public void setQuizRounds(String quizRounds){
        this.quizRounds = quizRounds;
    }
}

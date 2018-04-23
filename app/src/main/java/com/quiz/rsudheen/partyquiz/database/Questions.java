package com.quiz.rsudheen.partyquiz.database;

/**
 * Created by rsudheen on 1/8/2018.
 */

public class Questions {
    byte[] photo;
    int questionId;
    int roundId;
    int quizId;
    int categoryId;
    int scoreId;
    String choices;
    String answer;
    private String question;
    private String categoryName;

    public Questions(){

    }

    public Questions(int questionId, int roundId, int quizId, int scoreId, String choices, String answer, int category, String categoryName, byte[] photo) {
        this.questionId = questionId;
        this.roundId = roundId;
        this.quizId = quizId;
        this.scoreId = scoreId;
        this.choices = choices;
        this.answer = answer;
        this.categoryId = category;
        this.categoryName = categoryName;
        this.photo = photo;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
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

    public int getScoreId() {
        return scoreId;
    }

    public void setScoreId(int scoreId) {
        this.scoreId = scoreId;
    }

    public String getChoices() {
        return choices;
    }

    public void setChoices(String choices) {
        this.choices = choices;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getQuestion() {
        return question;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }
}


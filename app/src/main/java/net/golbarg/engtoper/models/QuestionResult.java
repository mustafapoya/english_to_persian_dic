package net.golbarg.engtoper.models;

public class QuestionResult {
    private int id;
    private int categoryId;
    private int correctAnswer;
    private int wrongAnswer;
    private int noAnswer;

    public QuestionResult(int id, int categoryId, int correctAnswer, int wrongAnswer, int noAnswer) {
        this.id = id;
        this.categoryId = categoryId;
        this.correctAnswer = correctAnswer;
        this.wrongAnswer = wrongAnswer;
        this.noAnswer = noAnswer;
    }

    public QuestionResult(int categoryId, int correctAnswer, int wrongAnswer, int noAnswer) {
        this.id = -1;
        this.categoryId = categoryId;
        this.correctAnswer = correctAnswer;
        this.wrongAnswer = wrongAnswer;
        this.noAnswer = noAnswer;
    }

    public QuestionResult(int categoryId) {
        this.id = -1;
        this.categoryId = categoryId;
        setCorrectAnswer(0);
        setWrongAnswer(0);
        setNoAnswer(0);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        if(correctAnswer >= 0) {
            this.correctAnswer = correctAnswer;
        } else {
            this.correctAnswer = 0;
        }

    }

    public void incrementCorrectAnswer() {
        this.correctAnswer++;
    }

    public void decrementCorrectAnswer() {
        this.correctAnswer--;
    }

    public int getWrongAnswer() {
        return wrongAnswer;
    }

    public void setWrongAnswer(int wrongAnswer) {
        if(wrongAnswer >= 0) {
            this.wrongAnswer = wrongAnswer;
        } else {
            this.wrongAnswer = 0;
        }

    }

    public void incrementWrongAnswer() {
        this.wrongAnswer++;
    }

    public void decrementWrongAnswer() {
        this.wrongAnswer--;
    }

    public int getNoAnswer() {
        return noAnswer;
    }

    public void setNoAnswer(int noAnswer) {
        if(noAnswer >= 0) {
            this.noAnswer = noAnswer;
        } else {
            this.noAnswer = 0;
        }

    }

    public void incrementNoAnswer() {
        this.noAnswer++;
    }

    public void decrementNoAnswer() {
        this.noAnswer--;
    }

    @Override
    public String toString() {
        return "ExamResult{" +
                "id=" + id +
                ", categoryId=" + categoryId +
                ", correctAnswer=" + correctAnswer +
                ", wrongAnswer=" + wrongAnswer +
                ", noAnswer=" + noAnswer +
                '}';
    }
}

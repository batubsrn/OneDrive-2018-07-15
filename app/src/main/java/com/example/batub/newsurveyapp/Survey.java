package com.example.batub.newsurveyapp;

class Survey {
    public String question,answer1,answer2;

    public Survey() {


    }

    public Survey(String question, String answer1, String answer2) {
        this.question = question;
        this.answer1 = answer1;
        this.answer2 = answer2;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }
}




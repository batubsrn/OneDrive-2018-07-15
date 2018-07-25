package com.example.batub.newsurveyapp;

class Survey {
    public String question,answer1,answer2;
    int id;

    public Survey() {


    }

    public  void changeAttributeSurvey(Survey s)
    {
        this.question=s.question;
        this.answer1=s.answer1;
        this.answer2=s.answer2;
        this.id=s.id;
    }


    public Survey(String question, String answer1, String answer2,int id) {
        this.question = question;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.id=id;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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




package org.example.model;

public class Advice {
    private String advice;
    private String problem_solved;

    public Advice(String advice, String problem_solved) {
        this.advice = advice;
        this.problem_solved = problem_solved;
    }

    public String getAdvice() {
        return advice;
    }

    public String getProblem_solved() {
        return problem_solved;
    }

    @Override
    public String toString() {
        return "We advise you to "+this.advice+". This will solve the potentially risk of "+this.problem_solved+".";
    }
}

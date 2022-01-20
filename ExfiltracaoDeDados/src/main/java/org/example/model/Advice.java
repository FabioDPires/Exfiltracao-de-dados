package org.example.model;

public class Advice {
    private String advice;
    private String problem_solved;
    public static String IMPLEMENT_DEFENSES="implement firewall, antivirus among other basic defenses";
    public static String WHITELIST="implement a whitelist of accessible domains, if not possible implement a blacklist of malicious domains gathered through threat intelligence";
    public static String RESTRICT_DOMAIN="further restrict the allowed domain, or implement SSL inspection";
    public static String RESTRICT_COMMUNICATION="restrict communication with ssh servers on the internet";
    public static String ANALYZE_QUERIES="analyse the queries done and restrict them based on destination, size and entropy. Blacklist certain domain from being queried";
        public static String MONITOR_EMAIL="monitor the email data being sent for sensitive information and implement a whitelist of accessible domains, if not possible implement a blacklist of malicious domains gathered through threat intelligence";

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

package org.example.model;


public class Evidence extends Fact {
    public static final String DEFENSES = "Are there defenses";
    public static final String HTTP_ALLOWED = "Is HTPP connections allowed to outside";
    public static final String DOMAIN_CHECKED = "Is the domain checked";
    public static final String ENCRYPTION = "Is encryption needed";
    public static final String ACCEPTED_DOMAIN = "Do we own an accepted domain";
    public static final String DNS_ALLOWED = "Are DNS queries allowed";
    public static final String HIGH_AMOUNT_DATA = "Is the amount of data high";
    public static final String SSH_ALLOWED = "Are SSH connections allowed";
    public static final String EMAIL_ALLOWED = "Is email communication permitted";

    private String evidence;
    private String value;

    public Evidence(String ev, String v) {
        evidence = ev;
        value = v;
    }

    public String getEvidence() {
        return evidence;
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return (evidence + " = " + value);
    }
}

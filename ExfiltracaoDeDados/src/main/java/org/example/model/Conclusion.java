package org.example.model;

public class Conclusion extends Fact {
    public static final String HTTP_OWNED_SERVER = "HTTP to owned server";
    public static final String COMMON_SITES = "Exfiltrate through common sites";
    public static final String QUERIES = "A queries";
    public static final String TXT = "TXT records";
    public static final String SSH_OWNED_SERVER = "SSH to owned server";
    public static final String EMAIL = "Send email with data";
    public static final String UNKNOWN = "No security flaws found";

    private String description;

    public Conclusion(String description) {
        this.description = description;
        Haemorrhage.agendaEventListener.addRhs(this);
    }

    public String getDescription() {
        return description;
    }

    public String toString() {
        return ("Conclusion: " + description);
    }
}

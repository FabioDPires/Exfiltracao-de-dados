package org.example.dataExtraction;

import org.example.model.*;
import org.example.ui.UI;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.LiveQuery;
import org.kie.api.runtime.rule.Row;
import org.kie.api.runtime.rule.ViewChangedEventListener;

import java.io.BufferedReader;
import java.util.*;

public class DataExfiltration {
    public static KieSession KS;
    public static BufferedReader BR;
    public static TrackingAgendaEventListener agendaEventListener;
    public static Map<Integer, Justification> justifications;
    public static List<Advice> advices;
    public static Conclusion conclusion;

    public static final void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String choice;
        UI.uiInit();
        do {
            runEngine();
            System.out.println("Do you want to make a new simulation? (Y-Yes)");
            choice = scanner.nextLine();
        } while (choice.equalsIgnoreCase("Y"));
        UI.uiClose();
    }

    private static void runEngine() {
        advices = new ArrayList<>();
        try {
            DataExfiltration.justifications = new TreeMap<Integer, Justification>();

            // load up the knowledge base
            KieServices ks = KieServices.Factory.get();
            KieContainer kContainer = ks.getKieClasspathContainer();
            final KieSession kSession = kContainer.newKieSession("ksession-rules");
            DataExfiltration.KS = kSession;
            DataExfiltration.agendaEventListener = new TrackingAgendaEventListener();
            kSession.addEventListener(agendaEventListener);

            // Query listener
            ViewChangedEventListener listener = new ViewChangedEventListener() {
                @Override
                public void rowDeleted(Row row) {
                }

                @Override
                public void rowInserted(Row row) {
                    conclusion = (Conclusion) row.get("$conclusion");
                    System.out.println("CONCKLUSION: " + conclusion.toString());
                    System.out.println(">>>" + conclusion.toString());
                    How how = new How(DataExfiltration.justifications);
                    System.out.println(how.getHowExplanation(conclusion.getId()));

                    // stop inference engine after as soon as got a conclusion
                    kSession.halt();

                }

                @Override
                public void rowUpdated(Row row) {
                }

            };

            LiveQuery query = kSession.openLiveQuery("Conclusions", null, listener);

            kSession.fireAllRules();
            // kSession.fireUntilHalt();

            query.close();
            getEvidences();
            getAdviceFromConclusion();
            printAdvices();

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private static void getEvidences() {
        Set<Map.Entry<Integer, Justification>> entrySet = justifications.entrySet();
        Map.Entry<Integer, Justification>[] entryArray
                = entrySet.toArray(
                new Map.Entry[entrySet.size()]);
        if (entrySet.size() > 0) {
            runTroughEvidences(entrySet.size(), entryArray);
        }
    }

    private static void runTroughEvidences(int size, Map.Entry<Integer, Justification>[] entryArray) {
        for (int i = 0; i < size; i++) {
            Justification justification = entryArray[i].getValue();
            List<Fact> lhs = justification.getLhs();
            for (int j = 0; j < lhs.size(); j++) {
                if (!(lhs.get(j) instanceof Hypothesis)) {
                    Evidence evi = (Evidence) lhs.get(j);
                    getAdviceFromEvidence(evi);
                }
            }
        }

    }

    private static void getAdviceFromEvidence(Evidence evi) {
            if (evi.getEvidence().equals(Evidence.DEFENSES) && evi.getValue().equals("no")) {
            advices.add(new Advice(Advice.IMPLEMENT_DEFENSES, Risk.NO_DEFENSES));
            return;
        }
        if (evi.getEvidence().equals(Evidence.DOMAIN_CHECKED) && evi.getValue().equals("no")) {
            advices.add(new Advice(Advice.WHITELIST, Risk.NO_CHECKED_DOMAIN));
            return;
        }
        if (evi.getEvidence().equals(Evidence.ACCEPTED_DOMAIN) && evi.getValue().equals("yes")) {
            advices.add(new Advice(Advice.RESTRICT_DOMAIN, Risk.ACCEPTED_DOMAIN));
            return;
        }
        if (evi.getEvidence().equals(Evidence.SSH_ALLOWED) && evi.getValue().equals("yes")) {
            advices.add(new Advice(Advice.RESTRICT_COMMUNICATION, Risk.SSH_CONNECTIONS));
            return;
        }
    }

    private static void getAdviceFromConclusion() {
        String conclusionDescription= conclusion.getDescription();
        if (conclusionDescription.equals(Conclusion.QUERIES) || conclusionDescription.equals(Conclusion.TXT)){
            advices.add(new Advice(Advice.ANALYZE_QUERIES,Risk.QUERIES_TXT));
            return;
        }

        if(conclusionDescription.equals(Conclusion.EMAIL)){
            advices.add(new Advice(Advice.MONITOR_EMAIL,Risk.EMAIL));
            return;
        }

    }

    private static void printAdvices() {
        System.out.println("How to improve our system: ");
        for (int i = 0; i < advices.size(); i++) {
            System.out.println(advices.get(i).toString());
        }
    }

}

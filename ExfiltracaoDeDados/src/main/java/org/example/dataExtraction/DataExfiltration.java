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
                    Conclusion conclusion = (Conclusion) row.get("$conclusion");
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
            giveTips();

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private static void giveTips() {
        Set<Map.Entry<Integer, Justification>> entrySet = justifications.entrySet();
        Map.Entry<Integer, Justification>[] entryArray
                = entrySet.toArray(
                new Map.Entry[entrySet.size()]);
        //if entry size =0 interrupt method;
        for (int i = 0; i < entrySet.size(); i++) {
            // Get Key using index and print

            // Get value using index and print
            Justification justification = entryArray[i].getValue();
            List<Fact> lhs=justification.getLhs();
            for(int j=0;j<lhs.size();j++){
                if(!(lhs.get(j) instanceof Hypothesis)){
                    Evidence evi= (Evidence) lhs.get(j);
                    System.out.println("EVIDENCE: "+evi.getEvidence());
                    System.out.println("VALUE: "+evi.getValue());
                }
            }
        }
    }

}


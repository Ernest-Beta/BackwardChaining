import java.io.*;
import java.util.*;

public class BackwardChaining {
    public static void main(String[] args) throws IOException {
        Resolver resolver = new Resolver();

        //diavazei th vasi gnwsewn apo to arxeio 'knowledge_base.txt'
        String arxeio = "knowledge_base.txt";
        BufferedReader reader = new BufferedReader(new FileReader(arxeio));
        String grammh;

        System.out.println("diavazei to arxeio: " + arxeio);
        while ((grammh = reader.readLine()) != null) {
            Clause clause = parseClause(grammh.trim());
            resolver.addClause(clause);
        }
        reader.close();

        //zhta apo ton xrhsth to goal pou thelei an elegthei
        Scanner scanner = new Scanner(System.in);
        System.out.println("Eisagete to goal:");
        String input = scanner.nextLine().trim();
        Predicate goal;
        goal = parsePredicate(input);
        System.out.println("");
        //telikos elegxos gia an einai true h false to input
        if (resolver.backwardchainstart(goal)) {
            System.out.println("H eisodos " + input + " einai True" );
        } else {
            System.out.println("H eisodos " + input + " einai False" );
        }
        scanner.close();
    }

    //syllegei ola ta clauses apo to arxeio me tis baseis gnwsewn
    private static Clause parseClause(String clauseString) {
        if (clauseString.contains("=>")) {
            String[] parts = clauseString.split("=>");

            String premiseString = parts[0].trim();
            

            List<Predicate> predicates = new ArrayList<>();

            if (premiseString.contains("&")) {
                String[] premiseParts = premiseString.split("&");
                for (String premisePart : premiseParts) {
                    predicates.add(parsePredicate(premisePart.trim()));
                }
            } else {

                predicates.add(parsePredicate(premiseString));
            }
    

            Predicate conclusion = parsePredicate(parts[1].trim());
            predicates.add(conclusion);
            
            return new Clause(predicates);
        } else {

            List<Predicate> predicates = new ArrayList<>();
            predicates.add(parsePredicate(clauseString.trim()));

            return new Clause(predicates);
        }
    }
    

    //syllgei ta predicates apo to string pinaka
    private static Predicate parsePredicate(String predicateString) {
        //an ksekinaei me ~ einai arnhsh
        boolean isNegated = predicateString.startsWith("~");
        String cleanPredicate;
        if (isNegated) {
            //an exei to ~ tote tha grapsei to clause apo ton deutero xarakthra gt den theloume na krathoume to symbolo not
            cleanPredicate = predicateString.substring(1);
        } else {
            cleanPredicate = predicateString;
        }
        //briskei thn thesi twn parenthesewn sto string gia na kserei pou tha brei ta terms
        int arparen = cleanPredicate.indexOf('(');
        int dexparen = cleanPredicate.indexOf(')');
        String name = cleanPredicate.substring(0, arparen).trim();
        String[] termStrings = cleanPredicate.substring(arparen + 1, dexparen).split(",");
        List<Term> terms = new ArrayList<>();
        for (String term : termStrings) {
            //krataei to term xwris kena
            term = term.trim();
            //an einai mikro gramma tote to thewroume variable 
            boolean isVariable = Character.isLowerCase(term.charAt(0));
            //kanei thn prosthiki sthn lista me ola ta terms san antikeimeno Term
            terms.add(new Term(term, isVariable));
        }
        //twra pou phre oles tis plhrofories pou xreiazotan mporei na ftiaksei predicate object
        return new Predicate(name, terms, isNegated);
    }
}
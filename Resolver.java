import java.util.*;

class Resolver {
    //lista pou periexei th vasi gnwshs (clauses)
    List<Clause> BG = new ArrayList<>(); 
    //lista gia tous upostoxous
    List<Predicate> subgoals = new ArrayList<>(); 
    //metabliti gia na elegxoume an yparxei apotyxia
    static boolean falsereturn = false;
    //metavliti an exei ginei print to minima gia tous upostoxous
    static boolean printed = false;
    static boolean truereturn = false;

    //prosthetei ena clause sth vasi gnwshs
    void addClause(Clause clause) {
        BG.add(clause);
    }

    //ksekinaei th diadikasia backward chaining me ton stoxo
    boolean backwardchainstart(Predicate goal) {
        //set gia na parakolouthoume poioi stoxoi exoun episkefthei
        Set<Predicate> visited = new HashSet<>(); 
        return backwardchain(goal, visited);
    }

    //kyriws sunarthsh backward chaining (anadromikh)
    private boolean backwardchain(Predicate goal, Set<Predicate> visited) {
        //elegxos gia kyklo (an exoume idi episkefthei ton stoxo)
        if (visited.contains(goal)) {
            return false;
        }

        int countersame = 0;
        //psaxnei an to goal yparxei ws fact sti vasi gnwshs
        for (Clause clause : BG) {
            //an h clause exei ena predicate dhlwnontas oti einai gegonos
            if (clause.predicates.size() == 1) { 
                Predicate singlePredicate = clause.predicates.get(0);
                countersame = 0;
                //elegxei an to onoma/megethos tou predicate kai oi oroi tairiazoun
                if (singlePredicate.name.equals(goal.name) && goal.terms.size() == singlePredicate.terms.size()) {
                    for (int counter = 0; counter <= goal.terms.size() - 1; counter++) {
                        if (goal.terms.get(counter).name.equals(singlePredicate.terms.get(counter).name)) {
                            //metraei poses metavlites exei idies me to goal
                            countersame += 1;
                            if(!goal.isNegated == singlePredicate.isNegated){
                                return false;
                            }
                        }
                    }
                }
            }
            //an oles oi metavlites einai idies tote simainei oti to goal uparxei idio mesa sthn vash gnwshs 
            if (countersame == goal.terms.size()) {
                //an to predicate vrethei ws gegonos epistrefei true
                truereturn=true;
                return true; 
            }
        }
        //prosthetei ton trexonta stoxo sto visited
        visited.add(goal); 
        //erlegxei an egine enopoihsh epitixos
        boolean unifiedSuccessfully = false; 

        //diatrexei oles tis clauses sti vasi gnwshs
        for (Clause clause : BG) {
            if (clause.predicates.get(clause.predicates.size() - 1).name.equals(goal.name)) {
                for (Predicate predicate1 : clause.predicates) {
                    Unifier unifier = new Unifier();
                    if (unifier.unify(goal, predicate1)) { // Prospathei na kanei enopoihsh tou predicate
                        unifiedSuccessfully = true;
                        //efarmozei allages stous orous ths clause meta tin enopoihsh
                        //etsi wste na kanei update tis metavlites tou kathe predicate sto antistoixo gegonos 
                        for (Predicate predicate2 : clause.predicates) {
                            for (Term term : predicate2.terms) {
                                for (int i = 0; i < unifier.getChangingTerms().size(); i++) {
                                    Term changingTerm = unifier.getChangingTerms().get(i);
                                    Term changedTerm = unifier.getChangedTerms().get(i);
                                    if (term.name.equals(changingTerm.name)) {
                                        term.name = changedTerm.name;
                                        term.isVariable = changedTerm.isVariable;
                                    }
                                }
                            }
                        }

                        //apothikeuei tous upostoxous me tetoio tropo wste an vrei ena predicate me to idio onoma to afairei kathws den einai updated
                        //xrhsimopoieitai copy gia na min exoume error otan kanoume parse mia lista kia thn epeksergazomaste tautoxrona
                        List<Predicate> predicatesCopy = new ArrayList<>(clause.predicates);
                        for (Predicate predicate : predicatesCopy) {
                            if (!predicate.name.equals(goal.name)) {
                                for (Predicate predicate4 : subgoals) {
                                    if (predicate.name.equals(predicate4.name)) {
                                        subgoals.remove(predicate);
                                    }
                                }
                                subgoals.add(predicate);
                            }
                        }

                        //aferei ton trexonta stoxo apo tous upostoxous
                        List<Predicate> subgoalsCopy = new ArrayList<>(subgoals);
                        for (Predicate predicate5 : subgoalsCopy) {
                            if (predicate5.name.equals(goal.name)) {
                                subgoals.remove(goal);
                            }
                        }

                        //efarmozei tis allages stous orous kai stous upostoxous wste opou epitrepetai na antikatastisei thn metavlith me to katallhlo gegonos 
                        for (Predicate subgoal : subgoals) {
                            for (Term term : subgoal.terms) {
                                for (int i = 0; i < unifier.getChangingTerms().size(); i++) {
                                    Term changingTerm = unifier.getChangingTerms().get(i);
                                    Term changedTerm = unifier.getChangedTerms().get(i);
                                    if (term.name.equals(changingTerm.name)) {
                                        term.name = changedTerm.name;
                                        term.isVariable = changedTerm.isVariable;
                                    }
                                }
                            }
                        }

                        //ektypwnei tous trexontes upostoxous
                        System.out.println("Oi trexontes upostoxoi einai oi eksis: " + subgoals);

                        //ektelei thn diadikasia anadromika
                        int index = 0;
                        while (index < subgoals.size()) {
                            backwardchain(subgoals.get(index), visited);
                            index++;
                        }
                    }
                }
            }
        }

        //elegxei an yparxei match gia kathe upostoxo
        for (Predicate subgoal : subgoals) {
            boolean foundMatch = false;
            for (Clause clause : BG) {
                if (clause.predicates.size() == 1) {
                    Predicate singlePredicate = clause.predicates.get(0);
                    if (singlePredicate.name.equals(subgoal.name) && subgoal.terms.size() == singlePredicate.terms.size()) {
                        for (int counter = 0; counter <= subgoal.terms.size() - 1; counter++) {
                            if (!subgoal.terms.get(counter).name.equals(singlePredicate.terms.get(counter).name)) {
                                foundMatch = true;
                                break;
                            }
                        }
                    }
                }
            }

            //an den vrethei match, epistrefei false
            if (!foundMatch && truereturn==false) {
                System.out.println("O upostoxos " + goal.name + " den vrethike sthn vasi gnwshs sto teleutaio stadio");
                falsereturn = true;
                return false;
            }
        }

        //elegxei an den egine epitixis enopoihsh kathws tote prepei na epistrafei ameso false
        if (!unifiedSuccessfully) {
            System.out.println("Den mporese na ginei enopoihsh sto " + goal.name);
            falsereturn = true;
            return false;
        }

        //an exei uparksei estw ena false tote ginontai ola false gia na min epistrepsei true to programma  akoma kai an isxuei gia ton upostoxo auton
        if (falsereturn == true) {
            return false;
        } else {
            if(printed==false){
                System.out.println("Oi parapanw upostoxoi yparxoun sthn vasi gnwsewn");
                printed=true;
        }
            truereturn=true;
            return true;
        }
    }
}

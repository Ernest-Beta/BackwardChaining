import java.util.*;

class Unifier {
    //periexei antikatastaseis metaksi orwn
    private Map<String, String> substitutions = new HashMap<>();
    //oroi pou allazoun kata tin enopoihsi
    private List<Term> changingTerms = new ArrayList<>();
    //oroi pou prokypoun meta tin enopoihsi
    private List<Term> changedTerms = new ArrayList<>();

    //o teleutaios oros ston opoio tha prepei o antistoixos changing na metatrapei kata tin enimerwsh twn subgoals
    private Term changed;
    //o teleutaios oros so opoios epeidh einai metavliti tha prepei na ton metatrepsoume sto antistoixo changed kata thn enimerwsh tou subgoals
    //gia na proxwrhsei h diadikasia backwards chaining
    private Term changing;

    //elegxei kai enopoiei duo orous
    boolean unify(Term x, Term y) {

        //elegxei an o x einai metavliti kai kanei antikatastasi
        if (x.isVariable) {
            boolean result = substitute(x.name, y.name);
            if (result) {
                changed = y;
                changing = x;
                //apothikeuei tous orous pou enopoithikan
                changingTerms.add(x);
                changedTerms.add(y);
            }
            return result;
        }
        //elegxei an o y einai metavliti kai kanei antikatastasi
        if (y.isVariable) {
            boolean result = substitute(y.name, x.name);
            if (result) {
                changed = x;
                changing = y;
                //apothikeuei tous orous pou enopoithikan
                changingTerms.add(y);
                changedTerms.add(x);
            }
            return result;
        }
        //elegxei an oi dyo oroi einai idioi
        boolean result = x.name.equals(y.name);
        if (result) {
            changed = y;
            changing = x;
            //apothikeuei tous orous pou enopoithikan
            changingTerms.add(x);
            changedTerms.add(y);
        }
        return result;
    }

    //enopoiei dyo protaseis
    boolean unify(Predicate x, Predicate y) {
        //elegxei an ta onomata, oi arniseis kai o arithmos orwn tairiazoun
        if (!x.name.equals(y.name) || x.isNegated != y.isNegated || x.terms.size() != y.terms.size()) {
            return false;
        }

        //enopoiei tous orous
        for (int i = 0; i < x.terms.size(); i++) {
            if (!unify(x.terms.get(i), y.terms.get(i))) {
                return false;
            }
        }

        System.out.println("H enopoihsh metaksu " + x + " kai " + y + " htan epituxhs");
        return true;
    }

    //prosthetei mia antikatastasi ston xarti substitutions
    private boolean substitute(String var, String value) {
        if (substitutions.containsKey(var)) {
            return substitutions.get(var).equals(value);
        }
        substitutions.put(var, value);
        return true;
    }

    //epistrefei tis antikatastaseis os string
    @Override
    public String toString() {
        return substitutions.toString();
    }

    //epistrefei olous tous orous pou enopoiithikan
    public List<Term> getChangingTerms() {
        return new ArrayList<>(changingTerms);
    }

    //epistrefei olous tous orous pou prokyptoun meta tin enopoihsi
    public List<Term> getChangedTerms() {
        return new ArrayList<>(changedTerms);
    }

    //epistrefei ton teleftaio oro pou enopoihthike
    public Term getChanged() {
        return changed;
    }

    //epistrefei ton oro pou egine enopoihsh
    public Term getChanging() {
        return changing;
    }

}


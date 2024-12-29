import java.util.*;

class Clause {
    List<Predicate> predicates;
    boolean mark;

    // Constructor
    public Clause(List<Predicate> predicates) {
        this.predicates = predicates;
    }

    public void setMark(boolean flag){
        mark=flag;
    }

}
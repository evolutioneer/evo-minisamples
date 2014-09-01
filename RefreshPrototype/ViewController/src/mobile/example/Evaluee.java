package mobile.example;

public class Evaluee {
    
    private String evalueeName;
    private String evalueeId;
    
    public Evaluee() {
        super();
    }

    public Evaluee(String evalueeName, String evalueeId) {
        super();
        this.evalueeName = evalueeName;
        this.evalueeId = evalueeId;
    }

    public String getEvalueeName() {
        return evalueeName;
    }

    public String getEvalueeId() {
        return evalueeId;
    }
}

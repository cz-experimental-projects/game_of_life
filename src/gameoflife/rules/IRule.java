package gameoflife.rules;

public interface IRule {
    int priority();
    
    boolean match(boolean isAlive, int neighborsCount);

    boolean canLive();
}

package gameoflife.rules;

public class ReproductionRule implements IRule {
    @Override
    public int priority() {
        return 0;
    }

    @Override
    public boolean match(boolean isAlive, int neighborsCount) {
        return !isAlive && neighborsCount == 3;
    }

    @Override
    public boolean canLive() {
        return true;
    }
}

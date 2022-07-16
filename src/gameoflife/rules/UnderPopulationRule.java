package gameoflife.rules;

public class UnderPopulationRule implements IRule {
    @Override
    public int priority() {
        return 0;
    }

    @Override
    public boolean canLive() {
        return false;
    }

    @Override
    public boolean match(boolean isAlive, int neighborsCount) {
        return isAlive && neighborsCount < 2;
    }
}

package gameoflife.rules.text;

import java.util.Arrays;

public class TextBirthRule implements ITextRule {

    private final int[] thresholds;
    
    public TextBirthRule(String rule) {
        thresholds = getValues(rule);
    }
    
    @Override
    public int priority() {
        return 0;
    }

    @Override
    public boolean match(boolean isAlive, int neighborsCount) {
        return !isAlive && Arrays.stream(thresholds).anyMatch(i -> i == neighborsCount);
    }

    @Override
    public boolean canLive() {
        return true;
    }
}

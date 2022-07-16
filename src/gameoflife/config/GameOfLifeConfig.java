package gameoflife.config;

public final class GameOfLifeConfig {
    private final float fadeSpeed;
    private final String[] rules;
    private final boolean removeDefaultRules;
    private final int delayBetweenUpdates;
    
    public GameOfLifeConfig(float fadeSpeed, String[] rules, boolean removeDefaultRules, int delayBetweenUpdates) {
        this.fadeSpeed = fadeSpeed;
        this.rules = rules;
        this.removeDefaultRules = removeDefaultRules;
        this.delayBetweenUpdates = delayBetweenUpdates;
    }

    public float fadeSpeed() {
        return fadeSpeed;
    }

    public String[] rules() {
        return rules;
    }

    public boolean removeDefaultRules() {
        return removeDefaultRules;
    }

    public int delayBetweenUpdates() {
        return delayBetweenUpdates;
    }
}

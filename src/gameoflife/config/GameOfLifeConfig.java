package gameoflife.config;

public final class GameOfLifeConfig {
    private final float fadeSpeed;
    private final String[] rules;
    private final boolean removeDefaultRules;

    public GameOfLifeConfig(float fadeSpeed, String[] rules, boolean removeDefaultRules) {
        this.fadeSpeed = fadeSpeed;
        this.rules = rules;
        this.removeDefaultRules = removeDefaultRules;
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
}

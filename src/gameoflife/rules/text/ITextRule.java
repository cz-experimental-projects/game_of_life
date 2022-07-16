package gameoflife.rules.text;

import gameoflife.rules.IRule;

public interface ITextRule extends IRule {
    default int[] getValues(String rule) {
        String[] numbers = rule.split(",");
        int size = numbers.length;
        int[] ints = new int[size];
        
        for (int i = 0; i < size; i++) {
            ints[i] = Integer.parseInt(numbers[i]);
        }
        
        return ints;
    }
}

package org.saucistophe.stats;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 A class allowing to store numbers, and retrieve some interesting statistics.
 */
public class IntegerStats
{
    /**
    A map of the entered values.
    */
    Map<Integer, Integer> values = new TreeMap<>();

    /**
    Adds an integer value to the distribution.
    @param value The value to add.
    */
    public void add(int value)
    {
        // If the value is already present, add 1 to its count.
        if (values.containsKey(value))
        {
            values.put(value, values.get(value) + 1);
        }
        // If the value is not present, add 1.
        else
        {
            values.put(value, 1);
        }
    }
    
    /**
    Adds a boolean value to the distribution, translated as 1 or 0.
    @param value The value to add.
    */
    public void add(boolean value)
    {
        add(value ? 1 : 0);
    }

    /**
    @return The smallest entered value.
    */
    public int getMin()
    {
        return values.keySet().stream().min(Integer::compare).get();
    }

    /**
    @return The biggest entered value.
    */
    public int getMax()
    {
        return values.keySet().stream().max(Integer::compare).get();
    }

    /**
    @return  The average of all entered values.
    */
    public double getAverage()
    {
        int totalValues = 0, sum = 0;

        for (Map.Entry<Integer, Integer> entry : values.entrySet())
        {
            totalValues += entry.getValue();
            sum += entry.getKey() * entry.getValue();
        }

        return ((double) sum) / totalValues;
    }

    public String getRepartitionString()
    {
        String result = "";

        // First compute the total sum of values.
        int sum = getSum();

        // Then, for each value, print the repartition if the current element.
        for (Map.Entry<Integer, Integer> entry : values.entrySet())
        {
            result += String.format("%d: %2.2f%%\n", entry.getKey(), entry.getValue() * 100. / sum);
        }

        return result;
    }

    public Map<Integer, Double> getPercentileRepartition()
    {
        Map<Integer, Double> result = new HashMap<>();
        // First compute the total sum of values.
        int sum = getSum();

        // For each value:
        int previous = 0;
        for (Map.Entry<Integer, Integer> entry : values.entrySet())
        {
            previous += entry.getValue();
            result.put(entry.getKey(), previous * 100. / sum);
        }

        return result;
    }

    public String getPercentileRepartitionString()
    {
        String result = "";
        for (Map.Entry<Integer, Double> entry : getPercentileRepartition().entrySet())
        {
            result += String.format("%d: %2.2f%%\n", entry.getKey(), entry.getValue() * 100.);
        }

        return result;
    }

    public String getInversePercentileRepartitionString()
    {
        String result = "";
        for (Map.Entry<Integer, Double> entry : getPercentileRepartition().entrySet())
        {
            result += String.format("%d: %2.2f%%\n", entry.getKey(), (1 - entry.getValue()) * 100.);
        }

        return result;
    }

    private int getSum()
    {
        int sum = 0;
        for (Map.Entry<Integer, Integer> entry : values.entrySet())
        {
            sum += entry.getValue();
        }
        return sum;
    }
}

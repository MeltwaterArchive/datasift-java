package org.datasift.examples.docs;

import java.util.HashMap;

import org.datasift.*;

/**
 * @author Courtney
 * @version 0.1
 */
public class CostExample {

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {

            String csdl = "interaction.type == \"twitter\" and (interaction.content contains \"music\")";

            // Step 1 - Authenticate
            User user = new User(Config.username, Config.api_key);

            // Create the definition
            System.out.println("Creating definition...");
            System.out.println("  " + csdl);
            Definition def = user.createDefinition(csdl);

            // Get the cost breakdown from which we can then see individual costs
            Cost c = def.getCostBreakdown();
            HashMap<String, CostItem> costs = c.getCosts();

            //iterate over each item
            for (String key : costs.keySet()) {
                System.out.println("Target : " + key);
                System.out.println("    Cost (Complexity): " + costs.get(key).getCost());
                System.out.println("    Count(Time used) : " + costs.get(key).getCount());
            }

            int totalCost = c.getTotalCost();

            int tierNum = 0;
            String tierDesc = "";

            if (totalCost > 1000) {
                tierNum = 3;
                tierDesc = "high complexity";
            } else if (totalCost > 100) {
                tierNum = 2;
                tierDesc = "medium complexity";
            } else {
                tierNum = 1;
                tierDesc = "simple complexity";
            }
            System.out.println("A total cost of " + totalCost + " puts this stream in tier " + tierNum + ", " + tierDesc);
        } catch (Exception e) {
            System.out.print("InvalidData: ");
            System.out.println(e.getMessage());
        }
    }
}

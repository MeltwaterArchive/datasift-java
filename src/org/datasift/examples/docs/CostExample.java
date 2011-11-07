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
            DPU d = def.getDPUBreakdown();
            HashMap<String, DPUItem> dpus = d.getDPU();

            //iterate over each item
            for (String key : dpus.keySet()) {
                System.out.println("Target : " + key);
                System.out.println("    DPU (Complexity): " + dpus.get(key).getDPU());
                System.out.println("    Count(Time used): " + dpus.get(key).getCount());
            }

            double totalDPU = d.getTotal();

            System.out.println("This gives a total DPU of " + totalDPU);
        } catch (Exception e) {
            System.out.print("InvalidData: ");
            System.out.println(e.getMessage());
        }
    }
}

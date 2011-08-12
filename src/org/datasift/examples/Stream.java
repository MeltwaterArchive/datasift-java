package org.datasift.examples;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.datasift.Config;
import org.datasift.Definition;
import org.datasift.Interaction;
import org.datasift.User;

/**
 *
 * @author courtney
 */
public class Stream {

    public static void main(String[] args) {
        try {
            //step 1 - create a user object for authentication
            User user = new User(Config.username, Config.api_key);
            //step 2 - create a definition
            Definition def = user.createDefinition("interaction.type == \"twitter\" and (interaction.content contains \"music\")");
            //step 3a - Method 1 - make a request to have our csdl compiled to get a hash
            def.compile();
            System.out.println("Hash:" + def.getHash());
            System.out.println("Created at:" + def.getCreatedAt());
            System.out.println("Total cost:" + def.getTotalCost());

            List<Interaction> data = null;
            while ((data = def.getBuffered()).size() == 0) {
                Thread.sleep(1000);//wait and retry after a second
            }
            for (Interaction i : data) {
                System.out.println(i.getString("interaction"));
            }

            //step 3b - get up to N amount of interactions
            System.out.println("\n--------------------------Limit to 5 max--------------------------------------\n");
            data = def.getBuffered(5);
            String fromid = null;
            for (Interaction i : data) {
                System.out.println(i.getString("interaction"));
                fromid = i.getStringVal("interaction.id");
            }
            //step 3c - get from a specific interaction onwards
            data = def.getBuffered(fromid);
            System.out.println("\n----------------------------Start from a given ID(" + fromid + ")------------------------------------\n");
            for (Interaction i : data) {
                System.out.println(i.getString("interaction"));
            }

            //step 3b - get N amount from a given id
            data = def.getBuffered(10, fromid);
            System.out.println("\n---------------------------Get 10 from a given ID(" + fromid + ")-------------------------------------\n");
            for (Interaction i : data) {
                System.out.println(i.getString("interaction"));
            }
        } catch (Exception ex) {
            System.err.println();
            Logger.getLogger("net.datasift.example").log(Level.WARNING, "An error occured: " + ex.getMessage());
        }

    }
}
package org.datasift.examples.docs;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.datasift.Definition;
import org.datasift.User;
import org.datasift.Config;
/**
 *
 * @author courtney
 */
public class Compile {

    public static void main(String[] args) {
        try {
            //step 1 - create a user object for authentication
            User user = new User(Config.username, Config.api_key);
            //step 2 - create a definition
            Definition def = user.createDefinition("interaction.type == \"twitter\" and (interaction.content contains \"music\")");
            //step 3 - make the request to have our csdl compiled
            def.compile();
            System.out.println("Hash:" + def.getHash());
            System.out.println("Created at:" + def.getCreatedAt());
            System.out.println("Total cost:" + def.getTotalCost());

        } catch (Exception ex) {
            System.err.println();
            Logger.getLogger("net.datasift.example").log(Level.WARNING, "An error occured: " + ex.getMessage());
        }

    }
}
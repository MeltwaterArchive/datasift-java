package org.datasift.examples.docs;

/**
 *
 * @author courtney
 */
import java.util.logging.Level;
import java.util.logging.Logger;
import org.datasift.Definition;
import org.datasift.Config;
import org.datasift.User;

public class Validate {

	public static void main(String[] args) {
		try {
			//step 1 - create a user object for authentication
			User user = new User(Config.username, Config.api_key);
			//step 2 - create a definition
			Definition def = user.createDefinition("interaction.type == \"twitter\" and (interaction.content contains \"music\")");
			//step 3 - make the request to have our csdl compiled
			def.validate();
			System.out.println("Hash:" + def.getHash());
			System.out.println("Created at:" + def.getCreatedAt());
			System.out.println("Total cost:" + def.getTotalCost());
		} catch (Exception ex) {
			System.err.println();
			Logger.getLogger("net.datasift.example").log(Level.WARNING, "An error occured: " + ex.getMessage());
			/**
			 * In the event of an error you would see something similar to the following:
			 * 26-Sep-2011 16:01:07 org.datasift.examples.docs.Validate main
			 *WARNING: An error occured: The target indteraction.type does not exist
			 */
		}
	}
}
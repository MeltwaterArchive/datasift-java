package com.datasift.client.push.pull;

import com.datasift.client.DataSiftClient;
import com.datasift.client.stream.Interaction;

/*
 * @author Courtney Robinson <courtney@crlog.info>
 */
public class LastInteraction extends Interaction {
    public static Interaction INSTANCE = new LastInteraction();

    protected LastInteraction() {
        super(DataSiftClient.MAPPER.createObjectNode());
    }
}

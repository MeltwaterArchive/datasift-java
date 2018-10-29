package com.datasift.client.managedsource.sources;

import com.datasift.client.DataSiftConfig;
import com.datasift.client.managedsource.ManagedDataSourceType;

import java.util.ArrayList;

/*
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class GooglePlus extends BaseSource<GooglePlus> {
    public GooglePlus(DataSiftConfig config) {
        super(config, ManagedDataSourceType.GOOGLE_PLUS);
    }

    public GooglePlus setParams(boolean enableComments, boolean enablePlusOnes) {
        return enableComments(enableComments).enablePlusOnes(enablePlusOnes);
    }

    /*
     * set to true, if you want to receive comment interactions.
     *
     * @return this
     */
    public GooglePlus enableComments(boolean enabled) {
        return setParametersField("comments", enabled);
    }

    /*
     * set to true, if you want to receive +1 interactions.
     *
     * @return this
     */
    public GooglePlus enablePlusOnes(boolean enabled) {
        return setParametersField("plus_ones", enabled);
    }

    public GooglePlus addResource(Type type, String userId) {
        ResourceParams parameterSet = newResourceParams();
        switch (type) {
            case PUBLIC_ACTIVITIES:
                parameterSet.set("type", "public_activities");
                break;
            case PEOPLE_UPDATES:
                if (userId == null || userId.isEmpty()) {
                    throw new IllegalArgumentException("If type is people_updates then userId is required");
                }
                parameterSet.set("type", "people_updates");
                parameterSet.set("user_id", userId);
                parameterSet.set("event_types", new ArrayList<String>() {
                    {
                        //activities is currently the only supported event type
                        add("activities");
                    }
                });
                break;
            default:
                throw new IllegalArgumentException("Unsupported type");
        }
        return this;
    }

    /*
     * Adds an OAuth token to the managed source
     *
     * @param oAuthAccessToken  an oauth2 token
     * @param oAuthRefreshToken an oauth2 refresh token
     * @param name              a human friendly name for this auth token
     * @param expires           identity resource expiry date/time as a UTC timestamp, i.e. when the token expires
     * @return this
     */
    public GooglePlus addOAutToken(String oAuthAccessToken, String oAuthRefreshToken, String name, long expires) {
        if (oAuthAccessToken == null || oAuthAccessToken.isEmpty() ||
                oAuthRefreshToken == null || oAuthRefreshToken.isEmpty()) {
            throw new IllegalArgumentException("A valid OAuth and refresh token is required");
        }
        AuthParams parameterSet = newAuthParams(name, expires);
        parameterSet.set("value", oAuthAccessToken);
        parameterSet.set("refresh_token", oAuthRefreshToken);
        return this;
    }

    public static enum Type {
        PEOPLE_UPDATES,
        PUBLIC_ACTIVITIES
    }

}

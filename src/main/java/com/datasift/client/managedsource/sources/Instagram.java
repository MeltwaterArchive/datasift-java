package com.datasift.client.managedsource.sources;

import com.datasift.client.DataSiftConfig;
import com.datasift.client.managedsource.ManagedDataSourceType;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class Instagram extends BaseSource<Instagram> {
    public Instagram(DataSiftConfig config) {
        super(config, ManagedDataSourceType.INSTAGRAM);
    }

    /**
     * set to true, if you want to receive like interactions.
     *
     * @return this
     */
    public Instagram enableLikes(boolean enabled) {
        return setParametersField("likes", enabled);
    }

    /**
     * set to true, if you want to receive comment interactions.
     *
     * @return this
     */
    public Instagram enableComments(boolean enabled) {
        return setParametersField("comments", enabled);
    }

    /**
     * set to true, if you want to receive comment count interactions.
     *
     * @return this
     */
    public Instagram enableCommentCounts(boolean enabled) {
        return setParametersField("comment_count", enabled);
    }

    /**
     * set to true, if you want to receive like count interactions.
     *
     * @return this
     */
    public Instagram enableLikeCounts(boolean enabled) {
        return setParametersField("like_count", enabled);
    }

    /**
     * Adds a resource by tag
     *
     * @param tag        the tag, e.g cat
     * @param exactMatch true when a tag must be an exact match or false when the tag should match the instragram tag
     *                   search behaviour
     * @return this
     */
    public Instagram byTag(String tag, boolean exactMatch) {
        addResource(Type.TAG, tag, -1, -1, -1, exactMatch, null);
        return this;
    }

    /**
     * Adds a resource object to the request of the given type, which is always required
     *
     * @param type the type of resource, all other params are optional dependent upon what this value is
     * @return this
     */
    protected Instagram addResource(Type type, String value, float longitude, float lattitude, int distance,
                                    boolean exactMatch, String fourSquareLocation) {
        ResourceParams parameterSet = newResourceParams();
        switch (type) {
            case TAG:
                if (value == null) {
                    throw new IllegalArgumentException("If type is user then value is required");
                }
                parameterSet.set("type", "tag");
                parameterSet.set("value", value);
                parameterSet.set("extact_match", exactMatch);
                break;
        }
        return this;
    }

    /**
     * Adds an OAuth token to the managed source
     *
     * @param oAuthAccessToken an oauth2 token
     * @param name             a human friendly name for this auth token
     * @param expires          identity resource expiry date/time as a UTC timestamp, i.e. when the token expires
     * @return this
     */
    public Instagram addOAutToken(String oAuthAccessToken, long expires, String name) {
        if (oAuthAccessToken == null || oAuthAccessToken.isEmpty()) {
            throw new IllegalArgumentException("A valid OAuth and refresh token is required");
        }
        AuthParams parameterSet = newAuthParams(name, expires);
        parameterSet.set("value", oAuthAccessToken);
        return this;
    }

    public static enum Type {
        TAG
    }
}

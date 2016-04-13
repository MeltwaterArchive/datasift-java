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
     * Adds a managed instagram source by user
     *
     * @param userId the ID of the user
     * @return this
     */
    public Instagram byUser(String userId) {
        addResource(Type.USER, userId, -1, -1, -1, false, null);
        return this;
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

    public Instagram byArea(float longitude, float lattitude) {
        return byArea(longitude, lattitude);
    }

    /**
     * Adds a resource a given area
     *
     * @param longitude the longitude value of the coordinates to use
     * @param latitude  the latitude value of the coordinate to use
     * @param distance  an optional distance which marks the radius around the given area,
     *                  defaults to 1000 and must be between 1 and 5000 metres
     * @return this
     */
    public Instagram byArea(float longitude, float latitude, int distance) {
        addResource(Type.AREA, null, longitude, latitude, distance, false, null);
        return this;
    }

    public Instagram byLocation(float longitude, float latitude) {
        return byLocation(longitude, latitude, 0);
    }

    /**
     * Adds a resource a given location
     *
     * @param longitude the longitude value of the coordinates to use
     * @param latitude  the latitude value of the coordinate to use
     * @param distance  an optional distance which marks the radius around the given area,
     *                  defaults to 1000 and must be between 1 and 5000 metres
     * @return this
     * @see #byArea(float, float, int)
     */
    public Instagram byLocation(float longitude, float latitude, int distance) {
        addResource(Type.LOCATION, null, longitude, latitude, distance, false, null);
        return this;
    }

    /**
     * The same as {@link #byLocation(float, float, int)} but instead of using coordinates a foursquare location ID
     * is provided
     *
     * @param fourSquareLocation a valid foursquare location ID e.g.  5XfVJe
     * @return this
     */
    public Instagram byFoursquareLocation(String fourSquareLocation) {
        throw new Exception("Instagram has deprecated foursquare support since 20th April 2016");
    }

    /**
     * Adds a resource that is filtered based on popularity
     *
     * @return this
     */
    public Instagram byPopularity() {
        addResource(Type.POPULAR, null, -1, -1, -1, false, null);
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
            case USER:
                if (value == null) {
                    throw new IllegalArgumentException("If type is user then value is required");
                }
                parameterSet.set("type", "user");
                parameterSet.set("value", value);
                break;
            case TAG:
                if (value == null) {
                    throw new IllegalArgumentException("If type is user then value is required");
                }
                parameterSet.set("type", "tag");
                parameterSet.set("value", value);
                parameterSet.set("extact_match", exactMatch);
                break;
            case AREA:
            case LOCATION:
                if (value == null || distance > 5000) {
                    throw new IllegalArgumentException("If provided distance must be between 1 and 5000 metres or < 0" +
                            " to be ignored");
                }
                if (type == Type.LOCATION) {
                    parameterSet.set("type", "location");
                    if (fourSquareLocation != null) {
                        parameterSet.set("foursq", fourSquareLocation);
                    }
                } else {
                    parameterSet.set("type", "area");
                }
                if (fourSquareLocation == null) {
                    parameterSet.set("lat", lattitude);
                    parameterSet.set("lng", longitude);
                    if (distance > 0) {
                        parameterSet.set("distance", lattitude);
                    }
                }
                break;
            case POPULAR:
                parameterSet.set("type", "popular");
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
        USER, TAG, AREA, LOCATION, POPULAR
    }
}

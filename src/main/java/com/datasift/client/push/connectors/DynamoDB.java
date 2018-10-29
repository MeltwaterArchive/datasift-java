package com.datasift.client.push.connectors;

import com.datasift.client.push.OutputType;

/*
 * See <a href="http://dev.datasift.com/docs/push/connectors/dynamodb">Official docs</a>
 *
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class DynamoDB extends BaseConnector<DynamoDB> {
    public DynamoDB() {
        super(OutputType.DYNAMO_DB);
        setup(this, "table", "auth.access_key", "auth.secret_key", "region");
    }

    /*
     * @param table The name of the Amazon DynamoDB table where the data is stored.
     * @return this
     */
    public DynamoDB table(String table) {
        return setParam("table", table);
    }

    /*
     * @param key Your Amazon AWS access key. Make sure this value is properly URL-encoded. This on-line tool can
     *            help you properly encode data you want to use in an HTTP request.
     * @return this
     */
    public DynamoDB accessKey(String key) {
        return setParam("auth.access_key", key);
    }

    /*
     * @param secret Your Amazon AWS secret key. Make sure this value is properly URL-encoded. This on-line tool can
     *               help you properly encode data you want to use in an HTTP request.
     * @return this
     */
    public DynamoDB secretKey(String secret) {
        return setParam("auth.secret_key", secret);
    }

    /*
     * The id of the AWS region your DynamoDB table lives in.
     * <p/>
     * This has to be the full-length id. Currently, the following ids are supported:
     * <p/>
     * dynamodb.ap-northeast-1.amazonaws.com - See {@link Region#AP_NORTH_EAST_1}
     * <p/>
     * dynamodb.eu-west-1.amazonaws.com  - See {@link Region#EU_WEST_1}
     * <p/>
     * dynamodb.us-east-1.amazonaws.com  - See {@link Region#US_EAST_1}
     * <p/>
     * dynamodb.us-west-2.amazonaws.com  - See {@link Region#US_WEST_2}
     * <p/>
     * dynamodb.us-west-1.amazonaws.com  - See {@link Region#US_WEST_1}
     * <p/>
     * dynamodb.ap-southeast-1.amazonaws.com  - See {@link Region#AP_SOUTH_EAST_1}
     *
     * @return this
     */
    public DynamoDB region(Region region) {
        return setParam("region", region.getRegion());
    }

    public static class Region {
        public static final Region AP_NORTH_EAST_1 = new Region("dynamodb.ap-northeast-1.amazonaws.com");
        public static final Region AP_SOUTH_EAST_1 = new Region("dynamodb.ap-southeast-1.amazonaws.com");
        public static final Region EU_WEST_1 = new Region("dynamodb.eu-west-1.amazonaws.com");
        public static final Region US_EAST_1 = new Region("dynamodb.us-east-1.amazonaws.com");
        public static final Region US_WEST_1 = new Region("dynamodb.us-west-1.amazonaws.com");
        public static final Region US_WEST_2 = new Region("dynamodb.us-west-2.amazonaws.com");
        protected final String region;

        public Region(String region) {
            if (region == null || region.isEmpty()) {
                throw new IllegalArgumentException("Region can't be empty or null");
            }
            this.region = region;
        }

        public String getRegion() {
            return region;
        }
    }
}

package com.datasift.client.push.destinations;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class BigQueryConnector extends BaseConnector<BigQueryConnector> {
    protected BigQueryConnector() {
        setup(this);
    }

    /**
     * Your Google API Access Client ID for the project you want to push interactions into. You can find it in the
     * API Access tab on your Google API account page for the project you are working with. It starts with a unique
     * project id and ends with .apps.googleusercontent.com.
     */
    public BigQueryConnector authClientId(String clientId) {
        return setParam("auth.client_id", clientId, true);
    }

    /**
     * The email address created for the service account for the project you want to push interactions into. You can
     * find it in the API Access tab on your Google API account page for the project you are working with. It starts
     * with the numeric id and ends with @developer.gserviceaccount.com.
     */
    public BigQueryConnector authServiceAccount(String serviceAccount) {
        return setParam("auth.service_account", serviceAccount, true);
    }

    /**
     * A Base64-encoded, URL-encoded contents of a PCKS12 key file generated using the Google API Console.
     */
    public BigQueryConnector authKeyFile(String keyFile) {
        return setParam("auth.key_file", keyFile, true);
    }

    /**
     * The numeric project ID generated for you by Google. You can find it in the Overview tab for your project in
     * the Google API Console. Look for Project Number.
     */
    public BigQueryConnector projectId(String projectId) {
        return setParam("project_id", projectId, true);
    }

    /**
     * The dataset id that you want to use to store your tables. The dataset must exist before you create a Push
     * subscription to the Google BigQuery connector. Its name may only contain letters, numbers, and underscores.
     */
    public BigQueryConnector datasetId(String datasetId) {
        return setParam("dataset_id", datasetId, true);
    }

    /**
     * A unique name of the table you want to use to store interactions. The the table does not exist,
     * DataSift will create it for you. The table name may only contain letters, numbers,
     * and underscores.
     */
    public BigQueryConnector tableId(String tableId) {
        return setParam("table_id", tableId, true);
    }

}

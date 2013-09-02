package com.datasift.client.push.connectors;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class BigQuery extends BaseConnector<BigQuery> {
    protected BigQuery() {
        setup(this, "auth.client_id", "auth.service_account", "auth.key_file", "project_id", "dataset_id", "table_id");
    }

    /**
     * Your Google API Access Client ID for the project you want to push interactions into. You can find it in the
     * API Access tab on your Google API account page for the project you are working with. It starts with a unique
     * project id and ends with .apps.googleusercontent.com.
     */
    public BigQuery authClientId(String clientId) {
        return setParam("auth.client_id", clientId);
    }

    /**
     * The email address created for the service account for the project you want to push interactions into. You can
     * find it in the API Access tab on your Google API account page for the project you are working with. It starts
     * with the numeric id and ends with @developer.gserviceaccount.com.
     */
    public BigQuery authServiceAccount(String serviceAccount) {
        return setParam("auth.service_account", serviceAccount);
    }

    /**
     * A Base64-encoded, URL-encoded contents of a PCKS12 key file generated using the Google API Console.
     */
    public BigQuery authKeyFile(String keyFile) {
        return setParam("auth.key_file", keyFile);
    }

    /**
     * The numeric project ID generated for you by Google. You can find it in the Overview tab for your project in
     * the Google API Console. Look for Project Number.
     */
    public BigQuery projectId(String projectId) {
        return setParam("project_id", projectId);
    }

    /**
     * The dataset id that you want to use to store your tables. The dataset must exist before you create a Push
     * subscription to the Google BigQuery connector. Its name may only contain letters, numbers, and underscores.
     */
    public BigQuery datasetId(String datasetId) {
        return setParam("dataset_id", datasetId);
    }

    /**
     * A unique name of the table you want to use to store interactions. The the table does not exist,
     * DataSift will create it for you. The table name may only contain letters, numbers,
     * and underscores.
     */
    public BigQuery tableId(String tableId) {
        return setParam("table_id", tableId);
    }

}

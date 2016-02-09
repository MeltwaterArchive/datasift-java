package com.datasift.client.pylon;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.datasift.client.pylon.PylonRecording.PylonRecordingId;

public class PylonQuery {
    @JsonIgnore
    protected PylonRecordingId recordingId;

    @JsonProperty
    protected PylonQueryParameters parameters;

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    protected String filter;

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    protected int start;

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    protected int end;

    public PylonQuery() { }

    public PylonQuery(PylonRecordingId recordingId,
                      PylonQueryParameters parameters,
                      String filter,
                      Integer start,
                      Integer end) {
        this.recordingId = recordingId;
        this.parameters = parameters;
        this.filter = filter;
        if (start != null) { this.start = start; }
        if (end != null) { this.end = end; }
    }

    public PylonQuery(PylonRecordingId recordingId, PylonQueryParameters parameters, String filter) {
        this(recordingId, parameters, filter, null, null);
    }

    public PylonQuery(PylonRecordingId recordingId, PylonQueryParameters parameters) {
        this(recordingId, parameters, null, null, null);
    }

    @JsonGetter
    private String getId() { return this.recordingId.id; }

    @JsonSetter
    private void setId(String id) { this.recordingId = new PylonRecordingId(id); }

    public PylonRecordingId getRecordingId() { return this.recordingId; }

    public PylonQueryParameters getParameters() { return this.parameters; }

    public String getFilter() { return this.filter; }

    public int getStart() { return this.start; }

    public int getEnd() { return this.end; }
}

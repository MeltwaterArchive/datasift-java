package com.datasift.client.pylon;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import com.datasift.client.pylon.PylonRecording.PylonRecordingId;

public class PylonSampleRequest {
    @JsonIgnore
    protected PylonRecordingId recordingId;

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    protected int count;

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    protected String filter;

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    protected int start;

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    protected int end;

    public PylonSampleRequest() { }

    public PylonSampleRequest(PylonRecordingId recordingId, Integer count, String filter, Integer start, Integer end) {
        this.recordingId = recordingId;
        this.filter = filter;
        if (count != null) { this.count = count; }
        if (start != null) { this.start = start; }
        if (end != null) { this.end = end; }
    }

    public PylonSampleRequest(PylonRecordingId recordingId, Integer count, String filter) {
        this(recordingId, count, filter, null, null);
    }

    public PylonSampleRequest(PylonRecordingId recordingId, Integer count) {
        this(recordingId, count, null, null, null);
    }

    public PylonSampleRequest(PylonRecordingId recordingId, String filter) {
        this(recordingId, null, filter, null, null);
    }

    public PylonSampleRequest(PylonRecordingId recordingId) { this(recordingId, null, null, null, null); }

    @JsonGetter
    private String getId() { return this.recordingId.id; }

    @JsonSetter
    private void setId(String id) { this.recordingId = new PylonRecordingId(id); }

    public PylonRecordingId getRecordingId() { return this.recordingId; }

    public Integer getCount() { return this.count; }

    public String getFilter() { return this.filter; }

    public int getStart() { return this.start; }

    public int getEnd() { return this.end; }
}

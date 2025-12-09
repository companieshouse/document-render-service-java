package uk.gov.companieshouse.documentrender.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Map;
import java.util.TreeMap;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Document {

    private final Map<String, Object> data = new TreeMap<>();

    public Map<String, Object> getData() {
        return data;
    }

    @com.fasterxml.jackson.annotation.JsonAnySetter
    public void setProperty(String name, Object value) {
        data.put(name, value);
    }

    @Override
    public String toString() {
        return "Document{" +
                "data=" + data +
                '}';
    }
}

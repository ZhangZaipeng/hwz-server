package com.hwz.platform.es;

import com.google.gson.Gson;
import com.hwz.platform.platform.JsonWapper;

import io.searchbox.client.JestResult;

public class RestFulResult extends JestResult {

    private JsonWapper jw;

    public RestFulResult(Gson gson) {
        super(gson);
    }

    public JsonWapper getJsonWapper() {
        if (jw == null) {
            jw = new JsonWapper(this.getJsonString());
        }
        return jw;
    }

}

package com.hwz.platform.es;

import com.google.gson.Gson;
import com.hwz.platform.platform.JsonWapper;

import io.searchbox.action.AbstractAction;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class RestFulAction extends AbstractAction<RestFulResult> {

    protected String url;
    protected Builder builder;

    protected RestFulAction(Builder builder) {
        super(builder);
        this.builder = builder;
        //this.payload = builder.jb.toString(false);
        this.payload = builder.body;
        setURI(buildURI());
    }

    @Override
    protected String buildURI() {
        return super.buildURI() + this.builder.getUrl();
    }

    @Override
    public String getRestMethodName() {
        return this.getMethod();
    }

    @Override
    public RestFulResult createNewElasticSearchResult(String responseBody, int statusCode, String reasonPhrase,
                                                      Gson gson) {
        return createNewElasticSearchResult(new RestFulResult(gson), responseBody, statusCode, reasonPhrase, gson);
    }

    public static class Builder extends AbstractAction.Builder<RestFulAction, Builder> {

        //private final JsonWapper jb;
        private final String body;
        private String url;
        private String method = "GET";

        public Builder() {
            //this.jb = new JsonWapper();
            this.body = "";
        }

        public Builder(JsonWapper jb) {
            //this.jb = jb;
            this.body = jb.toString(false);
        }

        public Builder(String body) {
            //this.jb = new JsonWapper(body);
            this.body = body;
        }

        @Override
        public RestFulAction build() {
            return new RestFulAction(this);
        }

        @Override
        public String toString() {
            //return this.jb.toString();
            return this.body;
        }

        public Builder setUrl(String url) {
            assert url.startsWith("/");
            this.url = url;
            return this;
        }

        public String getUrl() {
            return this.url;
        }

        public Builder setPostMethod() {
            this.method = "POST";
            return this;
        }

        public Builder setPutMethod() {
            this.method = "PUT";
            return this;
        }

        public Builder setDeleteMethod() {
            this.method = "DELETE";
            return this;
        }

        public Builder setUpdateMethod() {
            this.method = "UPDATE";
            return this;
        }

        public Builder setGetMethod() {
            this.method = "GET";
            return this;
        }
    }

    public String getMethod() {
        return this.builder.method;
    }

    @Override
    public String toString() {
        return "curl -X" + this.getMethod() + " '" + super.getURI() + "' -d'\r\n" +
                this.builder.toString() +
                "'";
    }

    public static void main(String[] args) throws IOException {
        final JestClientFactory factory = new JestClientFactory();
        List<String> servers = Arrays.asList("http://10.0.14.225:9200",
                "http://10.0.14.226:9200",
                "http://10.0.14.227:9200",
                "http://10.0.14.229:9200");
        factory.setHttpClientConfig(new HttpClientConfig.Builder(servers).multiThreaded(true).build());
        final JestClient client = factory.getObject();

        JsonWapper jb = new JsonWapper();
        jb.asList("text")
                .append("你好，今天吃饭了吗？")
                .append("奥迪 A4L 豪华版 极地白 中国");

        RestFulAction request = new Builder(jb)
                .setUrl("/_analyze")
                .setParameter("analyzer", "whitespace")
                        // .setParameter("text", "奥迪 A4L 豪华版 极地白 中国")
                .build();
        System.out.println(request);

        RestFulResult r = client.execute(request);
        System.out.println(r.getJsonWapper().toString());

        client.shutdownClient();
    }

}

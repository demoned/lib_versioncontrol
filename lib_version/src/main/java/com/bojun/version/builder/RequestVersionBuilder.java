package com.bojun.version.builder;

import com.bojun.version.callback.RequestVersionListener;
import com.bojun.version.core.http.HttpHeaders;
import com.bojun.version.core.http.HttpParams;
import com.bojun.version.core.http.HttpRequestMethod;
public class RequestVersionBuilder {
    private HttpRequestMethod requestMethod;
    private HttpParams requestParams;
    private String requestUrl;
    private com.bojun.version.core.http.HttpHeaders httpHeaders;
    private RequestVersionListener requestVersionListener;

    public RequestVersionBuilder() {
        requestMethod = HttpRequestMethod.GET;

    }

    public HttpRequestMethod getRequestMethod() {
        return requestMethod;
    }

    public RequestVersionBuilder setRequestMethod(HttpRequestMethod requestMethod) {
        this.requestMethod = requestMethod;
        return this;
    }

    public HttpParams getRequestParams() {
        return requestParams;
    }

    public RequestVersionBuilder setRequestParams(HttpParams requestParams) {
        this.requestParams = requestParams;
        return this;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public RequestVersionBuilder setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
        return this;
    }

    public HttpHeaders getHttpHeaders() {
        return httpHeaders;
    }

    public RequestVersionBuilder setHttpHeaders(HttpHeaders httpHeaders) {
        this.httpHeaders = httpHeaders;
        return this;
    }

    public RequestVersionListener getRequestVersionListener() {
        return requestVersionListener;
    }

    /**
     * @param requestVersionListener requestVersionResultListener
     * @return download builder setting
     */
    public DownloadBuilder request(RequestVersionListener requestVersionListener) {
        this.requestVersionListener = requestVersionListener;
        return new DownloadBuilder(this, null);
    }

    public void destory() {
        requestVersionListener = null;
    }
}

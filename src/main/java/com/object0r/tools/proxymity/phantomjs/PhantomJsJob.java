package com.object0r.tools.proxymity.phantomjs;

import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * The type Phantom js job.
 */
public class PhantomJsJob {
    /**
     * The constant STATUS_PENDING.
     */
    final static String STATUS_PENDING = "pending";
    /**
     * The constant STATUS_PROCESSING.
     */
    final static String STATUS_PROCESSING = "processing";
    /**
     * The constant STATUS_SUCCESS.
     */
    final static String STATUS_SUCCESS = "success";
    /**
     * The constant STATUS_FAILED.
     */
    final static String STATUS_FAILED = "failed";
    /**
     * The constant REQUEST_ACTION_POST.
     */
    final static String REQUEST_ACTION_POST = "POST";
    /**
     * The constant REQUEST_ACTION_GET.
     */
    final static String REQUEST_ACTION_GET = "GET";

    /**
     * The Element type.
     */
    static String elementType = null;
    /**
     * The Cookies.
     */
    public HashMap<String, String> cookies = new HashMap<String, String>();
    /**
     * The Post parameters.
     */
    public HashMap<String, String> postParameters = new HashMap<String, String>();
    /**
     * The Url.
     */
    String url;
    /**
     * The Phantom js job result.
     */
    PhantomJsJobResult phantomJsJobResult;
    /**
     * The Request.
     */
    String request = REQUEST_ACTION_GET;
    /**
     * The Status.
     */
    String status = STATUS_PENDING;
    /**
     * The Exception.
     */
    Exception exception;

    /**
     * Instantiates a new Phantom js job.
     *
     * @param url the url
     */
    public PhantomJsJob(String url) {
        this.setUrl(url);
    }

    /**
     * Instantiates a new Phantom js job.
     *
     * @param url      the url
     * @param postBody the post body
     */
    public PhantomJsJob(String url, String postBody) {
        this.setUrl(url);
        StringTokenizer st = new StringTokenizer(postBody, "&");
        while (st.hasMoreTokens()) {
            String line = st.nextToken();
            if (line.contains("=")) {
                StringTokenizer st2 = new StringTokenizer(line, "=");
                addPostParameter(st2.nextToken(), st2.nextToken());
            }
        }
        this.setRequestPost();
    }

    /**
     * Add post parameter.
     *
     * @param key   the key
     * @param value the value
     */
    public void addPostParameter(String key, String value) {
        postParameters.put(key, value);
    }

    /**
     * Add cookie.
     *
     * @param key   the key
     * @param value the value
     */
    public void addCookie(String key, String value) {
        cookies.put(key, value);
    }

    /**
     * Gets post parameters.
     *
     * @return the post parameters
     */
    public HashMap<String, String> getPostParameters() {
        return postParameters;
    }

    /**
     * Gets cookies.
     *
     * @return the cookies
     */
    public HashMap<String, String> getCookies() {
        return cookies;
    }

    /**
     * Sets cookies.
     *
     * @param cookies the cookies
     */
    public void setCookies(HashMap<String, String> cookies) {
        this.cookies = cookies;
    }

    /**
     * Gets url.
     *
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets url.
     *
     * @param url the url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Gets phantom js job result.
     *
     * @return the phantom js job result
     */
    public PhantomJsJobResult getPhantomJsJobResult() {
        return phantomJsJobResult;
    }

    /**
     * Sets phantom js job result.
     *
     * @param phantomJsJobResult the phantom js job result
     */
    public void setPhantomJsJobResult(PhantomJsJobResult phantomJsJobResult) {
        this.phantomJsJobResult = phantomJsJobResult;
    }

    /**
     * Sets request post.
     */
    public void setRequestPost() {
        this.request = REQUEST_ACTION_POST;
    }

    /**
     * Sets request get.
     */
    public void setRequestGet() {
        this.request = REQUEST_ACTION_GET;
    }

    /**
     * Is request post boolean.
     *
     * @return the boolean
     */
    public boolean isRequestPost() {
        return this.request == REQUEST_ACTION_POST;
    }

    /**
     * Is request get boolean.
     *
     * @return the boolean
     */
    public boolean isRequestGet() {
        return this.request == REQUEST_ACTION_GET;
    }

    /**
     * Is pending boolean.
     *
     * @return the boolean
     */
    public boolean isPending() {
        return status.equals(STATUS_PENDING);
    }

    /**
     * Is finished boolean.
     *
     * @return the boolean
     */
    public boolean isFinished() {
        return status.equals(STATUS_SUCCESS) || status.equals(STATUS_FAILED);
    }

    /**
     * Is successful boolean.
     *
     * @return the boolean
     */
    public boolean isSuccessful() {
        return status.equals(STATUS_SUCCESS);
    }

    /**
     * Sets status failed.
     */
    public void setStatusFailed() {
        this.status = STATUS_FAILED;
    }

    /**
     * Sets status success.
     */
    public void setStatusSuccess() {
        this.status = STATUS_SUCCESS;
    }

    /**
     * Sets status pending.
     */
    public void setStatusPending() {
        this.status = STATUS_PENDING;
    }

    /**
     * Sets status processing.
     */
    public void setStatusProcessing() {
        this.status = STATUS_PROCESSING;
    }

    /**
     * Gets exception.
     *
     * @return the exception
     */
    public Exception getException() {
        return exception;
    }

    /**
     * Sets exception.
     *
     * @param exception the exception
     */
    public void setException(Exception exception) {
        this.exception = exception;
    }
}

package common.util;

import java.io.Serializable;

/**
 * Class for get response value.
 */
public class Response implements Serializable {
    private ResponseCode responseCode;
    private String responseBody;

    public Response(ResponseCode responseCode, String responseBody) {
        this.responseCode = responseCode;
        this.responseBody = responseBody;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }


    public String getResponseBody() {
        return responseBody;
    }

    @Override
    public String toString() {
        return "Response[" + responseCode + ", " + responseBody + "]";
    }
}
public class RestResponse {

    private int code;
    private String message;
    private Object data;

    public RestResponse() {}

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static RestResponse success() {
        RestResponse restResponse = new RestResponse();
        restResponse.setCode(0);
        restResponse.setMessage("Success");
        return restResponse;
    }

    public static RestResponse success(Object data) {
        RestResponse restResponse = new RestResponse();
        restResponse.setCode(0);
        restResponse.setMessage("Success");
        restResponse.setData(data);
        return restResponse;
    }

    public static RestResponse success(String message) {
        RestResponse restResponse = new RestResponse();
        restResponse.setCode(0);
        restResponse.setMessage(message);
        return restResponse;
    }

    public static RestResponse success(String message, Object data) {
        RestResponse restResponse = new RestResponse();
        restResponse.setCode(0);
        restResponse.setMessage(message);
        restResponse.setData(data);
        return restResponse;
    }

    public static RestResponse fail(int code) {
        RestResponse restResponse = new RestResponse();
        restResponse.setCode(code);
        return restResponse;
    }

    public static RestResponse fail(int code, String message) {
        RestResponse restResponse = new RestResponse();
        restResponse.setCode(code);
        restResponse.setMessage(message);
        return restResponse;
    }
}

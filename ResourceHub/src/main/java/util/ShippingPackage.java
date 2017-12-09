package main.java.util;

public class ShippingPackage {

    private String data;
    private String requestMethod;
    private String resource;

    public ShippingPackage(String data, String requestMethod, String resource) throws Exception {
        this.data = data;
        this.resource = resource;

        if (requestMethod.equals("GET") || requestMethod.equals("POST")) {
            this.requestMethod = requestMethod;
        } else {
            throw new Exception("wrong requestMethod only GET or POST are accepted");
        }
    }

    public String getData() {
        return data;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getResource() {
        return resource;
    }

}

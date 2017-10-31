package main.java.connection;

public class IPAddressData {

    private int activePort;
    private int externalPort;
    private int localPort;
    private String activeAddress;
    private String externalAddress;
    private String localAddress;

    public IPAddressData() {
        activePort = -1;
        externalPort = -1;
        localPort = -1;
        activeAddress = null;
        externalAddress = null;
        localAddress = null;
    }

    public int getActivePort() {
        return activePort;
    }

    public int getExternalPort() {
        return externalPort;
    }

    public int getLocalPort() {
        return localPort;
    }

    public String getActiveAddress() {
        return activeAddress;
    }

    public String getExternalAddress() {
        return externalAddress;
    }

    public String getLocalAddress() {
        return localAddress;
    }

    public void setActivePort(int activePort) {
        this.activePort = activePort;
    }

    public void setExternalPort(int externalPort) {
        this.externalPort = externalPort;
    }

    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    public void setActiveAddress(String activeAddress) {
        this.activeAddress = activeAddress;
    }

    public void setExternalAddress(String externalAddress) {
        this.externalAddress = externalAddress;
    }

    public void setLocalAddress(String localAddress) {
        this.localAddress = localAddress;
    }

}

package email_client.global;

public class PortServices { 
    //Hầu hết các server đều chung các port
    String portIMAP = "993";
    String portTLS = "465";
    String portSSL = "587";

    public String getPortIMAP() {
        return portIMAP;
    }

    public String getPortTLS() {
        return portTLS;
    }

    public String getPortSSL() {
        return portSSL;
    }
    
}

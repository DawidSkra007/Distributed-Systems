package service.message;

import service.core.ClientInfo;

public class ClientInfoMessage implements MySerializable {

    private ClientInfo clientInfo;

    public ClientInfoMessage() {

    }

    public ClientInfoMessage(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    public void setClientInfo(ClientInfo info) {
        this.clientInfo = info;
    }

    public ClientInfo getClientInfo() {
        return clientInfo;
    }

}

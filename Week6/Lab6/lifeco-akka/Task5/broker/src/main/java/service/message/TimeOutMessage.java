package service.message;

public class TimeOutMessage {
    private long token;

    public TimeOutMessage(long token) { 
        this.token = token;
    }

    public long getToken() {
        return token;
    }
}

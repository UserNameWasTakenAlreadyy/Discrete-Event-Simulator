abstract class Event {
    protected final Customer customer;
    protected final double time;
    
    Event(Customer customer, double time) {
        this.customer = customer;
        this.time = time;
    }

    public abstract Pair<Event, ImList<Server>> next(ImList<Server> availableServer);

    Customer getCustomer() {
        return this.customer;
    }

    double getTime() {
        return this.time;
    }

    int findFirstSC(ImList<Server> availableServer) {
        int res = -1;
        for (Server s : availableServer) {
            if (s.isSC()) {
                res = availableServer.indexOf(s);
                break;
            }
        }
        return res;
    }

}






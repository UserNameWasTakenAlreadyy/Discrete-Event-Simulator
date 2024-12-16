class Done extends Event {
    private final int index;
    private final boolean isSelfCheck;

    Done(Customer customer, double time, int index, boolean isSelfCheck) {
        super(customer, time);
        this.index = index;
        this.isSelfCheck = isSelfCheck;
    }

    @Override
    public String toString() {
        String server = isSelfCheck ? " self-check" : ""; 
        return String.format("%.3f %s done serving by%s %s\n", 
                super.time, 
                this.customer.getId(),
                server,
                this.index + 1);
    }

    public Pair<Event, ImList<Server>> next(ImList<Server> availableServer) {
        Server currServer = availableServer.get(this.index);
        if (!currServer.isSC()) {
            currServer = availableServer.get(this.index).rest();
        }
        availableServer = availableServer.set(this.index, currServer);
        return new Pair<Event, ImList<Server>>(this, availableServer);
    }

}

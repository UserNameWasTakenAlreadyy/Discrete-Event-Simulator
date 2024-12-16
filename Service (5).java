class Service extends Event {

    private final int index;
    private final boolean isSelfCheck;

    Service(Customer customer, double time, int index, boolean isSelfCheck) {
        super(customer, time);
        this.index = index;
        this.isSelfCheck = isSelfCheck;
    }

    public String toString() {
        String server = isSelfCheck ? " self-check" : ""; 
        return String.format("%.3f %s serves by%s %s\n", 
                super.time, 
                this.customer.getId(),
                server,
                this.index + 1);
    }

    public Pair<Event, ImList<Server>> next(ImList<Server> availableServer) {
        Server currServer = availableServer.get(index).resetServe().incrementTotal();
        currServer = currServer.nextService(super.time + customer.getService());
        double done = currServer.getNextAvailableTime();
      
        availableServer = availableServer.set(this.index, currServer);
 
        return new Pair<Event, ImList<Server>>(
                new Done(super.customer, done, this.index, this.isSelfCheck), availableServer);
    }
}
  

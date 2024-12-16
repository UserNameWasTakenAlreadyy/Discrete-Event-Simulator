class Leave extends Event {

    Leave(Customer customer, double time) {
        super(customer, time);
    }

    @Override
    public String toString() {
        return String.format("%.3f %s leaves\n", 
                this.customer.getArrival(), 
                this.customer.getId());
    }

    public Pair<Event, ImList<Server>> next(ImList<Server> availableServer) {
        return new Pair<Event, ImList<Server>>(this, availableServer);
    }
}



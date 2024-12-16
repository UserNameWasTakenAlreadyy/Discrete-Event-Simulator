class Arrival extends Event {


    Arrival(Customer customer, double time) {
        super(customer, time);
    }

    @Override
    public String toString() {
        return String.format("%.3f %s arrives\n", 
                super.customer.getArrival(), 
                super.customer.getId());
    }
    
    int findAvailableServer(ImList<Server> serversAvailableTime, double time) {
        int index = 0;
        while (index < serversAvailableTime.size() && 
                serversAvailableTime.get(index).getNextAvailableTime() > time) {
            index = index + 1;
        }
        if (index < serversAvailableTime.size()) {
            return index;
        } else {
            return -1;
        }
       
    }

    public Pair<Event, ImList<Server>> next(ImList<Server> availableServer) {
        int index = findAvailableServer(availableServer, super.customer.getArrival());
        int notAvail = -1;
        int firstSC = findFirstSC(availableServer);
      
        if (index != notAvail) {
            Server res = availableServer.get(index);
            availableServer = availableServer.set(index, res.toggleServe());
            return new Pair<Event, ImList<Server>>(
                    new Service(super.customer, super.time, index, res.isSC()), 
                    availableServer);
        } else {
            for (Server s : availableServer) {
                int serverIndex = availableServer.indexOf(s);
                if (s.canQueue() && !s.isSC()) {
                    Server updatedServer = s.enterQueue();
                    availableServer = availableServer.set(serverIndex, updatedServer);
                    return new Pair<Event, ImList<Server>>(
                            new Wait(
                                super.customer, 
                                super.time, serverIndex, true, false), 
                            availableServer);
                } else if (s.isSC()) {
                    Server first = availableServer.get(firstSC);
                    if (first.canQueue()) {
                        Server updatedServer = first.enterQueue();
                        availableServer = availableServer.set(firstSC, updatedServer);
                        return new Pair<Event, ImList<Server>>(
                                new Wait(super.customer, super.time, serverIndex, true, true),
                                availableServer);
                    }
                }
            }
        }

        return new Pair<Event, ImList<Server>>(
                new Leave(super.customer, super.time), 
                availableServer);
    }

}

 


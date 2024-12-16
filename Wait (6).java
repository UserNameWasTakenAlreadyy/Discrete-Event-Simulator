class Wait extends Event {

    private final int index;
    private final boolean isOriginal;
    private final boolean isSelfCheck;

    Wait(Customer customer, double time, int index, boolean isOriginal, boolean isSelfCheck) {
        super(customer, time);
        this.index = index;
        this.isOriginal = isOriginal;
        this.isSelfCheck = isSelfCheck;
    }

    @Override
    public String toString() {
        String server = this.isSelfCheck ? " self-check" : "";
        if (this.isOriginal) {
            return String.format("%.3f %s waits at%s %s\n", 
                    super.time, 
                    super.customer.getId(),
                    server,
                    this.index + 1);
        } else {
            return "";
        }
    }

    Server minTime(ImList<Server> availableServer) {
        int first = findFirstSC(availableServer);
        Server res = availableServer.get(first);
        double resTime = res.getNextAvailableTime();
        for (int i = first; i < availableServer.size(); i++) {
            Server temp = availableServer.get(i);
            if (temp.getNextAvailableTime() < resTime) {
                resTime = temp.getNextAvailableTime();
                res = temp;
            }
        }
        return res;
    }
        

    public Pair<Event, ImList<Server>> next(ImList<Server> availableServer) {
        Server res = availableServer.get(this.index);
        if (availableServer.get(index).getNextAvailableTime() > super.time) {
            if (!res.isSC()) {
                return new Pair<Event, ImList<Server>>(
                        new Wait(super.customer, 
                            availableServer.get(index).getNextAvailableTime(), 
                            this.index, false, this.isSelfCheck), availableServer);
            } else {
                res = minTime(availableServer);
                int resIndex = availableServer.indexOf(res);
                if (res.getNextAvailableTime() <= super.time) {
                    int tempIndex = findFirstSC(availableServer);
                    Server tempFirst = availableServer.get(tempIndex).exitQueue();
                    availableServer = availableServer.set(tempIndex, tempFirst);
                    res = res.toggleServe();
                    return new Pair<Event, ImList<Server>>(
                            new Service(super.customer, res.getNextAvailableTime(),
                                resIndex, false), availableServer);
                }
                return new Pair<Event, ImList<Server>>(
                        new Wait(super.customer, res.getNextAvailableTime(), 
                            resIndex, false, this.isSelfCheck), availableServer);
            }
        } else {
            int isSelfOrNot = findFirstSC(availableServer); 
            Server currServer = availableServer.get(this.index);
            if (!currServer.isSC()) {
                currServer = currServer.toggleServe().exitQueue();
            } else {
                Server firstSelfCheck = availableServer.get(isSelfOrNot).exitQueue();
                availableServer = availableServer.set(isSelfOrNot, firstSelfCheck);
                currServer = minTime(availableServer).toggleServe();
            }
            Server updatedServer = currServer.incrementWaitTime(
                    currServer.getNextAvailableTime() - super.customer.getArrival());
            availableServer = availableServer.set(this.index, updatedServer);
            return new Pair<Event, ImList<Server>>(
                    new Service(
                        super.customer, 
                        updatedServer.getNextAvailableTime(), 
                        availableServer.indexOf(updatedServer), updatedServer.isSC()), 
                    availableServer);
        }
    }
}
 



 
        

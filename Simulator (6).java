import java.util.Iterator;
import java.util.function.Supplier;

class Simulator {
    private final int numOfServers;
    private final int numOfSelfChecks;
    private final ImList<Double> arrivalTimes;
    private final Supplier<Double> serviceTimes;
    private final int qMax;
    private final Supplier<Double> restTimes;
    
    Simulator(int numOfServers, int numOfSelfChecks, int qMax, ImList<Double> arrivalTimes, 
            Supplier<Double> serviceTimes, Supplier<Double> restTimes) {
        this.numOfServers = numOfServers;
        this.numOfSelfChecks = numOfSelfChecks;
        this.qMax = qMax;
        this.arrivalTimes = arrivalTimes;
        this.serviceTimes = serviceTimes;
        this.restTimes = restTimes;
    }

    private ImList<Server> initServersAvailableTime(int numOfServers) {
        ImList<Server> serversAvailableTime = new ImList<Server>();
        for (int i = 1; i <= numOfServers; i++) {
            serversAvailableTime = serversAvailableTime.add(
                    new Server(0.0, i, qMax, 0, false, 0.0, 0, this.restTimes, false));
        }
        for (int j = numOfServers + 1; j <= numOfServers + numOfSelfChecks; j++) {
            serversAvailableTime = serversAvailableTime.add(
                    new Server(0.0, j, qMax, 0, false, 0.0, 0, this.restTimes, true));
        }
        return serversAvailableTime;
    }

    private ImList<Customer> initIncomingCustomers(ImList<Double> arrivalTimes, 
            Supplier<Double> serviceTimes) {
        ImList<Customer> incomingCustomers = new ImList<Customer>(); 
        for (int j = 1; j <= arrivalTimes.size(); j++) {
            incomingCustomers = incomingCustomers.add(
                    new Customer(arrivalTimes.get(j - 1),
                        serviceTimes, j));
        }
        return incomingCustomers;
    }

    private PQ<Event> initQueue() {
        return new PQ<Event>(new EventComp());
    }


    String processCustomers(ImList<Customer> incomingCustomers, 
            ImList<Server> availableServer,
            PQ<Event> allEvents) {
        int noAvailableServers = -1;
        int numServed = 0;
        int totalCustomers = incomingCustomers.size();
        double totalWaitTime = 0.0;
        String output = "";
        Iterator<Customer> iter = incomingCustomers.iterator();

        for (Customer customer : incomingCustomers) {
            Event customerEvent = new Arrival(customer, customer.getArrival());
            allEvents = allEvents.add(customerEvent);
        }

        while (!allEvents.isEmpty()) {
            Pair<Event, PQ<Event>> updatedPriority = allEvents.poll();
            Event currEvent = updatedPriority.first();
            allEvents = updatedPriority.second();
            output = output + currEvent.toString();
            Pair<Event, ImList<Server>> next = currEvent.next(availableServer);
            Event updatedEvent = next.first();
            ImList<Server> updatedServer = next.second();

            if (updatedEvent != currEvent) {
                allEvents = allEvents.add(updatedEvent);
            }
            availableServer = updatedServer;
        }

        for (Server s : availableServer) {
            numServed = numServed + s.getTotalServed();
            totalWaitTime = totalWaitTime + s.getWaitTime();
        }

        double averageWaitTime = (numServed == 0) ? 0.0 : totalWaitTime / numServed;

        return output + String.format("[%.3f %s %s]", 
                averageWaitTime, numServed, totalCustomers - numServed);
    }

    String simulate() {
        ImList<Server> simulationServers = initServersAvailableTime(this.numOfServers);
        ImList<Customer> simulationCustomers = initIncomingCustomers(this.arrivalTimes, 
                this.serviceTimes);
        PQ<Event> simulationEvents = initQueue();
        return processCustomers(simulationCustomers, 
            simulationServers, 
            simulationEvents);
    }
}

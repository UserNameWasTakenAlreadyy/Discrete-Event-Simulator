import java.util.function.Supplier;

class Server {
    private final double nextAvailableTime;
    private final int serverId;
    private final int qMax;
    private final int qCurr;
    private final boolean isServe;
    private final double waitTime;
    private final int totalServed;
    private final Supplier<Double> restTimes;
    private final boolean isSelfCheck;

    Server(double nextAvailableTime, 
            int serverId, int qMax, 
            int qCurr, 
            boolean isServe, 
            double waitTime, 
            int totalServed,
            Supplier<Double> restTimes,
            boolean isSelfCheck) {
        this.nextAvailableTime = nextAvailableTime;
        this.serverId = serverId;
        this.qMax = qMax;
        this.qCurr = qCurr;
        this.isServe = isServe;
        this.waitTime = waitTime;
        this.totalServed = totalServed;
        this.restTimes = restTimes;
        this.isSelfCheck = isSelfCheck;
        
    }

    int getTotalServed() {
        return this.totalServed;
    }

    double getWaitTime() {
        return this.waitTime;
    }

    int getServerId() {
        return this.serverId;
    }

    double getNextAvailableTime() {
        return nextAvailableTime;
    }

    boolean isAvailable(double customerArrivalTime) {
        return customerArrivalTime >= this.nextAvailableTime;
    }

    boolean isSC() {
        return this.isSelfCheck;
    }

    Server nextService(double time) {
        return new Server(
                time, 
                this.serverId, 
                this.qMax, 
                this.qCurr,
                this.isServe,
                this.waitTime,
                this.totalServed,
                this.restTimes,
                this.isSelfCheck);
    }

    boolean canQueue() {
        return qCurr < qMax;
    }

    Server enterQueue() {
        int updatedCurr = (this.qCurr + 1 > this.qMax) ? this.qMax : this.qCurr + 1;
        return new Server(
                this.nextAvailableTime, 
                this.serverId, 
                this.qMax, 
                updatedCurr,
                this.isServe,
                this.waitTime,
                this.totalServed,
                this.restTimes,
                this.isSelfCheck);
    }

    Server exitQueue() {
        int updatedCurr = (this.qCurr - 1 < 0) ? 0 : this.qCurr - 1;
        return new Server(
                this.nextAvailableTime, 
                this.serverId, 
                this.qMax, 
                updatedCurr,
                this.isServe,
                this.waitTime,
                this.totalServed,
                this.restTimes,
                this.isSelfCheck);
    }

    Server toggleServe() {
        return new Server(
                this.nextAvailableTime, 
                this.serverId, 
                this.qMax, 
                this.qCurr, 
                !this.isServe, 
                this.waitTime, 
                this.totalServed,
                this.restTimes,
                this.isSelfCheck);
    }

    boolean serveStatus() {
        return this.isServe;
    }

    Server resetServe() {
        return new Server(
                this.nextAvailableTime, 
                this.serverId, 
                this.qMax, 
                this.qCurr, 
                false, 
                this.waitTime, 
                this.totalServed,
                this.restTimes,
                this.isSelfCheck);
    }

    Server incrementTotal() {
        return new Server(
                this.nextAvailableTime, 
                this.serverId, 
                this.qMax, 
                this.qCurr, 
                this.isServe, 
                this.waitTime, 
                this.totalServed + 1,
                this.restTimes,
                this.isSelfCheck);
    }

    Server incrementWaitTime(double time) {
        return new Server(
                this.nextAvailableTime, 
                this.serverId, 
                this.qMax, 
                this.qCurr, 
                this.isServe, 
                this.waitTime + time, 
                this.totalServed,
                this.restTimes,
                this.isSelfCheck);
    }


    Server rest() {
        double time = this.restTimes.get(); 
        return new Server(
                this.nextAvailableTime + time, 
                this.serverId, 
                this.qMax, 
                this.qCurr, 
                this.isServe, 
                this.waitTime, 
                this.totalServed,
                this.restTimes,
                this.isSelfCheck);
    }
}

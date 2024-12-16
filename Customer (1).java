import java.util.function.Supplier;

class Customer {
    private final double arrivalTime;
    private final Supplier<Double> serviceTime;
    private final int id;

    Customer(double arrivalTime, Supplier<Double> serviceTime, int id) {
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.id = id;
    }

    double getService() {
        return this.serviceTime.get();
    
    }

    double getArrival() {
        return this.arrivalTime;
    }

    int getId() {
        return id;
    }


}

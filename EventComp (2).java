import java.util.Comparator;


class EventComp implements Comparator<Event> {
    public int compare(Event firstEvent, Event secondEvent) {
        if (firstEvent.getTime() != secondEvent.getTime()) {
            if (firstEvent.getTime() > secondEvent.getTime()) {
                return 1;
            } else {
                return -1;
            }
        } else {
            if (firstEvent.getCustomer().getId() < secondEvent.getCustomer().getId()) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}

                



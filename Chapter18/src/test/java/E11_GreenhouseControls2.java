import section_08.Event;
import section_08.GreenhouseControls;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * @Author ZhangGJ
 * @Date 2019/10/03
 */
class GreenhouseControls2 extends GreenhouseControls {
    class Restart extends Event {
        private Event[] eventList;

        public Restart(long delayTime) {
            super(delayTime);
        }

        public void action() {
            for (Event e : eventList) {
                e.start(); // Rerun each event
                addEvent(e);
            }
            start();
            addEvent(this); // Rerun this Event
        }

        public String toString() {
            return "Restarting system";
        }

        public void setEventList(Event[] eventList) {
            this.eventList = eventList;
        }
    }


    class GHEventFactory {
        LinkedList<EventCreator> events = new LinkedList<EventCreator>();


        class EventCreator {
            Constructor<Event> ctor;
            long offset;

            public EventCreator(Constructor<Event> ctor, long offset) {
                this.ctor = ctor;
                this.offset = offset;
            }
        }

        @SuppressWarnings("unchecked")
        public GHEventFactory(String eventFile) {
            try {
                BufferedReader in = new BufferedReader(new FileReader(eventFile));
                String s;
                while ((s = in.readLine()) != null) {
                    int colon = s.indexOf(':');
                    // Must use '$' instead of '.' to
                    // describe inner classes:
                    String className = s.substring(0, colon).trim();
                    Class<?> outer = className.equals("Restart") ?
                        GreenhouseControls2.class :
                        GreenhouseControls.class;
                    String type = outer.getSimpleName() + "$" + className;
                    long offset = Long.parseLong(s.substring(colon + 1).trim());
                    // Use Reflection to find and call
                    // the right constructor:
                    Class<Event> eventClass = (Class<Event>) Class.forName(type);
                    // Inner class constructors implicitly
                    // take the outer-class object as a
                    // first argument:
                    Constructor<Event> ctor =
                        eventClass.getConstructor(new Class<?>[] {outer, long.class});
                    events.add(new EventCreator(ctor, offset));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        Iterator<Event> iterator() {
            return new Iterator<Event>() {
                Iterator<EventCreator> it = events.iterator();

                public boolean hasNext() {
                    return it.hasNext();
                }

                public Event next() {
                    EventCreator ec = it.next();
                    Event returnVal = null;
                    try {
                        returnVal =
                            ec.ctor.newInstance(new Object[] {GreenhouseControls2.this, ec.offset});
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    return returnVal;
                }

                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }
    }


    GHEventFactory gheFactory;

    public GreenhouseControls2(String initFile) {
        gheFactory = new GHEventFactory(initFile);
        // Now we need some logic to setup the system.
        // The restart event requires a special attention.
        LinkedList<Event> restartableEvents = new LinkedList<Event>();
        Iterator<Event> it = gheFactory.iterator();
        while (it.hasNext()) {
            Event e = it.next();
            if (e instanceof Bell || e instanceof Restart)
                continue;
            restartableEvents.add(e);
        }
        it = gheFactory.iterator();
        while (it.hasNext()) {
            Event e = it.next();
            addEvent(e);
            if (e instanceof Restart)
                ((Restart) e).setEventList(restartableEvents.toArray(new Event[0]));
        }
    }
}


/**
 * ThermostatNight: 100000
 * LightOn: 200000
 * LightOff: 400000
 * WaterOn: 600000
 * WaterOff: 800000
 * Bell: 1000000
 * ThermostatDay: 1400000
 * Restart: 2000000
 *
 * GreenhouseConfig.dat
 *
 * args : 5000000
 */

public class E11_GreenhouseControls2 {
    public static void main(String[] args) {
        GreenhouseControls2 gc = new GreenhouseControls2("GreenhouseConfig.dat");
        try {
            if (args.length == 1)
                gc.addEvent(new GreenhouseControls.Terminate(Long.parseLong(args[0])));
        } catch (NumberFormatException e) {
            System.err.println("Terminate event is not added!");
            e.printStackTrace();
        }
        gc.run();
    }
}

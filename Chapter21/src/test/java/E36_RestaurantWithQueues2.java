import section_07.Course;
import section_07.Food;

import java.util.*;
import java.util.concurrent.*;

import static net.mindview.util.Print.print;

/**
 * @Author ZhangGJ
 * @Date 2019/10/07
 */
// This is consisted of many orders, and there's one ticket
// per table:
class OrderTicket {
    private static int counter;
    private final int id = counter++;
    private final Table table;
    private final List<Order> orders = Collections.synchronizedList(new LinkedList<Order>());

    public OrderTicket(Table table) {
        this.table = table;
    }

    public WaitPerson1 getWaitPerson() {
        return table.getWaitPerson();
    }

    public void placeOrder(Customer cust, Food food) {
        Order order = new Order(cust, food);
        orders.add(order);
        order.setOrderTicket(this);
    }

    public List<Order> getOrders() {
        return orders;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Order Ticket: " + id + " for: " + table + "\n");
        synchronized (orders) {
            for (Order order : orders)
                sb.append(order.toString() + "\n");
        }
        // Prune away the last added 'new-line' character
        return sb.substring(0, sb.length() - 1).toString();
    }
}


class Table implements Runnable {
    private static int counter;
    private final int id = counter++;
    private final WaitPerson1 waitPerson;
    private final List<Customer> customers;
    private final OrderTicket orderTicket = new OrderTicket(this);
    private final CyclicBarrier barrier;
    private final int nCustomers;
    private final ExecutorService exec;

    public Table(WaitPerson1 waitPerson, int nCustomers, ExecutorService e) {
        this.waitPerson = waitPerson;
        customers = Collections.synchronizedList(new LinkedList<Customer>());
        this.nCustomers = nCustomers;
        exec = e;
        barrier = new CyclicBarrier(nCustomers + 1, new Runnable() {
            public void run() {
                print(orderTicket.toString());
            }
        });
    }

    public WaitPerson1 getWaitPerson() {
        return waitPerson;
    }

    public void placeOrder(Customer cust, Food food) {
        orderTicket.placeOrder(cust, food);
    }

    public void run() {
        Customer customer;
        for (int i = 0; i < nCustomers; i++) {
            customers.add(customer = new Customer(this, barrier));
            exec.execute(customer);
        }
        try {
            barrier.await();
        } catch (InterruptedException ie) {
            print(this + " interrupted");
            return;
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
        waitPerson.placeOrderTicket(orderTicket);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Table: " + id + " served by: " + waitPerson + "\n");
        synchronized (customers) {
            for (Customer customer : customers)
                sb.append(customer.toString() + "\n");
        }
        return sb.substring(0, sb.length() - 1).toString();
    }
}


// This is part of an order ticket (given to the chef):
class Order {
    private static int counter;
    private final int id;
    private volatile OrderTicket orderTicket;
    private final Customer customer;
    private final Food food;

    public Order(Customer cust, Food f) {
        customer = cust;
        food = f;
        synchronized (Order.class) {
            id = counter++;
        }
    }

    void setOrderTicket(OrderTicket orderTicket) {
        this.orderTicket = orderTicket;
    }

    public OrderTicket getOrderTicket() {
        return orderTicket;
    }

    public Food item() {
        return food;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String toString() {
        return "Order: " + id + " item: " + food + " for: " + customer;
    }
}


// This is what comes back from the chef:
class Plate {
    private final Order order;
    private final Food food;

    public Plate(Order ord, Food f) {
        order = ord;
        food = f;
    }

    public Order getOrder() {
        return order;
    }

    public Food getFood() {
        return food;
    }

    public String toString() {
        return food.toString();
    }
}


class Customer implements Runnable {
    private static int counter;
    private final int id;
    private final CyclicBarrier barrier;
    private final Table table;
    private int nPlates;  // Number of plates ordered

    public Customer(Table table, CyclicBarrier barrier) {
        this.table = table;
        this.barrier = barrier;
        synchronized (Customer.class) {
            id = counter++;
        }
    }

    // Only one course at a time can be received:
    private final SynchronousQueue<Plate> placeSetting = new SynchronousQueue<Plate>();

    public void deliver(Plate p) throws InterruptedException {
        // Only blocks if customer is still
        // eating the previous course:
        placeSetting.put(p);
    }

    public void run() {
        // First place an order:
        for (Course course : Course.values()) {
            Food food = course.randomSelection();
            table.placeOrder(this, food);
            ++nPlates;
        }
        try {
            barrier.await();
        } catch (InterruptedException ie) {
            print(this + " interrupted while ordering meal");
            return;
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
        // Now wait for each ordered plate:
        for (int i = 0; i < nPlates; i++)
            try {
                // Blocks until course has been delivered:
                print(this + "eating " + placeSetting.take());
            } catch (InterruptedException e) {
                print(this + "waiting for meal interrupted");
                return;
            }
        print(this + "finished meal, leaving");
    }

    public String toString() {
        return "Customer " + id + " ";
    }
}


class WaitPerson1 implements Runnable {
    private static int counter;
    private final int id = counter++;
    private final Restaurant1 restaurant;
    final BlockingQueue<Plate> filledOrders = new LinkedBlockingQueue<Plate>();

    public WaitPerson1(Restaurant1 rest) {
        restaurant = rest;
    }

    public void placeOrderTicket(OrderTicket orderTicket) {
        try {
            // Shouldn't actually block because this is
            // a LinkedBlockingQueue with no size limit:
            restaurant.orderTickets.put(orderTicket);
        } catch (InterruptedException e) {
            print(this + " placeOrderTicket interrupted");
        }
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                // Blocks until a course is ready
                Plate plate = filledOrders.take();
                print(this + "received " + plate + " delivering to " + plate.getOrder()
                    .getCustomer());
                plate.getOrder().getCustomer().deliver(plate);
            }
        } catch (InterruptedException e) {
            print(this + " interrupted");
        }
        print(this + " off duty");
    }

    public String toString() {
        return "WaitPerson " + id + " ";
    }
}


class Chef1 implements Runnable {
    private static int counter;
    private final int id = counter++;
    private final Restaurant1 restaurant;
    private static Random rand = new Random(47);

    public Chef1(Restaurant1 rest) {
        restaurant = rest;
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                // Blocks until an order ticket appears:
                OrderTicket orderTicket = restaurant.orderTickets.take();
                List<Order> orders = orderTicket.getOrders();
                synchronized (orders) {
                    for (Order order : orders) {
                        Food requestedItem = order.item();
                        // Time to prepare order:
                        TimeUnit.MILLISECONDS.sleep(rand.nextInt(500));
                        Plate plate = new Plate(order, requestedItem);
                        order.getOrderTicket().getWaitPerson().
                            filledOrders.put(plate);
                    }
                }
            }
        } catch (InterruptedException e) {
            print(this + " interrupted");
        }
        print(this + " off duty");
    }

    public String toString() {
        return "Chef " + id + " ";
    }
}


class Restaurant1 implements Runnable {
    private List<WaitPerson1> waitPersons = new ArrayList<WaitPerson1>();
    private List<Chef1> chefs = new ArrayList<Chef1>();
    private ExecutorService exec;
    private static Random rand = new Random(47);
    final BlockingQueue<OrderTicket> orderTickets = new LinkedBlockingQueue<OrderTicket>();

    public Restaurant1(ExecutorService e, int nWaitPersons, int nChefs) {
        exec = e;
        for (int i = 0; i < nWaitPersons; i++) {
            WaitPerson1 waitPerson = new WaitPerson1(this);
            waitPersons.add(waitPerson);
            exec.execute(waitPerson);
        }
        for (int i = 0; i < nChefs; i++) {
            Chef1 chef = new Chef1(this);
            chefs.add(chef);
            exec.execute(chef);
        }
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                // A new group of customers arrive; assign a
                // WaitPerson:
                WaitPerson1 wp = waitPersons.get(rand.nextInt(waitPersons.size()));
                int nCustomers = rand.nextInt(4) + 1;
                Table t = new Table(wp, nCustomers, exec);
                exec.execute(t);
                TimeUnit.MILLISECONDS.sleep(400 * nCustomers);
            }
        } catch (InterruptedException e) {
            print("Restaurant interrupted");
        }
        print("Restaurant closing");
    }
}


public class E36_RestaurantWithQueues2 {
    public static void main(String[] args) throws Exception {
        ExecutorService exec = Executors.newCachedThreadPool();
        Restaurant1 restaurant = new Restaurant1(exec, 5, 2);
        exec.execute(restaurant);
        if (args.length > 0) // Optional argument
            TimeUnit.SECONDS.sleep(new Integer(args[0]));
        else {
            print("Press 'ENTER' to quit");
            System.in.read();
        }
        exec.shutdownNow();
    }
}

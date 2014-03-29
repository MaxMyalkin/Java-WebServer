package messageSystem;

import java.util.concurrent.atomic.AtomicInteger;

/*
 * Created by maxim on 29.03.14.
 */
public class Address {
    static private AtomicInteger abonentIdCreator = new AtomicInteger();
    final private int abonentId;

    public Address(){
        this.abonentId = abonentIdCreator.incrementAndGet();
    }

    public int hashCode(){
        return abonentId;
    }
}

package messageSystem;

/*
 * Created by maxim on 29.03.14.
 */

import java.util.*;

public class AddressService {
    private Map<Class, List<Address>> services = new HashMap<>();

    public Address getService(Class clazz) {
        List<Address> service = services.get(clazz);
        return service.get(new Random().nextInt(service.size()));
    }


    public void addService(Address address, Class clazz) {
        List<Address> list = services.get(clazz);
        if(list == null) {
            services.put(clazz, new ArrayList<Address>());
            services.get(clazz).add(address);
        }
        else
            list.add(address);
    }

}
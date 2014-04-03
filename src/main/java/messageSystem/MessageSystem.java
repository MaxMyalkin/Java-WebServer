package messageSystem;

import message.Msg;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/*
 * Created by maxim on 29.03.14.
 */
public class MessageSystem {
    private Map<Address , ConcurrentLinkedQueue<Msg>> messages = new HashMap<>();
    private AddressService addressService;

    public MessageSystem(AddressService addressService){
        this.addressService = addressService;
    }

    public Queue<Msg> getQueue(Abonent abonent){
        return messages.get(abonent.getAddress());
    }

    public void addAbonent(Abonent abonent){
        this.messages.put(abonent.getAddress(), new ConcurrentLinkedQueue<Msg>());
    }

    public AddressService getAddressService(){
        return addressService;
    }

    public void sendMessage(Msg message) {
        Queue<Msg> messageQueue = messages.get(message.getTo());
        messageQueue.add(message);
    }

    public void execForAbonent(Abonent abonent){
        Queue<Msg> messageQueue = getQueue(abonent);
        if (messageQueue != null){
            while(!messageQueue.isEmpty()){
                messageQueue.poll().exec(abonent);
            }
        }
    }

}

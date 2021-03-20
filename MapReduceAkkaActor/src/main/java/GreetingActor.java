import akka.actor.UntypedAbstractActor;
public class GreetingActor extends UntypedAbstractActor {
    @Override
    public void onReceive(Object message) throws Throwable, Throwable {
        if (message instanceof Greeting)
        {
            System.out.println("Greeting "+((Greeting) message).who);
        }
        else if (message instanceof String){
            System.out.println("String  "+message);
        }
    }
}

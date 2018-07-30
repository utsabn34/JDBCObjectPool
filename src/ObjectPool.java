import java.util.Hashtable;

public abstract class ObjectPool {
    private long expirationTime;
    private Hashtable locked, unlocked;
    abstract Object create();
    abstract boolean validate( Object o );
    abstract void expire( Object o );
    synchronized Object checkOut(){
        Object object = new Object();
        return object;
    }
    synchronized void checkIn( Object o ){

    }

    ObjectPool()
    {
        expirationTime = 30000; // 30 seconds
        locked = new Hashtable();
        unlocked = new Hashtable();
    }
}

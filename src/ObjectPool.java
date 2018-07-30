import java.util.Enumeration;
import java.util.Hashtable;

public abstract class ObjectPool {
    private long expirationTime;
    private Hashtable locked, unlocked;
    abstract Object create();
    abstract boolean validate( Object o );
    abstract void expire( Object o );

    /**
     * this method will first checks to see if there are any objects in the unlocked hashtable
     * If so, it cycles through them and looks for a valid one.
     * @return {@link Object}
     */
    synchronized Object checkOut(){
        long now = System.currentTimeMillis();
        Object o;
        if( unlocked.size() > 0 )
        {
            Enumeration e = unlocked.keys();
            while( e.hasMoreElements() )
            {
                o = e.nextElement();
                if( ( now - ( ( Long ) unlocked.get( o ) ).longValue() ) >
                        expirationTime )
                {
                    // if object has expired
                    unlocked.remove( o );
                    expire( o );
                    o = null;
                }
                else
                {If so, it cycles through them and looks for a valid one. 
                    if( validate( o ) )
                    {
                        unlocked.remove( o );
                        locked.put( o, new Long( now ) );
                        return( o );
                    }
                    else
                    {
                        // object failed validation
                        unlocked.remove( o );
                        expire( o );
                        o = null;
                    }
                }
            }
        }

        // if no objects available, create a new one
        o = create();
        locked.put( o, new Long( now ) );
        return( o );
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

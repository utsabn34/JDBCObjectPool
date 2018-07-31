import java.util.Enumeration;
import java.util.Hashtable;

public abstract class ObjectPool {
    private long expirationTime;
    private Hashtable locked, unlocked;
    abstract Object create() throws Exception;
    abstract boolean validate( Object o );
    abstract void expire( Object o );
    private CleanUpThread cleaner;

    ObjectPool() {
        expirationTime = 30000; // 30 seconds
        locked = new Hashtable();
        unlocked = new Hashtable();
        cleaner = new CleanUpThread( this, expirationTime );
        cleaner.start();
    }

    /**
     * this method will first checks to see if there are any objects in the unlocked hashtable
     * If so, it cycles through them and looks for a valid one.
     * @return {@link Object}
     */
    synchronized Object checkOut() throws Exception{
        long now = System.currentTimeMillis();
        Object o;
        if( unlocked.size() > 0 ) {
            Enumeration e = unlocked.keys();
            while( e.hasMoreElements() ) {
                o = e.nextElement();

                if( validate( o ) ) {
                    unlocked.remove( o );
                    locked.put( o, new Long( now ) );
                    return( o );
                }
                else {
                    // object failed validation
                    unlocked.remove( o );
                    expire( o );
                    o = null;
                }

            }
        }

        // if no objects available, create a new one
        o = create();
        locked.put( o, new Long( now ) );
        return( o );
    }

    /**
     * Move passedin object from locked hashtable to unlocked hashtable
     * @param {@link Object}
     */
    synchronized void checkIn( Object o ){
        locked.remove( o );
        unlocked.put( o, new Long( System.currentTimeMillis() ) );
    }

    synchronized void cleanUp() {
        Object o;
        long now = System.currentTimeMillis();
        Enumeration e = unlocked.keys();
        while( e.hasMoreElements() ) {
            o = e.nextElement();
            if( ( now - ( ( Long ) unlocked.get( o ) ).longValue() ) > expirationTime ) {
                unlocked.remove( o );
                expire( o );
                o = null;
            }
        }
        System.gc();
    }



}

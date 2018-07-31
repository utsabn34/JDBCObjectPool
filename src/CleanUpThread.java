class CleanUpThread extends Thread {

    private ObjectPool pool;
    private long sleepTime;

    CleanUpThread( ObjectPool pool, long sleepTime ) {
        this.pool = pool;
        this.sleepTime = sleepTime;
    }

    public void run() {
        while( true ) {
            try {
                sleep( sleepTime );
            }
            catch( InterruptedException e ) {
                // ignore it
            }
            pool.cleanUp();
        }
    }
}
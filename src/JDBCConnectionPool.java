

public class JDBCConnectionPool extends ObjectPool{




    @Override
    Object create() {
        return null;
    }

    @Override
    boolean validate(Object o) {
        return  true;
    }

    @Override
    void expire(Object o) {

    }
}

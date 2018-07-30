import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConnectionPool extends ObjectPool{

    private String dsn, usr, pwd;

    public JDBCConnectionPool( String driver, String dsn, String usr, String pwd )
    {
        try
        {
            Class.forName( driver ).newInstance();
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
        this.dsn = dsn;
        this.usr = usr;
        this.pwd = pwd;
    }


    @Override
    Object create() {
        try
        {
            return( DriverManager.getConnection( dsn, usr, pwd ) );
        }
        catch( SQLException e )
        {
            e.printStackTrace();
            return( null );
        }
    }

    @Override
    boolean validate(Object o) {
        try
        {
            return( ! ( ( Connection ) o ).isClosed() );
        }
        catch( SQLException e )
        {
            e.printStackTrace();
            return( false );
        }
    }

    @Override
    void expire(Object o) {
        try
        {
            ( (Connection) o ).close();
        }
        catch( SQLException e )
        {
            e.printStackTrace();
        }
    }

    /**
     * Allow to borrow JDBC Connection
     * @return {@link Connection}
     */
    public Connection borrowConnection()
    {
        return( ( Connection ) super.checkOut() );
    }

    /**
     * Let the user to return the borrowed connection
     * @param {@link {@link Connection}}
     */
    public void returnConnection( Connection c )
    {
        super.checkIn( c );
    }
}

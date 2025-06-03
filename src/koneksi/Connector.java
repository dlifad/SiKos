package koneksi;

import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class Connector {
    private static Connection con;

    public static Connection connection() {
        try {
            if (con == null || con.isClosed()) {
                MysqlDataSource data = new MysqlDataSource();
                data.setDatabaseName("kos");
                data.setUser("root");
                data.setPassword("");
                data.setServerName("localhost");
                data.setPortNumber(3306);
                con = data.getConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            con = null;
        }
        return con;
    }
}

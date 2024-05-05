package net.cowtopia.dscjava.libs;

import java.sql.*;
import java.time.Instant;

public class SqliteMan
{
    public static void connect(String fileName) {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:" + fileName;
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    private Connection connection(String fileName) {
        // SQLite connection string
        String url = "jdbc:sqlite:" + fileName;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static void createNewDatabase(String fileName) {
        String url = "jdbc:sqlite:" + fileName;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createWarningsTable(String fileName) {
        // SQLite connection string
        String url = "jdbc:sqlite:" + fileName;

        // SQL statement for creating a new table

        String sql = "CREATE TABLE IF NOT EXISTS warnings ( id INTEGER PRIMARY KEY, user_id INTEGER NOT NULL, reason TEXT, author_id INTEGER NOT NULL, time INTEGER NOT NULL)";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public int countAllWarnings(String fileName, long userid) {
        String url = "jdbc:sqlite:" + fileName;
        String sql = "SELECT COUNT(*) AS total FROM warnings WHERE user_id='" + userid + "'";

        try (Connection conn = this.connection(fileName);
             Statement stmt = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql))
        {
            return rs.getInt("total");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public int warningWithBiggestIndex(String fileName) {
        String url = "jdbc:sqlite:" + fileName;
        String sql = "SELECT MAX(id) AS maks FROM warnings";

        try (Connection conn = this.connection(fileName);
             Statement stmt = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql))
        {
            return rs.getInt("maks");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public ISLDPair[] allReasons(String fileName, long userid) {
        String url = "jdbc:sqlite:" + fileName;
        String sql = "SELECT id, reason, author_id, time FROM warnings WHERE user_id='" + userid + "'";
        ISLDPair[] reasons = new ISLDPair[countAllWarnings(fileName, userid)];

        try (Connection conn = this.connection(fileName);
             Statement stmt = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql))
        {
            int i = 0;
            while (rs.next()) {
                reasons[i] = new ISLDPair(rs.getInt("id"),rs.getString("reason"),rs.getLong("author_id"),rs.getLong("time"));
                i++;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return reasons;
    }

    public void insertNewReason(String fileName, long userid, String reason, long authorid) {
        int ajdi = warningWithBiggestIndex(fileName) + 1;
        long time = Instant.now().getEpochSecond();

        String sql = "INSERT INTO warnings VALUES(" + ajdi + "," +
                userid + ",'" + reason + "'," + authorid + "," + time + ")";

        try (Connection conn = this.connection(fileName);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // merge ova dva kasnije (mrzi me sad)
    public void deleteReason(String fileName, int indeks) {
        String sql = "DELETE FROM warnings WHERE id='" + indeks + "'";

        try (Connection conn = this.connection(fileName);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteMultipleReasons(String fileName, String userOrMod, long inputid) {
        String sql = "DELETE FROM warnings WHERE " + userOrMod + "='" + inputid + "'";

        try (Connection conn = this.connection(fileName);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

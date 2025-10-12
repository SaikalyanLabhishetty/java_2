import java.sql.*;

public class AuditLog {
    private Connection connection;

    public AuditLog() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:audit.db");
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS audit_log (id INTEGER PRIMARY KEY AUTOINCREMENT, message TEXT, ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
        }
    }

    public void log(String message) throws SQLException {
        try (PreparedStatement pstmt = connection.prepareStatement("INSERT INTO audit_log(message) VALUES (?)")) {
            pstmt.setString(1, message);
            pstmt.executeUpdate();
        }
    }
}

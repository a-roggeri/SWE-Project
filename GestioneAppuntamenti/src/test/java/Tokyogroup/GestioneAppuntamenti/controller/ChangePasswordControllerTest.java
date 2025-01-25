package Tokyogroup.GestioneAppuntamenti.controller;
import Tokyogroup.GestioneAppuntamenti.model.DatabaseManager;
import Tokyogroup.GestioneAppuntamenti.model.User;
import Tokyogroup.GestioneAppuntamenti.model.UserDAO;
import org.junit.jupiter.api.*;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;





class ChangePasswordControllerTest {

    private ChangePasswordController changePasswordController;
    private User currentUser;

    @BeforeAll
    static void backupDatabase() throws Exception {
        DatabaseManager.backupDatabase();
    }

    @BeforeEach
    void setUp() throws Exception {
        DatabaseManager.deleteDatabaseFiles();
        DatabaseManager.initializeDatabase();

        currentUser = new User(1, "user", "oldPassword", "USER", true);
        changePasswordController = new ChangePasswordController(currentUser);
    }

    @AfterEach
    void tearDown() throws Exception {
        DatabaseManager.restoreDatabase();
    }

    @Test
    void testChangePasswordSuccess() throws SQLException {
        UserDAO userDAO = UserDAO.getInstance();
        userDAO.addUser(currentUser);

        boolean success = changePasswordController.changePassword("oldPassword", "newPassword");
        assertTrue(success);

        User updatedUser = userDAO.getUserById(currentUser.getId());
        assertEquals("newPassword", updatedUser.getPassword());
    }

    @Test
    void testChangePasswordFailure() throws SQLException {
        UserDAO userDAO = UserDAO.getInstance();
        userDAO.addUser(currentUser);

        boolean success = changePasswordController.changePassword("wrongOldPassword", "newPassword");
        assertFalse(success);

        User updatedUser = userDAO.getUserById(currentUser.getId());
        assertEquals("oldPassword", updatedUser.getPassword());
    }

    @Test
    void testChangePasswordSQLException() throws SQLException {
        UserDAO userDAO = UserDAO.getInstance();
        userDAO.addUser(currentUser);

        // Simulate SQLException by closing the database connection
        DatabaseManager.closeConnection();

        assertThrows(RuntimeException.class, () -> {
            changePasswordController.changePassword("oldPassword", "newPassword");
        });
    }
}
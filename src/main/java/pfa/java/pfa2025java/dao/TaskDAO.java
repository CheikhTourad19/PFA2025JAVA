package pfa.java.pfa2025java.dao;

import pfa.java.pfa2025java.model.Task;
import pfa.java.pfa2025java.model.TaskStatus;
import pfa.java.pfa2025java.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {
    private static final Connection connection;
    static {
        try {
            connection = DBconnection.connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean insertTask(Task task) throws SQLException {
        String sql = "INSERT INTO tasks (title, description, assigned_to, created_by, status, deadline) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDescription());
            stmt.setInt(3, task.getAssignedTo());
            stmt.setInt(4, task.getCreatedBy());
            stmt.setString(5, task.getStatus().toDbValue());
            stmt.setDate(6, Date.valueOf(task.getDeadline()));

            return stmt.executeUpdate() > 0;
        }
    }

    public List<Task> getAllTasks() throws SQLException {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Task task = new Task(
                        rs.getInt("task_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getInt("assigned_to"),
                        rs.getInt("created_by"),
                        TaskStatus.fromString(rs.getString("status")),
                        rs.getDate("created_at").toLocalDate(),
                        rs.getDate("due_date").toLocalDate()
                );
                tasks.add(task);
            }
        }
        return tasks;
    }
    public List<User> searchMedicalStaff(String searchTerm) throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, nom, prenom, role FROM user " +
                "WHERE (LOWER(nom) LIKE ? OR LOWER(prenom) LIKE ?) " +
                "AND role IN ('medecin', 'infermier') " +
                "LIMIT 10";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String pattern = "%" + searchTerm.toLowerCase() + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setNom(rs.getString("nom"));
                user.setPrenom(rs.getString("prenom"));
                user.setRole(rs.getString("role"));
                users.add(user);
            }
        }
        return users;
    }

    public List<Task> getTasksWithCreatorInfo(int userId) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT "
                + "t.task_id, " // Alias explicite
                + "t.title, "
                + "t.description, "
                + "t.deadline, "
                + "u.prenom || ' ' || u.nom AS creator_name "
                + "FROM tasks t "
                + "JOIN user u ON t.created_by = u.id "
                + "WHERE t.assigned_to = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Task task = new Task();
                task.setTaskId(rs.getInt("task_id"));
                task.setTitle(rs.getString("title"));
                task.setDescription(rs.getString("description"));
                task.setDeadline(rs.getDate("deadline").toLocalDate());
                task.setCreatorName(rs.getString("creator_name"));
                tasks.add(task);
            }
        } catch (SQLException e) {
            System.err.println("Erreur de chargement des tâches : " + e.getMessage());
        }
        return tasks;
    }
    public boolean updateTaskStatus(int taskId, TaskStatus status) throws SQLException {
        String sql = "UPDATE tasks SET status = ? WHERE task_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status.toDbValue());
            stmt.setInt(2, taskId);
            return stmt.executeUpdate() > 0;
        }
    }

    public List<Task> getTasksCreatedByUser(int creatorId) throws SQLException {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT t.task_id, t.title, t.deadline, t.status, "
                + "u.nom || ' ' || u.prenom AS assignee_name "  // Concaténation nom/prénom
                + "FROM tasks t "
                + "LEFT JOIN user u ON t.assigned_to = u.id "   // Correction de la colonne de jointure
                + "WHERE t.created_by = ?";                     // Supposition du nom de colonne

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, creatorId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Task task = new Task();
                task.setTaskId(rs.getInt("task_id"));
                task.setTitle(rs.getString("title"));
                task.setDeadline(rs.getDate("deadline").toLocalDate());
                task.setStatus(TaskStatus.fromString(rs.getString("status")));
                task.setAssigneeName(rs.getString("assignee_name")); // Récupération du nom complet
                tasks.add(task);
            }
        }
        return tasks;
    }
    public List<Task> getTasksByUser(int userId) throws SQLException {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT task_id, title, description, assigned_to, created_by, status, deadline "
                + "FROM tasks WHERE assigned_to = ? AND status != 'COMPLETED'";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Task task = new Task();

                // Récupération des données de base
                task.setTaskId(rs.getInt("task_id"));
                task.setTitle(rs.getString("title"));
                task.setDescription(rs.getString("description"));
                task.setAssignedTo(rs.getInt("assigned_to"));
                task.setCreatedBy(rs.getInt("created_by"));

                // Conversion du statut
                String statusValue = rs.getString("status");
                task.setStatus(TaskStatus.fromString(statusValue));

                // Gestion des dates NULL
                Date deadlineDate = rs.getDate("deadline");
                if (!rs.wasNull()) {
                    task.setDeadline(deadlineDate.toLocalDate());
                }

                tasks.add(task);
            }
        }
        return tasks;
    }
}

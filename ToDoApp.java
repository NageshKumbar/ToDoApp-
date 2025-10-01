import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

class Task {
    private String description;
    private boolean completed;
    
    public Task(String description) {
        this.description = description;
        this.completed = false;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public boolean isCompleted() {
        return completed;
    }
    
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    
    @Override
    public String toString() {
        return completed ? "✓ " + description : "○ " + description;
    }
}

class TaskPanel extends JPanel {
    private Task task;
    private JCheckBox checkBox;
    private JLabel taskLabel;
    private JButton deleteButton;
    private JButton editButton;
    private ToDoApp parent;
    
    public TaskPanel(Task task, ToDoApp parent) {
        this.task = task;
        this.parent = parent;
        
        setLayout(new BorderLayout(10, 0));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        checkBox = new JCheckBox();
        checkBox.setBackground(Color.WHITE);
        checkBox.setSelected(task.isCompleted());
        checkBox.addActionListener(e -> toggleTask());
        
        taskLabel = new JLabel(task.getDescription());
        taskLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        updateTaskAppearance();
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        editButton = new JButton("Edit");
        editButton.setFont(new Font("SansSerif", Font.PLAIN, 11));
        editButton.setForeground(new Color(52, 152, 219));
        editButton.setBackground(Color.WHITE);
        editButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        editButton.setFocusPainted(false);
        editButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editButton.addActionListener(e -> editTask());
        
        deleteButton = new JButton("Delete");
        deleteButton.setFont(new Font("SansSerif", Font.PLAIN, 11));
        deleteButton.setForeground(new Color(231, 76, 60));
        deleteButton.setBackground(Color.WHITE);
        deleteButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        deleteButton.setFocusPainted(false);
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteButton.addActionListener(e -> deleteTask());
        
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        
        add(checkBox, BorderLayout.WEST);
        add(taskLabel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.EAST);
    }
    
    private void toggleTask() {
        task.setCompleted(checkBox.isSelected());
        updateTaskAppearance();
        parent.updateStats();
    }
    
    private void updateTaskAppearance() {
        if (task.isCompleted()) {
            taskLabel.setForeground(Color.GRAY);
            taskLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
        } else {
            taskLabel.setForeground(Color.BLACK);
            taskLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        }
    }
    
    private void editTask() {
        String newDescription = JOptionPane.showInputDialog(
            this,
            "Edit task:",
            task.getDescription()
        );
        
        if (newDescription != null && !newDescription.trim().isEmpty()) {
            task.setDescription(newDescription.trim());
            taskLabel.setText(newDescription.trim());
        }
    }
    
    private void deleteTask() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this task?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            parent.removeTask(this);
        }
    }
    
    public Task getTask() {
        return task;
    }
}

public class ToDoApp extends JFrame {
    private ArrayList<TaskPanel> taskPanels;
    private JPanel taskListPanel;
    private JTextField taskInputField;
    private JButton addButton;
    private JLabel statsLabel;
    private JScrollPane scrollPane;
    
    public ToDoApp() {
        taskPanels = new ArrayList<>();
        
        setTitle("ToDo List Application");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(Color.WHITE);
        
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        taskListPanel = new JPanel();
        taskListPanel.setLayout(new BoxLayout(taskListPanel, BoxLayout.Y_AXIS));
        taskListPanel.setBackground(Color.WHITE);
        
        scrollPane = new JScrollPane(taskListPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel inputPanel = createInputPanel();
        mainPanel.add(inputPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        addSampleTasks();
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 152, 219));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("My ToDo List");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        
        statsLabel = new JLabel("0 tasks");
        statsLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        statsLabel.setForeground(Color.WHITE);
        
        JPanel statsPanel = new JPanel(new BorderLayout());
        statsPanel.setBackground(new Color(52, 152, 219));
        statsPanel.add(statsLabel, BorderLayout.NORTH);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(statsPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        taskInputField = new JTextField();
        taskInputField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        taskInputField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        taskInputField.setForeground(Color.GRAY);
        taskInputField.setText("Enter a new task...");
        
        taskInputField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (taskInputField.getText().equals("Enter a new task...")) {
                    taskInputField.setText("");
                    taskInputField.setForeground(Color.BLACK);
                }
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (taskInputField.getText().isEmpty()) {
                    taskInputField.setForeground(Color.GRAY);
                    taskInputField.setText("Enter a new task...");
                }
            }
        });
        
        taskInputField.addActionListener(e -> addTask());
        
        addButton = new JButton("Add Task");
        addButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        addButton.setForeground(Color.WHITE);
        addButton.setBackground(new Color(46, 204, 113));
        addButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        addButton.setFocusPainted(false);
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.addActionListener(e -> addTask());
        
        addButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                addButton.setBackground(new Color(39, 174, 96));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                addButton.setBackground(new Color(46, 204, 113));
            }
        });
        
        inputPanel.add(taskInputField, BorderLayout.CENTER);
        inputPanel.add(addButton, BorderLayout.EAST);
        
        return inputPanel;
    }
    
    private void addTask() {
        String taskText = taskInputField.getText().trim();
        
        if (taskText.isEmpty() || taskText.equals("Enter a new task...")) {
            JOptionPane.showMessageDialog(
                this,
                "Please enter a task description!",
                "Empty Task",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        Task task = new Task(taskText);
        TaskPanel taskPanel = new TaskPanel(task, this);
        taskPanels.add(taskPanel);
        taskListPanel.add(taskPanel);
        
        taskInputField.setText("");
        taskInputField.setForeground(Color.GRAY);
        taskInputField.setText("Enter a new task...");
        
        updateStats();
        taskListPanel.revalidate();
        taskListPanel.repaint();
        
        SwingUtilities.invokeLater(() -> {
            JScrollBar vertical = scrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
    }
    
    public void removeTask(TaskPanel taskPanel) {
        taskPanels.remove(taskPanel);
        taskListPanel.remove(taskPanel);
        updateStats();
        taskListPanel.revalidate();
        taskListPanel.repaint();
    }
    
    public void updateStats() {
        int total = taskPanels.size();
        int completed = 0;
        
        for (TaskPanel taskPanel : taskPanels) {
            if (taskPanel.getTask().isCompleted()) {
                completed++;
            }
        }
        
        statsLabel.setText(String.format(
            "%d task%s • %d completed",
            total,
            total == 1 ? "" : "s",
            completed
        ));
    }
    
    private void addSampleTasks() {
        String[] sampleTasks = {
            "Complete Java assignment",
            "Read Chapter 5 of textbook",
            "Practice coding problems"
        };
        
        for (String taskText : sampleTasks) {
            Task task = new Task(taskText);
            TaskPanel taskPanel = new TaskPanel(task, this);
            taskPanels.add(taskPanel);
            taskListPanel.add(taskPanel);
        }
        
        updateStats();
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            ToDoApp app = new ToDoApp();
            app.setVisible(true);
        });
    }
}
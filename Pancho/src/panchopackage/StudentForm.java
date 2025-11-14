/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package panchopackage;

/**
 *
 * @author Admin
 */
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class StudentForm extends JFrame {

    JTextField txtId, txtName, txtAge, txtGrade;
    JTextField txtSearch, txtDelete;
    JButton btnAdd, btnClear, btnSearch, btnDelete, btnSearchClear, btnDeleteClear;
    JTable table;

    ArrayList<Student> students = new ArrayList<>();
    DefaultTableModel model;

    File file = new File("students.txt");

   
    public StudentForm() {
        setTitle("Student Record System");
        setSize(600, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

      
        JLabel lblId = new JLabel("Student Id:");
        lblId.setBounds(50, 40, 100, 25);
        add(lblId);

        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(50, 80, 100, 25);
        add(lblName);

        JLabel lblAge = new JLabel("Age:");
        lblAge.setBounds(50, 120, 100, 25);
        add(lblAge);

        JLabel lblGrade = new JLabel("Grade:");
        lblGrade.setBounds(50, 160, 100, 25);
        add(lblGrade);

        
        txtId = new JTextField();
        txtId.setBounds(150, 40, 200, 25);
        add(txtId);

        txtName = new JTextField();
        txtName.setBounds(150, 80, 200, 25);
        add(txtName);

        txtAge = new JTextField();
        txtAge.setBounds(150, 120, 200, 25);
        add(txtAge);

        txtGrade = new JTextField();
        txtGrade.setBounds(150, 160, 200, 25);
        add(txtGrade);

       
        txtId.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar())) e.consume();
            }
        });

        txtAge.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar())) e.consume();
            }
        });

        
        btnAdd = new JButton("Add");
        btnAdd.setBounds(150, 200, 85, 30);
        add(btnAdd);

        btnClear = new JButton("Clear");
        btnClear.setBounds(260, 200, 85, 30);
        add(btnClear);

        JLabel lblSearch = new JLabel("Search:");
        lblSearch.setBounds(50, 250, 100, 25);
        add(lblSearch);

        txtSearch = new JTextField();
        txtSearch.setBounds(150, 250, 150, 25);
        add(txtSearch);

        btnSearch = new JButton("Search");
        btnSearch.setBounds(310, 250, 85, 25);
        add(btnSearch);

        btnSearchClear = new JButton("X");
        btnSearchClear.setBounds(400, 250, 50, 25);
        add(btnSearchClear);

        JLabel lblDelete = new JLabel("Delete:");
        lblDelete.setBounds(50, 290, 100, 25);
        add(lblDelete);

        txtDelete = new JTextField();
        txtDelete.setBounds(150, 290, 150, 25);
        add(txtDelete);

        btnDelete = new JButton("Delete");
        btnDelete.setBounds(310, 290, 85, 25);
        add(btnDelete);

        btnDeleteClear = new JButton("X");
        btnDeleteClear.setBounds(400, 290, 50, 25);
        add(btnDeleteClear);

        
        model = new DefaultTableModel();
        model.addColumn("Student Id");
        model.addColumn("Name");
        model.addColumn("Age");
        model.addColumn("Grade");

        table = new JTable(model);
        JScrollPane pane = new JScrollPane(table);
        pane.setBounds(50, 330, 500, 140);
        add(pane);

        
        btnAdd.addActionListener(e -> addStudent());
        btnClear.addActionListener(e -> clearInput());
        btnSearch.addActionListener(e -> searchStudent());
        btnSearchClear.addActionListener(e -> txtSearch.setText(""));
        btnDelete.addActionListener(e -> deleteStudent());
        btnDeleteClear.addActionListener(e -> txtDelete.setText(""));

       
        loadFromFile();
    }

    
    void addStudent() {
        String id = txtId.getText();
        String name = txtName.getText();
        String age = txtAge.getText();
        String grade = txtGrade.getText();

        if (id.isEmpty() || name.isEmpty() || age.isEmpty() || grade.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

       
        for (Student s : students) {
            if (s.id.equals(id)) {
                JOptionPane.showMessageDialog(this, "Student ID already exists!");
                return;
            }
        }

        Student s = new Student(id, name, age, grade);
        students.add(s);

        model.addRow(new Object[]{s.id, s.name, s.age, s.grade});

        saveToFile();

        JOptionPane.showMessageDialog(this, "Student added!");
        clearInput();
    }

    
    void searchStudent() {
        String search = txtSearch.getText();

        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).toString().equals(search)) {
                table.setRowSelectionInterval(i, i);
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Student not found!");
    }

    
    void deleteStudent() {
        String del = txtDelete.getText();

        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).id.equals(del)) {
                students.remove(i);
                model.removeRow(i);
                saveToFile();
                JOptionPane.showMessageDialog(this, "Student deleted!");
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Student not found!");
    }

   
    void clearInput() {
        txtId.setText("");
        txtName.setText("");
        txtAge.setText("");
        txtGrade.setText("");
    }

   
    void loadFromFile() {
        try {
            if (!file.exists()) {
                file.createNewFile();
                return;
            }

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length == 4) {
                    Student s = new Student(data[0], data[1], data[2], data[3]);
                    students.add(s);
                    model.addRow(new Object[]{s.id, s.name, s.age, s.grade});
                }
            }
            br.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading file!");
        }
    }

    
    void saveToFile() {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(file));

            for (Student s : students) {
                pw.println(s.id + "," + s.name + "," + s.age + "," + s.grade);
            }

            pw.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving file!");
        }
    }

   
    public static void main(String[] args) {
        new StudentForm().setVisible(true);
    }

    
    class Student {
        String id, name, age, grade;

        Student(String id, String name, String age, String grade) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.grade = grade;
        }
    }
}


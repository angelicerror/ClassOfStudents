package controller;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.Iterator;

import model.Student;
import model.StudentCollection;
import view.AddView;
import view.MainView;
import view.RemoveView;
import view.ShowView;

public class Controller {
    private MainView mainView;
    private  AddView addView;
    private  ShowView showView;
    private RemoveView removeView;
    private final StudentCollection model;
    public Controller() {
        this.model = new StudentCollection();
    }

    public void attachMainView(MainView view) {
        this.mainView = view;
    }

    public void notifyPressedAdd() {
        this.addView = new AddView(this);
        addView.display();
    }

    public void notifyAddStudent(final int id, final String name, final String surname, final int immYear, final String grades) {
        List<String> list = new ArrayList<>();

        if(model.verifyStudent(id, name, surname)) {
            if(grades.isEmpty()) {
                model.addStudent(id, name, surname, immYear, list);
            } else {
                String[] words = grades.split(" ");
                for (final String word : words) {
                    list.addFirst(word);
                }
            }
                model.addStudent(id, name, surname, immYear, list);
                addView.showConfirmMessage("Student added correctly");
                mainView.unlockButtons();
            
        } else {
            if (!name.isEmpty() && !surname.isEmpty()) {
                addView.showDuplicateMessage("This student is already present. Do you want to add new Grades?");
            } else {
                addView.showError("Cannot add new student because of invalid entrans");
            } 
        }
    }

    public void notifyAddGrades(final int id, final String grades) {
        List<String> list = new ArrayList<>();
        String[] words = grades.split(" ");
        for (final String word : words) {
            list.addFirst(word);
        }
        model.addGradeForStudent(id, list);
        addView.showConfirmMessage("grades added correctly");
    }

    public void notifyPressedRemove() {
        this.removeView = new RemoveView(this);
        this.removeView.display();
    }

    public void notifyDeleteStudent(int id) {
        if(model.isPresent(id)) {
            model.removeStudent(id);
            this.removeView.showConfirmMessage();
            if (model.isEmpty()) {
                this.mainView.lockButtons();
            }
        } else {
            this.removeView.showError("Cannot remove any student with ID: " + id);
        }
    }

    public void notifyPressedStudents() {
        this.showView = new ShowView(this);
        this.showView.display();
    }

    public void notifyShowStudents() {
        final Set<Map.Entry<Integer, Student>> students = model.getStudents();
        Iterator<Map.Entry<Integer, Student>> iter = students.iterator();
        Map.Entry<Integer, Student> entry;

        this.showView.clear();

        while (iter.hasNext()) {
            entry = iter.next();
            this.showView.printStudent(entry.getKey(), entry.getValue().getName(), 
            entry.getValue().getSurname(), entry.getValue().getImmatriculationYear(), entry.getValue().getActualGrades());
        }
    }
}
package bg.sofia.uni.fmi.mjt.taskmanager.server;

import java.util.List;

public class Task {

    class Student {

        private final String name;
        private final List<Integer> grades;

        public Student(String name, List<Integer> grades) {
            this.name = name;
            this.grades = grades;
        }

        public String getName() {
            return name;
        }

        public List<Integer> getGrades() {
            return grades;
        }

        public void addGrade(int grade) {
            synchronized (grades) {
                grades.add(grade);
            }
        }

    }

    class StudentRepo {

        private final List<Student> students;

        StudentRepo(List<Student> students) {
            this.students = students;
        }

        public void addStudent(Student student) {
            synchronized (students) {
                students.add(student);
            }
        }

        public void removeStudent(String name) {

        }

        public synchronized void addStudentGrade(String name, int grade) {
            synchronized (students) {
                notifyAll();
                for (Student student : students) {
                    if (student.getName().equals(name)) {
                        student.addGrade(grade);
                        students.notify();
                    }
                }
            }
        }

    }

}
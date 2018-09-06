import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        // Init streams

        // Init data
        ArrayList<Student> students = new ArrayList<Student>();

        for(int i = 0; i < 2000; i++) {
            students.add(new Male());
        }
        for(int i = 0; i < 2000; i++) {
            students.add(new Female());
        }

        try (BufferedWriter dataw = new BufferedWriter(new FileWriter("data.txt"))){
            for (int i = 0; i < 4000; i++) {
                dataw.write(students.get(i).getHeight() + "," + students.get(i).getWeight() + ","
                        + (students.get(i).getClass() == Male.class ? 0 : 1) + "\n");
            }
        } catch(Exception e) {
            e.printStackTrace();
            return;
        }

        int maleCorrect = 0;
        int femaleCorrect = 0;
        // guess
        for(int i = 0; i < 4000; i = i + 2) {
            if(guess(students.get(i))) maleCorrect++;
            if(guess(students.get(i+1))) femaleCorrect++;
        }

        System.out.println("males correct: " + maleCorrect);
        System.out.println("females correct: " + femaleCorrect);
    }

    public static boolean guess(Student student) {
        double x = 1; // height coeff
        double y = .7; // weight coeff
        double c = -225;

        System.out.print("height: " + student.getHeight() + " weight: " + student.getWeight()
                + " gender: " + student.getClass().toString() + " guess: ");

        if(x * student.getHeight() + y * student.getWeight() + c > 0 ) {
            System.out.println("male");
            if(student.getClass() == Male.class) return true;
            else return false;
        } else {
            System.out.println("female");
            if(student.getClass() == Female.class) return true;
            else return false;
        }
    }

}

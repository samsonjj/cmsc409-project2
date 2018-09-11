import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // Init streams

        // Init data
        ArrayList<Student> students = new ArrayList<Student>();

        if(args.length > 0 && args[0].equals("-read")) {
            try (Scanner in = new Scanner(new File("data.txt"))) {
                while(in.hasNextLine()) {
                    String line = in.nextLine();
                    String[] data = line.split(",");

                    double height = Double.parseDouble(data[0].trim());
                    double weight = Double.parseDouble(data[1].trim());
                    if(data[2].trim().equals("0")) {
                        students.add(new Male(height, weight));
                    } else {
                        students.add(new Female(height, weight));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

        } else {
            for (int i = 0; i < 2000; i++) {
                students.add(new Male());
            }
            for (int i = 0; i < 2000; i++) {
                students.add(new Female());
            }

            try (BufferedWriter dataw = new BufferedWriter(new FileWriter("data.txt"))) {
                for (int i = 0; i < 4000; i++) {
                    dataw.write(students.get(i).getHeight() + "," + students.get(i).getWeight() + ","
                            + (students.get(i).getClass() == Male.class ? 0 : 1) + "\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }

        int maleCorrect = 0;
        int femaleCorrect = 0;
        // guess
        for(int i = 0; i < 4000; i = i + 2) {
            if(guess(students.get(i))) maleCorrect++;
            if(guess(students.get(i+1))) femaleCorrect++;
        }

        double accuracy = ((maleCorrect + femaleCorrect) * 1.0/ 4000);
        double error = 1.0 - accuracy;


        System.out.println("error: " + error);
        System.out.println("accuracy: " + accuracy);
        System.out.println("males correct: " + maleCorrect);
        System.out.println("females correct: " + femaleCorrect);
    }

    public static boolean guess(Student student) {
        double x = 1; // height coeff
        double y = .7; // weight coeff
        double c = -225;

        if(x * student.getHeight() + y * student.getWeight() + c > 0 ) {
            if(student.getClass() == Male.class) return true;
            else return false;
        } else {
            if(student.getClass() == Female.class) return true;
            else return false;
        }
    }

}

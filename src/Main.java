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
            if(guess2D(students.get(i))) maleCorrect++;
            if(guess2D(students.get(i+1))) femaleCorrect++;
        }

        double accuracy = ((maleCorrect + femaleCorrect) * 1.0/ 4000);
        double error = 1.0 - accuracy;


        System.out.println("males correct: " + maleCorrect);
        System.out.println("females correct: " + femaleCorrect);
        System.out.println();

        System.out.println("error: " + error);
        System.out.println("accuracy: " + accuracy);
        System.out.println();

        System.out.println("We say that we are deciding whether the student is male or not.");
        System.out.println("true positive rate: " + (maleCorrect * 1.0 / 2000));
        System.out.println("true negative rate: " + (femaleCorrect * 1.0 / 2000));
        System.out.println("false positive rate: " + ((2000 - femaleCorrect) * 1.0 / 2000));
        System.out.println("false negative rate: " + ((2000 - maleCorrect) * 1.0 / 2000));


        int maleCorrect1D = 0;
        int femaleCorrect1D = 0;
        // guess
        for(int i = 0; i < 4000; i = i + 2) {
            if(guess1D(students.get(i))) maleCorrect1D++;
            if(guess1D(students.get(i+1))) femaleCorrect1D++;
        }

        double accuracy1D = ((maleCorrect1D + femaleCorrect1D) * 1.0/ 4000);
        double error1D = 1.0 - accuracy1D;


        System.out.println("\n1D scenario...\n");

        System.out.println("males correct: " + maleCorrect1D);
        System.out.println("females correct: " + femaleCorrect1D);
        System.out.println();

        System.out.println("error: " + error1D);
        System.out.println("accuracy: " + accuracy1D);
        System.out.println();

        System.out.println("We say that we are deciding whether the student is male or not.");
        System.out.println("true positive rate: " + (maleCorrect1D * 1.0 / 2000));
        System.out.println("true negative rate: " + (femaleCorrect1D * 1.0 / 2000));
        System.out.println("false positive rate: " + ((2000 - femaleCorrect1D) * 1.0 / 2000));
        System.out.println("false negative rate: " + ((2000 - maleCorrect1D) * 1.0 / 2000));

    }

    public static boolean guess2D(Student student) {
        double w1 = 1; // height coeff
        double w2 = .7; // weight coeff
        double c = -225;

        if(w1 * student.getHeight() + w2 * student.getWeight() + c > 0 ) {
            if(student.getClass() == Male.class) return true;
            else return false;
        } else {
            if(student.getClass() == Female.class) return true;
            else return false;
        }
    }

    public static boolean guess1D(Student student) {
        double w1 = 1;
        double c = -172;

        if(w1 * student.getHeight() + c > 0) {
            if(student.getClass() == Male.class) return true;
            else return false;
        } else {
            if(student.getClass() == Female.class) return true;
            else return false;
        }
    }

}

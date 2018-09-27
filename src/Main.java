import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class Main {

    final static int ITERATIONS = 10000;
    final static double PORTION_TO_TRAIN = .75;


    final static double TRAINING_CONSTANT = .00001;

    final static double SOFT_RANGE = 5;

    final static double WEIGHT1 = 1;
    final static double WEIGHT2 = 1;
    final static double WEIGHT3 = -200;



    public static void main(String[] args) {

        // Init data
        ArrayList<Student> students = new ArrayList<Student>();


        // Read from file or generate new data
        if(args.length > 0 && args[0].equals("-generate")) {
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
        } else {

            System.out.println("Reading data in from file...\n");

            try (Scanner in = new Scanner(new File("data.txt"))) {
                while(in.hasNextLine()) {
                    String line = in.nextLine();
                    String[] data = line.split(",");

                    double height = Double.parseDouble(data[0].trim());
                    double weight = Double.parseDouble(data[1].trim());
                    if(data[2].trim().equals("0")) {
                        students.add(new Male(height, weight));
                    } else {
                        Female f = new Female(height, weight);
                        students.add(new Female(height, weight));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }


        // Move some to test and some to train

        Random r = new Random();

        ArrayList<Student> dataTrain = new ArrayList<>();
        ArrayList<Student> dataTest = new ArrayList<>();

        int totalSize = students.size();
        for(int i = 0; i < PORTION_TO_TRAIN * totalSize; i++) {
            int index = r.nextInt(students.size());
            dataTrain.add(students.remove(index));
        }
        dataTest = students;

        System.out.println("dataTrain " + dataTrain.size());
        System.out.println("dataTest " + dataTest.size() + "\n");

        // Initiate neuron
        double w1 = WEIGHT1;
        double w2 = WEIGHT2;
        double w3 = WEIGHT3;


        // Train neuron
        for(int i = 0; i < ITERATIONS; i++) {

            Student inputStudent = dataTrain.get(i % dataTrain.size());

            // hard run
            double output = executeHard(inputStudent, w1, w2, w3);
            double desired = inputStudent.getClass() == Male.class ? 1 : 0;

            double delta1 = TRAINING_CONSTANT * inputStudent.getHeight() * (desired - output);
            double delta2 = TRAINING_CONSTANT * inputStudent.getWeight() * (desired - output);
            double delta3 = TRAINING_CONSTANT * 1 * (desired - output);

            w1 += delta1;
            w2 += delta2;
            w3 += delta3;

            //System.out.println("o = " + output + ", desired = " + desired + ", delta1 = " + delta1 + ", delta2 = " + delta2 + ", delta3 = " + delta3 + ", inputclass = " + inputStudent.getClass() + ", inputHeight = " + inputStudent.getHeight() + ", " + inputStudent.getWeight());

        }

        System.out.printf("w1 = %5.5f\n", w1);
        System.out.printf("w2 = %5.5f\n", w2);
        System.out.printf("w3 = %5.5f\n", w3);

        System.out.println();



        int maleCorrect = 0;
        int maleWrong = 0;
        int femaleCorrect = 0;
        int femaleWrong = 0;

        Class guess;

        // guess
        for(int i = 0; i < dataTest.size(); i++) {

            if(executeHard(dataTest.get(i), w1, w2, w3) >= .5) guess = Male.class;
            else guess = Female.class;

            if(guess == dataTest.get(i).getClass()) {
                if(dataTest.get(i).getClass() == Male.class) maleCorrect++;
                else femaleCorrect++;
            } else {
                if(dataTest.get(i).getClass() == Male.class) maleWrong++;
                else femaleWrong++;
            }
        }

        double accuracy = ((maleCorrect + femaleCorrect) * 1.0/ (maleCorrect + maleWrong + femaleCorrect + femaleWrong));
        double error = 1.0 - accuracy;


        System.out.println("males correct: " + maleCorrect);
        System.out.println("males wrong: " + maleWrong);
        System.out.println("females correct: " + femaleCorrect);
        System.out.println("females wrong: " + femaleWrong);


        System.out.println();

        System.out.println("error: " + error);
        System.out.println("accuracy: " + accuracy);
        System.out.println();

        System.out.println("Weight ratio: " + w1 / w2);




        ////////////////////////////////////////////////////////////////////////////////////////////////
        //
        //      SOFT SOFT SOFT SOFT SOFT SOFT SOFT
        //
        //
        ////////////////////////////////////////////////////////////////////////////////////////////////

        System.out.println("\nSOFTSOFTSOFTSOFTSOFT\n");

        // Initiate neuron
        w1 = WEIGHT1;
        w2 = WEIGHT2;
        w3 = WEIGHT3;


        // Train neuron
        for(int i = 0; i < ITERATIONS; i++) {

            Student inputStudent = dataTrain.get(i % dataTrain.size());

            // hard run
            double output = executeSoft(inputStudent, w1, w2, w3);
            double desired = inputStudent.getClass() == Male.class ? 1 : 0;

            double delta1 = TRAINING_CONSTANT * inputStudent.getHeight() * (desired - output);
            double delta2 = TRAINING_CONSTANT * inputStudent.getWeight() * (desired - output);
            double delta3 = TRAINING_CONSTANT * 1 * (desired - output);

            w1 += delta1;
            w2 += delta2;
            w3 += delta3;

            //System.out.println("o = " + output + ", desired = " + desired + ", delta1 = " + delta1 + ", delta2 = " + delta2 + ", delta3 = " + delta3 + ", inputclass = " + inputStudent.getClass() + ", inputHeight = " + inputStudent.getHeight() + ", " + inputStudent.getWeight());

        }

        System.out.printf("w1 = %5.5f\n", w1);
        System.out.printf("w2 = %5.5f\n", w2);
        System.out.printf("w3 = %5.5f\n", w3);

        System.out.println();



        maleCorrect = 0;
        maleWrong = 0;
        femaleCorrect = 0;
        femaleWrong = 0;


        // guess
        for(int i = 0; i < dataTest.size(); i++) {

            if(executeSoft(dataTest.get(i), w1, w2, w3) >= .5) guess = Male.class;
            else guess = Female.class;

            if(guess == dataTest.get(i).getClass()) {
                if(dataTest.get(i).getClass() == Male.class) maleCorrect++;
                else femaleCorrect++;
            } else {
                if(dataTest.get(i).getClass() == Male.class) maleWrong++;
                else femaleWrong++;
            }
        }

        accuracy = ((maleCorrect + femaleCorrect) * 1.0/ (maleCorrect + maleWrong + femaleCorrect + femaleWrong));
        error = 1.0 - accuracy;


        System.out.println("males correct: " + maleCorrect);
        System.out.println("males wrong: " + maleWrong);
        System.out.println("females correct: " + femaleCorrect);
        System.out.println("females wrong: " + femaleWrong);


        System.out.println();

        System.out.println("error: " + error);
        System.out.println("accuracy: " + accuracy);
        System.out.println();

        System.out.println("Weight ratio: " + w1 / w2);



//        System.out.println("We say that we are deciding whether the student is male or not.");
//        System.out.println("true positive rate: " + (maleCorrect * 1.0 / 2000));
//        System.out.println("true negative rate: " + (femaleCorrect * 1.0 / 2000));
//        System.out.println("false positive rate: " + ((2000 - femaleCorrect) * 1.0 / 2000));
//        System.out.println("false negative rate: " + ((2000 - maleCorrect) * 1.0 / 2000));
//
//
//        int maleCorrect1D = 0;
//        int femaleCorrect1D = 0;
//        // guess
//        for(int i = 0; i < 4000; i = i + 2) {
//            if(guess1D(students.get(i))) maleCorrect1D++;
//            if(guess1D(students.get(i+1))) femaleCorrect1D++;
//        }
//
//        double accuracy1D = ((maleCorrect1D + femaleCorrect1D) * 1.0/ 4000);
//        double error1D = 1.0 - accuracy1D;
//
//
//        System.out.println("\n1D scenario...\n");
//
//        System.out.println("males correct: " + maleCorrect1D);
//        System.out.println("females correct: " + femaleCorrect1D);
//        System.out.println();
//
//        System.out.println("error: " + error1D);
//        System.out.println("accuracy: " + accuracy1D);
//        System.out.println();
//
//        System.out.println("We say that we are deciding whether the student is male or not.");
//        System.out.println("true positive rate: " + (maleCorrect1D * 1.0 / 2000));
//        System.out.println("true negative rate: " + (femaleCorrect1D * 1.0 / 2000));
//        System.out.println("false positive rate: " + ((2000 - femaleCorrect1D) * 1.0 / 2000));
//        System.out.println("false negative rate: " + ((2000 - maleCorrect1D) * 1.0 / 2000));

    }

    public static double executeHard(Student student, double w1, double w2, double w3) {

        return w1 * student.getHeight() + w2 * student.getWeight() + w3 * 1 > 0 ? 1 : 0;

    }

    public static double executeSoft(Student student, double w1, double w2, double w3) {

        if(Math.abs(w1 * student.getHeight() + w2 * student.getWeight() + w3) >= SOFT_RANGE) {
            boolean guess = w1 * student.getHeight() + w2 * student.getWeight() + w3 > 0;
            return guess ? 1 : 0;
        } else {
            // Nice lil curved function we got here
            double softGuess = ((w1 * student.getHeight() + w2 * student.getWeight() + w3) + SOFT_RANGE) / (SOFT_RANGE * 2);
            return softGuess;
        }

    }
}

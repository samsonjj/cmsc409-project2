import java.util.Random;

public class Male implements Student {

    // centimeters
    final static double HEIGHT_MEAN  = 180;
    final static double HEIGHT_VAR = 15;
    // kilograms
    final static double WEIGHT_MEAN = 80;
    final static double WEIGHT_VAR = 15;

    private double height;
    private double weight;

    public Male() {
        Random random = new Random();
        this.height = random.nextGaussian()*HEIGHT_VAR+HEIGHT_MEAN;
        this.weight = random.nextGaussian()*WEIGHT_VAR+WEIGHT_MEAN;
    }

    public double getHeight() {
        return height;
    }

    public double getWeight() {
        return weight;
    }
}

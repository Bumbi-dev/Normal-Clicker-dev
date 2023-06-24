import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Scanner;

public class Player {

    File fila = new File("Date Player");

    double clicks;
    double clickPower;
    int pretu;
    String upgradeuri;

    public Player (double clicks, double clickPower, int pretu, String upgradeuri) {
        this.clicks = clicks;
        this.clickPower = clickPower;
        this.pretu = pretu;
        this.upgradeuri = upgradeuri;
    }

    public Player () {
        clicks = 0;
        clickPower = 1;
        pretu = 100;
        upgradeuri = ".";
    }

    public void loadProgress () {
        try {
            Scanner scanner = new Scanner(fila);

            clicks = Float.parseFloat(scanner.next());
            clickPower = Float.parseFloat(scanner.next());
            pretu = Integer.parseInt(scanner.next());
            scanner.nextLine();
            upgradeuri = scanner.nextLine();

            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator('.');
            DecimalFormat decimalFormat = new DecimalFormat("#0.0", symbols);

            String roundedNumber = decimalFormat.format(clicks);
            clicks = Double.parseDouble(roundedNumber);

            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save () {// pe viitor sa fie criptate salvarile
        String salvare = clicks + "\n" + clickPower + "\n" + pretu + "\n." + upgradeuri;
        try {
            FileWriter fr = new FileWriter(fila);
            fr.write(salvare);
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //fila.setReadOnly();
    }
}

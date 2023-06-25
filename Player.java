import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Player {

    File fila = new File("Date Player");

    String upgradeuri;
    double clicks, clickPower;
    int pretu;

    public Player (double clicks, double clickPower, int pretu, String upgradeuri) {
        this.clicks = clicks;
        this.clickPower = clickPower;
        this.pretu = pretu;
        this.upgradeuri = upgradeuri;
    }

    public Player () {//inceputu
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

            DecimalFormatSymbols symbols = new DecimalFormatSymbols();//citeste doubles si le rotunjeste
            symbols.setDecimalSeparator('.');
            DecimalFormat decimalFormat = new DecimalFormat("#0.0", symbols);

            String roundedNumber = decimalFormat.format(clicks);
            clicks = Double.parseDouble(roundedNumber);

            scanner.close();
        } catch (NoSuchElementException e) {//daca ceva nu e bine la fila (a fost modificata gresit) se reseteaza tot
            Player player = new Player();
            player.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save () {
        String salvare = clicks + "\n" + clickPower + "\n" + pretu + "\n." + upgradeuri;
        try {
            fila.setWritable(true);
            FileWriter fr = new FileWriter(fila);
            fr.write(salvare);
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        fila.setReadOnly();
    }
}

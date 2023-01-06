import Models.Coordinates;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class main {
    public static void main(String[] args) throws InterruptedException {
        final int NUMBEROFCOLOUMS = 10;
        final int NUMBEROFROWS = 10;
        final double SEED = 0.3;

        Array array = new Array(NUMBEROFROWS, NUMBEROFCOLOUMS);
        array.create(SEED);

        ArrayGameHandler agh = new ArrayGameHandler(array);
        agh.nextStep();
        TimeUnit.SECONDS.sleep(2);

        Coordinates c1 = new Coordinates(0,0);
        Coordinates c2 = new Coordinates(0,1);
        Coordinates c3 = new Coordinates(0,2);

        List<Coordinates> list = new ArrayList<>(List.of(c1,c2,c3));

        agh.changeHoverCellsStatus(list);
    }
}

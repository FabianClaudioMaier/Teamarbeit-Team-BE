public class main {
    public static void main(String[] args) {
       final int NUMBEROFCOLOUMS = 10;
       final int NUMBEROFROWS = 10;
       final double SEED = 0.3;

       Array array = new Array(NUMBEROFROWS,NUMBEROFCOLOUMS);
       array.create(SEED);

        for (int i = 0; i < 10; i++) {
           array.update();
           System.out.println(array);
       }
    }
}

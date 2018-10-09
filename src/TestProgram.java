
public class TestProgram {
    public static void main(String[] args) {

        Integer[] a = {1, 3, 5, 7};
        Liste<Integer> liste = new DobbeltLenketListe<>(a);
        for (int i = 1; i <= 10; i++) liste.leggInn(i);
        System.out.println(liste);

        ((DobbeltLenketListe<Integer>) liste).finnNode(4);
        liste.leggInn(4);

    }
}

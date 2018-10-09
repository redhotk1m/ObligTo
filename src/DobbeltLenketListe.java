import java.util.*;

public class DobbeltLenketListe<T> implements Liste<T>
{
    private static final class Node<T>   // en indre nodeklasse
    {
        // instansvariabler
        private T verdi;
        private Node<T> forrige, neste;

        private Node(T verdi, Node<T> forrige, Node<T> neste)  // konstruktør
        {
            this.verdi = verdi;
            this.forrige = forrige;
            this.neste = neste;
        }

        protected Node(T verdi)  // konstruktør
        {
            this(verdi, null, null);
        }

    }   // Node

    // instansvariabler
    private Node<T> hode;          // peker til den første i listen
    private Node<T> hale;          // peker til den siste i listen
    private int antall;            // antall noder i listen
    private int endringer;          // antall endringer i listen

    // konstruktør
    public DobbeltLenketListe()
    {
        hode = hale = null;
        antall = 0;
        endringer = 0;
    }

    // konstruktør
    public DobbeltLenketListe(T[] a)
    {
        Node p;
        if (a == null){
            throw new NullPointerException("Feil ved opprettelse av liste, nullverdi");
        } else {
            for (int i = 0; i<a.length; ++i){
                if (a[i]!=null){
                    if (hode == hale && hode == null){
                        hode = new Node<T>(a[i],null,null);
                        hale = hode;
                    }else {
                        p = new Node<T>(a[i], null, null);
                        p.forrige = hale;
                        hale.neste = p;
                        hale = p;
                    }
                }
            }
        }
    }

    public Node<T> finnNode(int indeks){
        indeksKontroll(indeks, false);
        Node<T> p;
        if(indeks<antall/2) {
            p = hode;
            for (int i = 0; i < indeks; i++) {
                p = p.neste;
            }
        }
        else{
            p=hale;
            for (int i = antall-1; i > indeks; i--) {
                p = p.forrige;
            }
        }
        return p;
    }
    // subliste


    public Liste<T> subliste(int fra, int til) {
        Liste<T> liste= new DobbeltLenketListe<>();
        Node p=hode;
        if(til>liste.antall()) throw new IllegalArgumentException("Til (" + til+") kan ikke være større enn antall ("+liste.antall()+")");
        for(int i=fra;i<til;i++){
            liste.leggInn(2);
            p=p.neste;
        }
        return liste;
    }

    @Override   public int antall()
    {

        antall = 0;
        Node currentNode = hode;

        while (currentNode!=null) {
            ++antall;
            currentNode = currentNode.neste;
        }
        return antall;
    }

    @Override   public boolean tom()
    {
        if (antall==0){
            return true;
        }
        return false;

    }

    @Override   public boolean leggInn(T verdi)
    {
        if (verdi == null){
            throw new NullPointerException("Verdiene er null");
        }

        if (hode == hale && hode == null){
            hode = new Node<T>(verdi,null,null);
            hale = hode;

        }else {
            Node<T> p = new Node<T>(verdi, null, null);
            p.forrige = hale;
            hale.neste = p;
            hale = p;
        }

        antall();
        return true;
    }

    @Override   public void leggInn(int indeks, T verdi)
    {
        throw new UnsupportedOperationException("Ikke laget ennå!");
    }

    @Override   public boolean inneholder(T verdi)
    {
        if(indeksTil(verdi)!=-1) return true;
        else return false;
    }

    @Override   public T hent(int indeks) {
        indeksKontroll(indeks, false);
        return finnNode(indeks).verdi;
    }

    @Override
    public int indeksTil(T verdi) {
        if(verdi==null) return -1;
        Node<T> p= hode;
        for(int i =0; i<antall;i++){
            if(p.verdi.equals(verdi)) return i;
            p=p.neste;
        }
        return -1;
    }

    @Override   public T oppdater(int indeks, T nyverdi) {
        Objects.requireNonNull(nyverdi, "Ny verdi kan ikke være null");
        indeksKontroll(indeks, false);
        Node<T> p= finnNode(indeks);
        T gammelverdi =p.verdi;
        p.verdi=nyverdi;
        return gammelverdi;
    }

    @Override   public boolean fjern(T verdi)
    {
        throw new UnsupportedOperationException("Ikke laget ennå!");
    }

    @Override   public T fjern(int indeks)
    {
        throw new UnsupportedOperationException("Ikke laget ennå!");
    }

    //Oppgave 7
    @Override   public void nullstill()
    {
        Node currentNode = hode;
        Node hjelper = null;

        while(currentNode != null){
            hjelper = currentNode;
            currentNode = hjelper.neste;
            hjelper.verdi=null;
            hjelper.neste=null;
            hjelper.forrige=null;
        }

        for (int i = 0; i <antall() ; i++) {
            fjern(i);

        }
    }

    @Override   public String toString() {
        Node p = hode;
        StringJoiner a = new StringJoiner(", ", "[", "]");

        while (p != null) {
            a.add(p.verdi.toString());
            p = p.neste;
        }
        return a.toString();
    }

    public String omvendtString()
    {
        Node p = hale;
        StringJoiner a = new StringJoiner(", ", "[", "]");

        while (p != null) {
            a.add(p.verdi.toString());
            p = p.forrige;
        }
        return a.toString();
    }

    public static <T> void sorter(Liste<T> liste, Comparator<? super T> c)
    {
        throw new UnsupportedOperationException("Ikke laget ennå!");
    }

    @Override
    public Iterator<T> iterator()
    {

        DobbeltLenketListeIterator iter = new DobbeltLenketListeIterator();
        return iter;

    }

    public Iterator<T> iterator(int indeks)
    {
        throw new UnsupportedOperationException("Ikke laget ennå!");
    }
    private void fratilKontroll(int antall, int fra, int til) {
        if (fra < 0)                                  // fra er negativ
            throw new IndexOutOfBoundsException
                    ("fra(" + fra + ") er negativ!");

        if (til > antall)                          // til er utenfor tabellen
            throw new IndexOutOfBoundsException
                    ("til(" + til + ") > antall(" + antall + ")");

        if (fra > til)                                // fra er større enn til
            throw new IllegalArgumentException
                    ("fra(" + fra + ") > til(" + til + ") - illegalt intervall!");
    }


    private class DobbeltLenketListeIterator implements Iterator<T>
    {
        private Node<T> denne;
        private boolean fjernOK;
        private int iteratorendringer;

        private DobbeltLenketListeIterator()
        {
            denne = hode;     // denne starter på den første i listen
            fjernOK = false;  // blir sann når next() kalles
            iteratorendringer = endringer;  // teller endringer
        }

        private DobbeltLenketListeIterator(int indeks)
        {
            for (int i = 0; i <indeks ; i++) {
                denne = denne.neste;
            }

            throw new UnsupportedOperationException("Ikke laget ennå!");

        }

        @Override
        public boolean hasNext()
        {
            return denne != null;  // denne koden skal ikke endres!     }
        }


        @Override
        public T next()
        {
            if(iteratorendringer!=endringer)
                throw new ConcurrentModificationException("De er ikke like");

            if(!hasNext()) {
                fjernOK = true;
                throw new NoSuchElementException("Ikke flere noder igjen");
            }
            T verdi = denne.verdi;
            denne=denne.neste;
            return verdi;
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException("Ikke laget ennå!");
        }

    } // DobbeltLenketListeIterator

} // DobbeltLenketListe
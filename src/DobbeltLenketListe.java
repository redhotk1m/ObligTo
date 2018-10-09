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
            //liste.leggInn(p.verdi);
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
        Objects.requireNonNull(verdi, "Null verdier er ikke lov."); //sjekker om verdi er null
        indeksKontroll(indeks, true); //Sjekker om indeks er lovlig

        if(antall == 0){ //Hvis det ikke finnes noen elementer i listen:
            hode = hale = new Node<T>(verdi, null, null); //Dette blir det eneste elementet i listen og
            //Både hode og hale peker på denne noden
        }
        else if (indeks == antall) { //Hvis det skal legges inn node i slutten av listen
            hale = hale.neste = new Node<T>(verdi, hale, null); //legges inn en ny node på hale.neste
            //så flyttes halen over til denne verdien. peker på null og den forrige verdien.
        }
        else if (indeks == 0) { //hvis den skal legges inn en node først i listen
            hode = hode.forrige = new Node<T>(verdi, null, hode); //legges inn ny node først ved hode.forrige
            //deretter flyttes hodet hit.
        }
        else
        {
            Node<T> enNode = finnNode(indeks); //bruker finnNode til å finne posisjonen til en Node
            enNode.forrige = enNode.forrige.neste = new Node<T>(verdi, enNode.forrige, enNode); //legger inn en ny node
            //Og setter pekere til og fra denne noden.
        }

        antall++; //oppdaterer antall i listen
        endringer++; //oppdaterer endringer
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
        {
            if (verdi == null) return false;

            for (Node<T> p = hode; p != null; p = p.neste)
            {
                if (p.verdi.equals(verdi))
                {
                    if (p == hode)
                    {
                        if (antall == 1) hode = hale = null;
                        else (hode = hode.neste).forrige = null;
                    }
                    else if (p == hale) (hale = hale.forrige).neste = null;
                    else (p.forrige.neste = p.neste).forrige = p.forrige;

                    antall--;
                    endringer++;
                    return true;
                }
            }
            return false;
        }
    }


/*

        if (verdi == null) {
            return false;
        }

        Node denneNoden = hode;    //Initialize current
        while (denneNoden != null)
        {
            if (denneNoden.verdi.equals(verdi)) {
                 if(denneNoden == hode){
                     hode = hode.neste;
                     return true;
                 }
                 if(antall == 1){
                     hale = null;
                     return true;
                 }
                 else {
                     Node<T> p = denneNoden.forrige;  // p er noden foran den som skal fjernes
                     Node<T> q = p.neste;               // q skal fjernes

                     if (q == hale) hale = p;           // q er siste node
                     p.neste = q.neste;
                     return true;
                 }
            }
            else{

                return false;    //data found
        }
    }

        for (Node<T> p = hode; p != null; p = p.neste)
        {
            if (p.verdi.equals(verdi))
            {
                if (p == hode)
                {
                    if (antall == 1) hode = hale = null;
                    else (hode = hode.neste).forrige = null;
                }
                else if (p == hale) (hale = hale.forrige).neste = null;
                else (p.forrige.neste = p.neste).forrige = p.forrige;

                antall--;
                endringer++;
                return true;
            }
        }
        return false;
    }
*/
    @Override   public T fjern(int indeks)
    {
        indeksKontroll(indeks, false);

        Node<T> p = finnNode(indeks);

        if (p == hode)
        {
            if (antall == 1) {
                hode = hale = null;
            }
            else {
                (hode = hode.neste).forrige = null;
            }
        }
        else if (p == hale){
            (hale = hale.forrige).neste = null;
        }
        else {
            (p.forrige.neste = p.neste).forrige = p.forrige;
        }

        antall--;
        endringer++;

        return p.verdi;

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
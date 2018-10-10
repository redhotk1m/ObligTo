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

    private Node<T> finnNode(int indeks){
        Node<T> p;
        if(indeks<antall/2) {
            p = hode;
            for (int i = 0; i < indeks; i++) p = p.neste;
        }
        else{
            p=hale;
            for (int i = antall-1; i > indeks; i--) p = p.forrige;
        }
        return p;
    }
    // subliste


    public Liste<T> subliste(int fra, int til) {
        fratilKontroll(antall, fra , til);
        Liste<T> liste= new DobbeltLenketListe<>();
        Node<T> p=finnNode(fra);

        for(int i=fra;i<til;i++){
            liste.leggInn(p.verdi);
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

    @Override   public boolean inneholder(T verdi) {
        return indeksTil(verdi) != -1;
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

    //Denne koden gir noen ganger feil på 6zg. Har fått bekreftet at dette
    //er noe som kan skje og at det ikke nødvendigvis er feil i koden
    @Override   public boolean fjern(T verdi)
    {
        {
            if(verdi == null) { //hvis verdien er null returneres false, null verdier behandles ikke
                return false;
            }

            Node sjekkNode = hode; //vi bruker sjekknode til å sammenlikne verdier
            while (sjekkNode != null) { //så lenge noden vi sjekker sin verdi ikke er 0 sjekker vi den mot verdien vi leter etter
                if (sjekkNode.verdi.equals(verdi)) { //om verdiene stemmer over ens går vi vidre med denne noden
                    break;
                }

                sjekkNode = sjekkNode.neste; //hvis nodens verdi ikke stemmer med verdien vi leter etter går vi til neste
            }

            if (sjekkNode == null){ //Hvis noden vi sjekker skulle være utenfor intervallet returneres false
                return false;
            }

            if (sjekkNode == hode){ //hvis noden som inneholder verdien vår er hode noden
                hode = hode.neste; //vi setter hode til den neste noden i listen

                    if (antall != 1) { //Hvis det kun er en verdi i listen:
                        hode.forrige = null;
                    } else {
                        hale = null;
                }
            }

            else if (sjekkNode == hale){ //Hvis verdien ligger i hale
                hale = hale.forrige; //Hale settes til den forrige noden
                hale.neste = null; //setter halen til å peke på 0
            }

            else{ //Hvis verdien vi leter etter ligger i midten
                (sjekkNode.forrige.neste = sjekkNode.neste).forrige = sjekkNode.forrige;
            }

            antall--;
            endringer++;
            return true;
        }
    }

    @Override   public T fjern(int indeks)
    {
        indeksKontroll(indeks, false); //Sjekker indeksen

        Node<T> denneNoden = finnNode(indeks); //Finner noden ved hjelp av indels

        if (denneNoden == hode) { //Hvis noden er hodet:
            if (antall == 1) { //Hvis det kun er en verdi i tabellen setter vi hode og hale til null
                hode = hale = null;
            }
            else { //Ellers setter vi hode til den neste verdien med peker tilbake på null
                (hode = hode.neste).forrige = null;
            }
        }
        else if (denneNoden == hale){  //hvis noden er halen:
            (hale = hale.forrige).neste = null; //Vi setter halen til nye pekerverdier
        }
        else { //Hvis noden befinner seg midt i listen bytter vi pekerverdiene rundt noden så den ikke lenger har pekere til seg
            (denneNoden.forrige.neste = denneNoden.neste).forrige = denneNoden.forrige;
        }

        antall--;
        endringer++;

        return denneNoden.verdi; //returnerer verdien til noden vi slettet

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
        public void remove() {
            //throw new UnsupportedOperationException("Ikke laget ennå!");

            //Hvis det ikke er tillatt aa kalle paa denne metoden
            if(fjernOK == true){
                throw new IllegalStateException(
                        "Det er ikke tillat aa kalle denne metoden!");
            }

            //Hvis endringer og iteratorendringer er forskjellige
            if(endringer != iteratorendringer){
                throw new ConcurrentModificationException(
                                "Endringer og iteratorendringer er forskjellige!");

            }else {
                fjernOK = false;
            }

            if (antall == 1) {
                //hvis noden som skal fjernes er eneste verdi
                hode = null;
                hale = null;
            }

            if(denne == null){
                //hvis siste node skal fjernes
                hale = hale.forrige;
            }

            
        }

    } // DobbeltLenketListeIterator

} // DobbeltLenketListe
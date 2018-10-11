import java.util.*;

public class DobbeltLenketListe<T> implements Liste<T> {
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
    public DobbeltLenketListe() {
        hode = hale = null;
        antall = 0;
        endringer = 0;
    }

    // konstruktør
    public DobbeltLenketListe(T[] a) {
        Node p;
        if (a == null) {
            throw new NullPointerException("Feil ved opprettelse av liste, nullverdi"); //Nullverdi som parameter
        } else {
            for (int i = 0; i < a.length; ++i) { //For løkke som itererer gjennom hele listen
                if (a[i] != null) { //Kjører så lenge indeksen ikke er null (dvs, tar ikke med null elementer i listen)
                    if (hode == hale && hode == null) { //Hvis det ikke eksisterer noen elementer i listen enda, altså hale = hode er samme verdi, null.
                        hode = new Node<T>(a[i], null, null); //Oppretter ny node.
                        hale = hode; //Setter hale og hode til samme verdi, fordi det bare er ett element i listen.
                    } else {
                        p = new Node<T>(a[i], null, null); //Lager elementet P.
                        p.forrige = hale; //Setter p sin forrige til hale
                        hale.neste = p; //Setter halen sin neste til å peke på p.
                        hale = p;  // Flytter halen til å peke på P.
                    }
                }
            }
        }
    }

    private Node<T> finnNode(int indeks) {                           //finnNode-metode
        Node<T> p;                                                  //Oppretter en Node
        if (indeks < antall / 2) {                                       //Sjekker om indeksen ligger i første halvdel av listen
            p = hode;                                               //Setter Node p til første indeks i listen
            for (int i = 0; i < indeks; i++) p = p.neste;           //For løkke som flytter p fram til indeks
        } else {                                                       //Dersom indeksen ligger i siste halvdel av tabellen:
            p = hale;                                                 //Setter Node p til siste indeks i listen
            for (int i = antall - 1; i > indeks; i--) p = p.forrige;  //Flytter p framover til indeks
        }
        return p;                                                   //Returnerer Noden p i indeks
    }
    // subliste


    public Liste<T> subliste(int fra, int til) {                    //Subliste oppretter en liste med verdiene mellom fra-til
        fratilKontroll(antall, fra, til);                          //Kjører fratilKontroll metoden
        Liste<T> liste = new DobbeltLenketListe<>();                 //Oppretter en ny liste
        Node<T> p = finnNode(fra);                                    //Opretter en Node og setter den på indeksen fra

        for (int i = fra; i < til; i++) {                                   //ittererer gjennom indeks fra-til
            liste.leggInn(p.verdi);                                 //legger til verdienn og referansen fra Node p inn i liste
            p = p.neste;                                              //Hopper til neste p i opprinnelig liste
        }
        return liste;                                               //returnerer den ny listen med Noder i intervallet fra-til
    }

    @Override
    public int antall() {

        antall = 0;  // Setter antall til null, for å begynne tellingen.
        Node currentNode = hode;  // Setter currentNode til å være hode.

        while (currentNode != null) {  //Så lenge noden eksisterer så :
            ++antall; //Øk antall med 1
            currentNode = currentNode.neste; // Flytt correntNode til neste node.
        }
        return antall; // Returnerer antall.
    }

    @Override
    public boolean tom() {
        if (antall == 0) { //Sjekker om antall er 0.
            return true; // Hvis antall er 0, return true.
        }
        return false; // Hvis antall ikke er 0, return false.

    }

    @Override
    public boolean leggInn(T verdi) {
        if (verdi == null) { //Hvis verdien til noden er null
            throw new NullPointerException("Verdiene er null"); // Kast en NullPointerException
        }

        if (hode == hale && hode == null) { // Det ikke er noen noder enda, altså hale og hode er begge null.
            hode = new Node<T>(verdi, null, null); // Lag ny node, og la hode peke på den.
            hale = hode; // La hale også peke på samme node som Hode.

        } else {
            Node<T> p = new Node<T>(verdi, null, null); // Lag ny node P
            p.forrige = hale;  // Sett p sin forrige til å være hale.
            hale.neste = p; // Sett hale sin neste til å være P.
            hale = p; // Sett hale til å nå peke på P.
        }

        antall(); // Beregn antall noder.
        return true; // Returner true.
    }

    @Override
    public void leggInn(int indeks, T verdi) {
        Objects.requireNonNull(verdi, "Null verdier er ikke lov."); //sjekker om verdi er null
        indeksKontroll(indeks, true); //Sjekker om indeks er lovlig

        if (antall == 0) { //Hvis det ikke finnes noen elementer i listen:
            hode = hale = new Node<T>(verdi, null, null); //Dette blir det eneste elementet i listen og
            //Både hode og hale peker på denne noden
        } else if (indeks == antall) { //Hvis det skal legges inn node i slutten av listen
            hale = hale.neste = new Node<T>(verdi, hale, null); //legges inn en ny node på hale.neste
            //så flyttes halen over til denne verdien. peker på null og den forrige verdien.
        } else if (indeks == 0) { //hvis den skal legges inn en node først i listen
            hode = hode.forrige = new Node<T>(verdi, null, hode); //legges inn ny node først ved hode.forrige
            //deretter flyttes hodet hit.
        } else {
            Node<T> enNode = finnNode(indeks); //bruker finnNode til å finne posisjonen til en Node
            enNode.forrige = enNode.forrige.neste = new Node<T>(verdi, enNode.forrige, enNode); //legger inn en ny node
            //Og setter pekere til og fra denne noden.
        }

        antall++; //oppdaterer antall i listen
        endringer++; //oppdaterer endringer
    }

    @Override
    public boolean inneholder(T verdi) {
        return indeksTil(verdi) != -1;
    }

    @Override
    public T hent(int indeks) {                             //Metoden hent
        indeksKontroll(indeks, false);                          //Kjører metoden indeksKontroll
        return finnNode(indeks).verdi;                                  //Returnerer verdien til Noden i input indeks
    }

    @Override
    public int indeksTil(T verdi) {
        if (verdi == null) return -1;
        Node<T> p = hode;
        for (int i = 0; i < antall; i++) {
            if (p.verdi.equals(verdi)) return i;
            p = p.neste;
        }
        return -1;
    }

    @Override
    public T oppdater(int indeks, T nyverdi) {                          //Metoden oppdater
        Objects.requireNonNull(nyverdi, "Ny verdi kan ikke være null");     //Sjekker at ikke den nye verdien er null
        indeksKontroll(indeks, false);                                      //Kjører metoden indekskontroll
        Node<T> p = finnNode(indeks);                                                //Oppretter en ny Node med verdiene til gammel node på gitt indeks
        T gammelverdi = p.verdi;                                                     //Lagrer den gamle verdien til Noden
        p.verdi = nyverdi;                                                            //Gir noden p ny verdi
        return gammelverdi;                                                         //Returnerer den gamle verdien til Noden
    }

    //Denne koden gir noen ganger feil på 6zg. Har fått bekreftet at dette
    //er noe som kan skje og at det ikke nødvendigvis er feil i koden
    @Override
    public boolean fjern(T verdi) {
        {
            if (verdi == null) { //hvis verdien er null returneres false, null verdier behandles ikke
                return false;
            }

            Node sjekkNode = hode; //vi bruker sjekknode til å sammenlikne verdier
            while (sjekkNode != null) { //så lenge noden vi sjekker sin verdi ikke er 0 sjekker vi den mot verdien vi leter etter
                if (sjekkNode.verdi.equals(verdi)) { //om verdiene stemmer over ens går vi vidre med denne noden
                    break;
                }

                sjekkNode = sjekkNode.neste; //hvis nodens verdi ikke stemmer med verdien vi leter etter går vi til neste
            }

            if (sjekkNode == null) { //Hvis noden vi sjekker skulle være utenfor intervallet returneres false
                return false;
            }

            if (sjekkNode == hode) { //hvis noden som inneholder verdien vår er hode noden
                hode = hode.neste; //vi setter hode til den neste noden i listen

                if (antall != 1) { //Hvis det kun er en verdi i listen:
                    hode.forrige = null;
                } else {
                    hale = null;
                }
            } else if (sjekkNode == hale) { //Hvis verdien ligger i hale
                hale = hale.forrige; //Hale settes til den forrige noden
                hale.neste = null; //setter halen til å peke på 0
            } else { //Hvis verdien vi leter etter ligger i midten
                (sjekkNode.forrige.neste = sjekkNode.neste).forrige = sjekkNode.forrige;
            }

            antall--;
            endringer++;
            return true;
        }
    }

    @Override
    public T fjern(int indeks) {
        indeksKontroll(indeks, false); //Sjekker indeksen

        Node<T> denneNoden = finnNode(indeks); //Finner noden ved hjelp av indels

        if (denneNoden == hode) { //Hvis noden er hodet:
            if (antall == 1) { //Hvis det kun er en verdi i tabellen setter vi hode og hale til null
                hode = hale = null;
            } else { //Ellers setter vi hode til den neste verdien med peker tilbake på null
                (hode = hode.neste).forrige = null;
            }
        } else if (denneNoden == hale) {  //hvis noden er halen:
            (hale = hale.forrige).neste = null; //Vi setter halen til nye pekerverdier
        } else { //Hvis noden befinner seg midt i listen bytter vi pekerverdiene rundt noden så den ikke lenger har pekere til seg
            (denneNoden.forrige.neste = denneNoden.neste).forrige = denneNoden.forrige;
        }

        antall--;
        endringer++;

        return denneNoden.verdi; //returnerer verdien til noden vi slettet

    }

    //Oppgave 7
    @Override
    public void nullstill() {
        Node currentNode = hode;
        Node hjelper = null;

        while (currentNode != null) {
            hjelper = currentNode;
            currentNode = hjelper.neste;
            hjelper.verdi = null;
            hjelper.neste = null;
            hjelper.forrige = null;
        }

        for (int i = 0; i < antall(); i++) {
            fjern(i);

        }
    }

    @Override
    public String toString() {
        Node p = hode;
        StringJoiner a = new StringJoiner(", ", "[", "]");

        while (p != null) {
            a.add(p.verdi.toString());
            p = p.neste;
        }
        return a.toString();
    }

    public String omvendtString() {
        Node p = hale;
        StringJoiner a = new StringJoiner(", ", "[", "]");

        while (p != null) {
            a.add(p.verdi.toString());
            p = p.forrige;
        }
        return a.toString();
    }

    public static <T> void sorter(Liste<T> liste, Comparator<? super T> c) {
        throw new UnsupportedOperationException("Ikke laget ennå!");
    }

    @Override
    public Iterator<T> iterator() {

        DobbeltLenketListeIterator iter = new DobbeltLenketListeIterator();
        return iter;

    }

    public Iterator<T> iterator(int indeks) {
        indeksKontroll(indeks, false);
        DobbeltLenketListeIterator iter2 = new DobbeltLenketListeIterator(indeks);
        return iter2;
    }

    private void fratilKontroll(int antall, int fra, int til) {                     //Fra til kontroll
        if (fra < 0)                                                                //Dersom fra er negativ
            throw new IndexOutOfBoundsException("fra(" + fra + ") er negativ!");    //kast unntak

        if (til > antall())                                                         //Dersom til er utenfor tabellen
            throw new IndexOutOfBoundsException("til(" + til + ") > antall(" + antall + ")"); //kast unntak

        if (fra > til)                                                               //Dersom fra er støre enn til
            throw new IllegalArgumentException("fra(" + fra + ") > til(" + til + ") - illegalt intervall!"); //Kast unntak
    }


    private class DobbeltLenketListeIterator implements Iterator<T> {
        private Node<T> denne;
        private boolean fjernOK;
        private int iteratorendringer;

        private DobbeltLenketListeIterator() {
            denne = hode;     // denne starter på den første i listen
            fjernOK = false;  // blir sann når next() kalles
            iteratorendringer = endringer;  // teller endringer
        }

        private DobbeltLenketListeIterator(int indeks) {
            for (int i = 0; i < indeks; i++) {
                denne = denne.neste;
            }


        }

        @Override
        public boolean hasNext() {
            return denne != null;  // denne koden skal ikke endres!     }
        }


        @Override
        public T next() {
            if (iteratorendringer != endringer)
                throw new ConcurrentModificationException("De er ikke like");

            if (!hasNext()) {
                fjernOK = true;
                throw new NoSuchElementException("Ikke flere noder igjen");
            }
            T verdi = denne.verdi;
            denne = denne.neste;
            return verdi;
        }

        //oppg 9
        @Override
        public void remove() {
            //Hvis det ikke er tillatt aa kalle paa denne metoden

            fjernOK = false;

            if (fjernOK == true) {
                throw new IllegalStateException(
                        "Det er ikke tillat aa kalle denne metoden!");
            }

            //Hvis endringer og iteratorendringer er forskjellige
            if (endringer != iteratorendringer) {
                throw new ConcurrentModificationException(
                        "Endringer og iteratorendringer er forskjellige!");

            }
            if(antall == 0){
                throw new IllegalStateException("Kan ikke fjerne fra tom liste!");
            }

            //Sjekker om første node skal fjernes
            if(antall == 1) {

                hode = null;
                hale = null;

            }
            //Sjekker om siste node skal fjernes.
           else if(denne == null) {

                hale = hale.forrige;
                hale.neste = null;
            }
            //Sjekker om første node skal fjernes
            else if(denne.forrige == hode) {

                hode = hode.neste;
                hode.forrige = null;
            }
            else {
                Node<T> q = denne.forrige;  //q skal fjernes
                Node<T> p = q.forrige;      //p er noden før q
                Node<T> r = denne;          //r er noden etter q, altså denne

                p.neste = r;                // Endrer pekerene
                r.forrige = p;

                //q har ikke lenger noen pekere.
            }
            antall--;
            endringer++;
            iteratorendringer++;
        }

    }
 }// DobbeltLenketListeIterator
package ch.epfl.tchu.game;

import ch.epfl.tchu.bot.Graph;
import ch.epfl.tchu.bot.Node;
import ch.epfl.tchu.game.Route.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ChMap {
    private ChMap() {
    }

    public static List<Station> stations() {
        return ALL_STATIONS;
    }

    public static List<Route> routes() {
        return ALL_ROUTES;
    }

    public static List<Node> nodes() {
        return ALL_NODES;
    }

    public static List<Ticket> tickets() {
        return ALL_TICKETS;
    }

    // Stations - cities
    private static final Station BAD = new Station(0, "Baden");
    private static final Station BAL = new Station(1, "Bâle");
    private static final Station BEL = new Station(2, "Bellinzone");
    private static final Station BER = new Station(3, "Berne");
    private static final Station BRI = new Station(4, "Brigue");
    private static final Station BRU = new Station(5, "Brusio");
    private static final Station COI = new Station(6, "Coire");
    private static final Station DAV = new Station(7, "Davos");
    private static final Station DEL = new Station(8, "Delémont");
    private static final Station FRI = new Station(9, "Fribourg");
    private static final Station GEN = new Station(10, "Genève");
    private static final Station INT = new Station(11, "Interlaken");
    private static final Station KRE = new Station(12, "Kreuzlingen");
    private static final Station LAU = new Station(13, "Lausanne");
    private static final Station LCF = new Station(14, "La Chaux-de-Fonds");
    private static final Station LOC = new Station(15, "Locarno");
    private static final Station LUC = new Station(16, "Lucerne");
    private static final Station LUG = new Station(17, "Lugano");
    private static final Station MAR = new Station(18, "Martigny");
    private static final Station NEU = new Station(19, "Neuchâtel");
    private static final Station OLT = new Station(20, "Olten");
    private static final Station PFA = new Station(21, "Pfäffikon");
    private static final Station SAR = new Station(22, "Sargans");
    private static final Station SCE = new Station(23, "Schaffhouse");
    private static final Station SCZ = new Station(24, "Schwyz");
    private static final Station SIO = new Station(25, "Sion");
    private static final Station SOL = new Station(26, "Soleure");
    private static final Station STG = new Station(27, "Saint-Gall");
    private static final Station VAD = new Station(28, "Vaduz");
    private static final Station WAS = new Station(29, "Wassen");
    private static final Station WIN = new Station(30, "Winterthour");
    private static final Station YVE = new Station(31, "Yverdon");
    private static final Station ZOU = new Station(32, "Zoug");
    private static final Station ZUR = new Station(33, "Zürich");

    // Stations - countries
    private static final Station DE1 = new Station(34, "Allemagne");
    private static final Station DE2 = new Station(35, "Allemagne");
    private static final Station DE3 = new Station(36, "Allemagne");
    private static final Station DE4 = new Station(37, "Allemagne");
    private static final Station DE5 = new Station(38, "Allemagne");
    private static final Station AT1 = new Station(39, "Autriche");
    private static final Station AT2 = new Station(40, "Autriche");
    private static final Station AT3 = new Station(41, "Autriche");
    private static final Station IT1 = new Station(42, "Italie");
    private static final Station IT2 = new Station(43, "Italie");
    private static final Station IT3 = new Station(44, "Italie");
    private static final Station IT4 = new Station(45, "Italie");
    private static final Station IT5 = new Station(46, "Italie");
    private static final Station FR1 = new Station(47, "France");
    private static final Station FR2 = new Station(48, "France");
    private static final Station FR3 = new Station(49, "France");
    private static final Station FR4 = new Station(50, "France");


    // Countries
    private static final List<Station> DE = List.of(DE1, DE2, DE3, DE4, DE5);
    private static final List<Station> AT = List.of(AT1, AT2, AT3);
    private static final List<Station> IT = List.of(IT1, IT2, IT3, IT4, IT5);
    private static final List<Station> FR = List.of(FR1, FR2, FR3, FR4);

    private static final List<Station> ALL_STATIONS = List.of(
            BAD, BAL, BEL, BER, BRI, BRU, COI, DAV, DEL, FRI, GEN, INT, KRE, LAU, LCF, LOC, LUC,
            LUG, MAR, NEU, OLT, PFA, SAR, SCE, SCZ, SIO, SOL, STG, VAD, WAS, WIN, YVE, ZOU, ZUR,
            DE1, DE2, DE3, DE4, DE5, AT1, AT2, AT3, IT1, IT2, IT3, IT4, IT5, FR1, FR2, FR3, FR4);

    // Routes
    private static final List<Route> ALL_ROUTES = List.of(
            new Route("AT1_STG_1", AT1, STG, 4, Level.UNDERGROUND, null),
            new Route("AT2_VAD_1", AT2, VAD, 1, Level.UNDERGROUND, Color.RED),
            new Route("BAD_BAL_1", BAD, BAL, 3, Level.UNDERGROUND, Color.RED),
            new Route("BAD_OLT_1", BAD, OLT, 2, Level.OVERGROUND, Color.VIOLET),
            new Route("BAD_ZUR_1", BAD, ZUR, 1, Level.OVERGROUND, Color.YELLOW),
            new Route("BAL_DE1_1", BAL, DE1, 1, Level.UNDERGROUND, Color.BLUE),
            new Route("BAL_DEL_1", BAL, DEL, 2, Level.UNDERGROUND, Color.YELLOW),
            new Route("BAL_OLT_1", BAL, OLT, 2, Level.UNDERGROUND, Color.ORANGE),
            new Route("BEL_LOC_1", BEL, LOC, 1, Level.UNDERGROUND, Color.BLACK),
            new Route("BEL_LUG_1", BEL, LUG, 1, Level.UNDERGROUND, Color.RED),
            new Route("BEL_LUG_2", BEL, LUG, 1, Level.UNDERGROUND, Color.YELLOW),
            new Route("BEL_WAS_1", BEL, WAS, 4, Level.UNDERGROUND, null),
            new Route("BEL_WAS_2", BEL, WAS, 4, Level.UNDERGROUND, null),
            new Route("BER_FRI_1", BER, FRI, 1, Level.OVERGROUND, Color.ORANGE),
            new Route("BER_FRI_2", BER, FRI, 1, Level.OVERGROUND, Color.YELLOW),
            new Route("BER_INT_1", BER, INT, 3, Level.OVERGROUND, Color.BLUE),
            new Route("BER_LUC_1", BER, LUC, 4, Level.OVERGROUND, null),
            new Route("BER_LUC_2", BER, LUC, 4, Level.OVERGROUND, null),
            new Route("BER_NEU_1", BER, NEU, 2, Level.OVERGROUND, Color.RED),
            new Route("BER_SOL_1", BER, SOL, 2, Level.OVERGROUND, Color.BLACK),
            new Route("BRI_INT_1", BRI, INT, 2, Level.UNDERGROUND, Color.WHITE),
            new Route("BRI_IT5_1", BRI, IT5, 3, Level.UNDERGROUND, Color.GREEN),
            new Route("BRI_LOC_1", BRI, LOC, 6, Level.UNDERGROUND, null),
            new Route("BRI_SIO_1", BRI, SIO, 3, Level.UNDERGROUND, Color.BLACK),
            new Route("BRI_WAS_1", BRI, WAS, 4, Level.UNDERGROUND, Color.RED),
            new Route("BRU_COI_1", BRU, COI, 5, Level.UNDERGROUND, null),
            new Route("BRU_DAV_1", BRU, DAV, 4, Level.UNDERGROUND, Color.BLUE),
            new Route("BRU_IT2_1", BRU, IT2, 2, Level.UNDERGROUND, Color.GREEN),
            new Route("COI_DAV_1", COI, DAV, 2, Level.UNDERGROUND, Color.VIOLET),
            new Route("COI_SAR_1", COI, SAR, 1, Level.UNDERGROUND, Color.WHITE),
            new Route("COI_WAS_1", COI, WAS, 5, Level.UNDERGROUND, null),
            new Route("DAV_AT3_1", DAV, AT3, 3, Level.UNDERGROUND, null),
            new Route("DAV_IT1_1", DAV, IT1, 3, Level.UNDERGROUND, null),
            new Route("DAV_SAR_1", DAV, SAR, 3, Level.UNDERGROUND, Color.BLACK),
            new Route("DE2_SCE_1", DE2, SCE, 1, Level.OVERGROUND, Color.YELLOW),
            new Route("DE3_KRE_1", DE3, KRE, 1, Level.OVERGROUND, Color.ORANGE),
            new Route("DE4_KRE_1", DE4, KRE, 1, Level.OVERGROUND, Color.WHITE),
            new Route("DE5_STG_1", DE5, STG, 2, Level.OVERGROUND, null),
            new Route("DEL_FR4_1", DEL, FR4, 2, Level.UNDERGROUND, Color.BLACK),
            new Route("DEL_LCF_1", DEL, LCF, 3, Level.UNDERGROUND, Color.WHITE),
            new Route("DEL_SOL_1", DEL, SOL, 1, Level.UNDERGROUND, Color.VIOLET),
            new Route("FR1_MAR_1", FR1, MAR, 2, Level.UNDERGROUND, null),
            new Route("FR2_GEN_1", FR2, GEN, 1, Level.OVERGROUND, Color.YELLOW),
            new Route("FR3_LCF_1", FR3, LCF, 2, Level.UNDERGROUND, Color.GREEN),
            new Route("FRI_LAU_1", FRI, LAU, 3, Level.OVERGROUND, Color.RED),
            new Route("FRI_LAU_2", FRI, LAU, 3, Level.OVERGROUND, Color.VIOLET),
            new Route("GEN_LAU_1", GEN, LAU, 4, Level.OVERGROUND, Color.BLUE),
            new Route("GEN_LAU_2", GEN, LAU, 4, Level.OVERGROUND, Color.WHITE),
            new Route("GEN_YVE_1", GEN, YVE, 6, Level.OVERGROUND, null),
            new Route("INT_LUC_1", INT, LUC, 4, Level.OVERGROUND, Color.VIOLET),
            new Route("IT3_LUG_1", IT3, LUG, 2, Level.UNDERGROUND, Color.WHITE),
            new Route("IT4_LOC_1", IT4, LOC, 2, Level.UNDERGROUND, Color.ORANGE),
            new Route("KRE_SCE_1", KRE, SCE, 3, Level.OVERGROUND, Color.VIOLET),
            new Route("KRE_STG_1", KRE, STG, 1, Level.OVERGROUND, Color.GREEN),
            new Route("KRE_WIN_1", KRE, WIN, 2, Level.OVERGROUND, Color.YELLOW),
            new Route("LAU_MAR_1", LAU, MAR, 4, Level.UNDERGROUND, Color.ORANGE),
            new Route("LAU_NEU_1", LAU, NEU, 4, Level.OVERGROUND, null),
            new Route("LCF_NEU_1", LCF, NEU, 1, Level.UNDERGROUND, Color.ORANGE),
            new Route("LCF_YVE_1", LCF, YVE, 3, Level.UNDERGROUND, Color.YELLOW),
            new Route("LOC_LUG_1", LOC, LUG, 1, Level.UNDERGROUND, Color.VIOLET),
            new Route("LUC_OLT_1", LUC, OLT, 3, Level.OVERGROUND, Color.GREEN),
            new Route("LUC_SCZ_1", LUC, SCZ, 1, Level.OVERGROUND, Color.BLUE),
            new Route("LUC_ZOU_1", LUC, ZOU, 1, Level.OVERGROUND, Color.ORANGE),
            new Route("LUC_ZOU_2", LUC, ZOU, 1, Level.OVERGROUND, Color.YELLOW),
            new Route("MAR_SIO_1", MAR, SIO, 2, Level.UNDERGROUND, Color.GREEN),
            new Route("NEU_SOL_1", NEU, SOL, 4, Level.OVERGROUND, Color.GREEN),
            new Route("NEU_YVE_1", NEU, YVE, 2, Level.OVERGROUND, Color.BLACK),
            new Route("OLT_SOL_1", OLT, SOL, 1, Level.OVERGROUND, Color.BLUE),
            new Route("OLT_ZUR_1", OLT, ZUR, 3, Level.OVERGROUND, Color.WHITE),
            new Route("PFA_SAR_1", PFA, SAR, 3, Level.UNDERGROUND, Color.YELLOW),
            new Route("PFA_SCZ_1", PFA, SCZ, 1, Level.OVERGROUND, Color.VIOLET),
            new Route("PFA_STG_1", PFA, STG, 3, Level.OVERGROUND, Color.ORANGE),
            new Route("PFA_ZUR_1", PFA, ZUR, 2, Level.OVERGROUND, Color.BLUE),
            new Route("SAR_VAD_1", SAR, VAD, 1, Level.UNDERGROUND, Color.ORANGE),
            new Route("SCE_WIN_1", SCE, WIN, 1, Level.OVERGROUND, Color.BLACK),
            new Route("SCE_WIN_2", SCE, WIN, 1, Level.OVERGROUND, Color.WHITE),
            new Route("SCE_ZUR_1", SCE, ZUR, 3, Level.OVERGROUND, Color.ORANGE),
            new Route("SCZ_WAS_1", SCZ, WAS, 2, Level.UNDERGROUND, Color.GREEN),
            new Route("SCZ_WAS_2", SCZ, WAS, 2, Level.UNDERGROUND, Color.YELLOW),
            new Route("SCZ_ZOU_1", SCZ, ZOU, 1, Level.OVERGROUND, Color.BLACK),
            new Route("SCZ_ZOU_2", SCZ, ZOU, 1, Level.OVERGROUND, Color.WHITE),
            new Route("STG_VAD_1", STG, VAD, 2, Level.UNDERGROUND, Color.BLUE),
            new Route("STG_WIN_1", STG, WIN, 3, Level.OVERGROUND, Color.RED),
            new Route("STG_ZUR_1", STG, ZUR, 4, Level.OVERGROUND, Color.BLACK),
            new Route("WIN_ZUR_1", WIN, ZUR, 1, Level.OVERGROUND, Color.BLUE),
            new Route("WIN_ZUR_2", WIN, ZUR, 1, Level.OVERGROUND, Color.VIOLET),
            new Route("ZOU_ZUR_1", ZOU, ZUR, 1, Level.OVERGROUND, Color.GREEN),
            new Route("ZOU_ZUR_2", ZOU, ZUR, 1, Level.OVERGROUND, Color.RED));

    // Tickets
    private static final Ticket deToNeighbors = ticketToNeighbors(DE, 0, 5, 13, 5);
    private static final Ticket atToNeighbors = ticketToNeighbors(AT, 5, 0, 6, 14);
    private static final Ticket itToNeighbors = ticketToNeighbors(IT, 13, 6, 0, 11);
    private static final Ticket frToNeighbors = ticketToNeighbors(FR, 5, 14, 11, 0);

    private static final List<Ticket> ALL_TICKETS = List.of(
            // City-to-city tickets
            new Ticket(BAL, BER, 5),
            new Ticket(BAL, BRI, 10),
            new Ticket(BAL, STG, 8),
            new Ticket(BER, COI, 10),
            new Ticket(BER, LUG, 12),
            new Ticket(BER, SCZ, 5),
            new Ticket(BER, ZUR, 6),
            new Ticket(FRI, LUC, 5),
            new Ticket(GEN, BAL, 13),
            new Ticket(GEN, BER, 8),
            new Ticket(GEN, SIO, 10),
            new Ticket(GEN, ZUR, 14),
            new Ticket(INT, WIN, 7),
            new Ticket(KRE, ZUR, 3),
            new Ticket(LAU, INT, 7),
            new Ticket(LAU, LUC, 8),
            new Ticket(LAU, STG, 13),
            new Ticket(LCF, BER, 3),
            new Ticket(LCF, LUC, 7),
            new Ticket(LCF, ZUR, 8),
            new Ticket(LUC, VAD, 6),
            new Ticket(LUC, ZUR, 2),
            new Ticket(LUG, COI, 10),
            new Ticket(NEU, WIN, 9),
            new Ticket(OLT, SCE, 5),
            new Ticket(SCE, MAR, 15),
            new Ticket(SCE, STG, 4),
            new Ticket(SCE, ZOU, 3),
            new Ticket(STG, BRU, 9),
            new Ticket(WIN, SCZ, 3),
            new Ticket(ZUR, BAL, 4),
            new Ticket(ZUR, BRU, 11),
            new Ticket(ZUR, LUG, 9),
            new Ticket(ZUR, VAD, 6),

            // City to country tickets
            ticketToNeighbors(List.of(BER), 6, 11, 8, 5),
            ticketToNeighbors(List.of(COI), 6, 3, 5, 12),
            ticketToNeighbors(List.of(LUG), 12, 13, 2, 14),
            ticketToNeighbors(List.of(ZUR), 3, 7, 11, 7),

            // Country to country tickets (two of each)
            deToNeighbors, deToNeighbors,
            atToNeighbors, atToNeighbors,
            itToNeighbors, itToNeighbors,
            frToNeighbors, frToNeighbors);

    private static Ticket ticketToNeighbors(List<Station> from, int de, int at, int it, int fr) {
        var trips = new ArrayList<Trip>();
        if (de != 0) trips.addAll(Trip.all(from, DE, de));
        if (at != 0) trips.addAll(Trip.all(from, AT, at));
        if (it != 0) trips.addAll(Trip.all(from, IT, it));
        if (fr != 0) trips.addAll(Trip.all(from, FR, fr));
        return new Ticket(trips);
    }

    private static final Node BAD_NODE = new Node(BAD);
    private static final Node BAL_NODE = new Node(BAL);
    private static final Node BEL_NODE = new Node(BEL);
    private static final Node BER_NODE = new Node(BER);
    private static final Node BRI_NODE = new Node(BRI);
    private static final Node BRU_NODE = new Node(BRU);
    private static final Node COI_NODE = new Node(COI);
    private static final Node DAV_NODE = new Node(DAV);
    private static final Node DEL_NODE = new Node(DEL);
    private static final Node FRI_NODE = new Node(FRI);
    private static final Node GEN_NODE = new Node(GEN);
    private static final Node INT_NODE = new Node(INT);
    private static final Node KRE_NODE = new Node(KRE);
    private static final Node LAU_NODE = new Node(LAU);
    private static final Node LCF_NODE = new Node(LCF);
    private static final Node LOC_NODE = new Node(LOC);
    private static final Node LUC_NODE = new Node(LUC);
    private static final Node LUG_NODE = new Node(LUG);
    private static final Node MAR_NODE = new Node(MAR);
    private static final Node NEU_NODE = new Node(NEU);
    private static final Node OLT_NODE = new Node(OLT);
    private static final Node PFA_NODE = new Node(PFA);
    private static final Node SAR_NODE = new Node(SAR);
    private static final Node SCE_NODE = new Node(SCE);
    private static final Node SCZ_NODE = new Node(SCZ);
    private static final Node SIO_NODE = new Node(SIO);
    private static final Node SOL_NODE = new Node(SOL);
    private static final Node STG_NODE = new Node(STG);
    private static final Node VAD_NODE = new Node(VAD);
    private static final Node WAS_NODE = new Node(WAS);
    private static final Node WIN_NODE = new Node(WIN);
    private static final Node YVE_NODE = new Node(YVE);
    private static final Node ZOU_NODE = new Node(ZOU);
    private static final Node ZUR_NODE = new Node(ZUR);

    // Stations - countries
    private static final Node DE1_NODE = new Node(DE1);
    private static final Node DE2_NODE = new Node(DE2);
    private static final Node DE3_NODE = new Node(DE3);
    private static final Node DE4_NODE = new Node(DE4);
    private static final Node DE5_NODE = new Node(DE5);
    private static final Node AT1_NODE = new Node(AT1);
    private static final Node AT2_NODE = new Node(AT2);
    private static final Node AT3_NODE = new Node(AT3);
    private static final Node IT1_NODE = new Node(IT1);
    private static final Node IT2_NODE = new Node(IT2);
    private static final Node IT3_NODE = new Node(IT3);
    private static final Node IT4_NODE = new Node(IT4);
    private static final Node IT5_NODE = new Node(IT5);
    private static final Node FR1_NODE = new Node(FR1);
    private static final Node FR2_NODE = new Node(FR2);
    private static final Node FR3_NODE = new Node(FR3);
    private static final Node FR4_NODE = new Node(FR4);

    //Nodes
    private static final List<Node> ALL_NODES = List.of(
            BAD_NODE, BAL_NODE, BEL_NODE, BER_NODE, BRI_NODE, BRU_NODE, COI_NODE, DAV_NODE, DEL_NODE, FRI_NODE, GEN_NODE, INT_NODE, KRE_NODE, LAU_NODE, LCF_NODE, LOC_NODE, LUC_NODE,
            LUG_NODE, MAR_NODE, NEU_NODE, OLT_NODE, PFA_NODE, SAR_NODE, SCE_NODE, SCZ_NODE, SIO_NODE, SOL_NODE, STG_NODE, VAD_NODE, WAS_NODE, WIN_NODE, YVE_NODE, ZOU_NODE, ZUR_NODE,
            DE1_NODE, DE2_NODE, DE3_NODE, DE4_NODE, DE5_NODE, AT1_NODE, AT2_NODE, AT3_NODE, IT1_NODE, IT2_NODE, IT3_NODE, IT4_NODE, IT5_NODE, FR1_NODE, FR2_NODE, FR3_NODE, FR4_NODE);

    private static Map<Station, Node> MAPPED_NODES = new HashMap<Station, Node>() {{
        this.put(BAD, BAD_NODE);
        this.put(BAL, BAL_NODE);
        this.put(BEL, BEL_NODE);
        this.put(BER, BER_NODE);
        this.put(BRI, BRI_NODE);
        this.put(BRU, BRU_NODE);
        this.put(COI, COI_NODE);
        this.put(DAV, DAV_NODE);
        this.put(DEL, DEL_NODE);
        this.put(FRI, FRI_NODE);
        this.put(GEN, GEN_NODE);
        this.put(INT, INT_NODE);
        this.put(KRE, KRE_NODE);
        this.put(LAU, LAU_NODE);
        this.put(LCF, LCF_NODE);
        this.put(LOC, LOC_NODE);
        this.put(LUC, LUC_NODE);
        this.put(LUG, LUG_NODE);
        this.put(MAR, MAR_NODE);
        this.put(NEU, NEU_NODE);
        this.put(OLT, OLT_NODE);
        this.put(PFA, PFA_NODE);
        this.put(SAR, SAR_NODE);
        this.put(SCE, SCE_NODE);
        this.put(SCZ, SCZ_NODE);
        this.put(SIO, SIO_NODE);
        this.put(SOL, SOL_NODE);
        this.put(STG, STG_NODE);
        this.put(VAD, VAD_NODE);
        this.put(WAS, WAS_NODE);
        this.put(WIN, WIN_NODE);
        this.put(YVE, YVE_NODE);
        this.put(ZOU, ZOU_NODE);
        this.put(ZUR, ZUR_NODE);
        this.put(DE1, DE1_NODE);
        this.put(DE2, DE2_NODE);
        this.put(DE3, DE3_NODE);
        this.put(DE4, DE4_NODE);
        this.put(DE5, DE5_NODE);
        this.put(AT1, AT1_NODE);
        this.put(AT2, AT2_NODE);
        this.put(AT3, AT3_NODE);
        this.put(IT1, IT1_NODE);
        this.put(IT2, IT2_NODE);
        this.put(IT3, IT3_NODE);
        this.put(IT4, IT4_NODE);
        this.put(IT5, IT5_NODE);
        this.put(FR1, FR1_NODE);
        this.put(FR2, FR2_NODE);
        this.put(FR3, FR3_NODE);
        this.put(FR4, FR4_NODE);
    }};

    public static Map<Station, Node> mappedNodes() {
        return MAPPED_NODES;
    }

    public static boolean isCountry(Node node) {
        return (DE.contains(node.station()) || AT.contains(node.station()) || IT.contains(node.station()) || FR.contains(node.station()));
    }

    public static List<Station> stationsGermany() {
        return DE;
    }

    public static List<Station> stationsFrance() {
        return FR;
    }

    public static List<Station> stationsAustria() {
        return AT;
    }

    public static List<Station> stationsItaly() {
        return IT;
    }
}


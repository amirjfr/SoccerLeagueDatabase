package ui;

import delegates.OperationTabDelegate;
import enums.AttributeType;
import enums.QueryType;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SoccerGUI extends JFrame {
    private final JTabbedPane tabbedPane;
    private final JPanel mainPanel;

    // Constants
    private static final Color MLS_BLUE = new Color(0, 31, 91);
    private static final Color MLS_WHITE = new Color(255, 255, 255);
    private static final Color MLS_RED = new Color(223, 35, 26);
    private static final int GUI_WIDTH = 1000;
    private static final int GUI_HEIGHT = 600;
    private OperationTabDelegate delegate = null;

    public SoccerGUI(String title, OperationTabDelegate delegate) {
        super(title);

        this.delegate = delegate;
        mainPanel = new JPanel();
        mainPanel.setBackground(MLS_RED);
        mainPanel.setLayout(new GridLayout(1, 1, -1, -1));
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(MLS_WHITE);
        mainPanel.add(tabbedPane);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
        this.setSize(GUI_WIDTH, GUI_HEIGHT);
        this.setLocationRelativeTo(null);

        addStatsTab();
        addInsertTab();
        addDeleteTab();
        addUpdateTab();
    }

    private void addInsertTab() {
        List<CustomQuery> insertQueries = new ArrayList<>();

        insertQueries.add(new CustomQuery("Register nationality",
                new String[]{"Nationality", "Federation"},
                new AttributeType[]{AttributeType.VARCHAR, AttributeType.VARCHAR},
                "INSERT INTO \"Nationality_Federation\" VALUES (?, ?)",
                QueryType.INSERT));

        insertQueries.add(new CustomQuery("Register a team member",
                new String[]{"FIFA Connect ID", "Name", "Address", "Birth Date", "Phone Number", "Nationality"},
                new AttributeType[]{AttributeType.VARCHAR, AttributeType.VARCHAR, AttributeType.VARCHAR, AttributeType.DATE, AttributeType.INT, AttributeType.VARCHAR},
                "INSERT INTO \"TeamMember\" VALUES (?, ?, ?, ?, ?, ?)",
                QueryType.INSERT));

        insertQueries.add(new CustomQuery("Register a team owner",
                new String[]{"Owner ID", "Name", "Team Name", "Birth Date", "Team Purchase Date"},
                new AttributeType[]{AttributeType.VARCHAR, AttributeType.VARCHAR, AttributeType.VARCHAR, AttributeType.DATE, AttributeType.DATE},
                "INSERT INTO \"TeamOwner\" VALUES (?, ?, ?, ?, ?)",
                QueryType.INSERT));

        insertQueries.add(new CustomQuery("Register a team",
                new String[]{"Team Name", "Home Jersey Colours", "Away Jersey Colours", "Inauguration Date", "Division"},
                new AttributeType[]{AttributeType.VARCHAR, AttributeType.VARCHAR, AttributeType.VARCHAR, AttributeType.DATE, AttributeType.VARCHAR},
                "INSERT INTO \"Team\" VALUES (?, ?, ?, ?, ?)",
                QueryType.INSERT));

        insertQueries.add(new CustomQuery("Add new match details",
                new String[]{"Match ID", "Home Score", "Away Score", "Match Date", "Home Team Name", "Away Team Name", "Arena Address"},
                new AttributeType[]{AttributeType.VARCHAR, AttributeType.INT, AttributeType.INT, AttributeType.DATE, AttributeType.VARCHAR, AttributeType.VARCHAR, AttributeType.VARCHAR},
                "INSERT INTO \"Match_Play\" VALUES (?, ?, ?, ?, ?, ?, ?)",
                QueryType.INSERT));

        insertQueries.add(new CustomQuery("Register a referee",
                new String[]{"Referee ID", "Referee Name", "Referee Address"},
                new AttributeType[]{AttributeType.VARCHAR, AttributeType.VARCHAR, AttributeType.VARCHAR},
                "INSERT INTO \"Referee\" VALUES (?, ?, ?)",
                QueryType.INSERT));

        insertQueries.add(new CustomQuery("Register an arena",
                new String[]{"Arena Address", "Number of Seats", "Arena Name", "Construction Date"},
                new AttributeType[]{AttributeType.VARCHAR, AttributeType.INT, AttributeType.VARCHAR, AttributeType.DATE},
                "INSERT INTO \"Arena\" VALUES (?, ?, ?, ?)",
                QueryType.INSERT));

        OperationTab insertTab = new OperationTab(insertQueries);
        insertTab.setDelegate(delegate);
        tabbedPane.add("ADD MLS RECORDS", insertTab);
        tabbedPane.revalidate();
    }

    private void addDeleteTab() {
        List<CustomQuery> deleteQueries = new ArrayList<>();

        deleteQueries.add(new CustomQuery("Remove a team member",
                new String[]{"FIFA Connect ID"},
                new AttributeType[]{AttributeType.VARCHAR},
                "DELETE FROM \"TeamMember\" WHERE \"FIFAConnectId\" = (?)",
                QueryType.DELETE));

        deleteQueries.add(new CustomQuery("Remove a referee",
                new String[]{"Referee ID"},
                new AttributeType[]{AttributeType.VARCHAR},
                "DELETE FROM \"Referee\" WHERE \"refereeId\" = (?)",
                QueryType.DELETE));

        deleteQueries.add(new CustomQuery("Delete a match record",
                new String[]{"Match ID"},
                new AttributeType[]{AttributeType.VARCHAR},
                "DELETE FROM \"Match_Play\" WHERE \"matchId\" = (?)",
                QueryType.DELETE));

        deleteQueries.add(new CustomQuery("Remove a team owner",
                new String[]{"Owner ID"},
                new AttributeType[]{AttributeType.VARCHAR},
                "DELETE FROM \"TeamOwner\" WHERE \"ownerId\" = (?)",
                QueryType.DELETE));

        deleteQueries.add(new CustomQuery("Remove a team",
                new String[]{"Team Name"},
                new AttributeType[]{AttributeType.VARCHAR},
                "DELETE FROM \"Team\" WHERE \"name\" = (?)",
                QueryType.DELETE));

        OperationTab deleteTab = new OperationTab(deleteQueries);
        deleteTab.setDelegate(delegate);
        tabbedPane.add("REMOVE MLS RECORDS", deleteTab);
        tabbedPane.revalidate();
    }

    private void addUpdateTab() {
        List<CustomQuery> updateQueries = new ArrayList<>();
        String[] uiArguments;
        String[] orderedArguments;
        String sqlQuery;
        AttributeType[] orderedAttributeTypes;

        uiArguments = new String[]{"FIFA Connect ID", "Name", "Address", "Phone Number"};
        orderedArguments = new String[]{"Name", "Address", "Phone Number", "FIFA Connect ID"};
        orderedAttributeTypes = new AttributeType[]{AttributeType.VARCHAR, AttributeType.VARCHAR, AttributeType.VARCHAR, AttributeType.VARCHAR};
        sqlQuery = "UPDATE \"TeamMember\" SET \"name\" = ?, \"address\" = ?, \"phoneNumber\" = ? WHERE \"FIFAConnectId\" = ?";
        updateQueries.add(new CustomQuery("Modify team member information", uiArguments, orderedArguments, orderedAttributeTypes, sqlQuery, QueryType.UPDATE));

        uiArguments = new String[]{"Owner ID", "Team Name"};
        orderedArguments = new String[]{"Team Name", "Owner ID"};
        orderedAttributeTypes = new AttributeType[]{AttributeType.VARCHAR, AttributeType.VARCHAR};
        sqlQuery = "UPDATE \"TeamOwner\" SET \"teamName\" = ? WHERE \"ownerId\" = ?";
        updateQueries.add(new CustomQuery("Modify team owner information", uiArguments, orderedArguments, orderedAttributeTypes, sqlQuery, QueryType.UPDATE));

        uiArguments = new String[]{"Referee ID", "Referee Name", "Referee Address"};
        orderedArguments = new String[]{"Referee Name", "Referee Address", "Referee ID"};
        orderedAttributeTypes = new AttributeType[]{AttributeType.VARCHAR, AttributeType.VARCHAR, AttributeType.VARCHAR};
        sqlQuery = "UPDATE \"Referee\" SET \"name\" = ?, \"address\" = ? WHERE \"refereeId\" = ?";
        updateQueries.add(new CustomQuery("Modify referee information", uiArguments, orderedArguments, orderedAttributeTypes, sqlQuery, QueryType.UPDATE));

        uiArguments = new String[]{"Match ID", "Match Date"};
        orderedArguments = new String[]{"Match Date", "Match ID"};
        orderedAttributeTypes = new AttributeType[]{AttributeType.TIMESTAMP, AttributeType.VARCHAR};
        sqlQuery = "UPDATE \"Match_Play\" SET \"matchDateTime\" = ? WHERE \"matchId\" = ?";
        updateQueries.add(new CustomQuery("Update match record", uiArguments, orderedArguments, orderedAttributeTypes, sqlQuery, QueryType.UPDATE));

        uiArguments = new String[]{"Team Name", "Age Group", "Table Position"};
        orderedArguments = new String[]{"Table Position", "Team Name", "Age Group"};
        orderedAttributeTypes = new AttributeType[]{AttributeType.INT, AttributeType.VARCHAR, AttributeType.VARCHAR};
        sqlQuery = "UPDATE \"AcademyTeam_Has_Team\" SET \"tablePosition\" = ? WHERE \"teamName\" = ? AND \"ageGroup\" = ?";
        updateQueries.add(new CustomQuery("Update academy team table position", uiArguments, orderedArguments, orderedAttributeTypes, sqlQuery, QueryType.UPDATE));

        uiArguments = new String[]{"FIFA Connect ID", "Team Name", "Start Date", "Shirt Number"};
        orderedArguments = new String[]{"Team Name", "Start Date", "Shirt Number", "FIFA Connect ID"};
        orderedAttributeTypes = new AttributeType[]{AttributeType.VARCHAR, AttributeType.DATE, AttributeType.INT, AttributeType.VARCHAR};
        sqlQuery = "UPDATE \"Player_PlayFor\" SET \"teamName\" = ?, \"startDate\" = ?, \"shirtNumber\" = ? WHERE \"FIFAConnectId\" = ?";
        updateQueries.add(new CustomQuery("Update Players' team records", uiArguments, orderedArguments, orderedAttributeTypes, sqlQuery, QueryType.UPDATE));

        OperationTab updateTab = new OperationTab(updateQueries);
        updateTab.setDelegate(delegate);
        tabbedPane.add("MODIFY EXISTING MLS RECORDS", updateTab);
        tabbedPane.revalidate();
    }

    private void addStatsTab() {
        List<CustomQuery> statsQueries = new ArrayList<>();

        addSelectionTab(statsQueries);
        addProjectionTab(statsQueries);
        addJoinTab(statsQueries);
        addGroupByTab(statsQueries);
        addHavingTab(statsQueries);
        addNestedGroupBy(statsQueries);
        addDivision(statsQueries);

        OperationTab selectionTab = new OperationTab(statsQueries);
        selectionTab.setDelegate(delegate);
        tabbedPane.add("GET MLS STATS", selectionTab);
        tabbedPane.revalidate();
    }

    private void addSelectionTab(List<CustomQuery> selectionQueries) {
        selectionQueries.add(new CustomQuery("List players under specified age",
                new String[]{"Age (upper limit)"},
                new AttributeType[]{AttributeType.INT},
                new String[]{"FIFA Connect ID", "Name", "Address", "Birth Date", "Phone Number", "Nationality", "Team Name"},
                "SELECT tm.*, pf.\"teamName\"\n" +
                        "FROM \"TeamMember\" tm, \"Player_PlayFor\" pf\n" +
                        "WHERE (CURRENT_DATE - \"birthDate\")/365.25 < ?\n" +
                        "AND tm.\"FIFAConnectId\" = pf.\"FIFAConnectId\"\n",
                QueryType.SELECTION));

        selectionQueries.add(new CustomQuery("List players that received a specific award",
                new String[]{"Award Name"},
                new AttributeType[]{AttributeType.VARCHAR},
                new String[]{"Name", "Award Name", "Award Year"},
                "SELECT DISTINCT t.\"name\", a.\"awardName\", a.\"awardYear\"\n" +
                        "FROM \"TeamMember\" t, \"TeamMember_Win_Award\" a, \"Player_PlayFor\" pf\n" +
                        "WHERE a.\"FIFAConnectId\" = t.\"FIFAConnectId\"\n" +
                        "AND a.\"awardName\" = ?\n" +
                        "AND t.\"FIFAConnectId\" = pf.\"FIFAConnectId\"",
                QueryType.SELECTION));

        selectionQueries.add(new CustomQuery("List players of a specific nationality",
                new String[]{"Nationality"},
                new AttributeType[]{AttributeType.VARCHAR},
                new String[]{"FIFA Connect ID", "Name", "Address", "Birth Date", "Phone Number", "Nationality"},
                "SELECT *\n" +
                        "FROM \"TeamMember\" t, \"Player_PlayFor\" pf\n" +
                        "WHERE \"nationality\" = ?\n" +
                        "AND t.\"FIFAConnectId\" = pf.\"FIFAConnectId\"",
                QueryType.SELECTION));

        selectionQueries.add(new CustomQuery("List players who play a specific position",
                new String[]{"Position"},
                new AttributeType[]{AttributeType.VARCHAR},
                new String[]{"FIFA Connect ID", "Name", "Address", "Birth Date", "Phone Number", "Nationality"},
                "SELECT *\n" +
                        "FROM \"TeamMember\" t, \"Player_PlayFor\" p\n" +
                        "WHERE t.\"FIFAConnectId\" = p.\"FIFAConnectId\" AND p.\"primaryPosition\" = ?",
                QueryType.SELECTION));

        selectionQueries.add(new CustomQuery("List owners of a specific arena",
                new String[]{"Arena Address"},
                new AttributeType[]{AttributeType.VARCHAR},
                new String[]{"Owner ID", "Name", "Team Name", "Birth Date", "Team Purchase Date"},
                "SELECT t.*\n" +
                        "FROM \"TeamOwner\" t, \"TeamOwner_Own_Arena\" a\n" +
                        "WHERE t.\"ownerId\" = a.\"teamOwnerId\" AND a.\"arenaAddress\" = ?",
                QueryType.SELECTION));

        selectionQueries.add(new CustomQuery("List awards distributed in a specific year",
                new String[]{"Award Year"},
                new AttributeType[]{AttributeType.INT},
                new String[]{"Name", "Year"},
                "SELECT * FROM \"Award\" WHERE \"year\" = ?",
                QueryType.SELECTION));
    }

    private void addProjectionTab(List<CustomQuery> projectionQueries) {
        projectionQueries.add(new CustomQuery("List player nationalities",
                new String[]{},
                new AttributeType[]{},
                new String[]{"Name", "Birth Date", "Nationality", "FIFA Federation"},
                "SELECT \"name\", \"birthDate\", tm.\"nationality\", \"FifaFederation\"\n" +
                        "FROM \"TeamMember\" tm, \"Nationality_Federation\" nf, \"Player_PlayFor\" ppf\n" +
                        "WHERE tm.\"nationality\" = nf.\"nationality\" AND ppf.\"FIFAConnectId\" = tm.\"FIFAConnectId\"\n" +
                        "ORDER BY \"birthDate\", tm.\"nationality\"",
                QueryType.PROJECTION));

        projectionQueries.add(new CustomQuery("List player positions",
                new String[]{},
                new AttributeType[]{},
                new String[]{"Name", "Team Name", "Shirt Number", "Primary Position", "Player Type"},
                "SELECT \"name\", \"teamName\", \"shirtNumber\", ppf.\"primaryPosition\", \"playerType\" " +
                        "FROM \"Player_PlayFor\" ppf, \"Position_Type\" pt, \"TeamMember\" tm " +
                        "WHERE ppf.\"primaryPosition\" = pt.\"primaryPosition\" AND tm.\"FIFAConnectId\" = ppf.\"FIFAConnectId\" " +
                        "ORDER BY \"teamName\", \"shirtNumber\"",
                QueryType.PROJECTION));
    }

    private void addJoinTab(List<CustomQuery> joinQueries) {
        joinQueries.add(new CustomQuery("List matches where players both scored and assisted with minimum X number of goals",
                new String[]{"Minimum Number of Goals"},
                new AttributeType[]{AttributeType.INT},
                new String[]{"Name", "Match ID", "Number of Goals", "Number of Assists"},
                "SELECT \"name\", \"Player_Score_Match\".\"matchId\", \"numberOfGoals\", \"numberOfAssists\"\n" +
                        "FROM \"Player_Score_Match\"\n" +
                        "INNER JOIN \"Player_Assist_Match\" ON \"Player_Score_Match\".\"FIFAConnectId\" = \"Player_Assist_Match\".\"FIFAConnectId\" AND \"Player_Score_Match\".\"matchId\" = \"Player_Assist_Match\".\"matchId\"\n" +
                        "INNER JOIN \"TeamMember\" ON \"Player_Score_Match\".\"FIFAConnectId\" = \"TeamMember\".\"FIFAConnectId\"\n" +
                        "WHERE \"numberOfGoals\" >= ?",
                QueryType.JOIN));
        joinQueries.add(new CustomQuery("List players' team and awards in year X",
                new String[]{"Award Year"},
                new AttributeType[]{AttributeType.INT},
                new String[]{"Name", "Team Name", "Award Name", "Award Year"},
                "SELECT \"name\", \"teamName\", \"awardName\", \"awardYear\"\n" +
                        "FROM \"Player_PlayFor\"\n" +
                        "INNER JOIN \"TeamMember_Win_Award\" ON \"Player_PlayFor\".\"FIFAConnectId\" = \"TeamMember_Win_Award\".\"FIFAConnectId\"\n" +
                        "INNER JOIN \"TeamMember\" ON \"Player_PlayFor\".\"FIFAConnectId\" = \"TeamMember\".\"FIFAConnectId\"\n" +
                        "WHERE \"awardYear\" = ?",
                QueryType.JOIN));
    }

    private void addGroupByTab(List<CustomQuery> groupByQueries) {
        groupByQueries.add(new CustomQuery("Count the total number of members across the different nationalities participating in MLS",
                new String[]{},
                new AttributeType[]{},
                new String[]{"Nationality", "Number of members"},
                "SELECT DISTINCT \"nationality\", COUNT(DISTINCT \"FIFAConnectId\") AS \"number of players\"\n" +
                        "FROM \"TeamMember\"\n" +
                        "GROUP BY \"nationality\"",
                QueryType.GROUPBY));

        groupByQueries.add(new CustomQuery("Count the number of player awards per team",
                new String[]{},
                new AttributeType[]{},
                new String[]{"Team Name", "Number of player awards"},
                "SELECT DISTINCT p.\"teamName\", COUNT(*) AS \"number of player awards\"\n" +
                        "FROM \"TeamMember\" t, \"TeamMember_Win_Award\" a, \"Player_PlayFor\" p\n" +
                        "WHERE t.\"FIFAConnectId\" = a.\"FIFAConnectId\" AND t.\"FIFAConnectId\" = p.\"FIFAConnectId\"\n" +
                        "GROUP BY p.\"teamName\"",
                QueryType.GROUPBY));

        groupByQueries.add(new CustomQuery("Find the average number of goals scored across each participating team",
                new String[]{},
                new AttributeType[]{},
                new String[]{"Name", "Average number of goals"},
                "SELECT DISTINCT result.\"name\", CAST(AVG(result.\"score\") AS DECIMAL(10,2))\n" +
                        "FROM (SELECT t.\"name\", m.\"homeScore\" AS \"score\"\n" +
                        "FROM \"Match_Play\" m, \"Team\" t\n" +
                        "WHERE t.\"name\" = m.\"homeTeamName\"\n" +
                        "UNION\n" +
                        "SELECT t.\"name\", m.\"awayScore\" AS \"score\"\n" +
                        "FROM \"Match_Play\" m, \"Team\" t\n" +
                        "WHERE t.\"name\" = m.\"awayTeamName\") result\n" +
                        "GROUP BY result.\"name\"",
                QueryType.GROUPBY));

        groupByQueries.add(new CustomQuery("Find the minimum ages of players across different teams",
                new String[]{},
                new AttributeType[]{},
                new String[]{"Team name", "Minimum age of player in the team"},
                "SELECT DISTINCT p.\"teamName\", MIN(ROUND((CURRENT_DATE - t.\"birthDate\")/365.25)) AS \"minimum age\"\n" +
                        "FROM \"TeamMember\" t, \"Player_PlayFor\" p\n" +
                        "WHERE t.\"FIFAConnectId\" = p.\"FIFAConnectId\"\n" +
                        "GROUP BY p.\"teamName\"",
                QueryType.GROUPBY));
    }

    private void addHavingTab(List<CustomQuery> havingQueries) {
        havingQueries.add(new CustomQuery("Find all divisions that have at least X awards among all players",
                new String[]{"Number of Awards"},
                new AttributeType[]{AttributeType.INT},
                new String[]{"Division", "Number of Awards"},
                "SELECT \"division\", COUNT(*) " +
                        "FROM \"Player_PlayFor\" ppf, \"Team\" t, \"TeamMember_Win_Award\" twa " +
                        "WHERE ppf.\"teamName\" = t.\"name\" AND twa.\"FIFAConnectId\" = ppf.\"FIFAConnectId\" " +
                        "GROUP BY t.\"division\" " +
                        "HAVING COUNT(*) >= ?",
                QueryType.HAVING));

        havingQueries.add(new CustomQuery("Find all teams that have X distinct players that have scored at least Y goals in a game",
                new String[]{"Number of Goals", "Number of Players"},
                new AttributeType[]{AttributeType.INT, AttributeType.INT},
                new String[]{"Team", "Number of Players"},
                "SELECT ppf.\"teamName\", COUNT(DISTINCT psm.\"FIFAConnectId\") " +
                        "FROM \"Player_PlayFor\" ppf, \"Player_Score_Match\" psm " +
                        "WHERE psm.\"numberOfGoals\" >= ? AND psm.\"FIFAConnectId\" = ppf.\"FIFAConnectId\" " +
                        "GROUP BY ppf.\"teamName\" " +
                        "HAVING COUNT(DISTINCT psm.\"FIFAConnectId\") >= ?",
                QueryType.HAVING));

        havingQueries.add(new CustomQuery("Find all teams that have X distinct players that have assisted at least Y times in a game",
                new String[]{"Number of Assists", "Number of Players"},
                new AttributeType[]{AttributeType.INT, AttributeType.INT},
                new String[]{"Team", "Number of Players"},
                "SELECT ppf.\"teamName\", COUNT(DISTINCT pam.\"FIFAConnectId\") " +
                        "FROM \"Player_PlayFor\" ppf, \"Player_Assist_Match\" pam " +
                        "WHERE pam.\"numberOfAssists\" >= ? AND pam.\"FIFAConnectId\" = ppf.\"FIFAConnectId\" " +
                        "GROUP BY ppf.\"teamName\" " +
                        "HAVING COUNT(DISTINCT pam.\"FIFAConnectId\") >= ?",
                QueryType.HAVING));
    }

    private void addNestedGroupBy(List<CustomQuery> nestedGroupByQueries) {
        nestedGroupByQueries.add(new CustomQuery("Find the player with most awards",
                new String[]{},
                new AttributeType[]{},
                new String[]{"FIFA Connect ID", "Name", "Number of Awards"},
                "SELECT \"TeamMember_Win_Award\".\"FIFAConnectId\", \"name\", COUNT(*) as \"Number of Awards\"\n" +
                        "FROM \"TeamMember_Win_Award\"\n" +
                        "INNER JOIN \"TeamMember\" on \"TeamMember_Win_Award\".\"FIFAConnectId\" = \"TeamMember\".\"FIFAConnectId\"\n" +
                        "GROUP BY \"TeamMember_Win_Award\".\"FIFAConnectId\", \"name\"\n" +
                        "HAVING COUNT(*) = (SELECT MAX(COUNT(*)) FROM \"TeamMember_Win_Award\" GROUP BY \"TeamMember_Win_Award\".\"FIFAConnectId\")",
                QueryType.NESTEDGROUPBY));

        nestedGroupByQueries.add(new CustomQuery("List players who have scored above average",
                new String[]{},
                new AttributeType[]{},
                new String[]{"FIFA Connect ID", "Name", "Number of Goals"},
                "SELECT \"Player_Score_Match\".\"FIFAConnectId\", \"name\", SUM(\"numberOfGoals\") as \"Number of Goals\"\n" +
                        "FROM \"Player_Score_Match\"\n" +
                        "INNER JOIN \"TeamMember\" on \"Player_Score_Match\".\"FIFAConnectId\" = \"TeamMember\".\"FIFAConnectId\"\n" +
                        "GROUP BY \"Player_Score_Match\".\"FIFAConnectId\", \"name\"\n" +
                        "HAVING SUM(\"numberOfGoals\") >\n" +
                        "(SELECT AVG(\"numberOfGoals\")\n" +
                        "FROM \"Player_Score_Match\")",
                QueryType.NESTEDGROUPBY));
    }

    private void addDivision(List<CustomQuery> divisionQueries) {
        divisionQueries.add(new CustomQuery("Find all teams that have played in all the arenas",
                new String[]{},
                new AttributeType[]{},
                new String[]{"Team Name"},
                "SELECT t.\"name\" " +
                        "FROM \"Team\" t " +
                        "WHERE NOT EXISTS (SELECT \"address\" " +
                        "FROM \"Arena\" " +
                        "MINUS " +
                        "(SELECT \"arenaAddress\" " +
                        "FROM \"Match_Play\" " +
                        "WHERE \"homeTeamName\" = t.\"name\" OR \"awayTeamName\" = t.\"name\"))",
                QueryType.DIVISION));

        divisionQueries.add(new CustomQuery("Find all referees that have refereed in all the arenas",
                new String[]{},
                new AttributeType[]{},
                new String[]{"Referee Name"},
                "SELECT r.\"name\" " +
                        "FROM \"Referee\" r " +
                        "WHERE NOT EXISTS (SELECT \"address\" " +
                        "FROM \"Arena\" " +
                        "MINUS " +
                        "(SELECT \"arenaAddress\" " +
                        "FROM \"Referee_Officiate_Match\" rom, \"Match_Play\" mp " +
                        "WHERE rom.\"matchId\" = mp.\"matchId\" AND rom.\"refereeId\" = r.\"refereeId\"))",
                QueryType.DIVISION));
    }
}
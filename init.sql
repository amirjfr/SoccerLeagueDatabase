-- Create table script

CREATE TABLE "Nationality_Federation" (
    "nationality" varchar(20) PRIMARY KEY,
    "FifaFederation" varchar(20) NOT NULL
);

CREATE TABLE "TeamMember" (
    "FIFAConnectId" varchar(20) PRIMARY KEY,
    "name" varchar(30) NOT NULL,
    "address" varchar(70),
    "birthDate" date NOT NULL,
    "phoneNumber" varchar(13),
    "nationality" varchar(20) NOT NULL,
    FOREIGN KEY ("nationality") REFERENCES "Nationality_Federation" ON DELETE CASCADE
);

CREATE TABLE "Award" (
    "name" varchar(30),
    "year" int,
    PRIMARY KEY ("name", "year")
);

CREATE TABLE "Divison_Conference" (
    "division" varchar(20) PRIMARY KEY,
    "conference" varchar(20) NOT NULL
);

CREATE TABLE "Team" (
    "name" varchar(30) PRIMARY KEY,
    "homeJerseyColour" varchar(30),
    "awayJerseyColour" varchar(30),
    "inaugurationDate" date NOT NULL,
    "division" varchar(20) NOT NULL,
    FOREIGN KEY ("division") REFERENCES "Divison_Conference" ON DELETE CASCADE
);

CREATE TABLE "TeamOwner" (
    "ownerId" varchar(20) PRIMARY KEY,
    "name" varchar(30) NOT NULL,
    "teamName" varchar(30) NOT NULL,
    "birthDate" date NOT NULL,
    "teamPurchaseDate" date NOT NULL,
    FOREIGN KEY ("teamName") REFERENCES "Team"("name") ON DELETE CASCADE
);

CREATE TABLE "Arena" (
    "address" varchar(70) PRIMARY KEY,
    "seats" int,
    "name" varchar(30) NOT NULL UNIQUE,
    "constructionDate" date
);

CREATE TABLE "Match_Play" (
    "matchId" varchar(20) PRIMARY KEY,
    "homeScore" int,
    "awayScore" int,
    "matchDateTime" timestamp NOT NULL,
    "homeTeamName" varchar(30) NOT NULL,
    "awayTeamName" varchar(30) NOT NULL,
    "arenaAddress" varchar(70) NOT NULL,
    FOREIGN KEY ("homeTeamName") REFERENCES "Team"("name") ON DELETE CASCADE,
    FOREIGN KEY ("awayTeamName") REFERENCES "Team"("name") ON DELETE CASCADE,
    FOREIGN KEY ("arenaAddress") REFERENCES "Arena"("address") ON DELETE CASCADE
);

CREATE TABLE "Referee" (
    "refereeId" varchar(20) PRIMARY KEY,
    "name" varchar(30) NOT NULL,
    "address" varchar(70)
);

CREATE TABLE "AcademyTeam_Has_Team" (
    "teamName" varchar(30),
    "ageGroup" varchar(10),
    "tablePosition" int,
    "inaugurationDate" date,
    FOREIGN KEY ("teamName") REFERENCES "Team"("name") ON DELETE CASCADE,
    PRIMARY KEY ("teamName", "ageGroup")
);

CREATE TABLE "TeamMember_Win_Award" (
    "FIFAConnectId" varchar(20),
    "awardName" varchar(30),
    "awardYear" int,
    FOREIGN KEY ("FIFAConnectId") REFERENCES "TeamMember" ON DELETE CASCADE,
    FOREIGN KEY ("awardName", "awardYear") REFERENCES "Award"("name", "year") ON DELETE CASCADE,
    PRIMARY KEY("FIFAConnectId", "awardName", "awardYear")
);

CREATE TABLE "Coach_Manage" (
    "FIFAConnectId" varchar(20) PRIMARY KEY,
    "teamName" varchar(30) NOT NULL,
    "coachLicense" varchar(20) NOT NULL,
    "startDate" date NOT NULL,
    FOREIGN KEY ("FIFAConnectId") REFERENCES "TeamMember" ON DELETE CASCADE,
    FOREIGN KEY ("teamName") REFERENCES "Team"("name") ON DELETE CASCADE
);

CREATE TABLE "Position_Type" (
    "primaryPosition" varchar(20) PRIMARY KEY,
    "playerType" varchar(20) NOT NULL
);

CREATE TABLE "Player_PlayFor" (
    "FIFAConnectId" varchar(20) PRIMARY KEY,
    "teamName" varchar(30) NOT NULL,
    "startDate" date NOT NULL,
    "shirtNumber" int NOT NULL,
    "primaryPosition" varchar(20) NOT NULL,
    UNIQUE ("teamName", "shirtNumber"),
    FOREIGN KEY ("FIFAConnectId") REFERENCES "TeamMember" ON DELETE CASCADE,
    FOREIGN KEY ("teamName") REFERENCES "Team"("name") ON DELETE CASCADE,
    FOREIGN KEY ("primaryPosition") REFERENCES "Position_Type" ON DELETE CASCADE
);

CREATE TABLE "Player_Score_Match" (
    "FIFAConnectId" varchar(20),
    "matchId" varchar(20),
    "numberOfGoals" int NOT NULL,
    PRIMARY KEY ("FIFAConnectId", "matchId"),
    FOREIGN KEY ("FIFAConnectId") REFERENCES "TeamMember" ON DELETE CASCADE,
    FOREIGN KEY ("matchId") REFERENCES "Match_Play" ON DELETE CASCADE
);

CREATE TABLE "Player_Assist_Match" (
    "FIFAConnectId" varchar(20),
    "matchId" varchar(20),
    "numberOfAssists" int NOT NULL,
    PRIMARY KEY ("FIFAConnectId", "matchId"),
    FOREIGN KEY ("FIFAConnectId") REFERENCES "TeamMember" ON DELETE CASCADE,
    FOREIGN KEY ("matchId") REFERENCES "Match_Play" ON DELETE CASCADE
);

CREATE TABLE "Referee_Officiate_Match" (
    "refereeId" varchar(20),
    "matchId" varchar(20),
    "role" varchar(20) NOT NULL,
    PRIMARY KEY ("refereeId", "matchId"),
    FOREIGN KEY ("matchId") REFERENCES "Match_Play" ON DELETE CASCADE,
    FOREIGN KEY ("refereeId") REFERENCES "Referee" ON DELETE CASCADE
);

CREATE TABLE "TeamOwner_Own_Arena" (
    "teamOwnerId" varchar(20),
    "arenaAddress" varchar(70),
    "purchaseDate" date NOT NULL,
    PRIMARY KEY ("teamOwnerId", "arenaAddress"),
    FOREIGN KEY ("arenaAddress") REFERENCES "Arena"("address") ON DELETE CASCADE,
    FOREIGN KEY ("teamOwnerId") REFERENCES "TeamOwner"("ownerId") ON DELETE CASCADE
);

-- Initialize tables with test data

INSERT INTO "Nationality_Federation" VALUES ('British', 'UEFA');
INSERT INTO "Nationality_Federation" VALUES ('Irish', 'UEFA');
INSERT INTO "Nationality_Federation" VALUES ('German', 'UEFA');
INSERT INTO "Nationality_Federation" VALUES ('Danish', 'UEFA');
INSERT INTO "Nationality_Federation" VALUES ('Portuguese', 'UEFA');
INSERT INTO "Nationality_Federation" VALUES ('Canadian', 'CONCACAF');
INSERT INTO "Nationality_Federation" VALUES ('American', 'CONCACAF');

INSERT INTO "TeamMember" VALUES ('N3HHJT', 'Kleon Rama', '10010 Breeze Hills, New York, USA', TO_DATE('28/06/1985', 'DD/MM/YYYY'), '+17180194592', 'American');
INSERT INTO "TeamMember" VALUES ('BPCQ78', 'Danny Firmino', '95012 Kuvalis Wall, England, UK', TO_DATE('11/02/1995', 'DD/MM/YYYY'), '+441632960240', 'British');
INSERT INTO "TeamMember" VALUES ('2K1741', 'Willie Esmund Marie', '4623 Ruth Forks, Ontario, Canada', TO_DATE('06/11/1990', 'DD/MM/YYYY'), '+16083592773', 'Canadian');
INSERT INTO "TeamMember" VALUES ('ZQD76H', 'Arthur Ventura', '63 Havekoster Strabe, Landkreis, Germany', TO_DATE('15/02/1996', 'DD/MM/YYYY'), '+492753845374', 'German');
INSERT INTO "TeamMember" VALUES ('DJM0RK', 'Ciara Pohl', '143 Behagane Road, County Kerry, Ireland', TO_DATE('23/09/1989', 'DD/MM/YYYY'), '+353953618798', 'Irish');
INSERT INTO "TeamMember" VALUES ('F95E65', 'Louis Suarez', '304 Database Street, British Columbia, Canada', TO_DATE('12/11/1987', 'DD/MM/YYYY'), '+16044520934', 'Canadian');
INSERT INTO "TeamMember" VALUES ('5FA547', 'Elliot MacDonald', '887 Cypress Road, California, USA', TO_DATE('02/02/1995', 'DD/MM/YYYY'), '+14062450939', 'American');
INSERT INTO "TeamMember" VALUES ('25613', 'Mose Jourinho', '3045 Tandem Road, New York, USA', TO_DATE('03/09/1963', 'DD/MM/YYYY'), '+17183529560', 'American');
INSERT INTO "TeamMember" VALUES ('CA7D7', 'Ole Jensen', '38 Bygmestervej, Copenhagen, Denmark', TO_DATE('28/04/1970', 'DD/MM/YYYY'), '+4560555403', 'Danish');
INSERT INTO "TeamMember" VALUES ('1B939', 'Wayne Ronnie', '7 Manor Road, England, UK', TO_DATE('12/12/1969', 'DD/MM/YYYY'), '+441622551321', 'British');
INSERT INTO "TeamMember" VALUES ('D6F70', 'Jenning Bim', '98 South Street, England, UK', TO_DATE('09/07/1965', 'DD/MM/YYYY'), '+441622024865', 'British');
INSERT INTO "TeamMember" VALUES ('025BE', 'Ressi Monaldo', '99 R Gago Coutinho, Aveiro, Portugal', TO_DATE('30/03/1968', 'DD/MM/YYYY'), '+351935361807', 'Portuguese');

INSERT INTO "Award" VALUES ('FIFA Ballon d''Or', 2014);
INSERT INTO "Award" VALUES ('FIFA Ballon d''Or', 2015);
INSERT INTO "Award" VALUES ('The Best FIFA Men''s Player', 2008);
INSERT INTO "Award" VALUES ('The Best FIFA Women''s Player', 2011);
INSERT INTO "Award" VALUES ('FIFA Fair Play Award', 2016);
INSERT INTO "Award" VALUES ('FIFA Puskás Award', 2014);
INSERT INTO "Award" VALUES ('MLS Golden Boot', 2014);
INSERT INTO "Award" VALUES ('MLS Golden Boot', 2015);
INSERT INTO "Award" VALUES ('MLS Golden Boot', 2016);
INSERT INTO "Award" VALUES ('MLS Goal of the Year', 2014);
INSERT INTO "Award" VALUES ('MLS Goal of the Year', 2015);
INSERT INTO "Award" VALUES ('MLS Goal of the Year', 2016);

INSERT INTO "Divison_Conference" VALUES('North West', 'Western');
INSERT INTO "Divison_Conference" VALUES('South West', 'Western');
INSERT INTO "Divison_Conference" VALUES('Central West', 'Western');
INSERT INTO "Divison_Conference" VALUES('North East', 'Eastern');
INSERT INTO "Divison_Conference" VALUES('South East', 'Eastern');

INSERT INTO "Team" VALUES('Vancouver Whitecaps FC', 'Blue, White', 'Black, White', TO_DATE('12/06/2002', 'DD/MM/YYYY'), 'North West');
INSERT INTO "Team" VALUES('Los Angeles Galaxy', 'White, Orange, Blue', 'Blue, Black', TO_DATE('24/05/2004', 'DD/MM/YYYY'), 'South West');
INSERT INTO "Team" VALUES('FC Dallas', 'Red', 'Blue, White', TO_DATE('08/08/2007', 'DD/MM/YYYY'), 'Central West');
INSERT INTO "Team" VALUES('Toronto FC', 'Red, Black', 'White, Red', TO_DATE('11/02/2011', 'DD/MM/YYYY'), 'North East');
INSERT INTO "Team" VALUES('New York City FC', 'Light Blue', 'Blue, Black', TO_DATE('07/11/2009', 'DD/MM/YYYY'), 'North East');

INSERT INTO "TeamOwner" VALUES('7eb9c69f', 'John Kenny', 'Vancouver Whitecaps FC', TO_DATE('30/09/1982', 'DD/MM/YYYY'), TO_DATE('21/06/2007', 'DD/MM/YYYY'));
INSERT INTO "TeamOwner" VALUES('a6ebacb5', 'Thomas Smith', 'Los Angeles Galaxy', TO_DATE('02/12/1973', 'DD/MM/YYYY'), TO_DATE('16/09/2008', 'DD/MM/YYYY'));
INSERT INTO "TeamOwner" VALUES('90bd3314', 'Maxwell James', 'FC Dallas', TO_DATE('23/11/1968', 'DD/MM/YYYY'), TO_DATE('17/04/2016', 'DD/MM/YYYY'));
INSERT INTO "TeamOwner" VALUES('570ee757', 'Clark Klutz', 'Toronto FC', TO_DATE('18/08/1990', 'DD/MM/YYYY'), TO_DATE('12/03/2011', 'DD/MM/YYYY'));
INSERT INTO "TeamOwner" VALUES('c92ccc07', 'Frederick Allen', 'New York City FC', TO_DATE('03/01/1987', 'DD/MM/YYYY'), TO_DATE('27/01/2017', 'DD/MM/YYYY'));

INSERT INTO "Arena" VALUES('777 Pacific Blvd, British Columbia, Canada', 54500, 'BC Place', TO_DATE('19/06/1983', 'DD/MM/YYYY'));
INSERT INTO "Arena" VALUES('18400 S Avalon Blvd, Carson, California, United States', 27000, 'Dignity Health Sports Park', TO_DATE('01/06/2003', 'DD/MM/YYYY'));
INSERT INTO "Arena" VALUES('9200 World Cup Way, Texas, United States', 20500, 'Toyota Stadium', TO_DATE('06/08/2005', 'DD/MM/YYYY'));
INSERT INTO "Arena" VALUES('170 Princes'' Blvd, Ontario, Canada', 30000, 'BMO Field', TO_DATE('28/04/2007', 'DD/MM/YYYY'));
INSERT INTO "Arena" VALUES('1 East 161st Street, New York, United States', 28743, 'Yankee Stadium', TO_DATE('02/04/2009', 'DD/MM/YYYY'));

INSERT INTO "Match_Play" VALUES('a7d37e568d55', 4, 2, TO_TIMESTAMP('02/04/2017 03:08', 'DD/MM/YYYY HH24:MI'), 'Vancouver Whitecaps FC', 'Los Angeles Galaxy', '777 Pacific Blvd, British Columbia, Canada');
INSERT INTO "Match_Play" VALUES('1b5c17764c2d', 0, 0, TO_TIMESTAMP('25/03/2018 03:17', 'DD/MM/YYYY HH24:MI'), 'Vancouver Whitecaps FC', 'Los Angeles Galaxy', '777 Pacific Blvd, British Columbia, Canada');
INSERT INTO "Match_Play" VALUES('7e2cd50af2d1', 3, 0, TO_TIMESTAMP('19/08/2020 01:09', 'DD/MM/YYYY HH24:MI'), 'Toronto FC', 'Vancouver Whitecaps FC', '170 Princes'' Blvd, Ontario, Canada');
INSERT INTO "Match_Play" VALUES('3882948c8bd2', 1, 0, TO_TIMESTAMP('21/08/2020 01:08', 'DD/MM/YYYY HH24:MI'), 'Toronto FC', 'Vancouver Whitecaps FC', '170 Princes'' Blvd, Ontario, Canada');
INSERT INTO "Match_Play" VALUES('5905854da634', 3, 2, TO_TIMESTAMP('06/09/2020 02:40', 'DD/MM/YYYY HH24:MI'), 'Vancouver Whitecaps FC', 'Toronto FC', '777 Pacific Blvd, British Columbia, Canada');
INSERT INTO "Match_Play" VALUES('b68443ffb2d6', 5, 3, TO_TIMESTAMP('02/08/2017 03:08', 'DD/MM/YYYY HH24:MI'), 'Los Angeles Galaxy', 'Vancouver Whitecaps FC', '18400 S Avalon Blvd, Carson, California, United States');
INSERT INTO "Match_Play" VALUES('76c187c4c6cf', 1, 4, TO_TIMESTAMP('09/08/2017 01:28', 'DD/MM/YYYY HH24:MI'), 'FC Dallas', 'Vancouver Whitecaps FC', '9200 World Cup Way, Texas, United States');
INSERT INTO "Match_Play" VALUES('a5c66fa167e4', 0, 5, TO_TIMESTAMP('16/08/2017 00:08', 'DD/MM/YYYY HH24:MI'), 'New York City FC', 'Vancouver Whitecaps FC', '1 East 161st Street, New York, United States');


INSERT INTO "Referee" VALUES('dba8092a2289', 'Jair Marrufo', '123 Goal Street, British Columbia, Canada');
INSERT INTO "Referee" VALUES('37233f6989dc', 'Ricardo Salazar', '789 Assist Avenue, Ontario, Canada');
INSERT INTO "Referee" VALUES('533399a8fff1', 'Peter Manikowski', '1024 Red Boulevard, California, United States');
INSERT INTO "Referee" VALUES('e3bf1853692e', 'Nick Uranga', '3141 Scorpion Drive, Texas, United States');
INSERT INTO "Referee" VALUES('a73bc19d0892', 'Fucho Mucho', '304 Database Drive, New York, United States');

INSERT INTO "AcademyTeam_Has_Team" VALUES('Vancouver Whitecaps FC', 'U-23', 3, TO_DATE('2007', 'YYYY'));
INSERT INTO "AcademyTeam_Has_Team" VALUES('Vancouver Whitecaps FC', 'U-19', 10, TO_DATE('2007', 'YYYY'));
INSERT INTO "AcademyTeam_Has_Team" VALUES('Los Angeles Galaxy', 'U-18/19', 7, TO_DATE('2015', 'YYYY'));
INSERT INTO "AcademyTeam_Has_Team" VALUES('FC Dallas', 'U-18/19', 2, TO_DATE('2008', 'YYYY'));
INSERT INTO "AcademyTeam_Has_Team" VALUES('Toronto FC', 'U-18/19', NULL, TO_DATE('2008', 'YYYY'));

INSERT INTO "TeamMember_Win_Award" VALUES('N3HHJT', 'FIFA Ballon d''Or', 2014);
INSERT INTO "TeamMember_Win_Award" VALUES('N3HHJT', 'FIFA Ballon d''Or', 2015);
INSERT INTO "TeamMember_Win_Award" VALUES('N3HHJT', 'FIFA Puskás Award', 2014);
INSERT INTO "TeamMember_Win_Award" VALUES('N3HHJT', 'MLS Golden Boot', 2014);
INSERT INTO "TeamMember_Win_Award" VALUES('ZQD76H', 'MLS Golden Boot', 2015);
INSERT INTO "TeamMember_Win_Award" VALUES('2K1741', 'MLS Golden Boot', 2016);
INSERT INTO "TeamMember_Win_Award" VALUES('ZQD76H', 'MLS Goal of the Year', 2014);
INSERT INTO "TeamMember_Win_Award" VALUES('2K1741', 'MLS Goal of the Year', 2015);
INSERT INTO "TeamMember_Win_Award" VALUES('2K1741', 'MLS Goal of the Year', 2016);

INSERT INTO "Coach_Manage" VALUES ('25613', 'Vancouver Whitecaps FC', 'Canada Soccer A', TO_DATE('20/05/2020', 'DD/MM/YYYY'));
INSERT INTO "Coach_Manage" VALUES ('CA7D7', 'Los Angeles Galaxy', 'USAF A', TO_DATE('05/01/2021', 'DD/MM/YYYY'));
INSERT INTO "Coach_Manage" VALUES ('1B939', 'FC Dallas', 'USAF A', TO_DATE('16/12/2018', 'DD/MM/YYYY'));
INSERT INTO "Coach_Manage" VALUES ('D6F70', 'Toronto FC', 'Canada Soccer A', TO_DATE('13/01/2021', 'DD/MM/YYYY'));
INSERT INTO "Coach_Manage" VALUES ('025BE', 'New York City FC', 'USAF A', TO_DATE('06/01/2020', 'DD/MM/YYYY'));

INSERT INTO "Position_Type" VALUES ('Centre back', 'Defender');
INSERT INTO "Position_Type" VALUES ('Wing back', 'Defender');
INSERT INTO "Position_Type" VALUES ('Centre midfield', 'Midfielder');
INSERT INTO "Position_Type" VALUES ('Defensive midfield', 'Midfielder');
INSERT INTO "Position_Type" VALUES ('Centre forward', 'Attacker');
INSERT INTO "Position_Type" VALUES ('Winger', 'Attacker');

INSERT INTO "Player_PlayFor" VALUES ('N3HHJT', 'Vancouver Whitecaps FC', TO_DATE('22/11/2019', 'DD/MM/YYYY'), 7, 'Centre midfield');
INSERT INTO "Player_PlayFor" VALUES ('F95E65', 'Vancouver Whitecaps FC', TO_DATE('20/11/2019', 'DD/MM/YYYY'), 11, 'Winger');
INSERT INTO "Player_PlayFor" VALUES ('BPCQ78', 'Los Angeles Galaxy', TO_DATE('06/02/2017', 'DD/MM/YYYY'), 5, 'Centre back');
INSERT INTO "Player_PlayFor" VALUES ('5FA547', 'Los Angeles Galaxy', TO_DATE('10/02/2017', 'DD/MM/YYYY'), 3, 'Centre back');
INSERT INTO "Player_PlayFor" VALUES ('2K1741', 'FC Dallas', TO_DATE('26/08/2020', 'DD/MM/YYYY'), 11, 'Winger');
INSERT INTO "Player_PlayFor" VALUES ('ZQD76H', 'Toronto FC', TO_DATE('03/04/2021', 'DD/MM/YYYY'), 9, 'Centre forward');
INSERT INTO "Player_PlayFor" VALUES ('DJM0RK', 'New York City FC', TO_DATE('16/03/2020', 'DD/MM/YYYY'), 2, 'Wing back');

INSERT INTO "Player_Score_Match" VALUES ('N3HHJT', '3882948c8bd2', 1);
INSERT INTO "Player_Score_Match" VALUES ('BPCQ78', 'a7d37e568d55', 3);
INSERT INTO "Player_Score_Match" VALUES ('BPCQ78', '1b5c17764c2d', 2);
INSERT INTO "Player_Score_Match" VALUES ('ZQD76H', '7e2cd50af2d1', 1);
INSERT INTO "Player_Score_Match" VALUES ('N3HHJT', '5905854da634', 2);
INSERT INTO "Player_Score_Match" VALUES ('F95E65', 'b68443ffb2d6', 2);
INSERT INTO "Player_Score_Match" VALUES ('5FA547', 'b68443ffb2d6', 1);

INSERT INTO "Player_Assist_Match" VALUES ('N3HHJT', '3882948c8bd2', 1);
INSERT INTO "Player_Assist_Match" VALUES ('BPCQ78', 'a7d37e568d55', 1);
INSERT INTO "Player_Assist_Match" VALUES ('BPCQ78', '1b5c17764c2d', 3);
INSERT INTO "Player_Assist_Match" VALUES ('ZQD76H', '7e2cd50af2d1', 1);
INSERT INTO "Player_Assist_Match" VALUES ('N3HHJT', '5905854da634', 2);
INSERT INTO "Player_Assist_Match" VALUES ('F95E65', 'b68443ffb2d6', 1);
INSERT INTO "Player_Assist_Match" VALUES ('5FA547', 'b68443ffb2d6', 3);

INSERT INTO "Referee_Officiate_Match" VALUES('dba8092a2289', 'a7d37e568d55', 'referee');
INSERT INTO "Referee_Officiate_Match" VALUES('dba8092a2289', '7e2cd50af2d1', 'referee');
INSERT INTO "Referee_Officiate_Match" VALUES('dba8092a2289', 'b68443ffb2d6', 'assistant referee');
INSERT INTO "Referee_Officiate_Match" VALUES('dba8092a2289', '76c187c4c6cf', 'assistant referee');
INSERT INTO "Referee_Officiate_Match" VALUES('dba8092a2289', 'a5c66fa167e4', 'referee');
INSERT INTO "Referee_Officiate_Match" VALUES('37233f6989dc', '1b5c17764c2d', 'assistant referee');
INSERT INTO "Referee_Officiate_Match" VALUES('37233f6989dc', '7e2cd50af2d1', 'assistant referee');
INSERT INTO "Referee_Officiate_Match" VALUES('37233f6989dc', 'b68443ffb2d6', 'referee');
INSERT INTO "Referee_Officiate_Match" VALUES('37233f6989dc', '76c187c4c6cf', 'referee');
INSERT INTO "Referee_Officiate_Match" VALUES('37233f6989dc', 'a5c66fa167e4', 'assistant referee');
INSERT INTO "Referee_Officiate_Match" VALUES('533399a8fff1', '7e2cd50af2d1', 'assistant referee');
INSERT INTO "Referee_Officiate_Match" VALUES('e3bf1853692e', '3882948c8bd2', 'referee');
INSERT INTO "Referee_Officiate_Match" VALUES('a73bc19d0892', '5905854da634', 'referee');

INSERT INTO "TeamOwner_Own_Arena" VALUES('7eb9c69f', '777 Pacific Blvd, British Columbia, Canada', TO_DATE('21/06/2007', 'DD/MM/YYYY'));
INSERT INTO "TeamOwner_Own_Arena" VALUES('a6ebacb5', '18400 S Avalon Blvd, Carson, California, United States', TO_DATE('16/09/2008', 'DD/MM/YYYY'));
INSERT INTO "TeamOwner_Own_Arena" VALUES('90bd3314', '9200 World Cup Way, Texas, United States', TO_DATE('17/04/2016', 'DD/MM/YYYY'));
INSERT INTO "TeamOwner_Own_Arena" VALUES('570ee757', '170 Princes'' Blvd, Ontario, Canada', TO_DATE('12/03/2011', 'DD/MM/YYYY'));
INSERT INTO "TeamOwner_Own_Arena" VALUES('c92ccc07', '1 East 161st Street, New York, United States', TO_DATE('27/01/2017', 'DD/MM/YYYY'));

COMMIT;

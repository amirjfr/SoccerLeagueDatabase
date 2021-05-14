-- Insert

INSERT INTO "Nationality_Federation"
VALUES ('$replace nationality', '$replace federation');

INSERT INTO "TeamMember"
VALUES ('$replace FIFAConnectId$', '$replace name here$', '$replace address here$', TO_DATE('$replace birth date here$', 'DD/MM/YYYY'), '$replace phone number here$', '$replace nationality here$');

INSERT INTO "TeamOwner"
VALUES ('$replace owner id here$', '$replace owner name here$', '$replace team name here$', TO_DATE('$replace birth date here$', 'DD/MM/YYYY'), TO_DATE('$replace team purchase date here$', 'DD/MM/YYYY'));

INSERT INTO "Team"
VALUES ('$replace team name here$', '$replace home jersey colour here$', '$replace away jersey colour here$', TO_DATE('$replace inauguration date here$', 'DD/MM/YYYY'), '$replace division here$');

INSERT INTO "Match_Play"
VALUES ('$replace match id here$', 10, 10, TO_DATE('$replace match date here$', 'DD/MM/YYYY'), '$replace home team name here$', '$replace away team name here$', '$replace arena address here$');

INSERT INTO "Referee"
VALUES ('$replace referee id here$', '$replace referee name here$', '$replace referee address here$');

INSERT INTO "Arena"
VALUES ('$replace arena address here$', '$replace no. of seats here$', '$replace arena name here$', TO_DATE('$replace construction date here$', 'DD/MM/YYYY'));

-- Delete
DELETE FROM "TeamMember"
WHERE "FIFAConnectId" = '$replace here$';

DELETE FROM "Referee"
WHERE "refereeId" = '$replace here$';

DELETE FROM "Match_Play"
WHERE "matchId" = '$replace here$';

DELETE FROM "TeamOwner"
WHERE "ownerId" = '$replace here$'

DELETE FROM "Team"
WHERE "name" = '$replace here$';

-- Update
UPDATE "TeamMember"
SET "name" = '$replace here$', "address" = '$replace here$', "phoneNumber" = '$replace here$'
WHERE "FIFAConnectId" = '$replace here$';

UPDATE "TeamOwner"
SET "teamName" = '$replace here$'
WHERE "ownerId" = '$replace here$';

UPDATE "Match_Play"
SET "matchDateTime" = '$replace here$'
WHERE "matchId" = '$replace here$';

UPDATE "Referee"
SET "name" = '$replace here$', "address" = '$replace here$'
WHERE "refereeId" = '$replace here$';

UPDATE "AcademyTeam_Has_Team"
SET "tablePosition" = '$replace here$'
WHERE "teamName" = '$replace here$' AND "ageGroup" = '$replace here$';

UPDATE "Player_PlayFor"
SET "teamName" = '$replace here$', "startDate" = '$replace here$', "shirtNumber" = '$replace here$'
WHERE "FIFAConnectId" = '$replace here$';

-- Selection

-- Select all players under a specific age
SELECT *
FROM "TeamMember" tm, "Player_PlayFor" pf
WHERE (CURRENT_DATE - "birthDate")/365.25 < '$replace age value here$'
AND tm."FIFAConnectId" = pf."FIFAConnectId";

-- Select all players that received a specific award
SELECT DISTINCT t."name", a."awardName", a."awardYear"
FROM "TeamMember" t, "TeamMember_Win_Award" a, "Player_PlayFor" pf
WHERE a."FIFAConnectId" = t."FIFAConnectId"
AND a."awardName" = '$replace award name here$'
AND t."FIFAConnectId" = pf."FIFAConnectId";

-- Select all players of a specific nationality
SELECT *
FROM "TeamMember" t, "Player_PlayFor" pf
WHERE "nationality" = '$replace nationality here$'
AND t."FIFAConnectId" = pf."FIFAConnectId";

-- Select all players who play a specific position
SELECT *
FROM "TeamMember" t, "Player_PlayFor" p
WHERE t."FIFAConnectId" = p."FIFAConnectId" AND p."primaryPosition" = '$replace position here$';

-- Select all owners of a specific arena
SELECT t.*
FROM "TeamOwner" t, "TeamOwner_Own_Arena" a
WHERE t."ownerId" = a."teamOwnerId" AND a."arenaAddress" = '$replace arena address here$';

-- Select all awards distributed in a specific year
SELECT *
FROM "Award"
WHERE "year" = '$replace award year$';

-- Projection

-- List player nationalities
SELECT "name", "birthDate", tm."nationality", "FifaFederation"
FROM "TeamMember" tm, "Nationality_Federation" nf, "Player_PlayFor" ppf
WHERE tm."nationality" = nf."nationality" AND ppf."FIFAConnectId" = tm."FIFAConnectId"
ORDER BY "birthDate", tm."nationality";

-- List player positions
SELECT "name", "teamName", "shirtNumber", ppf."primaryPosition", "playerType"
FROM "Player_PlayFor" ppf, "Position_Type" pt, "TeamMember" tm
WHERE ppf."primaryPosition" = pt."primaryPosition" AND tm."FIFAConnectId" = ppf."FIFAConnectId"
ORDER BY "teamName", "shirtNumber";

-- Join

-- List players' number of goals and assists
SELECT "name", "Player_Score_Match"."matchId", "numberOfGoals", "numberOfAssists"
FROM "Player_Score_Match"
INNER JOIN "Player_Assist_Match" ON "Player_Score_Match"."FIFAConnectId" = "Player_Assist_Match"."FIFAConnectId" AND "Player_Score_Match"."matchId" = "Player_Assist_Match"."matchId"
INNER JOIN "TeamMember" ON "Player_Score_Match"."FIFAConnectId" = "TeamMember"."FIFAConnectId"
WHERE "numberOfGoals" >= '$replace here$';

-- List players' team and awards
SELECT "name", "teamName", "awardName", "awardYear"
FROM "Player_PlayFor"
INNER JOIN "TeamMember_Win_Award" ON "Player_PlayFor"."FIFAConnectId" = "TeamMember_Win_Award"."FIFAConnectId"
INNER JOIN "TeamMember" ON "Player_PlayFor"."FIFAConnectId" = "TeamMember"."FIFAConnectId"
WHERE "awardYear" = '$replace here$';

-- Aggregation with Group GROUP BY 

-- Count the total number of players across the different nationalities participating in MLS
SELECT "nationality", COUNT(DISTINCT "FIFAConnectId") AS "number of players"
FROM "TeamMember"
GROUP BY "nationality";

-- Count the number of player awards per team
SELECT DISTINCT p."teamName", COUNT(*) AS "number of player awards"
FROM "TeamMember" t, "TeamMember_Win_Award" a, "Player_PlayFor" p
WHERE t."FIFAConnectId" = a."FIFAConnectId" AND t."FIFAConnectId" = p."FIFAConnectId"
GROUP BY p."teamName";

-- Find the average number of goals scored across each participating team
SELECT t."name", m."homeScore" AS "score"
FROM "Match_Play" m, "Team" t
WHERE t."name" = m."homeTeamName"
UNION
SELECT t."name", m."awayScore" AS "score"
FROM "Match_Play" m, "Team" t
WHERE t."name" = m."awayTeamName";

SELECT DISTINCT result."name", CAST(AVG(result."score") AS DECIMAL(10,2))
FROM (SELECT t."name", m."homeScore" AS "score"
 FROM "Match_Play" m, "Team" t
 WHERE t."name" = m."homeTeamName"
 UNION
 SELECT t."name", m."awayScore" AS "score"
 FROM "Match_Play" m, "Team" t
 WHERE t."name" = m."awayTeamName") result
GROUP BY result."name";

-- Find the minimum ages of players across different teams
SELECT DISTINCT p."teamName", MIN(ROUND((CURRENT_DATE - t."birthDate")/365.25)) AS "minimum age"
FROM "TeamMember" t, "Player_PlayFor" p
WHERE t."FIFAConnectId" = p."FIFAConnectId"
GROUP BY p."teamName";

-- GROUP BY WITH HAVING

-- Find all divisions that have at least X awards among all players
SELECT "division", COUNT(*)
FROM "Player_PlayFor" ppf, "Team" t, "TeamMember_Win_Award" twa
WHERE ppf."teamName" = t."name" AND twa."FIFAConnectId" = ppf."FIFAConnectId"
GROUP BY t."division"
HAVING COUNT(*) >= '$replace number of awards';

-- Find all teams that have X distinct players that have scored at least Y goals in a game
SELECT ppf."teamName", COUNT(DISTINCT psm."FIFAConnectId")
FROM "Player_PlayFor" ppf, "Player_Score_Match" psm
WHERE psm."numberOfGoals" >= '$replace number of goals' AND psm."FIFAConnectId" = ppf."FIFAConnectId"
GROUP BY ppf."teamName"
HAVING COUNT(DISTINCT psm."FIFAConnectId") >= '$replace number of times';

-- Find all teams that have X distinct players that have assisted at least Y times in a game
SELECT ppf."teamName", COUNT(DISTINCT pam."FIFAConnectId")
FROM "Player_PlayFor" ppf, "Player_Assist_Match" pam
WHERE pam."numberOfAssists" >= '$replace number of assists' AND pam."FIFAConnectId" = ppf."FIFAConnectId"
GROUP BY ppf."teamName"
HAVING COUNT(DISTINCT pam."FIFAConnectId") >= '$replace number of times';

-- Nested Aggregation with GROUP BY
-- Find the player with most awards
-- Consulted this link to come up with the following query:
-- https://stackoverflow.com/questions/1795198/sql-not-a-single-group-group-function
SELECT "TeamMember_Win_Award"."FIFAConnectId", "name", COUNT(*) as "Number of Awards"
FROM "TeamMember_Win_Award"
INNER JOIN "TeamMember" on "TeamMember_Win_Award"."FIFAConnectId" = "TeamMember"."FIFAConnectId"
GROUP BY "TeamMember_Win_Award"."FIFAConnectId", "name"
HAVING COUNT(*) = (SELECT MAX(COUNT(*)) FROM "TeamMember_Win_Award" GROUP BY "TeamMember_Win_Award"."FIFAConnectId");

-- Find players who have scored above average number of goals in all matches
SELECT "Player_Score_Match"."FIFAConnectId", "name", SUM("numberOfGoals") as "Number of Goals"
FROM "Player_Score_Match"
INNER JOIN "TeamMember" on "Player_Score_Match"."FIFAConnectId" = "TeamMember"."FIFAConnectId"
GROUP BY "Player_Score_Match"."FIFAConnectId", "name"
HAVING SUM("numberOfGoals") >
       (SELECT AVG("numberOfGoals")
        FROM "Player_Score_Match");

-- Division

-- Find all teams that have played in all the arenas
SELECT t."name"
FROM "Team" t
WHERE NOT EXISTS (SELECT "address"
                    FROM "Arena"
                    MINUS
                    (SELECT "arenaAddress"
                    FROM "Match_Play"
                    WHERE "homeTeamName" = t."name" OR "awayTeamName" = t."name"));

-- Find all referees that have refereed in all the arenas
SELECT r."name"
FROM "Referee" r
WHERE NOT EXISTS (SELECT "address"
                    FROM "Arena"
                    MINUS
                    (SELECT "arenaAddress"
                    FROM "Referee_Officiate_Match" rom, "Match_Play" mp
                    WHERE rom."matchId" = mp."matchId" AND rom."refereeId" = r."refereeId"));

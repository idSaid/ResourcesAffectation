require(RPostgreSQL)
require(MASS)
require(stringr)
# create a connection

# loads the PostgreSQL driver
drv <- dbDriver("PostgreSQL")
# creates a connection to the postgres database
# note that "con" will be used later in each connection to the database
con <- dbConnect(drv, dbname = "personnesagees",
                 host = "92.222.86.67", port = 5432,
                 user = "postgres", password = "toJIN")

# check if the table action_assitance exists (== TRUE)
dbExistsTable(con,"incidentsaccidents")

# query the data from postgreSQL 
df_postgres <- dbGetQuery(con, "SELECT idunique, score FROM (SELECT testing.idunique,
                          testing.chnonqualifies,
                          testing.ch0,
                          testing.ch1,
                          testing.ch2,
                          testing.ch3,
                          ((((2)::numeric * testing.ch2) + ((3)::numeric * testing.ch3)) / ((testing.ch2 + testing.ch3) + 0.01)) AS score,
                          chutes.\"classement.cluster\" as cluster
                          FROM ( SELECT test.idunique,
                          COALESCE(sum(test.chutesnonqualifies), (0)::numeric) AS chnonqualifies,
                          COALESCE(sum(test.chutes0), (0)::numeric) AS ch0,
                          COALESCE(sum(test.chutes1), (0)::numeric) AS ch1,
                          COALESCE(sum(test.chutes2), (0)::numeric) AS ch2,
                          COALESCE(sum(test.chutes3), (0)::numeric) AS ch3
                          FROM ( SELECT (event_assistance.idclient || (event_assistance.emplacement)::text) AS idunique,
                          count(*) AS chutesnonqualifies,
                          NULL::numeric AS chutes0,
                          NULL::numeric AS chutes1,
                          NULL::numeric AS chutes2,
                          NULL::numeric AS chutes3
                          FROM event_assistance
                          WHERE ((event_assistance.idniveau_urgence IS NULL) AND (event_assistance.emplacement IS NOT NULL))
                          GROUP BY (event_assistance.idclient || (event_assistance.emplacement)::text)
                          UNION
                          SELECT (event_assistance.idclient || (event_assistance.emplacement)::text) AS idunique,
                          NULL::numeric AS chutesnonqualifies,
                          count(*) AS chutes0,
                          NULL::numeric AS chutes1,
                          NULL::numeric AS chutes2,
                          NULL::numeric AS chutes3
                          FROM event_assistance
                          WHERE ((event_assistance.idniveau_urgence = 0) AND (event_assistance.emplacement IS NOT NULL))
                          GROUP BY (event_assistance.idclient || (event_assistance.emplacement)::text)
                          UNION
                          SELECT (event_assistance.idclient || (event_assistance.emplacement)::text) AS idunique,
                          NULL::numeric AS chutesnonqualifies,
                          NULL::numeric AS chutes0,
                          count(*) AS chutes1,
                          NULL::numeric AS chutes2,
                          NULL::numeric AS chutes3
                          FROM event_assistance
                          WHERE ((event_assistance.idniveau_urgence = 1) AND (event_assistance.emplacement IS NOT NULL))
                          GROUP BY (event_assistance.idclient || (event_assistance.emplacement)::text)
                          UNION
                          SELECT (event_assistance.idclient || (event_assistance.emplacement)::text) AS idunique,
                          NULL::numeric AS chutesnonqualifies,
                          NULL::numeric AS chutes0,
                          NULL::numeric AS chutes1,
                          count(*) AS chutes2,
                          NULL::numeric AS chutes3
                          FROM event_assistance
                          WHERE ((event_assistance.idniveau_urgence = 2) AND (event_assistance.emplacement IS NOT NULL))
                          GROUP BY (event_assistance.idclient || (event_assistance.emplacement)::text)
                          UNION
                          SELECT (event_assistance.idclient || (event_assistance.emplacement)::text) AS idunique,
                          NULL::numeric AS chutesnonqualifies,
                          NULL::numeric AS chutes0,
                          NULL::numeric AS chutes1,
                          NULL::numeric AS chutes2,
                          count(*) AS chutes3
                          FROM event_assistance
                          WHERE ((event_assistance.idniveau_urgence = 3) AND (event_assistance.emplacement IS NOT NULL))
                          GROUP BY (event_assistance.idclient || (event_assistance.emplacement)::text)) test
                          GROUP BY test.idunique
                          ORDER BY test.idunique) testing LEFT JOIN chutes ON chutes.idunique=testing.idunique) AS result")
#################################
# Implementation de K-Means ici #
#################################
nbClusters<-dbGetQuery(con,"select value from parametres where name ='nbclusters'")
scoreN=scale(df_postgres$score, center = TRUE, scale = FALSE)
classement=kmeans(scoreN,strtoi(nbClusters$value));
# close the connection
plot(scoreN,pch=16,col=classement$cluster);
scoreWithLabel =cbind(scoreN,idunique)
subId=str_replace_all(scoreWithLabel[,2],"hambre","")
scoreWithLabel=cbind(scoreWithLabel,subId)
points(classement$centers,pch=25)
text(scoreWithLabel[,1],labels=scoreWithLabel[,3])
#dev.print(device = png, file = "D:/Arthur/Documents/export.png", width = 600)
test=data.frame(classement$cluster)
res=cbind(scoreN, test)
idunique=df_postgres$idunique
res=cbind(res, idunique)
if(dbExistsTable(con,"chutes")){dbRemoveTable(con,"chutes")}
dbWriteTable(con,"chutes",res)
dbDisconnect(con)
dbUnloadDriver(drv)
require(RPostgreSQL)
require(MASS)
require(class)
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
df_postgres2 <- dbGetQuery(con, "SELECT *, idunique FROM chutes")
data <- dbGetQuery(con, "SELECT idunique, ch0,ch1,ch2,ch3 FROM (SELECT testing.idunique,
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
# Implementation de KNN ici     #
#################################
trueClass=df_postgres2[,3]
data=as.matrix(data)
data = cbind(data,trueClass)
cl=as.vector(data[,6])
res = knn(data[1:66,1:6],data[67:95,1:6],cl,k=3)
# close the connection
dbDisconnect(con)
dbUnloadDriver(drv)
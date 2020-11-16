# Derby database info

## Start derby engine using the following command

`java -jar %DERBY_HOME%\lib\derbyrun.jar server start` for windows CLI  
`java -jar $DERBY_HOME/lib/derbyrun.jar server start` for bash

---

## Start ij tool to interact with derby database

`java -jar %DERBY_HOME%\lib\derbyrun.jar ij`

### IJ Commands

`CONNECT 'jdbc:derby://localhost:1527/seconddb;create=true';` Connect to running instance specifying database and create it if not created

`CONNECT 'jdbc:derby://localhost:1527/D:/Programs/Databases/SPDB;user=u;password=p;create=true';` Same as previous one with username and password differences

`call SYSCS_UTIL.SYSCS_CREATE_USER('u','p');` At first time connection and creating the database. Specify username and password using this command

#### Use sql commands directly at IJ interactive shell tool

`CREATE TABLE SECONDTABLE (ID INT PRIMARY KEY, NAME VARCHAR(14));`  
`INSERT INTO SECONDTABLE VALUES (100,'ONE HUNDRED'),(200,'TWO HUNDRED'),(300,'THREE HUNDRED');`  
`SELECT * FROM SECONDTABLE;`  
`SELECT * FROM SECONDTABLE WHERE ID=200;`

#### To exit IJ

`exit;`

## Tips

- Using connection pool, treat connections as a scarce resource with limited number available and expensive to create new one. So hold the data you want and release the connection to the pool then do you business logic.
- Keep connections as long as you need them.
- Re-use same connection is preferred.

## Reference

<http://db.apache.org/derby/manuals/index.html> - Official documentation

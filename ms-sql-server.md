# Microsoft SQL Server

## DB Lock Help

`sp_who`

`SELECT @@SPID`

`DBCC INPUTBUFFER(73)`

```sql
select cmd,* from sys.sysprocesses
where blocked > 0
ORDER BY waittime
```

`EXEC sp_lock`

`kill {spid}`

### DB Leak Connections and Queries information

```sql
SELECT ec.session_id, last_read, last_write, text, client_net_address, program_name, host_process_id, login_name, *
FROM sys.dm_exec_connections  ec
JOIN sys.dm_exec_sessions es
  ON ec.session_id = es.session_id
CROSS APPLY sys.dm_exec_sql_text(ec.most_recent_sql_handle) AS dest
where client_net_address IN ('172.16.30.18', '172.16.30.1')
```

```sql
SELECT ec.session_id, last_read, last_write, text, client_net_address, program_name, host_process_id, login_name
FROM sys.dm_exec_connections  ec
JOIN sys.dm_exec_sessions es
  ON ec.session_id = es.session_id
CROSS APPLY sys.dm_exec_sql_text(ec.most_recent_sql_handle) AS dest
where es.status = 'sleeping'
```

```sql
select count(*) as sessions,
         s.host_name,
         s.host_process_id,
         s.program_name,
         db_name(s.database_id) as database_name
   from sys.dm_exec_sessions s
   where is_user_process = 1
   and program_name LIKE '%JDBC%'
   group by host_name, host_process_id, program_name, database_id
   order by count(*) desc;
```

```sql
 declare @host_process_id int = 1508;
  declare @host_name sysname = N'ODB-CMS';
  declare @database_name sysname = N'arrow_spo';

  select datediff(minute, s.last_request_end_time, getdate()) as minutes_asleep,
         s.session_id,
         db_name(s.database_id) as database_name,
         s.host_name,
         s.host_process_id,
         t.text as last_sql,
         s.program_name
    from sys.dm_exec_connections c
    join sys.dm_exec_sessions s
         on c.session_id = s.session_id
   cross apply sys.dm_exec_sql_text(c.most_recent_sql_handle) t
   where s.is_user_process = 1
         and s.status = 'sleeping'
         and db_name(s.database_id) = @database_name
         and s.host_name = @host_name
         and datediff(second, s.last_request_end_time, getdate()) > 60
   order by s.last_request_end_time;
```

## Terminate database and any connections to it and bring it back online again

```sql
USE [master];
ALTER DATABASE [foo] SET OFFLINE WITH ROLLBACK IMMEDIATE;
ALTER DATABASE [foo] SET ONLINE;
```

```sql
USE master
GO
DECLARE @kill varchar(max) = '';
SELECT @kill = @kill + 'KILL ' + CONVERT(varchar(10), spid) + '; '
FROM master..sysprocesses 
WHERE spid > 50 AND dbid = DB_ID('<Your_DB_Name>')
EXEC(@kill);
```

## Rename table and/or sequence

ALTER TABLE  RENEW_FOR_OTHERS_LOG RENAME TO PAY_FOR_OTHERS_LOG;
RENAME  RENEW_FOR_OTHERS_LOG_SEQ  TO PAY_FOR_OTHERS_LOG_SEQ;

## Find out what FOREIGN KEY constraint references a table

```sql
SELECT
   OBJECT_NAME(f.parent_object_id) TableName,
   COL_NAME(fc.parent_object_id,fc.parent_column_id) ColName
FROM
   sys.foreign_keys AS f
INNER JOIN
   sys.foreign_key_columns AS fc ON f.OBJECT_ID = fc.constraint_object_id
INNER JOIN
   sys.tables t ON t.OBJECT_ID = fc.referenced_object_id
WHERE  OBJECT_NAME (f.referenced_object_id) = 'YourTableName';
```

```sql
sp_help 'TableName';
```

```sql
SELECT
  object_name(parent_object_id) ParentTableName,
  object_name(referenced_object_id) RefTableName,
  name
FROM sys.foreign_keys
WHERE parent_object_id = object_id('Tablename')
```

```sql
EXEC sp_fkeys 'TableName'
```

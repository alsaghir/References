# Oracle Database

## Creating dump of oracle schema

`expdp 'sys/a as sysdba' schema=ARROW dumpfile=arrow-2019-05-02.dpdmp`

## Performance

### Execution plan operations

- Table operations
  - Table scan full - Most expensive for large tables
  - Table Access by index row id
- Index operations
  - Index range scan
  - Index unique scan
  - Index fast full scan
  - Index full scan
- Join operations
  - Nested loops join
  - merge join
  - hash join

### SQL Developer

- Auto-trace (SQL is actually executed)

- Analyze Execution Plan (SQL is not actually executed)

Effective data are

- `COST` which higher is worse.
- `Cardinality` which number of returned rows.
- `LAST_CR_BUFFER_GETS` which is logical reads
- `LAST_OUTPUT_ROWS` Actual output returned which is close to estimated `Cardinality`.
- `V$` Stats which a bit off from auto-trace for gathering real data.

### SQL Plus

`set linesize 120`
`set autotrace only` - To output auto-trace without SQL statement result.

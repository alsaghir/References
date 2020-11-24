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
  - Nested loops join - Fast for small tables
  - merge join
  - hash join - Best

#### Join performance questions

- Which table is used to drive the SQL statement ? The first table to select from should return minimum rows and not many rows to be filtered later by the join.
- Any indexes on join conditions ? They are faster than other join conditions.

#### Index tips

- Order of columns matter. Columns suspected to not be used in conditions should be in the last (most right). You want to have as many column from the front of the index as you can in your `where`  clause.
- Indexing `STATE, LAST_NAME, FIRST_NAME` makes the following SQL effected as follows

```SQL
-- Index is not used because STATE is not specified
SELECT * FROM STUDENTS WHERE LAST_NAME = 'Harris' AND FIRST_NAME = 'Sam'

-- One solution is to add STATE
SELECT * FROM STUDENTS WHERE LAST_NAME = 'Harris' AND FIRST_NAME = 'Sam' AND STATE = 'CO'

-- Another is re-order the index to be LAST_NAME, FIRST_NAME, STATE
-- Now this query is using the index
SELECT * FROM STUDENTS WHERE LAST_NAME = 'Harris' AND FIRST_NAME = 'Sam'

-- This will use index but based on how selective the first column (LAST_NAME) is.
-- Will be efficient than full table scan but not as efficient as having FIRST_NAME in the conditions
SELECT * FROM STUDENTS WHERE LAST_NAME = 'Harris' AND STATE = 'CO'
```

- Use index on values to be mostly unique. So favor indexes with higher selectivity.
- Good formula for index selectivity is `Index Selectivity = Number of unique values / Total number of rows`. Closest to 1 (higher) is better.
- Another way to look at it is `Expected rows per index key = Total number of rows in table / Number of unique values` which in this case makes lower numbers are better selectivity.
- Favor frequency of use over selectivity.
- 

### SQL Developer

- Auto-trace (SQL is actually executed)

- Analyze Execution Plan (SQL is not actually executed)

Effective data are

- `COST` which higher is worse.
- `Cardinality` which number of returned rows.
- `LAST_CR_BUFFER_GETS` which is logical reads performed for indicated step.
- `LAST_OUTPUT_ROWS` Actual output returned which is close to estimated `Cardinality`.
- `V$` Stats which a bit off from auto-trace for gathering real data.
- `Filter Predicates` - Data are scanned then filter predicate used to filter only wanted data. Mostly this filter is condition on non-indexed column. So filter predicates are applied AFTER data are scanned. On full table scans may indicate the need for an index.
- `Access Predicates` - Indexed condition. Used to filter data before `Filter Predicates` applied on them.

### SQL Plus

`set linesize 120`
`set autotrace only` - To output auto-trace without SQL statement result.

# QueryDSL
simple DSL to build SQL sentences

## Usage

```groovy
def queryDSL = QueryDSL.build {
        select 'a.field1, a.field2'
        from 'mytable a'
        where{
            and{
                eq "a.field1", 501
            }
        }
        innerJoin "table2 b" on "a.field1" eq "b.field1"
        groupBy 1
    }

    String select = queryDSL.SQL
    Sql sqlSelect = new Sql(dataSource)
    println sqlSelect.rows(sqlSelect)

    String countResources = queryDSL.CountSQL
    Sql sqlCount = new Sql(dataSource)
    println sqlCount.firstRow(countResources)
    
```
    

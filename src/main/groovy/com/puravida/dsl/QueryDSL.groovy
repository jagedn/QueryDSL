package com.puravida.dsl

import groovy.sql.Sql


/**
 * Created by jorge on 1/02/16.
 */
class QueryDSL implements Cloneable{

    Integer forYear
    String fromTables
    String fields
    String groupStr
    String whereStr
    def trace

    def static QueryDSL build( closure ) {
        QueryDSL dsl = new QueryDSL()
        closure.delegate = dsl
        closure()
        dsl
    }

    def traceOutput( output ){
        this.trace = output
    }

    def select(String field){
        this.fields = field
    }

    def select(String[]fields){
        this.fields = fields.join(',')
    }

    def select(List fields){
        this.fields = fields.join(',')
    }

    def from( String table){
        fromTables = forYear ? String.format(table,forYear) : table
    }

    def withYear( int year){
        forYear = year
    }

    def groupBy( int [] group){
        groupStr = group.join(',')
    }

    def groupBy( String [] group){
        groupStr = group.join(',')
    }

    def where(closure){
        String whereDSL = WhereDSL.Where(closure)
        if( !whereStr )
            whereStr = "WHERE "
        else
            whereStr = "$whereStr and "
        whereStr = "$whereStr $whereDSL"
    }

    def innerJoin( String table  ){
        join("inner",table)
    }

    def leftJoin( String table){
        join("left",table)
    }

    def rightJoin( String table ){
        join("right",table)
    }

    def join( String op, String table2){
        QueryDSL dsl = this
        [
                on:{ f1 ->
                    [
                     eq:{f2->
                        dsl.addJoin(op,table2,f1,'=',f2)
                         dsl
                     },
                     ge:{f2->
                         dsl.addJoin(op,table2,f1,'>=',f2)
                         dsl
                     },
                     le:{f2->
                         dsl.addJoin(op,table2,f1,'=<',f2)
                         dsl
                     },
                     gt:{f2->
                         dsl.addJoin(op,table2,f1,'>',f2)
                         dsl
                     },
                     lt:{f2->
                         dsl.addJoin(op,table2,f1,'<',f2)
                         dsl
                     },
                     distinct:{f2->
                         dsl.addJoin(op,table2,f1,'<>',f2)
                         dsl
                     },
                    ]
                }
        ]
    }

    private addJoin( final String inner, final String table, final String f1, final String method, final String f2){
        assert fromTables
        assert fromTables.size()
        String table2 = forYear ? String.format(table,forYear) : table
        fromTables = "$fromTables $inner join $table2 on $f1 $method $f2 "
    }

    public String getSQL( ){
        StringWriter writer = new StringWriter()
        writer.write("SELECT $fields")
        writer.write("\nFROM \n$fromTables")
        if( whereStr && whereStr.length())
            writer.write("\n$whereStr")
        if( groupStr && groupStr.length())
            writer.write("\nGROUP BY $groupStr")
        if( trace ){
            println( writer.toString() )
        }
        writer.toString()
    }

    public String getCountSQL( ){
        StringWriter writer = new StringWriter()
        writer.write("SELECT count(*)")
        writer.write("\nFROM \n$fromTables")
        if( whereStr && whereStr.length())
            writer.write("\n$whereStr")
        if( groupStr && groupStr.length())
            writer.write("\nGROUP BY $groupStr")
        if( trace ){
            println( writer.toString() )
        }
        writer.toString()
    }

    public QueryDSL namedSQL(closure){
        QueryDSL clone = this.clone()
        closure.delegate = clone
        closure()
        clone
    }


}

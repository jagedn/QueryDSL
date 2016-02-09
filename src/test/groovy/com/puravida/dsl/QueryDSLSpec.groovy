package com.puravida.dsl

import spock.lang.Shared
import spock.lang.Specification

import javax.sql.DataSource

/**
 * Created by jorge on 1/02/16.
 */
class QueryDSLSpec extends Specification{

    @Shared
    def queryDSL = QueryDSL.build {
        select 'a.coarticu'
        from "fvarticu a"
        where{
            and{
                eq "a.coarticu", 501
            }
        }
        innerJoin "fvruedas b" on "a.coarticu" eq "b.coarticu"
        groupBy 1
        traceOutput this
    }


    def setup() {

    }

    def cleanup() {
    }

    void "test simple select"() {
        given:

        when:
        def result = queryDSL.SQL

        then:
            result.startsWith("SELECT ")
    }

    void "test simple count"() {
        given:

        when:
        def result = queryDSL.countSQL

        then:
        result.startsWith("SELECT count(*)")
    }


    void "test named query"() {
        given:
        def namedQuery = queryDSL.namedSQL{
            traceOutput this
            where{
                and {
                    eq 'a.noarticu', ':noarticu'
                }
            }
        }

        when:
        def result = namedQuery.SQL

        then:
        result.indexOf(':noarticu') != -1
    }

    void "test qualified query"() {
        given:
        def queryDSL = QueryDSL.build {
            select 'a.coarticu'
            withYear 2015
            from "fvarticu%1\$04d a"
            where{
                and{
                    eq "a.coarticu", 501
                }
            }
            innerJoin "fvruedas b" on "a.coarticu" eq "b.coarticu"
            groupBy 1
            traceOutput this
        }
        when:
        def result = queryDSL.SQL

        then:
        result.indexOf('fvarticu2015') != -1
    }

}

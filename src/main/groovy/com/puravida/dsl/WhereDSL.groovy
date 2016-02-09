package com.puravida.dsl

/**
 * Created by jorge on 1/02/16.
 */
class WhereDSL {

    String whereStr =""
    String currentStr = ""

    def static Where( closure ){
        WhereDSL dsl = new WhereDSL()
        closure.delegate = dsl
        closure()
        dsl.whereStr
    }

    def addOp( field, op , value){
        if( currentStr.length() )
            currentStr = "$currentStr AND "
        currentStr = "$currentStr $field $op $value "
    }

    def eq ( field, value){
        addOp(field,'=',value)
    }

    def le ( field, value){
        addOp(field,'<=',value)
    }

    def ge ( field, value){
        addOp(field,'>=',value)
    }

    def lt ( field, value){
        addOp(field,'<',value)
    }

    def gt ( field, value){
        addOp(field,'>',value)
    }

    def disting ( field, value){
        addOp(field,'<>',value)
    }

    def and(closure){
        complex("AND",closure)
    }

    def or(closure){
        complex("OR",closure)
    }

    private complex( op, closure){
        currentStr = ""
        WhereDSL dsl = this
        closure.delegate = dsl
        closure()
        if( whereStr.length() )
            whereStr = "$whereStr $op "
        whereStr = "$whereStr ($currentStr) "
        dsl
    }
}

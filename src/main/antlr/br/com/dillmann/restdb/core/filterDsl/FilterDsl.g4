grammar FilterDsl;
@header {
package br.com.dillmann.restdb.core.filterDsl;
}

root: group (logicalOperator group)* EOF;
group: expressions | '(' group (logicalOperator group)* ')';
expressions: expression (logicalOperator expression)*;
expression: columnName '.' operation ('[' parameters ']')?;
columnName: SQL_COLUMN_NAME;
operation
    : 'equals'
    | 'notEquals'
    | 'lessThan'
    | 'lessOrEquals'
    | 'biggerThan'
    | 'biggerOrEquals'
    | 'between'
    | 'notBetween'
    | 'in'
    | 'notIn'
    | 'like'
    | 'notLike'
    | 'isNull'
    | 'isNotNull'
    | 'isEmpty'
    | 'isNotEmpty';
parameters: parameter (',' parameter)*;
parameter: (parameterNumericValue | parameterStringValue);
parameterStringValue: PARAMETER_STRING_VALUE;
parameterNumericValue: PARAMETER_NUMERIC_VALUE;
logicalOperator: '&&' | '||';

PARAMETER_NUMERIC_VALUE: ('-')?[0-9]+('.'[0-9]+)*;
PARAMETER_STRING_VALUE: '"' (~('"' | '\\' | '\r' | '\n') | '\\' ('"' | '\\'))+ '"';
SQL_COLUMN_NAME: [A-Za-z0-9_]+;
WS: [ \t\r\n]+ -> skip;
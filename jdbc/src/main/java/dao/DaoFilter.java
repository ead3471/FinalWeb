package dao;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by Freemind on 2016-11-07.
 */
public class DaoFilter {
    protected ArrayList<String> filterConditions=new ArrayList();
    protected ArrayList<String> postConditions=new ArrayList<>();
    protected   String limitCondition="";
    protected  StringBuilder resultSqlBuilder;

    protected DaoFilter addCondition(String column, String value){
        filterConditions.add(column+"="+value);
        return this;
    }

    public DaoFilter withLimit(int limit){
        limitCondition="LIMIT "+limit;
        return this;
    }

    public DaoFilter orderBy(String columnName,String orderDirection){
        postConditions.add(" ORDER BY "+columnName+" "+ Optional.ofNullable(orderDirection).filter(val->val.matches("^(ASC|DESC)$")).orElse(""));
        return this;
    }


    protected String build(){
        if(filterConditions.size()>0)
            resultSqlBuilder.append(" WHERE ").append(filterConditions.get(0));

        for(String condition:filterConditions){
            resultSqlBuilder.append(" AND ").append(condition);
        }

        for(String condition:postConditions){
            resultSqlBuilder.append(condition);
        }
        return resultSqlBuilder.toString()+limitCondition;
    }


}

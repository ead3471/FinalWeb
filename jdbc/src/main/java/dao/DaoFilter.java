package dao;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by Freemind on 2016-11-07.
 */
public class DaoFilter {

    private ArrayList<String> filterAndConditions =new ArrayList();

    private ArrayList<String> postConditions=new ArrayList<>();
    private ArrayList<String > filterOrConditions=new ArrayList<>();

    private   String limitCondition="";
    private  StringBuilder resultSqlBuilder;



    public DaoFilter(String initString){
        resultSqlBuilder=new StringBuilder(initString);
    }

    public DaoFilter addAndCondition(String column, String value){
        filterAndConditions.add(column+"='"+value+"'");
        return this;
    }

    public DaoFilter addAndCondition(String column,String relation ,String value){
        filterAndConditions.add(column+relation+"'"+value+"'");
        return this;
    }

    public DaoFilter addOrCondition(String column, String value){
        filterOrConditions.add(column+"='"+value+"'");
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
        if(filterAndConditions.size()>0  )
            resultSqlBuilder.append(" WHERE ").append(filterAndConditions.get(0));

       for(int i=1;i<filterAndConditions.size();i++){
           resultSqlBuilder.append(" AND ").append(filterAndConditions.get(i));
       }


        if(filterOrConditions.size()>0  ){
            if(filterAndConditions.size()==0){
                resultSqlBuilder.append(" WHERE ");
            }
            resultSqlBuilder.append(" ").append(filterOrConditions.get(0));
        }


        for(int i=1;i<filterOrConditions.size();i++){
            resultSqlBuilder.append(" OR ").append(filterOrConditions.get(i));
        }

        for(String condition:postConditions){
            resultSqlBuilder.append(condition);
        }
        return resultSqlBuilder.toString()+limitCondition;
    }


}

package com.prcsteel.ec.model.query;

import java.util.LinkedList;
import java.util.List;

/**
 * 查询条件
 * Created by Rolyer on 2016/5/5.
 */
public class ComplexCond {
    List<ComplexConditionNode> nodes=new LinkedList<>();


    public List<ComplexConditionNode> getNodes() {
        return nodes;
    }

    public ComplexCond col(String col){
        nodes.add(new ComplexConditionNode("col", col));
        return this;
    }

    public ComplexCond and(){
        nodes.add(new ComplexConditionNode("and"));
        return this;
    }
    public ComplexCond or(){
        nodes.add(new ComplexConditionNode("or"));
        return this;
    }

    public ComplexCond and(ComplexCond innerCondition){
        nodes.add(new ComplexConditionNode("and"));
        nodes.add(new ComplexConditionNode("("));
        nodes.addAll(innerCondition.getNodes());
        nodes.add(new ComplexConditionNode(")"));
        return this;
    }

    public ComplexCond or(ComplexCond innerCondition){
        nodes.add(new ComplexConditionNode("or"));
        nodes.add(new ComplexConditionNode("("));
        nodes.addAll(innerCondition.getNodes());
        nodes.add(new ComplexConditionNode(")"));
        return this;
    }

    public ComplexCond eq(Object value){
        nodes.add(new ComplexConditionNode("eq", value));
        return this;
    }

    public ComplexCond gt(Object value){
        nodes.add(new ComplexConditionNode("gt", value));
        return this;
    }

    public ComplexCond lt(Object value){
        nodes.add(new ComplexConditionNode("lt", value));
        return this;
    }

    public ComplexCond gte(Object value){
        nodes.add(new ComplexConditionNode("gte", value));
        return this;
    }

    public ComplexCond lte(Object value){
        nodes.add(new ComplexConditionNode("lte", value));
        return this;
    }

    public ComplexCond beginWith(Object value){
        nodes.add(new ComplexConditionNode("bw", value));
        return this;
    }

    public ComplexCond beginNotWith(Object value){
        nodes.add(new ComplexConditionNode("bn", value));
        return this;
    }

    public ComplexCond endWith(Object value){
        nodes.add(new ComplexConditionNode("ew", value));
        return this;
    }

    public ComplexCond endNotWith(Object value){
        nodes.add(new ComplexConditionNode("en", value));
        return this;
    }

    public ComplexCond contains(Object value){
        nodes.add(new ComplexConditionNode("cn", value));
        return this;
    }

    public ComplexCond notContains(Object value){
        nodes.add(new ComplexConditionNode("nc", value));
        return this;
    }

    public ComplexCond isNull(){
        nodes.add(new ComplexConditionNode("nu"));
        return this;
    }


    public ComplexCond notNull(){
        nodes.add(new ComplexConditionNode("nn"));
        return this;
    }

    public ComplexCond in(Object... objects){
        nodes.add(new ComplexConditionNode("in",objects));
        return this;
    }

    public ComplexCond notIn(Object... objects){
        nodes.add(new ComplexConditionNode("ni",objects));
        return this;
    }

    @Override
    public String toString() {
        String result="";
        for (ComplexConditionNode eachNode:nodes) {
            result+= eachNode.getLink();
            result+=" ";
            if(eachNode.getObjects()==null){
                continue;
            }
            for(Object eachObject:eachNode.getObjects()){
                result+=eachObject.toString()+" ";
            }
        }
        return result;
    }
}

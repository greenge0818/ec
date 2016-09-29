package com.prcsteel.ec.model.query;

/**
 * Created by Rolyer on 2016/5/5.
 */
public class ComplexConditionNode {
    private String link;
    private Object[] objects;
    private Object object;

    public ComplexConditionNode(String link) {
        super();
        this.link = link;
    }

    public ComplexConditionNode(String link, Object... objects) {
        super();
        this.link = link;
        this.objects = objects;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Object[] getObjects() {
        return objects;
    }

    public void setObjects(Object... objects) {
        this.objects = objects;
    }

    public Object getObject() {
        object = objects[0];
        return object;
    }
}

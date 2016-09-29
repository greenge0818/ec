package com.prcsteel.ec.core.model;

/**
 * AMQMessage demo domain object
 *
 * Created by Rolyer on 2016/5/23.
 */
public final class AMQMessage {
    private String text;
    private int count;

    public AMQMessage() {
    }

    public AMQMessage(String text, int count) {
        this();
        setText(text);
        setCount(count);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "AMQMessage{" +
                "text='" + getText() + '\'' +
                ", count=" + getCount() +
                '}';
    }
}

package com.mekcone.excrud.model.template;

public interface Template {
    boolean insert(String tag, String replacement);
    boolean insertOnce(String tag, String replacement);
    boolean remove(String tag);
}

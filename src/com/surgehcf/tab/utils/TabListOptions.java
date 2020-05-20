package com.surgehcf.tab.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true, chain = true)
public class TabListOptions {

    private boolean sendCreationMessage;

    public static TabListOptions getDefaultOptions() {
        return new TabListOptions().sendCreationMessage(true);
    }

}

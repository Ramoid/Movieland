package com.solovey.movieland.dao.enums;


public enum SortDirection {
    ASC("ASC"),
    DESC("DESC"),
    NOSORT("NOSORT");
    private final String direction;

    SortDirection(String type) {
        this.direction = type;
    }

    public static SortDirection getDirection(String sortString) {

        for (SortDirection sort : SortDirection.values()) {
            if (sort.direction.equalsIgnoreCase(sortString)) {
                return sort;
            }
        }
        //throw new IllegalArgumentException("Wrong sort direction");
        // nosort in case when wrong parameter value
        return SortDirection.NOSORT;
    }

    public String getDirection(){
        return  direction;
    }
}

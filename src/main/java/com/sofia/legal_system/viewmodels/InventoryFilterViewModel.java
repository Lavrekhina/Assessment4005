package com.sofia.legal_system.viewmodels;

import java.time.format.DateTimeFormatter;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class InventoryFilterViewModel {
    private final SimpleIntegerProperty qMin = new SimpleIntegerProperty(0);
    private final SimpleIntegerProperty qMax = new SimpleIntegerProperty(Integer.MAX_VALUE);
    private final SimpleStringProperty nameSearch = new SimpleStringProperty();
    private final SimpleStringProperty locationSearch = new SimpleStringProperty();


    public SimpleIntegerProperty getqMin() {
        return qMin;
    }

    public SimpleIntegerProperty getqMax() {
        return qMax;
    }

    public SimpleStringProperty getNameSearch() {
        return nameSearch;
    }

    public SimpleStringProperty getLocationSearch() {
        return locationSearch;
    }

    public String toSqlFilter() {
        String query = "";
        if (nameSearch.getValue() != null && !nameSearch.getValue().isEmpty()) {
            query += "item_name like '%" + nameSearch.getValue() + "%'";
        }

        if (locationSearch.getValue() != null && !locationSearch.getValue().isEmpty()) {
            query += (!query.isEmpty() ? " and " : "") + "item_location like '%" + locationSearch.getValue() + "%'";
        }

        if (qMin.getValue() != null && qMin.getValue() > 0) {
            query += (!query.isEmpty() ? " and " : "") + "item_quantity >=" + qMin.getValue();
        }

        if (qMax.getValue() != null && qMax.getValue() > 0) {
            query += (!query.isEmpty() ? " and " : "") + "item_quantity <=" + qMax.getValue();
        }

        return query;
    }
}

package com.sofia.legal_system.viewmodels;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class ShipmentsFilterViewModel {
    private final SimpleObjectProperty<LocalDate> dMin = new SimpleObjectProperty();
    private final SimpleObjectProperty<LocalDate> dMax = new SimpleObjectProperty();
    private final SimpleStringProperty destination = new SimpleStringProperty();
    private final SimpleStringProperty statusSearch = new SimpleStringProperty();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public SimpleObjectProperty getqMin() {
        return dMin;
    }

    public SimpleObjectProperty getqMax() {
        return dMax;
    }

    public SimpleStringProperty getDestinationSearch() {
        return destination;
    }

    public SimpleStringProperty getstatusSearch() {
        return statusSearch;
    }

    public String toSqlFilter() {
        String query = "";
        if (destination.getValue() != null && !destination.getValue().isEmpty()) {
            query += "destination like '%" + destination.getValue() + "%'";
        }

        if (statusSearch.getValue() != null && !statusSearch.getValue().isEmpty()) {
            query += (!query.isEmpty() ? " and " : "") + "shipment_status = '" + statusSearch.getValue() + "'";
        }

        if (dMin.getValue() != null) {
            query += (!query.isEmpty() ? " and " : "") + "shipment_date >= '" + dMin.getValue().format(formatter) + "'";
        }

        if (dMax.getValue() != null) {
            query += (!query.isEmpty() ? " and " : "") + "shipment_date <= '" + dMax.getValue().format(formatter) + "'";
        }

        return query;
    }
}

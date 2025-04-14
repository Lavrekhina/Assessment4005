package com.sofia.legal_system.viewmodels.orders;

import com.sofia.legal_system.viewmodels.BasePagingFilterViewModel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class OrdersFilterViewModel extends BasePagingFilterViewModel{
    private final SimpleObjectProperty<LocalDate> dMin = new SimpleObjectProperty();
    private final SimpleObjectProperty<LocalDate> dMax = new SimpleObjectProperty();
    private final SimpleStringProperty customerNameSearch = new SimpleStringProperty();
    private final SimpleStringProperty statusSearch = new SimpleStringProperty();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public SimpleObjectProperty getqMin() {
        return dMin;
    }

    public SimpleObjectProperty getqMax() {
        return dMax;
    }

    public SimpleStringProperty getcustomerNameSearch() {
        return customerNameSearch;
    }

    public SimpleStringProperty getstatusSearch() {
        return statusSearch;
    }

    public String toSqlFilter() {
        String query = "";
        if (customerNameSearch.getValue() != null && !customerNameSearch.getValue().isEmpty()) {
            query += "customer_name like '%" + customerNameSearch.getValue() + "%'";
        }

        if (statusSearch.getValue() != null && !statusSearch.getValue().isEmpty()) {
            query += (!query.isEmpty() ? " and " : "") + "order_status = '" + statusSearch.getValue() + "'";
        }

        if (dMin.getValue() != null) {
            query += (!query.isEmpty() ? " and " : "") + "order_date >= '" + dMin.getValue().format(formatter) + "'";
        }

        if (dMax.getValue() != null) {
            query += (!query.isEmpty() ? " and " : "") + "order_date <= '" + dMax.getValue().format(formatter) + "'";
        }

        return query;
    }
}

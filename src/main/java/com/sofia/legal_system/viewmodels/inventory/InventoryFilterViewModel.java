package com.sofia.legal_system.viewmodels.inventory;

import com.sofia.legal_system.viewmodels.BasePagingFilterViewModel;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class InventoryFilterViewModel extends BasePagingFilterViewModel {

    private final SimpleObjectProperty<Integer> qMin = new SimpleObjectProperty<Integer>(null);
    private final SimpleObjectProperty<Integer> qMax = new SimpleObjectProperty<Integer>(null);

    private final SimpleStringProperty nameSearch = new SimpleStringProperty();
    private final SimpleStringProperty locationSearch = new SimpleStringProperty();


    public SimpleObjectProperty<Integer> getqMin() {
        return qMin;
    }
    public SimpleObjectProperty<Integer> getqMax() {
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

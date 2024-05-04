package fr.u_paris.gla.project.User_interface;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class FilteredComboBoxModel extends AbstractListModel<String> implements ComboBoxModel<String> {
    private List<String> items;
    private List<String> filteredItems;
    private String selectedItem;

    public FilteredComboBoxModel(List<String> items) {
        this.items = items;
        this.filteredItems = new ArrayList<>(items);
    }

    @Override
    public int getSize() {
        return filteredItems.size();
    }

    @Override
    public String getElementAt(int index) {
        return filteredItems.get(index);
    }

    @Override
    public void setSelectedItem(Object anItem) {
        selectedItem = (String) anItem;
    }

    @Override
    public Object getSelectedItem() {
        return selectedItem;
    }

    public void filter(String text) {
        text = text.toLowerCase();
        filteredItems.clear();
        for (String item : items) {
            if (item.toLowerCase().contains(text)) {
                filteredItems.add(item);
            }
        }
        fireContentsChanged(this, 0, getSize() - 1);
    }
}
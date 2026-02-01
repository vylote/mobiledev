package com.vlt.lab2;

import android.content.Context;

import java.util.ArrayList;

public class ContactRepository {

    private final ArrayList<ContactItem> originalData = new ArrayList<>();
    private final ArrayList<ContactItem> displayData = new ArrayList<>();

    public ContactRepository(Context context) {
        originalData.add(new ContactItem("VyLoTe", "3849124", false, getUri(context, R.drawable.p1)));
        originalData.add(new ContactItem("TrungRua", "3217987", false, getUri(context, R.drawable.p2)));
        originalData.add(new ContactItem("BaMia", "334347", false, null));
        displayData.addAll(originalData);
    }

    public ArrayList<ContactItem> getDisplayData() {
        return displayData;
    }

    public ArrayList<ContactItem> getOriginalData() {
        return originalData;
    }

    public void add(ContactItem item) {
        originalData.add(item);
        displayData.clear();
        displayData.addAll(originalData);
    }

    public void update(int realIndex, ContactItem item) {
        originalData.set(realIndex, item);
    }

    public boolean deleteSelected() {
        boolean deleted = false;
        for (int i = displayData.size() - 1; i >= 0; i--) {
            if (displayData.get(i).isStatus()) {
                originalData.remove(displayData.get(i));
                displayData.remove(i);
                deleted = true;
            }
        }
        return deleted;
    }

    public void filter(String keyword) {
        displayData.clear();
        if (keyword == null || keyword.isEmpty()) {
            displayData.addAll(originalData);
            return;
        }
        String key = keyword.toLowerCase();
        for (ContactItem item : originalData) {
            if (item.getName().toLowerCase().contains(key)) {
                displayData.add(item);
            }
        }
    }

    private String getUri(Context context, int resId) {
        return "android.resource://" + context.getPackageName() + "/" + resId;
    }
}


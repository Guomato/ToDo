package com.guoyonghui.todo.data;

import java.util.Arrays;
import java.util.UUID;

public class Task {

    private String mID;

    private String mTitle;

    private String mDescription;

    private boolean mCompleted;

    public Task(String title, String description) {
        mID = UUID.randomUUID().toString();
        mTitle = title;
        mDescription = description;
        mCompleted = false;
    }

    public Task(String ID, String title, String description) {
        mID = ID;
        mTitle = title;
        mDescription = description;
        mCompleted = false;
    }

    public Task(String title, String description, boolean completed) {
        mID = UUID.randomUUID().toString();
        mTitle = title;
        mDescription = description;
        mCompleted = completed;
    }

    public Task(String ID, String title, String description, boolean completed) {
        mID = ID;
        mTitle = title;
        mDescription = description;
        mCompleted = completed;
    }

    public String getID() {
        return mID;
    }

    public void setID(String ID) {
        mID = ID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public boolean isCompleted() {
        return mCompleted;
    }

    public void setCompleted(boolean completed) {
        mCompleted = completed;
    }

    public String getTitleForList() {
        return (mTitle != null && !"".equals(mTitle)) ? mTitle : mDescription;
    }

    public boolean isActive() {
        return !mCompleted;
    }

    public boolean isEmpty() {
        return (mTitle == null || mTitle.isEmpty()) && (mDescription == null || mDescription.isEmpty());
    }

    @Override
    public String toString() {
        return "ToDo with title: " + mTitle;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[]{mID, mTitle, mDescription});
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return equals(mID, task.mID) &&
                equals(mTitle, task.mTitle) &&
                equals(mDescription, task.mDescription);
    }

    private boolean equals(Object a, Object b) {
        return (a == null) ? b == null : a.equals(b);
    }
}

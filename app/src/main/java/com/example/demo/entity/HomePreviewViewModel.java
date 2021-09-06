/* (c) Disney. All rights reserved. */
package com.example.demo.entity;

import java.util.Objects;

/**
 * Created by Jack Ke on 2021/8/27
 */
public class HomePreviewViewModel {
    private String url;

    private boolean isSelected;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HomePreviewViewModel that = (HomePreviewViewModel) o;
        return isSelected == that.isSelected &&
                Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, isSelected);
    }
}

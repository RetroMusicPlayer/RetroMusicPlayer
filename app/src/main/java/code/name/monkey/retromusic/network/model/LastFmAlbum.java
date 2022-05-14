/*
 * Copyright (c) 2019 Hemanth Savarala.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by
 *  the Free Software Foundation either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package code.name.monkey.retromusic.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class LastFmAlbum {

  @Expose private Album album;

  public Album getAlbum() {
    return album;
  }

  public void setAlbum(Album album) {
    this.album = album;
  }

  public static class Album {

    @Expose public String listeners;
    @Expose public String playcount;
    @Expose private List<Image> image = new ArrayList<>();
    @Expose private String name;
    @Expose private Tags tags;
    @Expose private Wiki wiki;

    public List<Image> getImage() {
      return image;
    }

    public void setImage(List<Image> image) {
      this.image = image;
    }

    public String getListeners() {
      return listeners;
    }

    public void setListeners(final String listeners) {
      this.listeners = listeners;
    }

    public String getName() {
      return name;
    }

    public void setName(final String name) {
      this.name = name;
    }

    public String getPlaycount() {
      return playcount;
    }

    public void setPlaycount(final String playcount) {
      this.playcount = playcount;
    }

    public Tags getTags() {
      return tags;
    }

    public Wiki getWiki() {
      return wiki;
    }

    public void setWiki(Wiki wiki) {
      this.wiki = wiki;
    }

    public static class Image {

      @SerializedName("#text")
      @Expose
      private String Text;

      @Expose private String size;

      public String getSize() {
        return size;
      }

      public void setSize(String size) {
        this.size = size;
      }

      public String getText() {
        return Text;
      }

      public void setText(String Text) {
        this.Text = Text;
      }
    }

    public class Tags {

      @Expose private List<Tag> tag = null;

      public List<Tag> getTag() {
        return tag;
      }
    }

    public class Tag {

      @Expose private String name;

      @Expose private String url;

      public String getName() {
        return name;
      }

      public String getUrl() {
        return url;
      }
    }

    public class Wiki {

      @Expose private String content;

      @Expose private String published;

      public String getContent() {
        return content;
      }

      public void setContent(String content) {
        this.content = content;
      }

      public String getPublished() {
        return published;
      }

      public void setPublished(final String published) {
        this.published = published;
      }
    }
  }
}

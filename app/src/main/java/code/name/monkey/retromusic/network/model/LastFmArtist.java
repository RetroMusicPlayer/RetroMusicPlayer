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

public class LastFmArtist {

  @Expose private Artist artist;

  public Artist getArtist() {
    return artist;
  }

  public void setArtist(Artist artist) {
    this.artist = artist;
  }

  public static class Artist {

    @Expose public Stats stats;
    @Expose private Bio bio;
    @Expose private List<Image> image = new ArrayList<>();

    public Bio getBio() {
      return bio;
    }

    public void setBio(Bio bio) {
      this.bio = bio;
    }

    public List<Image> getImage() {
      return image;
    }

    public void setImage(List<Image> image) {
      this.image = image;
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

    public static class Stats {

      @Expose public String listeners;

      @Expose public String playcount;

      public String getListeners() {
        return listeners;
      }

      public void setListeners(final String listeners) {
        this.listeners = listeners;
      }

      public String getPlaycount() {
        return playcount;
      }

      public void setPlaycount(final String playcount) {
        this.playcount = playcount;
      }
    }

    public class Bio {

      @Expose private String content;

      public String getContent() {
        return content;
      }

      public void setContent(String content) {
        this.content = content;
      }
    }
  }
}

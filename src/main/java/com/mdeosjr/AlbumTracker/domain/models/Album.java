package com.mdeosjr.AlbumTracker.domain.models;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "albums")
public class Album {
    @Id
    private String spotifyAlbumId;
    @Column(unique = true)
    private String name;
    private String artist;
    private String url;
    private String cover;
    @Enumerated(EnumType.STRING)
    private List list;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "albumToUser",
            joinColumns = @JoinColumn(name = "albumId", referencedColumnName = "spotifyAlbumId"),
            inverseJoinColumns = @JoinColumn(name = "userId", referencedColumnName = "userId"))
    private Set<User> users;

    public String getSpotifyAlbumId() { return spotifyAlbumId; }

    public void setSpotifyAlbumId(String spotifyAlbumId) { this.spotifyAlbumId = spotifyAlbumId; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getArtist() { return artist; }

    public void setArtist(String artist) { this.artist = artist; }

    public String getUrl() { return url; }

    public void setUrl(String url) { this.url = url; }

    public String getCover() { return cover; }

    public void setCover(String cover) { this.cover = cover; }

    public Set<User> getUsers() { return users; }

    public void setUsers(Set<User> users) { this.users = users; }

    public List getList() { return list; }

    public void setList(List list) { this.list = list; }
}


package com.kristofer.traveling.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="post_table")
public class PostModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String phrase;
    private String img;
    @ManyToOne
    @JoinColumn(name="creator_id")
    private UserModel creator;
    @JsonIgnore
    @OneToMany(mappedBy = "post")
    private List<LikeModel> likes = new ArrayList<>();
    @JsonIgnore
    @OneToMany(mappedBy = "post")
    private List<FavoriteModel> favorites = new ArrayList<>();
    //private List<CommentModel> comments = new ArrayList<>();
    private Date datePublic;
}

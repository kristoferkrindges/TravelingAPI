package com.kristofer.traveling.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    // private List<UserModel> likes = new ArrayList<>();
    // private List<UserModel> favs = new ArrayList<>();
    //private List<CommentModel> comments = new ArrayList<>();
    private Date date;
}
